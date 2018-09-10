package no.nav.sbl.dialogarena.mapping;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import no.nav.tjeneste.domene.brukerdialog.arkivtjenester.v2.typer.*;
import org.joda.time.DateTime;

public class ArkivpostMapper {

    private Gson gson;

    public ArkivpostMapper() {
        gson = new GsonBuilder().create();
    }

    public String mapToJson(Arkivpost post) {
        JsonObject obj = new JsonObject();
        obj.addProperty("arkivertDato", JodaDatetimeMapper.jodaDatetimeToString(post.getDokumentDato()));
        obj.addProperty("mottattDato", JodaDatetimeMapper.jodaDatetimeToString(post.getMottattDato()));
        obj.addProperty("utgaarDato", JodaDatetimeMapper.jodaDatetimeToString(post.getUtgaarDato()));
        obj.addProperty("temagruppe", post.getArkivtema());
        obj.addProperty("arkivpostType", post.getType().value());
        obj.addProperty("dokumentType", post.getDokumentinfoRelasjon().getDokumenttype());
        if(post.getKryssreferanse() != null) {
            obj.addProperty("kryssreferanseId", post.getKryssreferanse().getReferanseId());
        }
        obj.addProperty("kanal", post.getKanal());
        if(post.getType() == ArkivpostType.INNGAAENDE) {
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
        obj.addProperty("kategoriKode", post.getDokumentinfoRelasjon().getKategorikode());
        obj.addProperty("signert", post.isSignert());
        obj.addProperty("erOrganInternt", post.isErOrganinternt());
        obj.addProperty("sensitiv", post.getDokumentinfoRelasjon().isSensitivitet());
        return gson.toJson(obj);
    }

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
        dr.setKategorikode(obj.getAsJsonPrimitive("kategoriKode").getAsString());
        dr.setSensitivitet(obj.getAsJsonPrimitive("arkivpostId").getAsBoolean());
        ap.setDokumentinfoRelasjon(dr);
        ap.setKryssreferanse(new Kryssreferanse().withReferanseId(obj.getAsJsonPrimitive("kryssreferanseId").getAsString()));
        ap.setKanal(obj.getAsJsonPrimitive("kanal").getAsString());
        if(ap.getType() == ArkivpostType.INNGAAENDE) {
            ap.setForBruker(new Person().withAktoerId(obj.getAsJsonPrimitive("aktoerId").getAsString())
                    .withFodselsnummer(obj.getAsJsonPrimitive("fodselsnummer").getAsString()));
        } else {
            ap.setForBruker(new Saksbehandler().withNavIdent(obj.getAsJsonPrimitive("navIdent").getAsString()));
            ap.setEksternPart(new Person().withAktoerId(obj.getAsJsonPrimitive("aktoerId").getAsString())
                    .withFodselsnummer(obj.getAsJsonPrimitive("fodselsnummer").getAsString()));
        }
        ap.setInnhold(obj.getAsJsonPrimitive("innhold").getAsString());
        ap.setJournalfoerendeEnhetREF(obj.getAsJsonPrimitive("journalfoerendeEnhet").getAsString());
        ap.setArkivStatus(ArkivStatusType.fromValue(obj.getAsJsonPrimitive("status").getAsString()));
        ap.setSignert(obj.getAsJsonPrimitive("signert").getAsBoolean());
        ap.setErOrganinternt(obj.getAsJsonPrimitive("erOrganInternt").getAsBoolean());
        return ap;
    }
}
