package security.jwt.domain.dto;

import lombok.Builder;

@Builder
public record AuthResponse(
        Long id,
        String nome,
        String email
) {
}
