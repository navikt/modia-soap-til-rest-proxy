package no.nav.sbl.dialogarena.modiasoaprest.soap;

import no.nav.apiapp.soap.SoapTjeneste;
import no.nav.sbl.dialogarena.modiasoaprest.service.SamlToOidcService;
import no.nav.tjeneste.domene.brukerdialog.arkiverhenvendelsebehandling.v2.aktivitet.ArkiverHenvendelseBehandlingV2;
import no.nav.tjeneste.domene.brukerdialog.arkivtjenester.v2.typer.Arkivpost;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.PhaseInterceptorChain;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@SoapTjeneste("/ArkiverHenvendelseBehandling_v2")
public class BehandleHenvendelseWs implements ArkiverHenvendelseBehandlingV2 {

    @Autowired
    SamlToOidcService samlToOidcService;

    @Autowired
    RestUtils restUtils;

    @Override
    public void ping() {
    }

    @Override
    public void settUtgaarDato(String arkivpostId, DateTime dateTime) {
        Message currentMessage = PhaseInterceptorChain.getCurrentMessage();
        String oidcToken = samlToOidcService.konverterSamlTokenTilOIDCToken(currentMessage);



        //TODO: Do call
    }

    @Override
    public String arkiverHenvendelse(Arkivpost arkivpost) {
        Message currentMessage = PhaseInterceptorChain.getCurrentMessage();
        String oidcToken = samlToOidcService.konverterSamlTokenTilOIDCToken(currentMessage);

        restUtils.arkiverHenvendelse(oidcToken, arkivpost);

        //TODO: Do call
        return null;
    }
}
