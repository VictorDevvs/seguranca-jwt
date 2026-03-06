package security.jwt.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Builder;

@Builder
public record RegistroRequest(
        @NotBlank(message = "Nome é obrigatório")
        @JsonProperty("nome")
        String nome,

        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email inválido")
        @JsonProperty("email")
        String email,

        @NotBlank(message = "Senha é obrigatória")
        @Size(min = 8, message = "Senha deve ter no mínimo 8 caracteres")
        @Pattern(regexp = "(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[$*&@#])[\\w $*&@#]{8,}")
        @JsonProperty("senha")
        String senha
) {
}
