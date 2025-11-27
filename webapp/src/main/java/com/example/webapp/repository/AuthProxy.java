package com.example.webapp.repository;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.webapp.dto.LoginRequestDTO;
import com.example.webapp.dto.LoginResponseDTO;
import com.example.webapp.CustomProperties;
import com.example.webapp.dto.RegisterRequestDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AuthProxy {

	@Autowired
	private CustomProperties customProperties;
	
    private RestTemplate restTemplate = new RestTemplate();
	
	/**
	 * Enregister un utilisateur
	 * @param registerRequestDTO
	 * @return
	 */
	public String register(RegisterRequestDTO registerRequestDTO) {
		String baseUrl = customProperties.getApiUrl();
		String registerUrl = baseUrl + "/auth/register";
		
		try {
			// Crée les headers
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			// Mets le body + headers dans un HttpEntity
			HttpEntity<RegisterRequestDTO> requestEntity = new HttpEntity<>(registerRequestDTO, headers);

			ResponseEntity<String> responseEntity = restTemplate.exchange(
					registerUrl,
					HttpMethod.POST,
					requestEntity,
					String.class
					);
			log.debug("Register call " + responseEntity.getStatusCode().toString());
			return responseEntity.getBody();
			
			
		} catch (HttpClientErrorException e) {
			log.error("Register failed: {}",e.getResponseBodyAsString());
			throw e;
		}
	}
	
	/**
	 * Connexion utilisateur
	 * @param loginRequestDTO
	 * @return
	 */
	public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
	    String baseUrl = customProperties.getApiUrl();
	    String userUrl = baseUrl + "/auth/login";

	    try {
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);

	        HttpEntity<LoginRequestDTO> request = new HttpEntity<>(loginRequestDTO, headers);

	        ResponseEntity<LoginResponseDTO> response = restTemplate.postForEntity(
	                userUrl, request, LoginResponseDTO.class
	        );

	        return response.getBody(); // Retourne le token directement

	    } catch (HttpClientErrorException e) {
	        log.error("Login failed: {}", e.getResponseBodyAsString());
	        throw e; // tu peux aussi renvoyer un message plus clair
	    }
	}

	public Map<String, Object> loginWithGoogle(String idToken) {
	    String baseUrl = customProperties.getApiUrl();
	    String userUrl = baseUrl + "/auth/google";

	    Map<String, String> payload = Map.of("idToken", idToken);

	    try {
	    	ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
	    	                userUrl,
	    	                HttpMethod.POST,
	    	                new HttpEntity<>(payload),
	    	                new ParameterizedTypeReference<Map<String, Object>>() {}
	    	        );

	    	return response.getBody();
	    } catch (HttpClientErrorException e) {
	        log.error("Google login failed: {}", e.getResponseBodyAsString());
	        throw e;
	    }
	}

	public void logout(String token) {
		String baseUrl = customProperties.getApiUrl();
	    String userUrl = baseUrl + "/auth/logout";

		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(token);
		
		HttpEntity<Void> request = new HttpEntity<>(headers);
		try {
			restTemplate.postForEntity(userUrl, request, String.class);
		} catch (Exception e) {
	        System.out.println("Erreur lors de la déconnexion côté API: " + e.getMessage());
		}
	}


}
