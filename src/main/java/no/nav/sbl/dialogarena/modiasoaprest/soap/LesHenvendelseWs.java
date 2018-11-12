package no.nav.sbl.dialogarena.modiasoaprest.soap;

import no.nav.apiapp.soap.SoapTjeneste;
import no.nav.sbl.dialogarena.modiasoaprest.service.SamlToOidcService;
import no.nav.tjeneste.domene.brukerdialog.arkiverthenvendelse.v2.informasjon.ArkivertHenvendelseV2;
import no.nav.tjeneste.domene.brukerdialog.arkivtjenester.v2.typer.Arkivpost;
import no.nav.tjeneste.domene.brukerdialog.arkivtjenester.v2.typer.ArkivpostTemagruppe;
import no.nav.tjeneste.domene.brukerdialog.arkivtjenester.v2.typer.Filter;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.PhaseInterceptorChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static no.nav.sbl.dialogarena.modiasoaprest.common.Constants.*;

@Service
@SoapTjeneste("/ArkivertHenvendelseV2")
public class LesHenvendelseWs implements ArkivertHenvendelseV2 {
    private Logger logger = LoggerFactory.getLogger(LesHenvendelseWs.class);

    @Autowired
    SamlToOidcService samlToOidcService;

    @Autowired
    RestUtils restUtils;

    @Override
    public void ping() {
    }

    @Override
    public List<Arkivpost> hentArkiverteHenvendelser(String aktorId, Filter filter) {

        logger.error("####");
        logger.error(TEST);
        logger.error("####");
        logger.error(TEST2);
        logger.error("####");
        logger.error(TEST3);
        logger.error("####");
        logger.error(TEST4);
        logger.error("####");

        Message currentMessage = PhaseInterceptorChain.getCurrentMessage();
        String oidcToken = samlToOidcService.konverterSamlTokenTilOIDCToken(currentMessage);

        return restUtils.hentArkivposterFraRestService(oidcToken, aktorId, filter);
    }

    @Override
    public List<ArkivpostTemagruppe> hentArkiverteTemagrupper(String aktorId) {
        Message currentMessage = PhaseInterceptorChain.getCurrentMessage();
        String oidcToken = samlToOidcService.konverterSamlTokenTilOIDCToken(currentMessage);
        
        List<ArkivpostTemagruppe> arkivpostTemagruppe = restUtils.hentArkivpostTemagruppeFraRestService(oidcToken, aktorId);
        return arkivpostTemagruppe;
    }

    @Override
    public Arkivpost hentArkivertHenvendelse(String arkivpostId) {
        Message currentMessage = PhaseInterceptorChain.getCurrentMessage();
        String oidcToken = samlToOidcService.konverterSamlTokenTilOIDCToken(currentMessage);

        Arkivpost arkivpost = restUtils.hentArkivpostFraRestService(oidcToken, arkivpostId);

        return arkivpost;
    }
}
