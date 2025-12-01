package security.jwt.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import security.jwt.domain.Email;
import security.jwt.domain.Usuario;
import security.jwt.domain.dto.AuthLoginRequest;
import security.jwt.domain.dto.AuthLoginResponse;
import security.jwt.domain.dto.AuthResponse;
import security.jwt.domain.dto.RegistroRequest;
import security.jwt.repository.UsuarioRepository;
import security.jwt.service.AuthService;
import security.jwt.service.EmailService;
import security.jwt.service.VerificarEmailService;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Validated
@CrossOrigin(origins = {"http://localhost:5500", "http://127.0.0.1:5500"})
public class AuthController {

    private final AuthService service;
    private final EmailService emailService;
    private final UsuarioRepository repository;
    private final VerificarEmailService verificarEmailService;
    private final String linkAtivacao = "http://localhost:8080/api/v1/auth/ativar?token=";

    @PostMapping("/registro")
    public ResponseEntity<AuthResponse> registro(@RequestBody @Valid RegistroRequest request){
        String token = UUID.randomUUID().toString();
        AuthResponse response = service.registro(request);
        Usuario usuario = repository.findByEmail(request.email()).orElseThrow();
        usuario.setTokenVerificacaoEmail(token);
        usuario.setExpiracaoToken(LocalDateTime.now().plusHours(24));
        repository.save(usuario);

        Email email = new Email(request.email(), "Olá, " + request.nome(),
                "Clique no link para ativar sua conta: " + linkAtivacao + token);
        emailService.sendEmail(email);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthLoginResponse> login(@RequestBody @Valid AuthLoginRequest request){
        return ResponseEntity.ok(service.login(request));
    }

    @GetMapping("/ativar")
    public RedirectView ativar(@RequestParam String token){
        boolean verificado = verificarEmailService.verificarEmail(token);
        if(verificado){
            return new RedirectView("http://localhost:5500/index.html?status=ok");
        } else {
            return new RedirectView("http://localhost:5500/index.html?status=erro");
        }
    }
}
