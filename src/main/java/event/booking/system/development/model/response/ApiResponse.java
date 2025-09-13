package event.booking.system.development.model.response;
import java.time.Instant;
import java.util.Map;

public record ApiResponse<T>(
        int status,
        String message,
        Instant timestamp,
        T data,
        Map<String, Object> meta
) {}
