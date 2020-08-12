package no.nav.sbl.dialogarena.modiasoaprest;

import no.nav.common.utils.Credentials;
import no.nav.common.utils.EnvironmentUtils;
import no.nav.common.utils.NaisUtils;
import no.nav.common.utils.SslUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static no.nav.common.utils.EnvironmentUtils.Type.PUBLIC;
import static no.nav.common.utils.EnvironmentUtils.Type.SECRET;
import static no.nav.sbl.dialogarena.modiasoaprest.common.Constants.SERVICEUSER_PASSWORD_PROPERTYNAME;
import static no.nav.sbl.dialogarena.modiasoaprest.common.Constants.SERVICEUSER_USERNAME_PROPERTYNAME;

@SpringBootApplication
public class Main {
    public static void main(String... args) {
        setupVault();
        SslUtils.setupTruststore();
        SpringApplication.run(Main.class, args);
    }


    private static void setupVault() {
        Credentials serviceUser = NaisUtils.getCredentials("service_user");
        EnvironmentUtils.setProperty(SERVICEUSER_USERNAME_PROPERTYNAME, serviceUser.username, PUBLIC);
        EnvironmentUtils.setProperty(SERVICEUSER_PASSWORD_PROPERTYNAME, serviceUser.password, SECRET);
    }
}
