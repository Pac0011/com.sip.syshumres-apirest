package com.sip.syshumres_apirest.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import org.springframework.beans.factory.annotation.Autowired;

@Component
public class JWTAuthorizationFilter extends OncePerRequestFilter {
	
	private JWTService jwtService;
	
	@Autowired
	public JWTAuthorizationFilter(JWTService jwtService) {
		this.jwtService = jwtService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, 
									HttpServletResponse response, 
									FilterChain filterChain) throws ServletException, IOException {
		String bearerToken =  request.getHeader("Authorization");
		
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			String token = bearerToken.replace("Bearer ", "");
			//UsernamePasswordAuthenticationToken usernamePAT = TokenUtils.getAuthentication(token, userDetailServiceImpl);
			UsernamePasswordAuthenticationToken usernamePAT = jwtService.getAuthentication(token);
			SecurityContextHolder.getContext().setAuthentication(usernamePAT);
		}
		filterChain.doFilter(request, response);
	}

}
