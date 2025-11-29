package security.jwt.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record UsuarioMeResponse(
        @JsonProperty("id")
        Long id,
        @JsonProperty("nome")
        String nome,
        @JsonProperty("email")
        String email
) {
}
