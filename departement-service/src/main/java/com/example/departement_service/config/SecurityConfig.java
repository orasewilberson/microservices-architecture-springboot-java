package com.example.departement_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableMethodSecurity
@Configuration
public class SecurityConfig {

	@SuppressWarnings("removal")
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {
		security.csrf(csrf -> csrf.disable())
		.authorizeHttpRequests(
				auth -> auth
				.requestMatchers("/h2-console/**", "/departements", "/departements/**").permitAll()
				.anyRequest().authenticated()
				).headers(headers -> headers.frameOptions().disable());
					
		
		return security.build();
	}
}
