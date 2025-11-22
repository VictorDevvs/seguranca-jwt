package security.jwt;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import security.jwt.domain.Role;
import security.jwt.domain.dto.RegistroRequest;
import security.jwt.service.AuthService;

@SpringBootApplication
public class JwtApplication {

	public static void main(String[] args) {
		SpringApplication.run(JwtApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(AuthService service){
		return args -> {

			var admin = RegistroRequest.builder()
					.nome("admin")
					.email("admin@ex.com")
					.senha("123")
					.role(Role.ADMIN)
					.build();

			var comum = RegistroRequest.builder()
					.nome("comum")
					.email("comum@ex.com")
					.senha("123")
					.role(Role.COMUM)
					.build();

			service.registro(admin);
			service.registro(comum);

		};
	}

}
