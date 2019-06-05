package no.nav.sbl.dialogarena.modiasoaprest.mapping;

import com.google.gson.*;
import no.nav.tjeneste.domene.brukerdialog.arkivtjenester.v2.typer.*;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static no.nav.sbl.dialogarena.modiasoaprest.common.Constants.KRYSSREFERANSEKODE_SPORMSMAL_OG_SVAR;

public class ArkivpostMapper {
    private Logger logger = LoggerFactory.getLogger(ArkivpostMapper.class);

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
        obj.addProperty("begrensetPartInnsyn", post.getDokumentinfoRelasjon().isBegrensetPartsInnsyn());

        List<DokumentInnhold> beskriverInnhold = post.getDokumentinfoRelasjon().getBeskriverInnhold();
        if(beskriverInnhold != null) {
            JsonArray array = new JsonArray();
            for (DokumentInnhold dokumentInnhold: beskriverInnhold) {
                JsonObject vedlegg = new JsonObject();

                vedlegg.addProperty("filnavn", dokumentInnhold.getFilnavn());
                vedlegg.addProperty("filtype", dokumentInnhold.getFiltype());
                vedlegg.addProperty("variantformat", dokumentInnhold.getVariantformat());
                vedlegg.addProperty("tittel", dokumentInnhold.getTittel());
                vedlegg.addProperty("brevkode", dokumentInnhold.getBrevkode());
                vedlegg.addProperty("strukturert", dokumentInnhold.isInnholdStrukturert());

                if(dokumentInnhold.getInnhold() != null) {
                    vedlegg.addProperty("dokument", Base64.getEncoder().encodeToString(dokumentInnhold.getInnhold()));
                }

                array.add(vedlegg);
            }
            obj.add("vedleggListe", array);
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

    public Arkivpost mapToArkivpost(JsonObject obj) {
        Arkivpost arkivpost = new Arkivpost();
        arkivpost.setArkivpostId(obj.getAsJsonPrimitive("arkivpostId").getAsString());
        arkivpost.setDokumentDato(DateTime.parse(obj.getAsJsonPrimitive("arkivertDato").getAsString()));
        arkivpost.setMottattDato(DateTime.parse(obj.getAsJsonPrimitive("mottattDato").getAsString()));
        arkivpost.setUtgaarDato(DateTime.parse(obj.getAsJsonPrimitive("utgaarDato").getAsString()));
        arkivpost.setArkivtema(obj.getAsJsonPrimitive("temagruppe").getAsString());
        arkivpost.setType(ArkivpostType.fromValue(obj.getAsJsonPrimitive("arkivpostType").getAsString()));

        DokumentinfoRelasjon dokumentinfoRelasjon = new DokumentinfoRelasjon();
        dokumentinfoRelasjon.setDokumenttype(obj.getAsJsonPrimitive("dokumentType").getAsString());
        dokumentinfoRelasjon.setKategorikode(obj.getAsJsonPrimitive("kategorikode").getAsString());
        dokumentinfoRelasjon.setSensitivitet(obj.getAsJsonPrimitive("sensitiv").getAsBoolean());

        List<DokumentInnhold> beskriverInnhold = dokumentinfoRelasjon.getBeskriverInnhold();
        JsonArray vedleggJsonArray = obj.getAsJsonArray("vedleggListe");
        if(vedleggJsonArray != null) {
            for (JsonElement element: vedleggJsonArray) {
                JsonObject elementJsonObject = element.getAsJsonObject();

                DokumentInnhold dokumentInnhold = new DokumentInnhold();
                dokumentInnhold.setBrevkode(elementJsonObject.getAsJsonPrimitive("brevkode").getAsString());
                dokumentInnhold.setFilnavn(elementJsonObject.getAsJsonPrimitive("filnavn").getAsString());
                dokumentInnhold.setFiltype(elementJsonObject.getAsJsonPrimitive("filtype").getAsString());
                dokumentInnhold.setTittel(elementJsonObject.getAsJsonPrimitive("tittel").getAsString());
                dokumentInnhold.setVariantformat(elementJsonObject.getAsJsonPrimitive("variantformat").getAsString());

                if(elementJsonObject.getAsJsonPrimitive("strukturert") != null) {
                    dokumentInnhold.setInnholdStrukturert(elementJsonObject.getAsJsonPrimitive("strukturert").getAsBoolean());
                }

                JsonPrimitive base64encodedInnhold = elementJsonObject.getAsJsonPrimitive("dokument");
                if(base64encodedInnhold != null && base64encodedInnhold.getAsString() != "") {
                    dokumentInnhold.setInnhold(Base64.getDecoder().decode(base64encodedInnhold.getAsString()));
                }

                beskriverInnhold.add(dokumentInnhold);
            }

        }
        if (beskriverInnhold.size() == 0) {
            logger.error("Fant tom beskriverInnhold for " + arkivpost.getArkivpostId());
        }
        arkivpost.setDokumentinfoRelasjon(dokumentinfoRelasjon);

        arkivpost.setKryssreferanse(new Kryssreferanse()
                .withReferansekode(KRYSSREFERANSEKODE_SPORMSMAL_OG_SVAR)
                .withReferanseId(obj.getAsJsonPrimitive("kryssreferanseId").getAsString()));
        arkivpost.setKanal(obj.getAsJsonPrimitive("kanal").getAsString());

        if (arkivpost.getType() != ArkivpostType.INNGAAENDE) {
            arkivpost.setForBruker(new Saksbehandler().withNavIdent(obj.getAsJsonPrimitive("navIdent").getAsString()));
        } else {
            arkivpost.setForBruker(new Person()
                    .withAktoerId(obj.getAsJsonPrimitive("aktoerId").getAsString())
                    .withFodselsnummer(obj.getAsJsonPrimitive("fodselsnummer").getAsString()));
        }

        arkivpost.setEksternPart(new Person().withAktoerId(obj.getAsJsonPrimitive("aktoerId").getAsString())
                .withFodselsnummer(obj.getAsJsonPrimitive("fodselsnummer").getAsString()));

        arkivpost.setInnhold(obj.getAsJsonPrimitive("innhold").getAsString());
        arkivpost.setJournalfoerendeEnhetREF(obj.getAsJsonPrimitive("journalfoerendeEnhet").getAsString());
        arkivpost.setArkivStatus(ArkivStatusType.fromValue(obj.getAsJsonPrimitive("status").getAsString()));
        arkivpost.setSignert(obj.getAsJsonPrimitive("signert").getAsBoolean());
        arkivpost.setErOrganinternt(obj.getAsJsonPrimitive("erOrganInternt").getAsBoolean());
        return arkivpost;
    }
}
