package no.nav.sbl.dialogarena.modiasoaprest.soap;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.rt.security.saml.claims.SAMLSecurityContext;
import org.apache.cxf.security.SecurityContext;
import org.apache.wss4j.common.principal.SAMLTokenPrincipal;
import org.jose4j.base64url.Base64;
import org.opensaml.saml.saml2.core.Assertion;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.HashMap;

public class MyInterceptor extends AbstractPhaseInterceptor<Message>{
    public MyInterceptor() {
        super(Phase.PRE_INVOKE);
    }

    @Override
    public void handleMessage(Message message) throws Fault {
        SAMLSecurityContext securityContext = (SAMLSecurityContext) message.get("org.apache.cxf.security.SecurityContext");

        SecurityContext sc = (SecurityContext) message.get(SecurityContext.class.getName());
        if(sc == null) {
            throw new RuntimeException("Cannot get SecurityContext from SoapMessage");
        }
        SAMLTokenPrincipal samlTokenPrincipal = (SAMLTokenPrincipal) sc.getUserPrincipal();
        if(samlTokenPrincipal == null) {
            throw new RuntimeException("Cannot get SAMLTokenPrincipal from SecurityContext");
        }
        Assertion assertion = samlTokenPrincipal.getToken().getSaml2();

        try {
            String samlAssertion = getSamlAssertionAsString(assertion);

            String encodedSamlToken = java.util.Base64.getUrlEncoder().encodeToString(samlAssertion.getBytes());

            String url = "https://security-token-service.nais.preprod.local/rest/v1/sts/token/exchange";

            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            String auth = "srvModiabrukerdialog:<pw>";
            String encodedAuth = "Basic <auth>";
            headers.set("Authorization", encodedAuth);

            MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
            map.add("grant_type", "urn:ietf:params:oauth:grant-type:token-exchange");
            map.add("requested_token_type", "urn:ietf:params:oauth:token-type:access_token");
            map.add("subject_token_type", "urn:ietf:params:oauth:token-type:saml2");
            map.add("subject_token", encodedSamlToken);


            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

            ResponseEntity<String> response = restTemplate.postForEntity( url, request , String.class );

            System.out.println("ya?");



        } catch (TransformerException e) {
            e.printStackTrace();
        }

        System.out.println(securityContext.getAssertionElement());

        //Base64 assertion
        //send assertion til converter
        //sett oidc-token på message

    }

    String getSamlAssertionAsString(Assertion assertion) throws TransformerException {
        StringWriter writer = new StringWriter();
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.transform(new DOMSource(assertion.getDOM()), new StreamResult(writer));
        return writer.toString();
    }

    @Override
    public void handleFault(Message message) {
        System.out.println("123");
    }
}
