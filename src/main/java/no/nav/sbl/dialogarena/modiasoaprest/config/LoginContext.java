package no.nav.sbl.dialogarena.modiasoaprest.config;

import no.nav.common.auth.oidc.filter.OidcAuthenticationFilter;
import no.nav.common.auth.oidc.filter.OidcAuthenticator;
import no.nav.common.auth.oidc.filter.OidcAuthenticatorConfig;
import no.nav.common.auth.subject.IdentType;
import no.nav.common.log.LogFilter;
import no.nav.common.rest.filter.SetStandardHttpHeadersFilter;
import no.nav.common.utils.EnvironmentUtils;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import static java.util.Collections.singletonList;
import static no.nav.common.auth.Constants.OPEN_AM_ID_TOKEN_COOKIE_NAME;
import static no.nav.common.auth.Constants.REFRESH_TOKEN_COOKIE_NAME;
import static no.nav.common.utils.EnvironmentUtils.isDevelopment;
import static no.nav.common.utils.EnvironmentUtils.requireApplicationName;
import static no.nav.sbl.dialogarena.modiasoaprest.common.Constants.APPLICATION_NAME;

@Configuration
public class LoginContext {
    private static final String issoClientId = EnvironmentUtils.getRequiredProperty("ISSO_CLIENT_ID");
    private static final String issoDiscoveryUrl = EnvironmentUtils.getRequiredProperty("ISSO_DISCOVERY_URL");
    private static final String issoRefreshUrl = EnvironmentUtils.getRequiredProperty("ISSO_REFRESH_URL");

    public OidcAuthenticator openAmAuthConfig() {
        return OidcAuthenticator.fromConfig(
                new OidcAuthenticatorConfig()
                        .withDiscoveryUrl(issoDiscoveryUrl)
                        .withClientId(issoClientId)
                        .withRefreshUrl(issoRefreshUrl)
                        .withIdTokenCookieName(OPEN_AM_ID_TOKEN_COOKIE_NAME)
                        .withRefreshTokenCookieName(REFRESH_TOKEN_COOKIE_NAME)
                        .withIdentType(IdentType.InternBruker)
        );
    }

    @Bean
    @Conditional(UseAuth.class)
    public FilterRegistrationBean authenticationFilterRegistration() {
        FilterRegistrationBean<OidcAuthenticationFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new OidcAuthenticationFilter(singletonList(openAmAuthConfig())));
        registration.setOrder(1);
        registration.addUrlPatterns("/api/*");
        return registration;
    }

    @Bean
    public FilterRegistrationBean logFilterRegistrationBean() {
        FilterRegistrationBean<LogFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new LogFilter(APPLICATION_NAME, isDevelopment().orElse(false)));
        registration.setOrder(2);
        registration.addUrlPatterns("/*");
        return registration;
    }

    @Bean
    public FilterRegistrationBean setStandardHeadersFilterRegistrationBean() {
        FilterRegistrationBean<SetStandardHttpHeadersFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new SetStandardHttpHeadersFilter());
        registration.setOrder(3);
        registration.addUrlPatterns("/*");
        return registration;
    }
}
