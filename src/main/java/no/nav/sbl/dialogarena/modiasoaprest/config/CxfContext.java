package no.nav.sbl.dialogarena.modiasoaprest.config;

import no.nav.sbl.dialogarena.modiasoaprest.soap.BehandleHenvendelseWs;
import no.nav.sbl.dialogarena.modiasoaprest.soap.LesHenvendelseWs;
import no.nav.sbl.dialogarena.modiasoaprest.soap.infra.SoapServlet;
import no.nav.tjeneste.domene.brukerdialog.arkiverhenvendelsebehandling.v2.aktivitet.ArkiverHenvendelseBehandlingV2;
import no.nav.tjeneste.domene.brukerdialog.arkiverthenvendelse.v2.informasjon.ArkivertHenvendelseV2;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

@Configuration
public class CxfContext {
    @Bean
    public ArkiverHenvendelseBehandlingV2 arkiverHenvendelseBehandlingV2() {
        return new BehandleHenvendelseWs();
    }

    @Bean
    public ArkivertHenvendelseV2 arkivertHenvendelseV2() {
        return new LesHenvendelseWs();
    }

    @Bean
    public ServletRegistrationBean<SoapServlet> registerCXF(
            ArkiverHenvendelseBehandlingV2 arkiverHenvendelseBehandlingV2,
            ArkivertHenvendelseV2 arkivertHenvendelseV2
    ) {
        SoapServlet soap = new SoapServlet(new HashMap<>(){{
            put("/ArkiverHenvendelseBehandling_v2", arkiverHenvendelseBehandlingV2);
            put("/ArkivertHenvendelseV2", arkivertHenvendelseV2);
        }});

        ServletRegistrationBean<SoapServlet> registrationBean = new ServletRegistrationBean<>(soap, "/ws/*");
        registrationBean.setLoadOnStartup(1);

        return registrationBean;
    }
}
