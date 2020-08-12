package no.nav.sbl.dialogarena.modiasoaprest.config;

import no.nav.common.nais.NaisYamlUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ApplicationContext.class})
public class TestContextTest {

    @BeforeClass
    public static void setUp() {
        NaisYamlUtils.loadFromYaml(".nais/qa-template.yaml");
    }

    @Test
    public void skalStarteAppContext() {
        assertEquals(true, true);
    }
}
