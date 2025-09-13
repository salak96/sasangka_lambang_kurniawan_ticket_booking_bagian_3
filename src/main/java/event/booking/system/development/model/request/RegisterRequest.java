package event.booking.system.development.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

public record RegisterRequest(

        @NotBlank(message = "email tidak boleh kosong")
        @Email(message = "format email tidak valid")
        String email,

        @NotBlank(message = "username tidak boleh kosong")
        @Size(min = 3, max = 50, message = "username minimal 3 dan maksimal 50 karakter")
        @JsonProperty("username")            // <-- pastikan nama JSON = "username"
        String username,

        @NotBlank(message = "nama tidak boleh kosong")
        @JsonProperty("name")
        String name,

        @NotBlank(message = "password tidak boleh kosong")
        @Size(min = 6, max = 72, message = "password minimal 6 dan maksimal 72 karakter")
        @JsonProperty("password")
        String password
) {}
