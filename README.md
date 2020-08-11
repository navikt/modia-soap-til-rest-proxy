# modia-soap-til-rest-proxy
En proxy for å gjøre om et SOAP-SAML request til et REST-OIDC kall.

Dette er et mellomsteg for å sørge for at overgangen til rest blir så smooth som mulig og gjør det enklere å gjøre dette stegvis.

### Lokal kjøring

Appen startes med no.nav.sbl.dialogarena.modiasoaprest.MainTest-klassen, og denne forventer å finne en fasit.properties-fil med minimum følgende innhold
```
domenebrukernavn=<dinNavIdent>
domenepassord=<dittNavPassord>
testmiljo=t6
```

Webservice-tjenestene er eksponert på følgende URL
[http://localhost:8802/modia-soap-til-rest-proxy/ws/](http://localhost:8802/modia-soap-til-rest-proxy/ws/)


### Steg for steg - oppsett av ny tjeneste
1. Oppdater [Tjenestespesifikasjoner](https://github.com/navikt/tjenestespesifikasjoner) med WSDL, eller gjør det selv. Jeg brukte Tjenestespesifikasjoner fordi denne bygger artifakt på travis som kan trekkes inn i både produsent og konsument. Men det kan gjøres manuelt.

2. Sett opp SOAP-tjeneste - benytt @Service og @Soaptjeneste("soaptjenestenavn")

3. Sjekk at du klarer å kalle SOAP-tjeneste (eksempelvis med SoapUI)

4. Konverter SAML til OIDC - benytt SamtToOidcService.java

5. Map om objekter som kommer inn fra SOAP-kallet.

6. Bruk disse i kallet til REST.

7. Konverter svar fra REST-tjeneste til fornuftig svar på SOAP-kall.
