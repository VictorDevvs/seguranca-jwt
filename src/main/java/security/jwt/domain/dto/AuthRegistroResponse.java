package security.jwt.domain.dto;

import lombok.Builder;

@Builder
public record AuthRegistroResponse(
        Long id,
        String nome,
        String email
) {
}
