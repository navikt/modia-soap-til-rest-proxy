package no.nav.sbl.dialogarena.modiasoaprest.provider;

import no.nav.dialogarena.config.DevelopmentSecurity;
import no.nav.sbl.dialogarena.common.jetty.Jetty;
import no.nav.sbl.dialogarena.test.SystemProperties;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static java.lang.System.setProperty;
import static no.nav.sbl.dialogarena.common.jetty.Jetty.usingWar;
import static no.nav.sbl.dialogarena.common.jetty.JettyStarterUtils.*;

public class StartJetty {
    private static final int PORT = 8991;

    public static final String APPLICATION_NAME = "veilarbdialog";
    
    public static void main(String[] args) throws Exception {
        setProperty("henvendelsesarkiv.informasjon.endpoint.url", "https://modapp-t6.adeo.no/henvendelsesarkiv/services/domene.Brukerdialog/ArkivertHenvendelse_v1");
        setProperty("henvendelsesarkiv.aktivitet.endpoint.url", "https://modapp-t6.adeo.no/henvendelsesarkiv/services/domene.Brukerdialog/ArkiverHenvendelseBehandling_v1");

        try {
            SystemProperties.setFrom(new FileInputStream("credentials.properties"));
        } catch (FileNotFoundException e) {
            System.err.println("credentials.properties mangler. Kopier den fra noen andre!");
        }

        Jetty jetty = DevelopmentSecurity.setupSamlLogin(usingWar()
                        .at("/modia-soap-rest-proxy")
                        .port(PORT)
                        .sslPort(PORT + 1)
                , new DevelopmentSecurity.SamlSecurityConfig(APPLICATION_NAME)
        ).buildJetty();

        jetty.startAnd(first(waitFor(gotKeypress())).then(jetty.stop));
    }
}
