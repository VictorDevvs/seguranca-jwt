package security.jwt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import security.jwt.domain.Usuario;
import security.jwt.domain.dto.AuthResponse;
import security.jwt.domain.dto.ErrorResponse;
import security.jwt.domain.dto.UsuarioMeResponse;
import security.jwt.service.UsuarioService;

@RestController
@RequestMapping("/api/v1/usuario")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5500", "http://127.0.0.1:5500"})
public class UsuarioController {

    private final UsuarioService service;

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id){
        Usuario usuarioLogado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(!usuarioLogado.getId().equals(id)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ErrorResponse(
                            "Acesso negado. Você não tem permissão para acessar os dados deste usuário.",
                            usuarioLogado.getId()
                    ));
        }

        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/me")
    public ResponseEntity<?> getUsuarioLogado(@AuthenticationPrincipal Usuario usuarioLogado){
        return ResponseEntity.ok(
                UsuarioMeResponse.builder()
                        .id(usuarioLogado.getId())
                        .nome(usuarioLogado.getNome())
                        .email(usuarioLogado.getEmail())
                        .build()
        );
    }
}
