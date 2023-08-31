package com.sip.syshumres_apirest.security;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	private final JWTService jwtService;

	@Autowired
	public JWTAuthenticationFilter(JWTService jwtService) {
		super();
		this.jwtService = jwtService;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
												HttpServletResponse response) throws AuthenticationException {
		AuthCredentials authCredentials = new AuthCredentials();
		try {
			authCredentials = new ObjectMapper().readValue(request.getReader(), AuthCredentials.class);
		} catch (IOException e) {
			
		}
		
		UsernamePasswordAuthenticationToken usernamePAT = new UsernamePasswordAuthenticationToken(
				authCredentials.getUsername(),
				authCredentials.getPassword(),
				Collections.emptyList()
        );
				
		return getAuthenticationManager().authenticate(usernamePAT);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request,
											HttpServletResponse response,
											FilterChain chain,
											Authentication authResult) throws IOException, ServletException {
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authResult.getPrincipal();
		String token = jwtService.createToken(userDetails.getFirstName(), userDetails.getUsername(), request);
		
		response.addHeader("Authorization", "Bearer " + token);
		response.getWriter().flush();
		
		super.successfulAuthentication(request, response, chain, authResult);
	}

}
