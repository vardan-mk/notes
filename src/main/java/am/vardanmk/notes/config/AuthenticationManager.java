package am.vardanmk.notes.config;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {

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
            System.out.println(e);
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

            return Mono.just(authenticationToken);
        } else {
            return Mono.empty();
        }
    }
}
