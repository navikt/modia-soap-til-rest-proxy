package no.nav.sbl.dialogarena.modiasoaprest.soap;

import no.nav.apiapp.soap.SoapTjeneste;
import no.nav.tjeneste.domene.brukerdialog.arkiverthenvendelse.v2.informasjon.ArkivertHenvendelseV2;
import no.nav.tjeneste.domene.brukerdialog.arkivtjenester.v2.typer.Arkivpost;
import no.nav.tjeneste.domene.brukerdialog.arkivtjenester.v2.typer.ArkivpostTemagruppe;
import no.nav.tjeneste.domene.brukerdialog.arkivtjenester.v2.typer.Filter;
import org.apache.cxf.interceptor.InInterceptors;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import java.util.List;

@Service
@InInterceptors(interceptors = {"no.nav.sbl.dialogarena.modiasoaprest.soap.MyInterceptor"})
@SoapTjeneste("/ArkivertHenvendelseV2")
public class LesHenvendelseWs implements ArkivertHenvendelseV2 {

    @Resource
    private WebServiceContext wsContext;

    @Override
    public List<Arkivpost> hentArkiverteHenvendelser(String s, Filter filter) {
        return null;
    }

    @Override
    public List<ArkivpostTemagruppe> hentArkiverteTemagrupper(String s) {
        return null;
    }

    @Override
    public Arkivpost hentArkivertHenvendelse(String s) {
        // konverter token (?)
        // kall rest-henvendelsesarkiv
        // returner
        System.out.println(wsContext);
        return null;
    }

    @Override
    public void ping() {

    }

    public void setWsContext(WebServiceContext wsContext) {
        this.wsContext = wsContext;
    }
}
