package no.nav.sbl.dialogarena.modiasoaprest.soap;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import no.nav.sbl.dialogarena.modiasoaprest.common.Constants;
import no.nav.sbl.dialogarena.modiasoaprest.mapping.ArkivpostMapper;
import no.nav.sbl.dialogarena.modiasoaprest.mapping.ArkivpostTemagruppeMapper;
import no.nav.tjeneste.domene.brukerdialog.arkivtjenester.v2.typer.Arkivpost;
import no.nav.tjeneste.domene.brukerdialog.arkivtjenester.v2.typer.ArkivpostTemagruppe;
import no.nav.tjeneste.domene.brukerdialog.arkivtjenester.v2.typer.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class RestUtils {
    private Logger logger = LoggerFactory.getLogger(RestUtils.class);

    List<ArkivpostTemagruppe> hentArkivpostTemagruppeFraRestService(String oidcToken, String aktorId) {
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> arkivPostTemagruppe = null;
        try {
            arkivPostTemagruppe = restTemplate.exchange(Constants.HENVENDELSESARKIV_TEMAGRUPPE_BASEURL + "/" + aktorId,
                    HttpMethod.GET, getRequestHttpEntity(oidcToken), String.class);
        } catch (RestClientException e) {
            logger.error("Feilet i henting av arkivpost", e);
            throw new RuntimeException("Feilet i henting av arkivpost", e);
        }

        JsonParser parser = new JsonParser();
        JsonArray o = parser.parse(arkivPostTemagruppe.getBody().toString()).getAsJsonArray();
        logger.info("###" + arkivPostTemagruppe.getBody().toString() + "###");

        ArkivpostTemagruppeMapper mapper = new ArkivpostTemagruppeMapper();
        return mapper.mapToArkivpostTemagruppe(o);
    }

    Arkivpost hentArkivpostFraRestService(String oidcToken, String arkivpostId) {
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> arkivPost = null;
        try {
            arkivPost = restTemplate.exchange(Constants.HENVENDELSESARKIV_ARKIVPOST_BASEURL + "/" + arkivpostId,
                    HttpMethod.GET, getRequestHttpEntity(oidcToken), String.class);
        } catch (RestClientException e) {
            logger.error("Feilet i henting av arkivpost", e);
            throw new RuntimeException("Feilet i henting av arkivpost", e);
        }

        JsonParser parser = new JsonParser();
        JsonObject o = parser.parse(arkivPost.getBody().toString()).getAsJsonObject();

        ArkivpostMapper mapper = new ArkivpostMapper();
        return mapper.mapToArkivpost(o);
    }

    List<Arkivpost> hentArkivposterFraRestService(String oidcToken, String aktorId, Filter filter) {
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> arkivPost = null;
        try {
            logger.info("### Henter arkivposter ###");
            arkivPost = restTemplate.exchange(Constants.HENVENDELSESARKIV_ARKIVPOST_BASEURL + "/aktoer/" + aktorId, HttpMethod.GET,
                    getRequestHttpEntity(oidcToken), String.class);
            logger.info("###" + arkivPost.getBody().toString() + "###");
        } catch (RestClientException e) {
            logger.error("Feilet i henting av arkivpost", e);
            throw new RuntimeException("Feilet i henting av arkivposter", e);
        }

        JsonParser parser = new JsonParser();
        JsonArray o = parser.parse(arkivPost.getBody().toString()).getAsJsonArray();

        ArkivpostMapper mapper = new ArkivpostMapper();

        return mapper.mapToArkivpostList(o);
    }

    private HttpEntity<String> getRequestHttpEntity(String oidcToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + oidcToken);
        return new HttpEntity<>(headers);
    }

    public Object arkiverHenvendelse(String oidcToken, Arkivpost arkivpost) {
        RestTemplate restTemplate = new RestTemplate();

        ArkivpostMapper mapper = new ArkivpostMapper();
        String arkivpostJson = mapper.mapToJson(arkivpost);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + oidcToken);
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.setAccept(
                Arrays.asList(MediaType.ALL)
        );
        HttpEntity<String> requestEntity = new HttpEntity<>(arkivpostJson, headers);

        ResponseEntity<Object> arkivpostId = null;
        try {
           logger.info("### lagrer henvendelse ###");
            arkivpostId = restTemplate.exchange(Constants.HENVENDELSESARKIV_ARKIVPOST_BASEURL,
                   HttpMethod.POST, requestEntity, Object.class);
        } catch (RestClientException e) {
            logger.error("Feilet i lagring av arkivpost", e);
            throw new RuntimeException("Feilet i lagring av arkivpost");
        }

        return arkivpostId.getBody();
    }
}