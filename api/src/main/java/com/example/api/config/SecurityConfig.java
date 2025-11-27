package com.example.api.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.api.jwt.JwtAuthFilter;

@EnableMethodSecurity
@Configuration
public class SecurityConfig {

	@Autowired
    private JwtAuthFilter jwtAuthFilter;


	@SuppressWarnings("removal")
	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
            		 .requestMatchers(
        	                "/auth/**",                // login
        	                "/swagger-ui/**",          // Swagger UI
        	                "/v3/api-docs/**",         // OpenAPI docs
        	                "/swagger-ui.html",
        	                "/h2-console/**",
        	                "/internal/**"
        	                ).permitAll()
                    .anyRequest().authenticated()
                    
                ).csrf(csrf -> csrf.disable()) // H2 console ne fonctionne pas avec CSRF activé
            	.headers(headers -> headers.frameOptions().disable()) 
	            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            							.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));

        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
    	return new BCryptPasswordEncoder();
    }
    
//    @Bean
//    public RestTemplate restTemplate() {
//    	return new RestTemplate();
//    }
}
