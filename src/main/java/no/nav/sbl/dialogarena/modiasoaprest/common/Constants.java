package no.nav.sbl.dialogarena.modiasoaprest.common;

import static no.nav.sbl.dialogarena.modiasoaprest.common.FasitProperties.getFasitProperty;

public class Constants {
    public static final String APPLICATION_NAME = "modia-soap-til-rest-proxy";

    public static final String BASIC_AUTH_SEPERATOR = ":";
    public static final String DATO_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    public static final String SECURITY_TOKEN_SERVICE_BASEURL = getFasitProperty("SECURITY_TOKEN_SERVICE_TOKEN_URL");
    public static final String GRANT_TYPE_PARAM = "urn:ietf:params:oauth:grant-type:token-exchange";
    public static final String REQUESTED_TOKEN_TYPE_PARAM = "urn:ietf:params:oauth:token-type:access_token";
    public static final String SUBJECT_TOKEN_TYPE_PARAM = "urn:ietf:params:oauth:token-type:saml2";

    public static final String MODIASOAPRESTPROXY_SYSTEM_USER = getFasitProperty("SRVMODIA_SOAP_TIL_REST_PROXY_USERNAME");
    public static final String MODIASOAPRESTPROXY_SYSTEM_USER_PASSWORD = getFasitProperty("SRVMODIA_SOAP_TIL_REST_PROXY_PASSWORD");

    public static final String HENVENDELSESARKIV_ARKIVPOST_BASEURL = getFasitProperty("HENVENDELSESARKIV_ARKIVPOST_URL");
    public static final String HENVENDELSESARKIV_TEMAGRUPPE_BASEURL = getFasitProperty("HENVENDELSESARKIV_TEMAGRUPPER_URL");

    public static final String KRYSSREFERANSEKODE_SPORMSMAL_OG_SVAR = "DIALOG_REKKE";
}
