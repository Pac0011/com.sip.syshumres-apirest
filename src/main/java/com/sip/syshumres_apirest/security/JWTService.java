package com.sip.syshumres_apirest.security;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sip.syshumres_entities.dtos.MenuDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sip.syshumres_apirest.config.AppProperties;
import com.sip.syshumres_entities.Module;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Autowired;

@Service
public class JwtService {
	private static final String ACCESS_TOKEN_SECRET = "sjkdhfjisd8726347jJHHHSDhsgdfhsgdfh";
	
	private final UserDetailServiceImpl userDetailServiceImpl;
	
	private final AppProperties appProperties;
	
	@Autowired
	public JwtService(UserDetailServiceImpl userDetailServiceImpl, AppProperties appProperties) {
		this.userDetailServiceImpl = userDetailServiceImpl;
		this.appProperties = appProperties;
	}

	public String createToken(UserDetailsImpl userDetailsImpl) 
			throws UsernameNotFoundException, JsonProcessingException {
		long expirationTime = appProperties.getAccessTokenValiditySeconds() * 1_000;
		Date expirationDate = new Date(System.currentTimeMillis() + expirationTime);
		
		Map<String, Object> extra = new HashMap<>();
		//userDetails
		extra.put("idBranchOffice", userDetailsImpl.getBranchOffice().getId());
		extra.put("branchOffice", userDetailsImpl.getBranchOffice().getDescription());
		extra.put("multiBranchOffice", userDetailsImpl.isMultiBranchOffice());
		extra.put("seeAllBranchs", userDetailsImpl.isSeeAllBranchs());
		
		//Generate menu for username
        String menubase64 = encodeToBase64(convertListToJson(generateMenu(userDetailsImpl.getUsername())));
        extra.put("menu", menubase64);

		return Jwts.builder()
				.setSubject(userDetailsImpl.getUsername())
				.addClaims(extra)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(expirationDate)
				.signWith(Keys.hmacShaKeyFor(ACCESS_TOKEN_SECRET.getBytes()))//.signWith(SignatureAlgorithm.HS512, jwtSecret)
				.compact();
	}
    
    public UsernamePasswordAuthenticationToken getAuthentication(String token) 
			throws UsernameNotFoundException {
		try {
			Claims claims = Jwts.parserBuilder()
					.setSigningKey(ACCESS_TOKEN_SECRET.getBytes())
					.build()
					.parseClaimsJws(token)
					.getBody();
			String username = claims.getSubject();
			//Get user details
			UserDetails userDetails = userDetailServiceImpl.loadUserByUsername(username);
			
			return new UsernamePasswordAuthenticationToken(username, null, userDetails.getAuthorities());
		} catch (JwtException e) {
			return null;
		}
	}
    
    private List<MenuDTO> generateMenu(String username) {
    	List<MenuDTO> listMenus = new ArrayList<>();
    	
		List<Module> modulesChilds = userDetailServiceImpl.findModulesChildByUsername(username);
		List<Module> modules = userDetailServiceImpl.findModulesFatherByUsername(username);
		if (modules != null) {
			modules.stream().forEach(module -> {
				List<MenuDTO> listMenusChilds = new ArrayList<>();
				module.getChilds().forEach(c -> {
					if (modulesChilds.contains(c)) {
					    listMenusChilds.add(new MenuDTO(c.getDescription(), c.getUrlMenu(), c.getIcon()));
					}
				});
				listMenusChilds.sort(Comparator.comparing(MenuDTO::getN));
				listMenus.add(new MenuDTO(module.getDescription(), module.getUrlMenu(), module.getIcon(), listMenusChilds));
			});
		}
    	
		return listMenus;
    }
    
    private String convertListToJson(List<MenuDTO> list) 
    		throws JsonProcessingException {
        // Utilizar Jackson para convertir la lista a JSON
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(list);
    }
    
    private String encodeToBase64(String input) {
        Base64.Encoder encoder = Base64.getEncoder();
        
        return encoder.encodeToString(input.getBytes());
    }

}
