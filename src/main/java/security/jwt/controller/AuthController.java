package security.jwt.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import security.jwt.domain.dto.*;
import security.jwt.service.AuthService;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Validated
@CrossOrigin(origins = {"http://localhost:5500", "http://127.0.0.1:5500"})
public class AuthController {

    private final AuthService service;

    @PostMapping("/registro")
    public ResponseEntity<AuthResponse> registro(@RequestBody @Valid RegistroRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registro(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthLoginResponse> login(@RequestBody @Valid AuthLoginRequest request){
        return ResponseEntity.ok(service.login(request));
    }

    @GetMapping("/ativar")
    public RedirectView ativar(@RequestParam String token){
        return service.ativar(token);
    }

    @PostMapping("redefinir-senha")
    public ResponseEntity<Void> redefinirSenha(@RequestBody EmailRequest email){
        service.redefinirsenha(email);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/nova-senha")
    public ResponseEntity<Void> novaSenha(@RequestBody @Valid NovaSenhaRequest request){
        service.novaSenha(request);
        return ResponseEntity.ok().build();
    }
}
