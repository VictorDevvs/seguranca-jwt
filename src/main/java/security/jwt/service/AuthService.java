package security.jwt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import security.jwt.domain.Role;
import security.jwt.domain.Usuario;
import security.jwt.domain.dto.AuthRequest;
import security.jwt.domain.dto.AuthResponse;
import security.jwt.domain.dto.RegistroRequest;
import security.jwt.repository.UsuarioRepository;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository repository;
    private final PasswordEncoder encoder;
    private final JwtService service;

    public AuthResponse registro(RegistroRequest request) {
        Usuario usuario = Usuario.builder()
                .nome(request.nome())
                .email(request.email())
                .senha(encoder.encode(request.senha()))
                .role(Role.COMUM)
                .build();

        repository.save(usuario);
        String token = service.gerarToken(usuario);
        return AuthResponse.builder()
                .token(token)
                .build();
    }

    public AuthResponse auth(AuthRequest request) {

    }
}
