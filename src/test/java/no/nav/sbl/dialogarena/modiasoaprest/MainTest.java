package no.nav.sbl.dialogarena.modiasoaprest;

import no.nav.common.nais.NaisYamlUtils;
import no.nav.common.test.SystemProperties;
import org.springframework.boot.SpringApplication;

import java.util.HashMap;

class MainTest {
    public static void main(String[] args) {
//        SystemProperties.setFrom(".vault.properties");
        NaisYamlUtils.loadFromYaml(NaisYamlUtils.getTemplatedConfig("./.nais/qa-template.yaml", new HashMap<String, String>() {{
            put("image", "dontcare");
            put("namespace", "q0");
        }}));

        SpringApplication application = new SpringApplication(Main.class);
        application.setAdditionalProfiles("local");
        application.run(args);
    }
}
