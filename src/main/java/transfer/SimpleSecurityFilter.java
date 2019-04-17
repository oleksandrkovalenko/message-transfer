package transfer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Configuration
@Slf4j
public class SimpleSecurityFilter extends GenericFilterBean {

    @Value("${transfer.server.masterToken.random:false}")
    private boolean useRandomToken;

    @Value("${transfer.server.masterToken.token:#{null}}")
    private UUID token;

    private UUID randomToken = UUID.randomUUID();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (isAccessAllowed(token, request.getRequestURI())) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            log.info("401 "+request.getRequestURI());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    private boolean isAccessAllowed(String token, String requestURI) {
        return checkToken(token)
                || "/health".equals(requestURI)
                || "/account".equals(requestURI);
    }

    private boolean checkToken(String token) {
        try {
            return expectedToken().equals(UUID.fromString(token));
        } catch (IllegalArgumentException | NullPointerException e) {
            log.error("Invalid token is send {}", token);
            return false;
        }
    }

    private UUID expectedToken() {
        if (useRandomToken) {
            log.info("Random Token "+ randomToken);
            return randomToken;
        } else {
            return token;
        }
    }

}
