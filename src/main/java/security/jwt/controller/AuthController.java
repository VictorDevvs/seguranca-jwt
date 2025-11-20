package security.jwt.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import security.jwt.domain.dto.AuthRegistroResponse;
import security.jwt.domain.dto.AuthRequest;
import security.jwt.domain.dto.AuthLoginResponse;
import security.jwt.domain.dto.RegistroRequest;
import security.jwt.service.AuthService;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final AuthService service;

    @PostMapping("/registro")
    public ResponseEntity<AuthRegistroResponse> registro(@RequestBody @Valid RegistroRequest request){
        return ResponseEntity.ok(service.registro(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthLoginResponse> auth(@RequestBody @Valid AuthRequest request){
        return ResponseEntity.ok(service.auth(request));
    }
}
