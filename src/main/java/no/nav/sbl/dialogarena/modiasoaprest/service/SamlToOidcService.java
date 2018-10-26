package no.nav.sbl.dialogarena.modiasoaprest.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.cxf.message.Message;
import org.apache.cxf.security.SecurityContext;
import org.apache.wss4j.common.principal.SAMLTokenPrincipal;
import org.opensaml.saml.saml2.core.Assertion;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.Base64;

import static no.nav.sbl.dialogarena.modiasoaprest.common.Constants.*;

@Service
public class SamlToOidcService {

    public String konverterSamlTokenTilOIDCToken(Message currentMessage) {
        String samlAssertion = getSamlAssertion(currentMessage);
        String encodedSamlToken = Base64.getUrlEncoder().encodeToString(samlAssertion.getBytes());
        ResponseEntity<String> tokenResponse = getOidcTokenFromSamlToken(encodedSamlToken);

        JsonParser jsonParser = new JsonParser();
        JsonElement jsonResponse = jsonParser.parse(tokenResponse.getBody());
        JsonElement oidcToken = jsonResponse.getAsJsonObject().get("access_token");

        if(oidcToken == null || oidcToken.getAsString().isEmpty()) {
            throw new RuntimeException("Har ikke fått OIDC-token, OIDC-token er: "+ oidcToken);
        }

        return oidcToken.getAsString();
    }

    private ResponseEntity<String> getOidcTokenFromSamlToken(String encodedSamlToken) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String auth = MODIASOAPRESTPROXY_SYSTEM_USER + BASIC_AUTH_SEPERATOR + MODIASOAPRESTPROXY_SYSTEM_USER_PASSWORD;

        String encodedAuth = "Basic " + Base64.getEncoder().encodeToString(auth.getBytes());
        headers.set("Authorization", encodedAuth);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("grant_type", GRANT_TYPE_PARAM);
        map.add("requested_token_type", REQUESTED_TOKEN_TYPE_PARAM);
        map.add("subject_token_type", SUBJECT_TOKEN_TYPE_PARAM);
        map.add("subject_token", encodedSamlToken);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        ResponseEntity<String> tokenResponse = null;
        try {
            tokenResponse = restTemplate.postForEntity(SECURITY_TOKEN_SERVICE_URL, request, String.class);
        } catch (RestClientException e) {
            throw new RuntimeException("Feilet i henting av OIDC-token", e);
        }
        return tokenResponse;
    }

    private String getSamlAssertion(Message currentMessage) {
        SecurityContext sc = (SecurityContext) currentMessage.get(SecurityContext.class.getName());
        if (sc == null) {
            throw new RuntimeException("Cannot get SecurityContext from SoapMessage");
        }
        SAMLTokenPrincipal samlTokenPrincipal = (SAMLTokenPrincipal) sc.getUserPrincipal();
        if (samlTokenPrincipal == null) {
            throw new RuntimeException("Cannot get SAMLTokenPrincipal from SecurityContext");
        }
        Assertion assertion = samlTokenPrincipal.getToken().getSaml2();

        try {
            String samlAssertion = getSamlAssertionAsString(assertion);
            return samlAssertion;
        } catch (TransformerException e) {
            throw new RuntimeException("Klarte ikke finne SAML-assertion", e);
        }
    }

    private String getSamlAssertionAsString(Assertion assertion) throws TransformerException {
        StringWriter writer = new StringWriter();
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.transform(new DOMSource(assertion.getDOM()), new StreamResult(writer));
        return writer.toString();
    }

}