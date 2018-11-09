package no.nav.sbl.dialogarena.modiasoaprest.mapping;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import no.nav.tjeneste.domene.brukerdialog.arkivtjenester.v2.typer.*;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class ArkivpostMapper {

    private Gson gson;

    public ArkivpostMapper() {
        gson = new GsonBuilder().create();
    }

    public String mapToJson(List<Arkivpost> arkrivposter) {
        return String.format("[%s]", String.join(",", arkrivposter.stream().map(a -> mapToJson(a)).toArray(String[]::new)));
    }

    public String mapToJson(Arkivpost post) {
        JsonObject obj = new JsonObject();
        obj.addProperty("arkivpostId", post.getArkivpostId());
        obj.addProperty("arkivertDato", JodaDatetimeMapper.jodaDatetimeToString(post.getDokumentDato()));
        obj.addProperty("mottattDato", JodaDatetimeMapper.jodaDatetimeToString(post.getMottattDato()));
        obj.addProperty("utgaarDato", JodaDatetimeMapper.jodaDatetimeToString(post.getUtgaarDato()));
        obj.addProperty("temagruppe", post.getArkivtema());
        obj.addProperty("arkivpostType", post.getType().value());
        obj.addProperty("dokumentType", post.getDokumentinfoRelasjon().getDokumenttype());
        if (post.getKryssreferanse() != null) {
            obj.addProperty("kryssreferanseId", post.getKryssreferanse().getReferanseId());
            obj.addProperty("kryssreferanseKode", post.getKryssreferanse().getReferansekode());
        }
        if (post.getEksternPart() != null) {
            Person eksternPart = (Person) post.getEksternPart();
            obj.addProperty("eksternPartAktorId", eksternPart.getAktoerId());
            obj.addProperty("eksternPartFodselsnummer", eksternPart.getFodselsnummer());
        }
        obj.addProperty("kanal", post.getKanal());
        if (post.getType() == ArkivpostType.INNGAAENDE) {
            Person p = (Person) post.getForBruker();
            obj.addProperty("fodselsnummer", p.getFodselsnummer());
            obj.addProperty("aktoerId", p.getAktoerId());
        } else {
            Saksbehandler sb = (Saksbehandler) post.getForBruker();
            obj.addProperty("navIdent", sb.getNavIdent());
            Person p = (Person) post.getEksternPart();
            obj.addProperty("fodselsnummer", p.getFodselsnummer());
            obj.addProperty("aktoerId", p.getAktoerId());
        }
        obj.addProperty("innhold", post.getInnhold());
        obj.addProperty("journalfoerendeEnhet", post.getJournalfoerendeEnhetREF());
        obj.addProperty("status", post.getArkivStatus().toString());
        obj.addProperty("kategorikode", post.getDokumentinfoRelasjon().getKategorikode());
        List<DokumentInnhold> beskriverInnhold = post.getDokumentinfoRelasjon().getBeskriverInnhold();
        if(beskriverInnhold != null) {
            obj.add("beskriverInnhold", gson.toJsonTree(beskriverInnhold));
            //obj.addProperty("beskriverInnhold",gson.toJson(beskriverInnhold));
        }
        obj.addProperty("signert", post.isSignert());
        obj.addProperty("erOrganInternt", post.isErOrganinternt());
        obj.addProperty("sensitiv", post.getDokumentinfoRelasjon().isSensitivitet());
        return gson.toJson(obj);
    }

    public List<Arkivpost> mapToArkivpostList(JsonArray jsonArray) {
        ArrayList<Arkivpost> result = new ArrayList<>();
        for(JsonElement element : jsonArray) {
            result.add(mapToArkivpost(element.getAsJsonObject()));
        }
        return result;
    }

    //Todo feilhåndtering
    public Arkivpost mapToArkivpost(JsonObject obj) {
        Arkivpost ap = new Arkivpost();
        ap.setArkivpostId(obj.getAsJsonPrimitive("arkivpostId").getAsString());
        ap.setDokumentDato(DateTime.parse(obj.getAsJsonPrimitive("arkivertDato").getAsString()));
        ap.setMottattDato(DateTime.parse(obj.getAsJsonPrimitive("mottattDato").getAsString()));
        ap.setUtgaarDato(DateTime.parse(obj.getAsJsonPrimitive("utgaarDato").getAsString()));
        ap.setArkivtema(obj.getAsJsonPrimitive("temagruppe").getAsString());
        ap.setType(ArkivpostType.fromValue(obj.getAsJsonPrimitive("arkivpostType").getAsString()));
        DokumentinfoRelasjon dr = new DokumentinfoRelasjon();
        dr.setDokumenttype(obj.getAsJsonPrimitive("dokumentType").getAsString());
        dr.setKategorikode(obj.getAsJsonPrimitive("kategorikode").getAsString());
        dr.setSensitivitet(obj.getAsJsonPrimitive("sensitiv").getAsBoolean());

        List<DokumentInnhold> beskriverInnhold = dr.getBeskriverInnhold();

        JsonArray vedleggJsonArray = obj.getAsJsonArray("beskriverInnhold");
        if(vedleggJsonArray != null) {
            beskriverInnhold.addAll(gson.fromJson(vedleggJsonArray, new TypeToken<List<DokumentInnhold>>(){}.getType()));
        }
        
        ap.setDokumentinfoRelasjon(dr);
        ap.setKryssreferanse(new Kryssreferanse()
                .withReferansekode("DIALOG_REKKE")
                .withReferanseId(obj.getAsJsonPrimitive("kryssreferanseId").getAsString()));
        ap.setKanal(obj.getAsJsonPrimitive("kanal").getAsString());
        if (ap.getType() != ArkivpostType.INNGAAENDE) {
            ap.setForBruker(new Saksbehandler().withNavIdent(obj.getAsJsonPrimitive("navIdent").getAsString()));
        }
        ap.setForBruker(new Person().withAktoerId(obj.getAsJsonPrimitive("aktoerId").getAsString())
                .withFodselsnummer(obj.getAsJsonPrimitive("fodselsnummer").getAsString()));

        ap.setInnhold(obj.getAsJsonPrimitive("innhold").getAsString());
        ap.setJournalfoerendeEnhetREF(obj.getAsJsonPrimitive("journalfoerendeEnhet").getAsString());
        ap.setArkivStatus(ArkivStatusType.fromValue(obj.getAsJsonPrimitive("status").getAsString()));
        ap.setSignert(obj.getAsJsonPrimitive("signert").getAsBoolean());
        ap.setErOrganinternt(obj.getAsJsonPrimitive("erOrganInternt").getAsBoolean());
        return ap;
    }
}
