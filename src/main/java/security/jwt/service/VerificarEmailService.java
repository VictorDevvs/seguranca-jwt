package security.jwt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import security.jwt.domain.Usuario;
import security.jwt.repository.UsuarioRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class VerificarEmailService {

    private final UsuarioRepository usuarioRepository;

    public boolean verificarEmail(String token) {
        Usuario usuario = usuarioRepository.findByTokenVerificacaoEmail(token);
        if(usuario != null && !tokenExpirado(usuario)){
            usuario.setEmailVerificado(true);
            usuario.setTokenVerificacaoEmail(null);
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
