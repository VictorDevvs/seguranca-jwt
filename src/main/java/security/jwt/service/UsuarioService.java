package security.jwt.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import security.jwt.domain.Usuario;
import security.jwt.domain.dto.AuthResponse;
import security.jwt.repository.UsuarioRepository;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;

    public AuthResponse buscarPorId(Long id){
        Usuario usuario = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Usuário encontrado"));

        return AuthResponse.builder()
                .id(id)
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .build();
    }

}
