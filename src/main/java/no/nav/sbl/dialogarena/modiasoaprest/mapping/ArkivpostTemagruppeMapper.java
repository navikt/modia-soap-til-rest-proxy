package no.nav.sbl.dialogarena.modiasoaprest.mapping;

import com.google.gson.*;
import no.nav.tjeneste.domene.brukerdialog.arkivtjenester.v2.typer.ArkivStatusType;
import no.nav.tjeneste.domene.brukerdialog.arkivtjenester.v2.typer.Arkivpost;
import no.nav.tjeneste.domene.brukerdialog.arkivtjenester.v2.typer.ArkivpostTemagruppe;

import java.util.ArrayList;
import java.util.List;

public class ArkivpostTemagruppeMapper {

    private Gson gson;

    public ArkivpostTemagruppeMapper() {
        gson = new GsonBuilder().create();
    }

    public String mapToJson(List<ArkivpostTemagruppe> arkrivpostTemagruppe) {
        return "";
    }

    public List<ArkivpostTemagruppe> mapToArkivpostTemagruppe(JsonArray arkivPostTemagrupper) {
        List<ArkivpostTemagruppe> result = new ArrayList<>();
        if(arkivPostTemagrupper != null) {
            for (JsonElement element : arkivPostTemagrupper) {
                JsonObject elementJsonObject = element.getAsJsonObject();

                ArkivpostTemagruppe arkivpostTemagruppe = new ArkivpostTemagruppe();

                arkivpostTemagruppe.setArkivpostId(elementJsonObject.getAsJsonPrimitive("arkivpostId").getAsString());
                arkivpostTemagruppe.setAktoerId(elementJsonObject.getAsJsonPrimitive("aktoerId").getAsString());
                arkivpostTemagruppe.setFodselsnummer(elementJsonObject.getAsJsonPrimitive("fodselsnummer").getAsString());
                arkivpostTemagruppe.setStatus(ArkivStatusType.valueOf(elementJsonObject.getAsJsonPrimitive("arkivStatusType").getAsString()));
                arkivpostTemagruppe.setTemagruppe(elementJsonObject.getAsJsonPrimitive("temagruppe").getAsString());

                result.add(arkivpostTemagruppe);
            }
        }

        return result;
    }
}
