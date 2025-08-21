package com.rouandi.rouandi;

import com.rouandi.rouandi.entities.User;
import com.rouandi.rouandi.entities.Role;
import com.rouandi.rouandi.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableScheduling
public class RouandiApplication {

	public static void main(String[] args) {
		SpringApplication.run(RouandiApplication.class, args);

	}
	@Bean
	CommandLineRunner run(UserRepository userRepository, PasswordEncoder encoder) {
		return args -> {
			if (userRepository.findByEmail("fz@email.com").isEmpty()) {
				User user = new User();
				user.setEmail("fz@email.com");
				user.setNom("fz");
				user.setPassword(encoder.encode("1234"));
				user.setRole(Role.ADMIN);
				user.setFonctionalite("comptable");
				userRepository.save(user);
			}
		};
	}


}
