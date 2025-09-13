package event.booking.system.development.model.response;

import java.time.Instant;
import java.util.Set;

public record AuthResponse(
        String accessToken,
        String tokenType,
        Instant expiresAt,
        String userId,
        String email,
        String name,
        Set<String> roles
) {
    public static AuthResponse of(String token, Instant exp, String userId, String email, String name, Set<String> roles) {
        return new AuthResponse(token, "Bearer", exp, userId, email, name, roles);
    }
}
