package am.vardanmk.notes.config;

import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {

    private static final Logger log = getLogger(AuthenticationManager.class);

    private final JwtUtils jwtUtils;

    @Autowired
    public AuthenticationManager(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }
    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String authToken = authentication.getCredentials().toString();

        String username;

        try {
            username = jwtUtils.extractUsername(authToken);
        } catch (Exception e) {
            username = null;
            log.error("Can't extract user token, something went wrong", e);
        }

        if (username != null && jwtUtils.validateToken(authToken)) {
            Claims claims = jwtUtils.getClaimsFromToken(authToken);
            List<String> role = claims.get("role", List.class);
            List<SimpleGrantedAuthority> authorities = role.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    authorities
            );

            log.info("Authentication passed successfully for user " + username);

            return Mono.just(authenticationToken);
        } else {
            log.warn("Authentication failed for user " + username + "something went wrong");
            return Mono.empty();
        }
    }
}
