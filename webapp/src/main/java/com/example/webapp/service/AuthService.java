package com.example.webapp.service;



import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.webapp.dto.LoginResponseDTO;
import com.example.webapp.dto.LoginRequestDTO;
import com.example.webapp.dto.RegisterRequestDTO;
import com.example.webapp.repository.AuthProxy;

@Service
public class AuthService {
		
	@Autowired
	private AuthProxy authProxy;
	
	public String register(RegisterRequestDTO registerRequestDTO) {
		return authProxy.register(registerRequestDTO);
	}
	
	public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
		return authProxy.login(loginRequestDTO);
	}

	public Map<String, Object> loginWithGoogle(String idToken) {
	    return authProxy.loginWithGoogle(idToken);
	}

	public void logout(String token) {
		authProxy.logout(token);
		
	}
}
