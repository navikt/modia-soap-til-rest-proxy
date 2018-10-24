package no.nav.sbl.dialogarena.modiasoaprest.soap;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import no.nav.apiapp.soap.SoapTjeneste;
import no.nav.sbl.dialogarena.modiasoaprest.service.SamlToOidcService;
import no.nav.sbl.dialogarena.modiasoaprest.mapping.ArkivpostMapper;
import no.nav.tjeneste.domene.brukerdialog.arkiverthenvendelse.v2.informasjon.ArkivertHenvendelseV2;
import no.nav.tjeneste.domene.brukerdialog.arkivtjenester.v2.typer.Arkivpost;
import no.nav.tjeneste.domene.brukerdialog.arkivtjenester.v2.typer.ArkivpostTemagruppe;
import no.nav.tjeneste.domene.brukerdialog.arkivtjenester.v2.typer.Filter;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.PhaseInterceptorChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;
import java.util.List;

@Service
@SoapTjeneste("/ArkivertHenvendelseV2")
public class LesHenvendelseWs implements ArkivertHenvendelseV2 {

    private Logger logger = LoggerFactory.getLogger(LesHenvendelseWs.class);

    @Inject
    SamlToOidcService samlToOidcService;

    @Override
    public void ping() {
    }

    @Override
    public List<Arkivpost> hentArkiverteHenvendelser(String aktorId, Filter filter) {
        Message currentMessage = PhaseInterceptorChain.getCurrentMessage();
        String oidcToken = samlToOidcService.konverterSamlTokenTilOIDCToken(currentMessage);

        //TODO: Do call
        return null;
    }

    @Override
    public List<ArkivpostTemagruppe> hentArkiverteTemagrupper(String aktorId) {
        Message currentMessage = PhaseInterceptorChain.getCurrentMessage();
        String oidcToken = samlToOidcService.konverterSamlTokenTilOIDCToken(currentMessage);

        //TODO: Do call
        return null;
    }

    @Override
    public Arkivpost hentArkivertHenvendelse(String arkivpostId) {
        Message currentMessage = PhaseInterceptorChain.getCurrentMessage();
        String oidcToken = samlToOidcService.konverterSamlTokenTilOIDCToken(currentMessage);

        Arkivpost arkivpost = hentArkivpostFraRestService(oidcToken, arkivpostId);

        return arkivpost;
    }

    private Arkivpost hentArkivpostFraRestService(String oidcToken, String arkivpostId) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.set("Authorization", "Bearer " + oidcToken);

        ResponseEntity<String> arkivPost = null;
        try {
            arkivPost = restTemplate.getForEntity("http://localhost:7070/isAlive?id=" + arkivpostId, String.class);
        } catch (RestClientException e) {
            throw new RuntimeException("Feilet i henting av arkivpost", e);
        }

        JsonParser parser = new JsonParser();
        JsonObject o = parser.parse(arkivPost.toString()).getAsJsonObject();

        ArkivpostMapper mapper = new ArkivpostMapper();
        return mapper.mapToArkivpost(o);
    }
}
