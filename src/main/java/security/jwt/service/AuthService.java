package security.jwt.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import security.jwt.domain.Role;
import security.jwt.domain.Usuario;
import security.jwt.domain.dto.AuthResponse;
import security.jwt.domain.dto.AuthLoginRequest;
import security.jwt.domain.dto.AuthLoginResponse;
import security.jwt.domain.dto.RegistroRequest;
import security.jwt.repository.UsuarioRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UsuarioRepository repository;
    private final PasswordEncoder encoder;
    private final JwtService service;
    private final AuthenticationManager manager;

    public AuthResponse registro(RegistroRequest request) {
        Optional<Usuario> usuarioExistente = repository.findByEmail(request.email());
        if(usuarioExistente.isPresent()){
            throw new IllegalArgumentException("Email já existe no banco de dados!");
        }

        Usuario usuario = Usuario.builder()
                .nome(request.nome())
                .email(request.email())
                .senha(encoder.encode(request.senha()))
                .role(Role.COMUM)
                .build();

        repository.save(usuario);
        log.info("ROLE = " + usuario.getRole());
        return AuthResponse.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .build();
    }

    public AuthLoginResponse auth(AuthLoginRequest request) {
        manager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.senha()
                )
        );

        Usuario usuario = repository.findByEmail(request.email()).orElseThrow();
        String token = service.gerarToken(usuario);
        return AuthLoginResponse.builder()
                .token(token)
                .build();
    }
}
