package no.nav.sbl.dialogarena.modiasoaprest.soap;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import no.nav.apiapp.soap.SoapTjeneste;
import no.nav.sbl.dialogarena.modiasoaprest.mapping.ArkivpostMapper;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static no.nav.sbl.dialogarena.modiasoaprest.common.Constants.TODO_HENVENDELSESARKIV_REST_URL;

@Service
@SoapTjeneste("/ArkivertHenvendelseV2")
public class LesHenvendelseWs implements ArkivertHenvendelseV2 {
    private Logger logger = LoggerFactory.getLogger(LesHenvendelseWs.class);

    @Autowired
    SamlToOidcService samlToOidcService;

    @Override
    public void ping() {
    }

    @Override
    public List<Arkivpost> hentArkiverteHenvendelser(String aktorId, Filter filter) {
        Message currentMessage = PhaseInterceptorChain.getCurrentMessage();
        String oidcToken = samlToOidcService.konverterSamlTokenTilOIDCToken(currentMessage);

        return hentArkivposterFraRestService(oidcToken,aktorId,filter);
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
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> arkivPost = null;
        try {
            arkivPost = restTemplate.exchange(TODO_HENVENDELSESARKIV_REST_URL + arkivpostId, HttpMethod.GET, entity, String.class);
        } catch (RestClientException e) {
            throw new RuntimeException("Feilet i henting av arkivpost", e);
        }

        JsonParser parser = new JsonParser();
        JsonObject o = parser.parse(arkivPost.toString()).getAsJsonObject();

        ArkivpostMapper mapper = new ArkivpostMapper();
        return mapper.mapToArkivpost(o);
        
    }

    private List<Arkivpost> hentArkivposterFraRestService(String oidcToken, String aktorId, Filter filter) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + oidcToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> arkivPost = null;
        try {
            arkivPost = restTemplate.exchange(TODO_HENVENDELSESARKIV_REST_URL, HttpMethod.GET, entity, String.class);
        } catch (RestClientException e) {
            throw new RuntimeException("Feilet i henting av arkivposter", e);
        }

        JsonParser parser = new JsonParser();
        JsonArray o = parser.parse(arkivPost.toString()).getAsJsonArray();

        ArkivpostMapper mapper = new ArkivpostMapper();

        return mapper.mapToArkivpostList(o);

    }
}
