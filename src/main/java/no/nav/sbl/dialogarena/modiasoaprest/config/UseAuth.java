package no.nav.sbl.dialogarena.modiasoaprest.config;

import no.nav.common.utils.EnvironmentUtils;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class UseAuth implements Condition {
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        return EnvironmentUtils
                .getOptionalProperty("NAIS_NAMESPACE", "NAIS_CLUSTER_NAME", "NAIS_APP_NAME")
                .isPresent();
    }
}
