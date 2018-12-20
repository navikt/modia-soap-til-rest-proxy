package no.nav.sbl.dialogarena.modiasoaprest.soap;

import no.nav.apiapp.soap.SoapTjeneste;
import no.nav.sbl.dialogarena.modiasoaprest.service.SamlToOidcService;
import no.nav.tjeneste.domene.brukerdialog.arkiverhenvendelsebehandling.v2.aktivitet.ArkiverHenvendelseBehandlingV2;
import no.nav.tjeneste.domene.brukerdialog.arkivtjenester.v2.typer.Arkivpost;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.PhaseInterceptorChain;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@SoapTjeneste("/ArkiverHenvendelseBehandling_v2")
public class BehandleHenvendelseWs implements ArkiverHenvendelseBehandlingV2 {

    Logger logger = LoggerFactory.getLogger(BehandleHenvendelseWs.class);

    @Autowired
    SamlToOidcService samlToOidcService;

    @Autowired
    RestUtils restUtils;

    @Override
    public void ping() {
    }

    @Override
    public void settUtgaarDato(String arkivpostId, DateTime dato) {
        Message currentMessage = PhaseInterceptorChain.getCurrentMessage();
        String oidcToken = null;
        try {
            oidcToken = samlToOidcService.konverterSamlTokenTilOIDCToken(currentMessage);
        } catch (Throwable t) {
            logger.error("Noe feila: ", t);
            throw t;
        }

        restUtils.settUtgaarDato(oidcToken, arkivpostId, dato);
    }

    @Override
    public String arkiverHenvendelse(Arkivpost arkivpost) {
        logger.info("i arkiverHenvendelse - start");
        Message currentMessage = PhaseInterceptorChain.getCurrentMessage();
        String oidcToken = null;
        try {
            oidcToken = samlToOidcService.konverterSamlTokenTilOIDCToken(currentMessage);
        } catch (Throwable t) {
            logger.error("Noe feila: ", t);
            throw t;
        }
        logger.info("i arkiverHenvendelse - har token");

        String arkivpostId = restUtils.arkiverHenvendelse(oidcToken, arkivpost);

        logger.info("i arkiverHenvendelse - har arkivpost");
        return arkivpostId;
    }
}
