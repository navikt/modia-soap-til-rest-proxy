import no.nav.apiapp.ApiApp;
import no.nav.brukerdialog.security.Constants;
import no.nav.common.nais.utils.NaisUtils;
import no.nav.sbl.dialogarena.modiasoaprest.config.ApplicationContext;
import no.nav.sbl.util.EnvironmentUtils;

import static no.nav.sbl.dialogarena.modiasoaprest.common.Constants.SERVICEUSER_PASSWORD_PROPERTYNAME;
import static no.nav.sbl.dialogarena.modiasoaprest.common.Constants.SERVICEUSER_USERNAME_PROPERTYNAME;
import static no.nav.sbl.util.EnvironmentUtils.Type.PUBLIC;
import static no.nav.sbl.util.EnvironmentUtils.Type.SECRET;

public class Main {

    public static void main(String... args) {
        setupVault();
        ApiApp.runApp(ApplicationContext.class, args);
    }


    private static void setupVault() {
        NaisUtils.Credentials serviceUser = NaisUtils.getCredentials("service_user");
        EnvironmentUtils.setProperty(SERVICEUSER_USERNAME_PROPERTYNAME, serviceUser.username, PUBLIC);
        EnvironmentUtils.setProperty(SERVICEUSER_PASSWORD_PROPERTYNAME, serviceUser.password, SECRET);

        NaisUtils.Credentials issoRPUser = NaisUtils.getCredentials("isso-rp-user");
        EnvironmentUtils.setProperty(Constants.ISSO_RP_USER_USERNAME_PROPERTY_NAME, issoRPUser.username, PUBLIC);
        EnvironmentUtils.setProperty(Constants.ISSO_RP_USER_PASSWORD_PROPERTY_NAME, issoRPUser.password, SECRET);
    }
}
