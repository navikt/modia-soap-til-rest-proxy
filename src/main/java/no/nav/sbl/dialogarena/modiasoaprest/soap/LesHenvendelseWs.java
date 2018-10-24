package no.nav.sbl.dialogarena.modiasoaprest.soap;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import no.nav.apiapp.soap.SoapTjeneste;
import no.nav.sbl.dialogarena.modiasoaprest.mapping.ArkivpostMapper;
import no.nav.tjeneste.domene.brukerdialog.arkiverthenvendelse.v2.informasjon.ArkivertHenvendelseV2;
import no.nav.tjeneste.domene.brukerdialog.arkivtjenester.v2.typer.Arkivpost;
import no.nav.tjeneste.domene.brukerdialog.arkivtjenester.v2.typer.ArkivpostTemagruppe;
import no.nav.tjeneste.domene.brukerdialog.arkivtjenester.v2.typer.Filter;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.PhaseInterceptorChain;
import org.apache.cxf.security.SecurityContext;
import org.apache.wss4j.common.principal.SAMLTokenPrincipal;
import org.opensaml.saml.saml2.core.Assertion;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.Base64;
import java.util.List;

@Service
@SoapTjeneste("/ArkivertHenvendelseV2")
public class LesHenvendelseWs implements ArkivertHenvendelseV2 {

    public static final String BASIC_AUTH_SEPERATOR = ":";
    public static final String SECURITY_TOKEN_SERVICE_URL = "https" + BASIC_AUTH_SEPERATOR + "//security-token-service.nais.preprod.local/rest/v1/sts/token/exchange";
    public static final String GRANT_TYPE_PARAM = "urn" + BASIC_AUTH_SEPERATOR + "ietf:params:oauth:grant-type:token-exchange";
    public static final String REQUESTED_TOKEN_TYPE_PARAM = "urn" + BASIC_AUTH_SEPERATOR + "ietf:params:oauth:token-type:access_token";
    public static final String SUBJECT_TOKEN_TYPE_PARAM = "urn" + BASIC_AUTH_SEPERATOR + "ietf:params:oauth:token-type:saml2";
    public static final String MODIASOAPRESTPROXY_SYSTEM_USER = "srvmodiasoaprestpr";
    public static final String MODIASOAPRESTPROXY_SYSTEM_USER_PASSWORD = System.getProperty("no.nav.modig.security.systemuser.password");

    @Override
    public void ping() {
    }

    @Override
    public List<Arkivpost> hentArkiverteHenvendelser(String s, Filter filter) {
        return null;
    }

    @Override
    public List<ArkivpostTemagruppe> hentArkiverteTemagrupper(String s) {
        return null;
    }

    @Override
    public Arkivpost hentArkivertHenvendelse(String arkivpostId) {
        Message currentMessage = PhaseInterceptorChain.getCurrentMessage();
        String oidcToken = konverterSamlTokenTilOIDCToken(currentMessage);

        Arkivpost arkivpost = hentArkivpostFraRestService(oidcToken, arkivpostId);

        return arkivpost;
    }

    private String konverterSamlTokenTilOIDCToken(Message currentMessage) {
        String samlAssertion = getSamlAssertion(currentMessage);
        String encodedSamlToken = Base64.getUrlEncoder().encodeToString(samlAssertion.getBytes());
        ResponseEntity<String> tokenResponse = getOidcTokenFromSamlToken(encodedSamlToken);

        JsonParser jsonParser = new JsonParser();
        JsonElement jsonResponse = jsonParser.parse(tokenResponse.getBody());
        JsonElement oidcToken = jsonResponse.getAsJsonObject().get("access_token");

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

        return restTemplate.postForEntity(SECURITY_TOKEN_SERVICE_URL, request, String.class);
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

    private Arkivpost hentArkivpostFraRestService(String oidcToken, String arkivpostId) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.set("Authorization", "Bearer " + oidcToken);

        ResponseEntity<String> arkivPost = restTemplate.getForEntity("http" + BASIC_AUTH_SEPERATOR + "//localhost:7070/isAlive?id=" + arkivpostId, String.class);

        JsonParser parser = new JsonParser();
        JsonObject o = parser.parse(arkivPost.toString()).getAsJsonObject();

        ArkivpostMapper mapper = new ArkivpostMapper();
        return mapper.mapToArkivpost(o);
    }


    private String getSamlAssertionAsString(Assertion assertion) throws TransformerException {
        StringWriter writer = new StringWriter();
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.transform(new DOMSource(assertion.getDOM()), new StreamResult(writer));
        return writer.toString();
    }


}
