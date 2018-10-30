package no.nav.sbl.dialogarena.modiasoaprest.common;

public class Constants {
    public static final String BASIC_AUTH_SEPERATOR = ":";
    public static final String SECURITY_TOKEN_SERVICE_URL = "https://security-token-service.nais.preprod.local/rest/v1/sts/token/exchange";
    public static final String TODO_HENVENDELSESARKIV_REST_URL = "http://localhost:7070/isAlive?id=";
    public static final String GRANT_TYPE_PARAM = "urn:ietf:params:oauth:grant-type:token-exchange";
    public static final String REQUESTED_TOKEN_TYPE_PARAM = "urn:ietf:params:oauth:token-type:access_token";
    public static final String SUBJECT_TOKEN_TYPE_PARAM = "urn:ietf:params:oauth:token-type:saml2";
    public static final String MODIASOAPRESTPROXY_SYSTEM_USER = System.getProperty("srvmodia-soap-til-rest-proxy_username");
    public static final String MODIASOAPRESTPROXY_SYSTEM_USER_PASSWORD = System.getProperty("srvmodia-soap-til-rest-proxy_password");
    public static final String APPLICATION_NAME = "modia-soap-til-rest-proxy";
}
