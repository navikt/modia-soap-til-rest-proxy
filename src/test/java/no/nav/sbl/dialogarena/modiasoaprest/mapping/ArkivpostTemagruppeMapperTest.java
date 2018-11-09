package no.nav.sbl.dialogarena.modiasoaprest.mapping;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import no.nav.tjeneste.domene.brukerdialog.arkivtjenester.v2.typer.ArkivStatusType;
import no.nav.tjeneste.domene.brukerdialog.arkivtjenester.v2.typer.ArkivpostTemagruppe;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ArkivpostTemagruppeMapperTest {

    ArkivpostTemagruppeMapper mapper = new ArkivpostTemagruppeMapper();
    JsonParser parser = new JsonParser();


    @Test
    public void skalMappeFraJsonTilArkivpostTemagruppe () {
        List<ArkivpostTemagruppe> expectedTemaGrupper = createArkivpostTemaGruppe(3);

        String json = "[{\"arkivpostId\": \"ID1234560\",\n" +
                "  \"aktoerId\": \"123123123\",\n" +
                "  \"fodselsnummer\": \"12345678901\",\n" +
                "  \"temagruppe\": \"ARBD\",\n" +
                "  \"arkivStatusType\": \"ARKIVERT\"}, {\"arkivpostId\": \"ID1234561\",\n" +
                "  \"aktoerId\": \"123123123\",\n" +
                "  \"fodselsnummer\": \"12345678901\",\n" +
                "  \"temagruppe\": \"ARBD\",\n" +
                "  \"arkivStatusType\": \"ARKIVERT\"}, {\"arkivpostId\": \"ID1234562\",\n" +
                "    \"aktoerId\": \"123123123\",\n" +
                "    \"fodselsnummer\": \"12345678901\",\n" +
                "    \"temagruppe\": \"ARBD\",\n" +
                "    \"arkivStatusType\": \"ARKIVERT\"}" +
                "]";
        JsonArray jsonArray = parser.parse(json).getAsJsonArray();

        List<ArkivpostTemagruppe> actualArkivpostTemagruppe = mapper.mapToArkivpostTemagruppe(jsonArray);

        assertEquals(3, actualArkivpostTemagruppe.size());
        assertEquals(expectedTemaGrupper.get(0).getFodselsnummer(), actualArkivpostTemagruppe.get(0).getFodselsnummer());
        assertEquals(expectedTemaGrupper.get(0).getAktoerId(), actualArkivpostTemagruppe.get(0).getAktoerId());
        assertEquals(expectedTemaGrupper.get(0).getArkivpostId(), actualArkivpostTemagruppe.get(0).getArkivpostId());
        assertEquals(expectedTemaGrupper.get(0).getStatus(), actualArkivpostTemagruppe.get(0).getStatus());
        assertEquals(expectedTemaGrupper.get(0).getTemagruppe(), actualArkivpostTemagruppe.get(0).getTemagruppe());

    }

    private List<ArkivpostTemagruppe> createArkivpostTemaGruppe(int antall) {
        List<ArkivpostTemagruppe> result = new ArrayList<>();
        for (int i=0; i<antall; i++) {
            ArkivpostTemagruppe arkivpostTemagruppe = new ArkivpostTemagruppe()
                    .withArkivpostId("ID123456"+i)
                    .withAktoerId("123123123")
                    .withFodselsnummer("12345678901")
                    .withStatus(ArkivStatusType.ARKIVERT)
                    .withTemagruppe("ARBD");

            result.add(arkivpostTemagruppe);
        }

        return result;
    }
}