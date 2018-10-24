package no.nav.sbl.dialogarena.modiasoaprest.soap;

import no.nav.apiapp.soap.SoapTjeneste;
import no.nav.tjeneste.domene.brukerdialog.arkiverhenvendelsebehandling.v2.aktivitet.ArkiverHenvendelseBehandlingV2;
import no.nav.tjeneste.domene.brukerdialog.arkivtjenester.v2.typer.Arkivpost;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

@Service
@SoapTjeneste("/ArkiverHenvendelseBehandling_v2")
public class BehandleHenvendelseWs implements ArkiverHenvendelseBehandlingV2 {

    @Override
    public void ping() {
        System.out.println("skal konvertere token og sende videre");


    }

    @Override
    public void settUtgaarDato(String arkivpostId, DateTime dateTime) {

    }

    @Override
    public String arkiverHenvendelse(Arkivpost arkivpost) {
        return null;
    }
}
