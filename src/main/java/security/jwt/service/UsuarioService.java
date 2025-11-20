package security.jwt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import security.jwt.repository.UsuarioRepository;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;


}
