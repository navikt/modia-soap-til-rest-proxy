package no.nav.sbl.dialogarena.modiasoaprest.common;

public class FasitProperties {
    public static String getFasitProperty(String propertyName) {
        String envPropertyValue = System.getenv(propertyName);
        String propertyValue = System.getProperty(propertyName);

        if(envPropertyValue != null && !envPropertyValue.isEmpty()) {
            return envPropertyValue;
        } else if(propertyValue != null && !propertyValue.isEmpty()) {
            return propertyValue;
        }
        throw new RuntimeException("Mangler property:" + propertyName);
    }

}
