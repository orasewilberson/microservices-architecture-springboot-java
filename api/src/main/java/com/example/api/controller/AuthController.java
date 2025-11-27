package com.example.api.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.api.dto.LoginRequestDTO;
import com.example.api.dto.LoginResponseDTO;
import com.example.api.dto.RegisterRequestDTO;
import com.example.api.jwt.JwtUtil;
import com.example.api.jwt.TokenService;
import com.example.api.model.AppUser;
import com.example.api.repository.UserRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;


@RestController
@RequestMapping("/auth")
public class AuthController {
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody RegisterRequestDTO registerRequestDTO) {
		//Verifier si l'utilisateur existe deja 
		if (userRepository.findByUsername(registerRequestDTO.getUsername()).isPresent()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already taken");
		}
		
		//Creer le nouveau l'utilisateur 
		AppUser newUser = new AppUser();
		newUser.setUsername(registerRequestDTO.getUsername());
		newUser.setRole(registerRequestDTO.getRole() != null ? registerRequestDTO.getRole() : "ROLE_USER");
		//Encoder le password
		newUser.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
		
		userRepository.save(newUser);
		
		return ResponseEntity.ok("user registered successfully");
	} 

	 @PostMapping("/login")
	 public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest) {
		 Optional<AppUser> optionalUser = userRepository.findByUsername(loginRequest.getUsername());
		 
		 if (optionalUser.isEmpty()) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
		}
		 
		AppUser user = optionalUser.get(); 
		
		//comparer le mot de passe harche
		if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
		}
		 
		String token = jwtUtil.generateToken(user.getUsername(), user.getRole());
		
		return ResponseEntity.ok(
				new LoginResponseDTO(token, user.getRole(), user.getUsername()));
	 }
	 
	 @PostMapping("/logout")
	    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authHeader) {
	        if(authHeader != null && authHeader.startsWith("Bearer ")) {
	            String token = authHeader.substring(7);
	            tokenService.revokeToken(token);
	            return ResponseEntity.ok("Logged out successfully");
	        }
	        return ResponseEntity.badRequest().body("Missing or invalid Authorization header");
	    }

	    @PostMapping("/refresh")
	    public ResponseEntity<String> refresh(@RequestParam String refreshToken) {
	        if(jwtUtil.validateToken(refreshToken, jwtUtil.extractUsername(refreshToken))) {
	            String username = jwtUtil.extractUsername(refreshToken);
	            // Ici tu peux définir le rôle de l'utilisateur, ou l'extraire depuis DB
	            String role = "USER"; 
	            String newToken = jwtUtil.generateToken(username, role);
	            return ResponseEntity.ok(newToken);
	        }
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
	    }
	    
	    
	    @PostMapping("/google")
	    public ResponseEntity<?> loginWithGoogle(@RequestBody Map<String,String> payload) {
	        String idTokenString = payload.get("idToken");
	        String clientId = "834377693770-f5fb3s527rclc0o0mi0d8bcud7aj00vl.apps.googleusercontent.com"; // mettre en properties

	        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
	                new NetHttpTransport(),
	                GsonFactory.getDefaultInstance()
	        ).setAudience(Collections.singletonList(clientId)).build();

	        try {
	            GoogleIdToken idToken = verifier.verify(idTokenString);
	            if (idToken == null) {
	                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Google ID token");
	            }
	            GoogleIdToken.Payload p = idToken.getPayload();
	            String email = p.getEmail();
	            // ... rechercher / créer l'utilisateur, générer ton JWT interne ...
	            Optional<AppUser> userSeach = userRepository.findByUsername(email);
	            if (!userSeach.isPresent()) {
		            AppUser appUser = new AppUser();
		            appUser.setUsername(email);
		            appUser.setPassword(passwordEncoder.encode("google_auth"));
		            appUser.setRole("ROLE_USER");
		            userRepository.save(appUser);
	            }
	            
	            String jwt = jwtUtil.generateToken(email, "ROLE_USER");
	            
	            return ResponseEntity.ok(Map.of("token", jwt, "email", email, "name", p.get("name")));
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error verifying token");
	        }
	       }

	    //Crée un endpoint pour que l’utilisateur connecté puisse récupérer ses infos.
//	    @GetMapping("/me")
//	    public Optional<AppUser> getCurrentUser(@AuthenticationPrincipal OAuth2User principal) {
//	        return userRepository.findByUsername(principal.getAttribute("email"));
//	    }
	    
	    @GetMapping("/users/search")
	    public List<AppUser> searchUsers(@RequestParam String q) {
	    	return userRepository.findByUsernameContainingIgnoreCase(q);

	    }


//	    @GetMapping("/loginAuth2")
//	    public String login() {
//	        return "Login with OAuth 2.0 <a href='/oauth2/authorization/google'>Google</a>";
//	    }

}
