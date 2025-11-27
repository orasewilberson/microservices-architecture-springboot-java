package com.example.webapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {


	    @Bean
	    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	        http
	            .authorizeHttpRequests(auth -> auth
	                .requestMatchers("/", "/auth/**", "/oauth2/**","/employees/**","/login**", "/error").permitAll()
	                .anyRequest().authenticated()
	            )
	            .oauth2Login(oauth -> oauth
	                .defaultSuccessUrl("/oauth2/login/success", true) // ton endpoint après login
	            )
	            .csrf(csrf -> csrf.disable()); // désactive CSRF pour tests
	        return http.build();
	    }

}
