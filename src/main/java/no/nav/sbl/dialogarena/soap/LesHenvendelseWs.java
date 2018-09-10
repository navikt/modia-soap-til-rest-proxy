package no.nav.sbl.dialogarena.soap;

import no.nav.tjeneste.domene.brukerdialog.arkiverthenvendelse.v2.informasjon.ArkivertHenvendelseV2;
import no.nav.tjeneste.domene.brukerdialog.arkivtjenester.v2.typer.Arkivpost;
import no.nav.tjeneste.domene.brukerdialog.arkivtjenester.v2.typer.ArkivpostTemagruppe;
import no.nav.tjeneste.domene.brukerdialog.arkivtjenester.v2.typer.Filter;

import java.util.List;

public class LesHenvendelseWs implements ArkivertHenvendelseV2 {

    public List<Arkivpost> hentArkiverteHenvendelser(String s, Filter filter) {
        return null;
    }

    public List<ArkivpostTemagruppe> hentArkiverteTemagrupper(String s) {
        return null;
    }

    public Arkivpost hentArkivertHenvendelse(String s) {
        return null;
    }

    public void ping() {

    }
}
