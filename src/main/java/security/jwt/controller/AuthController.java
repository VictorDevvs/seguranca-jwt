package security.jwt.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import security.jwt.domain.Email;
import security.jwt.domain.dto.AuthLoginRequest;
import security.jwt.domain.dto.AuthLoginResponse;
import security.jwt.domain.dto.AuthResponse;
import security.jwt.domain.dto.RegistroRequest;
import security.jwt.service.AuthService;
import security.jwt.service.EmailService;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Validated
@CrossOrigin(origins = {"http://localhost:5500", "http://127.0.0.1:5500"})
public class AuthController {

    private final AuthService service;
    private final EmailService emailService;

    @PostMapping("/registro")
    public ResponseEntity<AuthResponse> registro(@RequestBody @Valid RegistroRequest request){
        Email email = new Email(request.email(), "Bem-vindo ao Sistema",
                "Olá, " + request.nome() + "! Seu registro foi realizado com sucesso.");
        emailService.sendEmail(email);
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registro(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthLoginResponse> login(@RequestBody @Valid AuthLoginRequest request){
        return ResponseEntity.ok(service.login(request));
    }
}
