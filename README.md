# modia-soap-til-rest-proxy
En proxy for å gjøre om et SOAP-SAML request til et REST-OIDC kall.

Dette er et mellomsteg for å sørge for at overgangen til rest blir så smooth som mulig og gjør det enklere å gjøre dette stegvis.

Dokumenterer alle steg for jeg misstenker at dette må gjøres et par ganger...

### Steg for steg
Oppdater [Tjenestespesifikasjoner](http://stash.devillo.no/projects/FELLES/repos/tjenestespesifikasjoner/browse) med WSDL, eller gjør det selv. Jeg brukte Tjenestespesifikasjoner fordi denne muliggjør å bruke en byggejobb på **cisbl** som da lager en artifact i nexus som kan brukes av alle tjenestene som skal bruke disse SOAP-kallene. Men det kan gjøres manuelt.

Sett opp SOAP-tjeneste.

Sjekk at du klarer å kalle SOAP-tjeneste.

Konverter SAML til OIDC.

Map om objekter som kommer inn fra SOAP-kallet.

Bruk disse i kallet til REST.
