package no.nav.sbl.dialogarena.modiasoaprest.config;

import no.nav.apiapp.ApiApplication;
import no.nav.apiapp.config.ApiAppConfigurator;
import no.nav.apiapp.config.IssoConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import static no.nav.sbl.dialogarena.modiasoaprest.common.Constants.MODIASOAPRESTPROXY_SYSTEM_USER;
import static no.nav.sbl.dialogarena.modiasoaprest.common.Constants.MODIASOAPRESTPROXY_SYSTEM_USER_PASSWORD;

@Configuration
@EnableAspectJAutoProxy
@ComponentScan("no.nav.sbl.dialogarena.modiasoaprest")
public class ApplicationContext implements ApiApplication.NaisApiApplication {

    Logger logger = LoggerFactory.getLogger(ApplicationContext.class);

    @Override
    public void configure(ApiAppConfigurator apiAppConfigurator) {
        apiAppConfigurator
                .issoLogin(getIssoLoginConfig())
                .sts();
    }

    private IssoConfig getIssoLoginConfig() {
        return IssoConfig.builder().username(MODIASOAPRESTPROXY_SYSTEM_USER).password(MODIASOAPRESTPROXY_SYSTEM_USER_PASSWORD).build();
    }
}
