package security.jwt.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record EmailRequest(
        @NotNull
        @Email
        @JsonProperty("email")
        String email
) {
}
