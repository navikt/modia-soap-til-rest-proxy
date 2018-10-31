package no.nav.sbl.dialogarena.modiasoaprest.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FasitPropertiesTest {

    String minPropertyName = "srvmodia-soap-til-rest-proxy_username";
    String minPropertyVerdi = "minVerdi";


    @Test
    public void skalFeileOmPropertyIkkeFunnet() {
        assertThrows(RuntimeException.class, () -> {
            FasitProperties.getFasitProperty(minPropertyName);
        });

    }

    @Test
    public void skalHentePropertyOmSatt() {
        System.setProperty(minPropertyName.toUpperCase(), minPropertyVerdi);

        assertEquals(minPropertyVerdi, FasitProperties.getFasitProperty(minPropertyName));

        System.clearProperty(minPropertyName);
    }



}