package security.jwt.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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
        @Min(value = 6, message = "Senha deve ter no mínimo 6 caracteres")
        @JsonProperty("senha")
        String senha
) {
}
