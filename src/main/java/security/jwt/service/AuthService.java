package security.jwt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.RedirectView;
import security.jwt.domain.Email;
import security.jwt.domain.Usuario;
import security.jwt.domain.dto.*;
import security.jwt.repository.UsuarioRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository repository;
    private final PasswordEncoder encoder;
    private final JwtService service;
    private final AuthenticationManager manager;
    private final EmailService emailService;
    private final VerificarEmailService verificarEmailService;
    private final String linkAtivacao = "http://localhost:8080/api/v1/auth/ativar?token=";
    private final String linkRedefinicao = "http://localhost:5500/index.html?resetToken=";

    public AuthResponse registro(RegistroRequest request) {
        Optional<Usuario> usuarioExistente = repository.findByEmail(request.email());
        if(usuarioExistente.isPresent()){
            throw new RuntimeException("Email já existe no banco de dados!");
        }

        String token = UUID.randomUUID().toString();
        Usuario usuario = Usuario.builder()
                .nome(request.nome())
                .email(request.email())
                .senha(encoder.encode(request.senha()))
                .tokenVerificacaoEmail(token)
                .expiracaoToken(LocalDateTime.now().plusHours(24))
                .build();

        repository.save(usuario);

        Email email = new Email(request.email(), "Olá, " + request.nome(),
                "Clique no link para ativar sua conta: " + linkAtivacao + token);

        emailService.sendEmail(email);

        return AuthResponse.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .build();
    }

    public AuthLoginResponse login(AuthLoginRequest request) {
        Authentication auth = manager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.senha()
                )
        );

        Usuario usuario = (Usuario) auth.getPrincipal();

        if(!usuario.isEmailVerificado()){
            throw new RuntimeException("EMAIL NÃO VERIFICADO!");
        }

        String token = service.gerarToken(usuario);
        return AuthLoginResponse.builder()
                .token(token)
                .nome(usuario.getNome())
                .build();
    }

    public RedirectView ativar(String token){
        boolean verificado = verificarEmailService.verificarEmail(token);
        if(verificado){
            return new RedirectView("http://localhost:5500/index.html?status=ok");
        } else {
            return new RedirectView("http://localhost:5500/index.html?status=erro");
        }
    }

    public void redefinirsenha(EmailRequest emailRequest){
        String email = emailRequest.email();
        Usuario usuario = repository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o email: " + email));

        String token = UUID.randomUUID().toString();
        usuario.setTokenVerificacaoSenha(token);
        usuario.setExpiracaoToken(LocalDateTime.now().plusHours(24));
        repository.save(usuario);

        Email emailRedefinicao = new Email(email, "Redefinição de Senha",
                "Clique no link para redefinir sua senha: " + linkRedefinicao + token);

        emailService.sendEmail(emailRedefinicao);
    }

    public void novaSenha(NovaSenhaRequest request){
        Usuario usuario = repository.findByTokenVerificacaoSenha(request.token());

        if(usuario == null || usuario.getExpiracaoToken().isBefore(LocalDateTime.now())){
            throw new RuntimeException("Token inválido ou expirado!");
        }

        if(!request.senha().equals(request.senhaConfirmacao())){
            throw new RuntimeException("As senhas não coincidem!");
        }

        usuario.setSenha(encoder.encode(request.senha()));
        usuario.setSenhaRedefinida(true);
        usuario.setTokenVerificacaoSenha(null);
        usuario.setExpiracaoToken(null);
        repository.save(usuario);
    }
}
