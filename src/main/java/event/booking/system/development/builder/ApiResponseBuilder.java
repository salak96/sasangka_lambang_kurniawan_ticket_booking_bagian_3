package event.booking.system.development.builder;
import event.booking.system.development.model.response.ApiResponse;
import event.booking.system.development.model.response.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.Map;
public final class ApiResponseBuilder {

    private ApiResponseBuilder() {}

    public static <T> ResponseEntity<ApiResponse<T>> ok(T data) {
        return wrap(HttpStatus.OK, "OK", data, null);
    }

    public static <T> ResponseEntity<ApiResponse<T>> ok(T data, String message) {
        return wrap(HttpStatus.OK, message, data, null);
    }

    public static <T> ResponseEntity<ApiResponse<T>> created(T data) {
        return wrap(HttpStatus.CREATED, "Created", data, null);
    }

    public static <T> ResponseEntity<ApiResponse<T>> accepted(T data, String message) {
        return wrap(HttpStatus.ACCEPTED, message, data, null);
    }
    public static <T> ResponseEntity<ApiResponse<java.util.List<T>>> page(Page<T> page) {
        PageResponse meta = new PageResponse(
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast(),
                page.getNumberOfElements(),
                page.hasNext(),
                page.hasPrevious()
        );
        return wrap(HttpStatus.OK, "OK", page.getContent(), Map.of("page", meta));
    }

    public static ResponseEntity<ApiResponse<Object>> error(HttpStatus status, String message) {
        return wrap(status, message, null, null);
    }

    public static ResponseEntity<ApiResponse<Object>> error(
            HttpStatus status, String message, Map<String, Object> details) {
        return wrap(status, message, null, details);
    }

    private static <T> ResponseEntity<ApiResponse<T>> wrap(
            HttpStatus status, String message, T data, Map<String, Object> meta) {

        ApiResponse<T> body = new ApiResponse<>(
                status.value(),
                message,
                Instant.now(),
                data,
                meta
        );
        return ResponseEntity.status(status).body(body);
    }
}
