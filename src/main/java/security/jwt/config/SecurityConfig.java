package security.jwt.config;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import security.jwt.domain.Usuario;
import security.jwt.repository.UsuarioRepository;
import security.jwt.service.JwtService;

import java.util.Optional;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter filter;
    private final AuthenticationProvider provider;
    private final JwtService service;
    private final UsuarioRepository repository;
    private final PasswordEncoder encoder;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        return http.
                csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/api/v1/auth/**", "/login/oauth2/**").permitAll()
                                .requestMatchers("/api/v1/usuario/**").authenticated()
                                .anyRequest().authenticated())
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(uf -> uf.userService(oAuth2UserService()))
                        .successHandler(oAuth2SuccessHandler())
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json");
                            response.getWriter().write("{\"error\":\"Não autorizado\"}");
                        })
                )
                .authenticationProvider(provider)
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public DefaultOAuth2UserService oAuth2UserService() {
        return new DefaultOAuth2UserService(){
            @Override
            public OAuth2User loadUser(OAuth2UserRequest request){
                OAuth2User oAuth2User = super.loadUser(request);
                String provider = request.getClientRegistration().getRegistrationId();
                String email = null;
                String nome = null;

                if(provider.equals("google")){
                    email = oAuth2User.getAttribute("email");
                    nome = oAuth2User.getAttribute("name");
                } else if (provider.equals("github")) {
                    email = oAuth2User.getAttribute("email");
                    nome = oAuth2User.getAttribute("name");
                    if(email == null){
                        email = oAuth2User.getAttribute("login") + "@github.com";
                    }

                    if(nome == null){
                        nome = oAuth2User.getAttribute("login");
                    }
                }

                String finalNome = nome;
                String finalEmail = email;
                Usuario usuario = repository.findByEmail(email).orElseGet(() -> Usuario.builder()
                        .email(finalEmail)
                        .nome(finalNome)
                        .senha("")
                        .build()
                );

                repository.save(usuario);

                return oAuth2User;
            }
        };
    }

    @Bean
    public AuthenticationSuccessHandler oAuth2SuccessHandler() {
        return (request, response, authentication) -> {
            OAuth2User user = (OAuth2User) authentication.getPrincipal();
            String email = user.getAttribute("email");
            Usuario usuario = repository.findByEmail(email).orElseThrow(() ->
                    new EntityNotFoundException("Usuário não encontrado")
                    );

            String token = service.gerarToken(usuario);

            response.sendRedirect("http://localhost:5500?token=" + token);
        };
    }
}
