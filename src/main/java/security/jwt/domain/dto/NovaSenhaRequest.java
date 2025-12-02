package security.jwt.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NovaSenhaRequest(
        @NotBlank
        String token,
        @NotNull
        String senha,
        @NotNull
        String senhaConfirmacao
) {
}
