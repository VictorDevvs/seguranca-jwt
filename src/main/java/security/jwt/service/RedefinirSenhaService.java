package security.jwt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import security.jwt.domain.Usuario;
import security.jwt.repository.UsuarioRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RedefinirSenhaService {

    private final UsuarioRepository usuarioRepository;

    public boolean redefinirSenha(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        if(usuario != null && !tokenExpirado(usuario)){
            usuario.setSenhaRedefinida(true);
            usuario.setTokenVerificacaoSenha(null);
            usuario.setExpiracaoToken(null);
            usuarioRepository.save(usuario);
            return true;
        }
        else {
            return false;
        }
    }

    private boolean tokenExpirado(Usuario usuario) {
        return usuario.getExpiracaoToken().isBefore(LocalDateTime.now());
    }
}
