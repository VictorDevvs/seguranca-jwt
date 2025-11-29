package security.jwt.domain.dto;

import lombok.Builder;

@Builder
public record ErrorResponse(
        String message,
        Long id
) {
}
