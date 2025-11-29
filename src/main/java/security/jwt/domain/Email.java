package security.jwt.domain;

public record Email(
        String destinatario,
        String assunto,
        String corpo
){
}
