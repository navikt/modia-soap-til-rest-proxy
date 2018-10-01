package no.nav.sbl.dialogarena.soap;

import no.nav.tjeneste.domene.brukerdialog.arkiverhenvendelsebehandling.v2.aktivitet.ArkiverHenvendelseBehandlingV2;
import no.nav.tjeneste.domene.brukerdialog.arkivtjenester.v2.typer.Arkivpost;
import org.joda.time.DateTime;

public class BehandleHenvendelseWs implements ArkiverHenvendelseBehandlingV2 {

    public void ping() {

    }

    public void settUtgaarDato(String s, DateTime dateTime) {

    }

    public String arkiverHenvendelse(Arkivpost arkivpost) {
        return null;
    }
}
