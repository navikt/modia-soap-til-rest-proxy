package no.nav.sbl.dialogarena.modiasoaprest.config;

import org.jose4j.base64url.Base64Url;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;

public class TokenFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        
        String samlToken = "<token>";
        java.util.Base64.getUrlEncoder().withoutPadding().encodeToString(samlToken.getBytes());
        //   System.out.println(httpServletRequest);

        String restult = null;
        //Base64 decoder = new Base64(true);

        System.out.println("#######################");

//        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
//        HttpServletRequest httpRequest = (HttpServletRequest) request;
//
//        String origin = httpRequest.getHeader("Origin");
//        setCorsHeadere(httpResponse, origin);

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
