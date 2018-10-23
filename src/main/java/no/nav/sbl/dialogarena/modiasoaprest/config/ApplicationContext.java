package no.nav.sbl.dialogarena.modiasoaprest.config;

import no.nav.apiapp.ApiApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
@ComponentScan("no.nav.sbl.dialogarena.modiasoaprest")
//@Import({AbacContext.class, AktorConfig.class})
//@Import({BehandleHenvendelseConfig.class, LesHenvendelseConfig.class})
public class ApplicationContext implements ApiApplication {


    @Override
    public String getApplicationName() {
        return "modia-soap-rest-proxy";
    }

    @Override
    public Sone getSone() {
        return Sone.FSS;
    }
}
