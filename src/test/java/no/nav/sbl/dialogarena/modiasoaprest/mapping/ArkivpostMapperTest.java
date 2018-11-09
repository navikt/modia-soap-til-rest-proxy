package no.nav.sbl.dialogarena.modiasoaprest.mapping;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import no.nav.tjeneste.domene.brukerdialog.arkivtjenester.v2.typer.*;
import org.joda.time.DateTime;
import org.junit.jupiter.api.Test;

import java.util.*;

import static no.nav.tjeneste.domene.brukerdialog.arkivtjenester.v2.typer.ArkivStatusType.ARKIVERT;
import static no.nav.tjeneste.domene.brukerdialog.arkivtjenester.v2.typer.ArkivpostType.INNGAAENDE;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ArkivpostMapperTest {

    ArkivpostMapper mapper = new ArkivpostMapper();
    JsonParser parser = new JsonParser();

    @Test
    public void base64() {
        String expectedString = "<ns2:metadataListe xmlns:ns2=\"http://nav.no/melding/domene/brukerdialog/behandlingsinformasjon/v1\" xmlns:ns4=\"http://nav.no/melding/virksomhet/dokumentforsendelse/v1\" xmlns:ns3=\"http://nav.no/melding/virksomhet/varsel/v1/varsel\">\n" +
                "    <metadata xsi:type=\"ns2:meldingTilBruker\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                "        <temagruppe>ARBD</temagruppe>\n" +
                "        <fritekst>dafasfasf</fritekst>\n" +
                "        <kanal>TELEFON</kanal>\n" +
                "        <navident>Z990370</navident>\n" +
                "    </metadata>\n" +
                "</ns2:metadataListe>";
        byte[] decode = Base64.getDecoder().decode("PG5zMjptZXRhZGF0YUxpc3RlIHhtbG5zOm5zMj0iaHR0cDovL25hdi5uby9tZWxkaW5nL2RvbWVuZS9icnVrZXJkaWFsb2cvYmVoYW5kbGluZ3NpbmZvcm1hc2pvbi92MSIgeG1sbnM6bnM0PSJodHRwOi8vbmF2Lm5vL21lbGRpbmcvdmlya3NvbWhldC9kb2t1bWVudGZvcnNlbmRlbHNlL3YxIiB4bWxuczpuczM9Imh0dHA6Ly9uYXYubm8vbWVsZGluZy92aXJrc29taGV0L3ZhcnNlbC92MS92YXJzZWwiPgogICAgPG1ldGFkYXRhIHhzaTp0eXBlPSJuczI6bWVsZGluZ1RpbEJydWtlciIgeG1sbnM6eHNpPSJodHRwOi8vd3d3LnczLm9yZy8yMDAxL1hNTFNjaGVtYS1pbnN0YW5jZSI+CiAgICAgICAgPHRlbWFncnVwcGU+QVJCRDwvdGVtYWdydXBwZT4KICAgICAgICA8ZnJpdGVrc3Q+ZGFmYXNmYXNmPC9mcml0ZWtzdD4KICAgICAgICA8a2FuYWw+VEVMRUZPTjwva2FuYWw+CiAgICAgICAgPG5hdmlkZW50Plo5OTAzNzA8L25hdmlkZW50PgogICAgPC9tZXRhZGF0YT4KPC9uczI6bWV0YWRhdGFMaXN0ZT4=");

        assertEquals(expectedString, new String(decode));
    }

    @Test
    public void skalMappeFraJsonTilArkivPost() {
        Arkivpost expectedArkivpost = lagArkivpost(2);
        String json = "{\"arkivpostId\":\"Id123\",\"arkivertDato\":\"2012-12-11T11:11:00\",\"mottattDato\":\"2012-12-12T12:12:00\",\"utgaarDato\":\"2013-12-11T11:11:00\",\"temagruppe\":\"temagruppe\",\"arkivpostType\":\"INNGAAENDE\",\"dokumentType\":\"dok-type\",\"kryssreferanseId\":\"ABC123\",\"kryssreferanseKode\":\"DIALOG_REKKE\",\"kanal\":\"kanal\",\"fodselsnummer\":\"fnr\",\"aktoerId\":\"aktor\",\"innhold\":\"innhold\",\"journalfoerendeEnhet\":\"journal-ref\",\"status\":\"ARKIVERT\",\"kategorikode\":\"kat\",\"vedleggListe\":[{\"filnavn\":\"fil0\",\"filtype\":\"xml0\",\"variantformat\":\"xml0\",\"tittel\":\"hei på deg0\",\"brevkode\":\"hva er dette?0\",\"innholdStrukturert\":true,\"innhold\":\"PG5zMjptZXRhZGF0YUxpc3RlIHhtbG5zOm5zMj0iaHR0cDovL25hdi5uby9tZWxkaW5nL2RvbWVuZS9icnVrZXJkaWFsb2cvYmVoYW5kbGluZ3NpbmZvcm1hc2pvbi92MSIgeG1sbnM6bnM0PSJodHRwOi8vbmF2Lm5vL21lbGRpbmcvdmlya3NvbWhldC9kb2t1bWVudGZvcnNlbmRlbHNlL3YxIiB4bWxuczpuczM9Imh0dHA6Ly9uYXYubm8vbWVsZGluZy92aXJrc29taGV0L3ZhcnNlbC92MS92YXJzZWwiPgogICAgPG1ldGFkYXRhIHhzaTp0eXBlPSJuczI6bWVsZGluZ1RpbEJydWtlciIgeG1sbnM6eHNpPSJodHRwOi8vd3d3LnczLm9yZy8yMDAxL1hNTFNjaGVtYS1pbnN0YW5jZSI+CiAgICAgICAgPHRlbWFncnVwcGU+QVJCRDwvdGVtYWdydXBwZT4KICAgICAgICA8ZnJpdGVrc3Q+ZGFmYXNmYXNmPC9mcml0ZWtzdD4KICAgICAgICA8a2FuYWw+VEVMRUZPTjwva2FuYWw+CiAgICAgICAgPG5hdmlkZW50Plo5OTAzNzA8L25hdmlkZW50PgogICAgPC9tZXRhZGF0YT4KPC9uczI6bWV0YWRhdGFMaXN0ZT4=\"},{\"filnavn\":\"fil1\",\"filtype\":\"xml1\",\"variantformat\":\"xml1\",\"tittel\":\"hei på deg1\",\"brevkode\":\"hva er dette?1\",\"innholdStrukturert\":true,\"innhold\":\"PG5zMjptZXRhZGF0YUxpc3RlIHhtbG5zOm5zMj0iaHR0cDovL25hdi5uby9tZWxkaW5nL2RvbWVuZS9icnVrZXJkaWFsb2cvYmVoYW5kbGluZ3NpbmZvcm1hc2pvbi92MSIgeG1sbnM6bnM0PSJodHRwOi8vbmF2Lm5vL21lbGRpbmcvdmlya3NvbWhldC9kb2t1bWVudGZvcnNlbmRlbHNlL3YxIiB4bWxuczpuczM9Imh0dHA6Ly9uYXYubm8vbWVsZGluZy92aXJrc29taGV0L3ZhcnNlbC92MS92YXJzZWwiPgogICAgPG1ldGFkYXRhIHhzaTp0eXBlPSJuczI6bWVsZGluZ1RpbEJydWtlciIgeG1sbnM6eHNpPSJodHRwOi8vd3d3LnczLm9yZy8yMDAxL1hNTFNjaGVtYS1pbnN0YW5jZSI+CiAgICAgICAgPHRlbWFncnVwcGU+QVJCRDwvdGVtYWdydXBwZT4KICAgICAgICA8ZnJpdGVrc3Q+ZGFmYXNmYXNmPC9mcml0ZWtzdD4KICAgICAgICA8a2FuYWw+VEVMRUZPTjwva2FuYWw+CiAgICAgICAgPG5hdmlkZW50Plo5OTAzNzA8L25hdmlkZW50PgogICAgPC9tZXRhZGF0YT4KPC9uczI6bWV0YWRhdGFMaXN0ZT4=\"}],\"signert\":false,\"erOrganInternt\":false,\"sensitiv\":true}";
        JsonObject jsonObject = parser.parse(json).getAsJsonObject();

        Arkivpost arkivpost = mapper.mapToArkivpost(jsonObject);

        DokumentInnhold expectedDokumentInnhold = expectedArkivpost.getDokumentinfoRelasjon().getBeskriverInnhold().get(0);
        DokumentInnhold actualDokumentInnhold = arkivpost.getDokumentinfoRelasjon().getBeskriverInnhold().get(0);

        assertEquals(expectedDokumentInnhold.getFilnavn(), actualDokumentInnhold.getFilnavn());
        assertEquals(expectedArkivpost.getMottattDato(), arkivpost.getMottattDato());
        assertEquals(expectedArkivpost.getArkivpostId(), arkivpost.getArkivpostId());
    }

    @Test
    public void skalMappeFraJsonTilArkivPostMedManglendeVedlegg() {
        Arkivpost expectedArkivpost = lagArkivpost(0);

        String json = "{\n" +
                "    \"arkivpostId\": Id123,\n" +
                "    \"arkivertDato\": \"2012-12-11T11:11:00\",\n" +
                "    \"mottattDato\": \"2012-12-12T12:12:00\",\n" +
                "    \"utgaarDato\": \"2013-12-11T11:11:00\",\n" +
                "    \"temagruppe\": \"temagruppe\",\n" +
                "    \"arkivpostType\": \"INNGAAENDE\",\n" +
                "    \"dokumentType\": \"dok-type\",\n" +
                "    \"kryssreferanseId\": \"ABC123\",\n" +
                "    \"kanal\": \"kanal\",\n" +
                "    \"aktoerId\": \"aktoerId\",\n" +
                "    \"fodselsnummer\": \"fnr\",\n" +
                "    \"innhold\": \"inhold\",\n" +
                "    \"journalfoerendeEnhet\": \"journal-ref\",\n" +
                "    \"status\": \"ARKIVERT\",\n" +
                "    \"kategorikode\": \"kat\",\n" +
                "    \"signert\": false,\n" +
                "    \"erOrganInternt\": false,\n" +
                "    \"begrensetPartInnsyn\": false,\n" +
                "    \"sensitiv\": true,\n" +
                "    \"vedleggListe\": [\n" +
                "      {\n" +
                "        \"arkivpostId\": ID123,\n" +
                "        \"filnavn\": \"fil0\",\n" +
                "        \"filtype\": \"xml0\",\n" +
                "        \"variantformat\": \"xml0\",\n" +
                "        \"tittel\": \"hei på deg0\",\n" +
                "        \"brevkode\": \"hva er dette?0\",\n" +
                "        \"strukturert\": true\n" +
                "      }\n" +
                "    ]\n" +
                "  }";

        JsonObject jsonObject = parser.parse(json).getAsJsonObject();

        Arkivpost arkivpost = mapper.mapToArkivpost(jsonObject);


        assertEquals(expectedArkivpost.getDokumentinfoRelasjon().getBeskriverInnhold().size(), 0);
        assertEquals(expectedArkivpost.getMottattDato(), arkivpost.getMottattDato());
        assertEquals(expectedArkivpost.getArkivpostId(), arkivpost.getArkivpostId());
    }

    @Test
    public void skalMappeFraJsonListeTilAkivpostListe() {
        List<Arkivpost> expectedArkivposter = Arrays.asList(
                lagArkivpost(1),
                lagArkivpost(2),
                lagArkivpost(0));

       String json = "[{\"arkivpostId\":\"Id123\",\"arkivertDato\":\"2012-12-11T11:11:00\",\"mottattDato\":\"2012-12-12T12:12:00\",\"utgaarDato\":\"2013-12-11T11:11:00\",\"temagruppe\":\"temagruppe\",\"arkivpostType\":\"INNGAAENDE\",\"dokumentType\":\"dok-type\",\"kryssreferanseId\":\"ABC123\",\"kryssreferanseKode\":\"DIALOG_REKKE\",\"kanal\":\"kanal\",\"fodselsnummer\":\"fnr\",\"aktoerId\":\"aktor\",\"innhold\":\"innhold\",\"journalfoerendeEnhet\":\"journal-ref\",\"status\":\"ARKIVERT\",\"kategorikode\":\"kat\",\"vedleggListe\":[{\"filnavn\":\"fil0\",\"filtype\":\"xml0\",\"variantformat\":\"xml0\",\"tittel\":\"hei på deg0\",\"brevkode\":\"hva er dette?0\",\"innholdStrukturert\":true,\"innhold\":\"PG5zMjptZXRhZGF0YUxpc3RlIHhtbG5zOm5zMj0iaHR0cDovL25hdi5uby9tZWxkaW5nL2RvbWVuZS9icnVrZXJkaWFsb2cvYmVoYW5kbGluZ3NpbmZvcm1hc2pvbi92MSIgeG1sbnM6bnM0PSJodHRwOi8vbmF2Lm5vL21lbGRpbmcvdmlya3NvbWhldC9kb2t1bWVudGZvcnNlbmRlbHNlL3YxIiB4bWxuczpuczM9Imh0dHA6Ly9uYXYubm8vbWVsZGluZy92aXJrc29taGV0L3ZhcnNlbC92MS92YXJzZWwiPgogICAgPG1ldGFkYXRhIHhzaTp0eXBlPSJuczI6bWVsZGluZ1RpbEJydWtlciIgeG1sbnM6eHNpPSJodHRwOi8vd3d3LnczLm9yZy8yMDAxL1hNTFNjaGVtYS1pbnN0YW5jZSI+CiAgICAgICAgPHRlbWFncnVwcGU+QVJCRDwvdGVtYWdydXBwZT4KICAgICAgICA8ZnJpdGVrc3Q+ZGFmYXNmYXNmPC9mcml0ZWtzdD4KICAgICAgICA8a2FuYWw+VEVMRUZPTjwva2FuYWw+CiAgICAgICAgPG5hdmlkZW50Plo5OTAzNzA8L25hdmlkZW50PgogICAgPC9tZXRhZGF0YT4KPC9uczI6bWV0YWRhdGFMaXN0ZT4=\"}],\"signert\":false,\"erOrganInternt\":false,\"sensitiv\":true},{\"arkivpostId\":\"Id123\",\"arkivertDato\":\"2012-12-11T11:11:00\",\"mottattDato\":\"2012-12-12T12:12:00\",\"utgaarDato\":\"2013-12-11T11:11:00\",\"temagruppe\":\"temagruppe\",\"arkivpostType\":\"INNGAAENDE\",\"dokumentType\":\"dok-type\",\"kryssreferanseId\":\"ABC123\",\"kryssreferanseKode\":\"DIALOG_REKKE\",\"kanal\":\"kanal\",\"fodselsnummer\":\"fnr\",\"aktoerId\":\"aktor\",\"innhold\":\"innhold\",\"journalfoerendeEnhet\":\"journal-ref\",\"status\":\"ARKIVERT\",\"kategorikode\":\"kat\",\"vedleggListe\":[{\"filnavn\":\"fil0\",\"filtype\":\"xml0\",\"variantformat\":\"xml0\",\"tittel\":\"hei på deg0\",\"brevkode\":\"hva er dette?0\",\"innholdStrukturert\":true,\"innhold\":\"PG5zMjptZXRhZGF0YUxpc3RlIHhtbG5zOm5zMj0iaHR0cDovL25hdi5uby9tZWxkaW5nL2RvbWVuZS9icnVrZXJkaWFsb2cvYmVoYW5kbGluZ3NpbmZvcm1hc2pvbi92MSIgeG1sbnM6bnM0PSJodHRwOi8vbmF2Lm5vL21lbGRpbmcvdmlya3NvbWhldC9kb2t1bWVudGZvcnNlbmRlbHNlL3YxIiB4bWxuczpuczM9Imh0dHA6Ly9uYXYubm8vbWVsZGluZy92aXJrc29taGV0L3ZhcnNlbC92MS92YXJzZWwiPgogICAgPG1ldGFkYXRhIHhzaTp0eXBlPSJuczI6bWVsZGluZ1RpbEJydWtlciIgeG1sbnM6eHNpPSJodHRwOi8vd3d3LnczLm9yZy8yMDAxL1hNTFNjaGVtYS1pbnN0YW5jZSI+CiAgICAgICAgPHRlbWFncnVwcGU+QVJCRDwvdGVtYWdydXBwZT4KICAgICAgICA8ZnJpdGVrc3Q+ZGFmYXNmYXNmPC9mcml0ZWtzdD4KICAgICAgICA8a2FuYWw+VEVMRUZPTjwva2FuYWw+CiAgICAgICAgPG5hdmlkZW50Plo5OTAzNzA8L25hdmlkZW50PgogICAgPC9tZXRhZGF0YT4KPC9uczI6bWV0YWRhdGFMaXN0ZT4=\"},{\"filnavn\":\"fil1\",\"filtype\":\"xml1\",\"variantformat\":\"xml1\",\"tittel\":\"hei på deg1\",\"brevkode\":\"hva er dette?1\",\"innholdStrukturert\":true,\"innhold\":\"PG5zMjptZXRhZGF0YUxpc3RlIHhtbG5zOm5zMj0iaHR0cDovL25hdi5uby9tZWxkaW5nL2RvbWVuZS9icnVrZXJkaWFsb2cvYmVoYW5kbGluZ3NpbmZvcm1hc2pvbi92MSIgeG1sbnM6bnM0PSJodHRwOi8vbmF2Lm5vL21lbGRpbmcvdmlya3NvbWhldC9kb2t1bWVudGZvcnNlbmRlbHNlL3YxIiB4bWxuczpuczM9Imh0dHA6Ly9uYXYubm8vbWVsZGluZy92aXJrc29taGV0L3ZhcnNlbC92MS92YXJzZWwiPgogICAgPG1ldGFkYXRhIHhzaTp0eXBlPSJuczI6bWVsZGluZ1RpbEJydWtlciIgeG1sbnM6eHNpPSJodHRwOi8vd3d3LnczLm9yZy8yMDAxL1hNTFNjaGVtYS1pbnN0YW5jZSI+CiAgICAgICAgPHRlbWFncnVwcGU+QVJCRDwvdGVtYWdydXBwZT4KICAgICAgICA8ZnJpdGVrc3Q+ZGFmYXNmYXNmPC9mcml0ZWtzdD4KICAgICAgICA8a2FuYWw+VEVMRUZPTjwva2FuYWw+CiAgICAgICAgPG5hdmlkZW50Plo5OTAzNzA8L25hdmlkZW50PgogICAgPC9tZXRhZGF0YT4KPC9uczI6bWV0YWRhdGFMaXN0ZT4=\"}],\"signert\":false,\"erOrganInternt\":false,\"sensitiv\":true},{\"arkivpostId\":\"Id123\",\"arkivertDato\":\"2012-12-11T11:11:00\",\"mottattDato\":\"2012-12-12T12:12:00\",\"utgaarDato\":\"2013-12-11T11:11:00\",\"temagruppe\":\"temagruppe\",\"arkivpostType\":\"INNGAAENDE\",\"dokumentType\":\"dok-type\",\"kryssreferanseId\":\"ABC123\",\"kryssreferanseKode\":\"DIALOG_REKKE\",\"kanal\":\"kanal\",\"fodselsnummer\":\"fnr\",\"aktoerId\":\"aktor\",\"innhold\":\"innhold\",\"journalfoerendeEnhet\":\"journal-ref\",\"status\":\"ARKIVERT\",\"kategorikode\":\"kat\",\"vedleggListe\":[],\"signert\":false,\"erOrganInternt\":false,\"sensitiv\":true}]";

        JsonArray jsonArray = parser.parse(json).getAsJsonArray();

        List<Arkivpost> arkivposts = mapper.mapToArkivpostList(jsonArray);

        assertEquals(3, arkivposts.size());
        assertEquals(expectedArkivposter.get(0).getArkivpostId(), arkivposts.get(0).getArkivpostId());
        assertEquals(expectedArkivposter.get(0).getType(), arkivposts.get(0).getType());
        assertEquals(expectedArkivposter.get(0).getUtgaarDato(), arkivposts.get(0).getUtgaarDato());

    }

    @Test
    public void skalMappeEnkeltArkivpostTilJson() {
        String expectedJson = "{\"arkivpostId\":\"Id123\",\"arkivertDato\":\"2012-12-11T11:11:00\",\"mottattDato\":\"2012-12-12T12:12:00\",\"utgaarDato\":\"2013-12-11T11:11:00\",\"temagruppe\":\"temagruppe\",\"arkivpostType\":\"INNGAAENDE\",\"dokumentType\":\"dok-type\",\"kryssreferanseId\":\"ABC123\",\"kryssreferanseKode\":\"DIALOG_REKKE\",\"kanal\":\"kanal\",\"fodselsnummer\":\"fnr\",\"aktoerId\":\"aktor\",\"innhold\":\"innhold\",\"journalfoerendeEnhet\":\"journal-ref\",\"status\":\"ARKIVERT\",\"kategorikode\":\"kat\",\"vedleggListe\":[{\"filnavn\":\"fil0\",\"filtype\":\"xml0\",\"variantformat\":\"xml0\",\"tittel\":\"hei på deg0\",\"brevkode\":\"hva er dette?0\",\"innholdStrukturert\":true,\"innhold\":[98,121,116,101,115,49]},{\"filnavn\":\"fil1\",\"filtype\":\"xml1\",\"variantformat\":\"xml1\",\"tittel\":\"hei på deg1\",\"brevkode\":\"hva er dette?1\",\"innholdStrukturert\":true,\"innhold\":[98,121,116,101,115,49]}],\"signert\":false,\"erOrganInternt\":false,\"sensitiv\":true}";
        Arkivpost arkivpost = lagArkivpost(2);

        String json = mapper.mapToJson(arkivpost);

        assertEquals(expectedJson, json);
    }

    @Test
    public void skalMappeArkivpostlisteTilJson() {
        String expectedJson = "[{\"arkivpostId\":\"Id123\",\"arkivertDato\":\"2012-12-11T11:11:00\",\"mottattDato\":\"2012-12-12T12:12:00\",\"utgaarDato\":\"2013-12-11T11:11:00\",\"temagruppe\":\"temagruppe\",\"arkivpostType\":\"INNGAAENDE\",\"dokumentType\":\"dok-type\",\"kryssreferanseId\":\"ABC123\",\"kryssreferanseKode\":\"DIALOG_REKKE\",\"kanal\":\"kanal\",\"fodselsnummer\":\"fnr\",\"aktoerId\":\"aktor\",\"innhold\":\"innhold\",\"journalfoerendeEnhet\":\"journal-ref\",\"status\":\"ARKIVERT\",\"kategorikode\":\"kat\",\"vedleggListe\":[{\"filnavn\":\"fil0\",\"filtype\":\"xml0\",\"variantformat\":\"xml0\",\"tittel\":\"hei på deg0\",\"brevkode\":\"hva er dette?0\",\"innholdStrukturert\":true,\"innhold\":[98,121,116,101,115,49]}],\"signert\":false,\"erOrganInternt\":false,\"sensitiv\":true},{\"arkivpostId\":\"Id123\",\"arkivertDato\":\"2012-12-11T11:11:00\",\"mottattDato\":\"2012-12-12T12:12:00\",\"utgaarDato\":\"2013-12-11T11:11:00\",\"temagruppe\":\"temagruppe\",\"arkivpostType\":\"INNGAAENDE\",\"dokumentType\":\"dok-type\",\"kryssreferanseId\":\"ABC123\",\"kryssreferanseKode\":\"DIALOG_REKKE\",\"kanal\":\"kanal\",\"fodselsnummer\":\"fnr\",\"aktoerId\":\"aktor\",\"innhold\":\"innhold\",\"journalfoerendeEnhet\":\"journal-ref\",\"status\":\"ARKIVERT\",\"kategorikode\":\"kat\",\"vedleggListe\":[{\"filnavn\":\"fil0\",\"filtype\":\"xml0\",\"variantformat\":\"xml0\",\"tittel\":\"hei på deg0\",\"brevkode\":\"hva er dette?0\",\"innholdStrukturert\":true,\"innhold\":[98,121,116,101,115,49]},{\"filnavn\":\"fil1\",\"filtype\":\"xml1\",\"variantformat\":\"xml1\",\"tittel\":\"hei på deg1\",\"brevkode\":\"hva er dette?1\",\"innholdStrukturert\":true,\"innhold\":[98,121,116,101,115,49]}],\"signert\":false,\"erOrganInternt\":false,\"sensitiv\":true},{\"arkivpostId\":\"Id123\",\"arkivertDato\":\"2012-12-11T11:11:00\",\"mottattDato\":\"2012-12-12T12:12:00\",\"utgaarDato\":\"2013-12-11T11:11:00\",\"temagruppe\":\"temagruppe\",\"arkivpostType\":\"INNGAAENDE\",\"dokumentType\":\"dok-type\",\"kryssreferanseId\":\"ABC123\",\"kryssreferanseKode\":\"DIALOG_REKKE\",\"kanal\":\"kanal\",\"fodselsnummer\":\"fnr\",\"aktoerId\":\"aktor\",\"innhold\":\"innhold\",\"journalfoerendeEnhet\":\"journal-ref\",\"status\":\"ARKIVERT\",\"kategorikode\":\"kat\",\"vedleggListe\":[],\"signert\":false,\"erOrganInternt\":false,\"sensitiv\":true}]";

        List<Arkivpost> arkivposter = Arrays.asList(
                lagArkivpost(1),
                lagArkivpost(2),
                lagArkivpost(0));

        String json = mapper.mapToJson(arkivposter);

        assertEquals(expectedJson, json);
    }


    private static Arkivpost lagArkivpost(final int antallDokumentinnhold) {
        return new Arkivpost()
                .withArkivpostId("Id123")
                .withType(INNGAAENDE)
                .withMottattDato(new DateTime(2012,12,12,12,12))
                .withDokumentDato(new DateTime(2012,12,11,11,11))
                .withJournalfoerendeEnhetREF("journal-ref")
                .withKanal("kanal")
                .withSignert(false)
                .withArkivtema("temagruppe")
                .withForBruker(lagPerson())
                .withInnhold("innhold")
                .withEksternPart(lagPerson())
                .withArkivStatus(ARKIVERT)
                .withUtgaarDato(new DateTime(2013,12,11,11,11))
                .withKryssreferanse(new Kryssreferanse()
                        .withReferanseId("ABC123")
                        .withReferansekode("DIALOG_REKKE"))
                .withDokumentinfoRelasjon(new DokumentinfoRelasjon()
                        .withBegrensetPartsInnsyn(false)
                        .withDokumenttype("dok-type")
                        .withSensitivitet(true)
                        .withKategorikode("kat")
                        .withBeskriverInnhold(lagDokumentinnhold(antallDokumentinnhold)));
    }

    private static Collection<DokumentInnhold> lagDokumentinnhold(final int antallDokumentinnhold) {
        if(antallDokumentinnhold == 0) {
            return null;
        }
        final List<DokumentInnhold> dokumentInnhold = new ArrayList<>();
        for (int i = 0; i < antallDokumentinnhold; i++) {
            dokumentInnhold.add(new DokumentInnhold()
                    .withFilnavn("fil" + i)
                    .withFiltype("xml" + i)
                    .withVariantformat("xml" + i)
                    .withTittel("hei på deg" + i)
                    .withBrevkode("hva er dette?" + i)
                    .withInnholdStrukturert(true)
                    .withInnhold(("bytes" + 1).getBytes()));
        }
        return dokumentInnhold;
    }


    private static Person lagPerson() {
        return new Person().withAktoerId("aktor").withFodselsnummer("fnr");
    }
}