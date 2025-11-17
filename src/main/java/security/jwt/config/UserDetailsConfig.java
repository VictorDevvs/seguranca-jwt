package security.jwt.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import security.jwt.repository.UsuarioRepository;

@Component
@RequiredArgsConstructor
public class UserDetailsConfig {

    private final UsuarioRepository repository;

    @Bean
    public UserDetailsService userDetailsService(){
        return username -> repository.findByEmail(username).orElseThrow(()
        -> new UsernameNotFoundException("Usuário não encontrado no banco de dados"));
    }
}
