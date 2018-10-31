import no.nav.sbl.dialogarena.modiasoaprest.config.AppContextTest;
import no.nav.testconfig.ApiAppTest;

class MainTest {

    public static final String TEST_PORT = "8802";

    public static void main(String[] args) {
        AppContextTest.setUp();



        ApiAppTest.setupTestContext(ApiAppTest.Config.builder().applicationName("modia-soap-til-rest-proxy").build());

        Main.main(TEST_PORT);
    }

}