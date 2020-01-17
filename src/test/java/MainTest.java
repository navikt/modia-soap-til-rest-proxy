import no.nav.apiapp.ApiApp;
import no.nav.common.nais.utils.NaisYamlUtils;
import no.nav.sbl.dialogarena.modiasoaprest.config.ApplicationContext;
import no.nav.sbl.dialogarena.test.SystemProperties;
import no.nav.testconfig.ApiAppTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import static java.util.Collections.emptyList;

class MainTest {
    private static final Logger LOG = LoggerFactory.getLogger(MainTest.class);
    public static final String TEST_PORT = "8802";

    public static void main(String[] args) {
        SystemProperties.setFrom(".vault.properties");
        lastYaml(NaisYamlUtils.getTemplatedConfig("./.nais/qa-template.yaml", new HashMap<String, String>(){{
            put("image", "dontcare");
            put("namespace", "q0");
        }}));

        ApiAppTest.setupTestContext(ApiAppTest.Config.builder().applicationName("modia-soap-til-rest-proxy").build());

        ApiApp.runApp(ApplicationContext.class, new String[]{TEST_PORT});
    }

    // TODO vente på at no.nav.common:1.2020.01.17-09.51-f81773e433fb blir tilgjengelig via mvn så kan denne fjernes
    private static void lastYaml(NaisYamlUtils.NaiseratorSpec yaml) {
        Properties target = System.getProperties();
        List<NaisYamlUtils.NaiseratorSpec.EnvProperty> env = Optional.ofNullable(yaml)
                .map((e) -> e.spec)
                .map((s) -> s.env)
                .orElse(emptyList());

        env.forEach((property) -> {
            String name = property.name;
            String value = property.value;

            if (target.containsKey(property.name)) {
                LOG.warn("Old value '{}' is replaced with", target.getProperty(name));
                LOG.warn("{} = {}", name, value);
            } else {
                LOG.info("Setting {} = '{}'", name, value);
            }

            target.setProperty(property.name, property.value);
        });
    }

}