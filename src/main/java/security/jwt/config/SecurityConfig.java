package security.jwt.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import security.jwt.domain.Permissoes;
import security.jwt.domain.Role;

import static org.springframework.http.HttpMethod.*;
import static security.jwt.domain.Permissoes.*;
import static security.jwt.domain.Role.ADMIN;
import static security.jwt.domain.Role.COMUM;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter filter;
    private final AuthenticationProvider provider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        return http.
                csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/api/v1/auth/**", "/error").permitAll()
                                .requestMatchers("/api/v1/usuario/**").authenticated()
                                .requestMatchers("/api/v1/admin").hasRole(ADMIN.name())
                                .requestMatchers(GET, "/api/v1/admin").hasAuthority(ADMIN_READ.getPermissao())
                                .requestMatchers(POST, "/api/v1/admin").hasAuthority(ADMIN_CREATE.getPermissao())
                                .requestMatchers(PUT, "/api/v1/admin").hasAuthority(ADMIN_UPDATE.getPermissao())
                                .requestMatchers(DELETE, "/api/v1/admin").hasAuthority(ADMIN_DELETE.getPermissao())
                                .anyRequest().authenticated())
                .authenticationProvider(provider)
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
                .build();

    }
}
