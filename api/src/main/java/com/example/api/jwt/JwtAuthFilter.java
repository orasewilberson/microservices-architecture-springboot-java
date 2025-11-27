package com.example.api.jwt;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
	
	private JwtUtil jwtUtil;
	
	private final TokenService tokenService;

	public JwtAuthFilter(JwtUtil jwtUtil, TokenService tokenService) {
		this.tokenService = tokenService;
		this.jwtUtil = jwtUtil;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String header = request.getHeader("Authorization");
		String token = null;
		String username = null;
		
		if(header != null && header.startsWith("Bearer ")) {
			token = header.substring(7);
			username = jwtUtil.extractUsername(token);
		}
		
		if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			if (!tokenService.isRevoked(token) && jwtUtil.validateToken(token, username)) {
			    String role = jwtUtil.extractRole(token); 
			    var authorities = List.of(new SimpleGrantedAuthority(role));

			    UsernamePasswordAuthenticationToken auth =
			        new UsernamePasswordAuthenticationToken(username, null, authorities);

			    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			    SecurityContextHolder.getContext().setAuthentication(auth);
			}

		}
		filterChain.doFilter(request, response);
	}
	
	
}
