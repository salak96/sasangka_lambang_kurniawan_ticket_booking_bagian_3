package event.booking.system.development.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private final Key key;
    private final String issuer;
    private final long ttlMinutes;

    public JwtTokenProvider(
            @Value("${security.jwt.secret}") String secret,
            @Value("${security.jwt.issuer}") String issuer,
            @Value("${security.jwt.access-token-ttl-minutes}") long ttlMinutes
    ) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.issuer = issuer;
        this.ttlMinutes = ttlMinutes;
    }

    public String generateToken(Authentication auth) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(ttlMinutes * 60);

        return Jwts.builder()
                .setSubject(auth.getName())
                .setIssuer(issuer)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(exp))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Instant getExpiry(String token) {
        Claims claims = getParser().parseClaimsJws(token).getBody();
        return claims.getExpiration().toInstant();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = getParser().parseClaimsJws(token).getBody();
        String subject = claims.getSubject();
        return new UsernamePasswordAuthenticationToken(subject, token, null);
    }

    public boolean validate(String token) {
        try {
            getParser().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private JwtParser getParser() {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build();
    }
}
