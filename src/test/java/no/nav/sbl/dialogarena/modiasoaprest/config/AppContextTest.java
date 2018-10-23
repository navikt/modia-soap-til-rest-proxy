package no.nav.sbl.dialogarena.modiasoaprest.config;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static java.lang.System.setProperty;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ApplicationContext.class})
public class AppContextTest {

    @BeforeClass
    public static void setUp() {
        setProperty("henvendelsesarkiv.informasjon.endpoint.url", "https://modapp-t6.adeo.no/henvendelsesarkiv/services/domene.Brukerdialog/ArkivertHenvendelse_v1");
        setProperty("henvendelsesarkiv.aktivitet.endpoint.url", "https://modapp-t6.adeo.no/henvendelsesarkiv/services/domene.Brukerdialog/ArkiverHenvendelseBehandling_v1");
    }

    @Test
    public void skalStarteAppContext() {
    }
}
