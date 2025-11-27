package com.example.webapp.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.HttpClientErrorException;

import com.example.webapp.dto.LoginResponseDTO;
import com.example.webapp.dto.LoginRequestDTO;
import com.example.webapp.dto.RegisterRequestDTO;
import com.example.webapp.service.AuthService;

import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {
	
	@Autowired
	private AuthService authService;
	
	@Autowired
	private OAuth2AuthorizedClientService authorizedClientService;

	@GetMapping("/")
	public String loginForm(Model model) {
		model.addAttribute("loginRequest", new LoginRequestDTO());
		return "login";
	}
	
	@PostMapping("/auth/login")
	public String login(@ModelAttribute LoginRequestDTO loginRequestDTO,
			HttpSession session, Model model){
		
		try {
			LoginResponseDTO loginResponseDTO = authService.login(loginRequestDTO);
			session.setAttribute("jwtToken", loginResponseDTO.getToken()); //sauvegarde token en session
			session.setAttribute("role", loginResponseDTO.getRole());
			session.setAttribute("username", loginResponseDTO.getUsername());
			
			return "redirect:/employees";
			
		} catch (Exception e) {
			model.addAttribute("error", "Login failed: " + e.getMessage());
            return "login";
		}
		
	}
	
	@GetMapping("/auth/createAccount")
	public String showRegisterForm(Model model) {
		model.addAttribute("registerRequest", new RegisterRequestDTO());
		return "register";
	}
	
	@PostMapping("/auth/register")
	public String register(@ModelAttribute RegisterRequestDTO registerRequestDTO, Model model) {
		try {
			String response = authService.register(registerRequestDTO);
			model.addAttribute("success", response);
			return "redirect:/"; //redirection vers login apres succes
		} catch (HttpClientErrorException e) {
			model.addAttribute("error", e.getResponseBodyAsString());
			return "register";
		}
		
	}
	
	@GetMapping("/oauth2/login/success")
	public String getOauth2Info(OAuth2AuthenticationToken authentication,
	                            HttpSession session) {

	    if (authentication == null) {
	        throw new IllegalStateException("L'utilisateur n'est pas authentifié via OAuth2");
	    }

	    OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
	            authentication.getAuthorizedClientRegistrationId(),
	            authentication.getName());

	    // ✅ Récupérer le vrai ID Token
	    OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
	    String idToken = oidcUser.getIdToken().getTokenValue();

	    // Envoyer le bon token à l’API
	    Map<String, Object> response = authService.loginWithGoogle(idToken);

	    session.setAttribute("jwtToken", response.get("token"));
	    session.setAttribute("username", response.get("email"));
	    session.setAttribute("role", "ROLE_USER");

	    return "redirect:/employees";
	}


	
	
	@GetMapping("/auth/logout")
	public String logout(HttpSession session) {
		String token = (String) session.getAttribute("jwtToken");
		
		// Appel à ton API pour invalider le token
	    if (token != null) {
	        authService.logout(token);
	    }

	    // Supprimer tout ce qui est stocké dans la session
	    session.invalidate();

	    // Redirection vers la page de login
	    return "redirect:/";
		
		
	}

}
