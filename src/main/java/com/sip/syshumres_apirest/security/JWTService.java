package com.sip.syshumres_apirest.security;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sip.syshumres_entities.User;
import com.sip.syshumres_entities.dtos.MenuDTO;
import com.sip.syshumres_apirest.config.AppProperties;
import com.sip.syshumres_entities.Module;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Autowired;

@Service
public class JWTService {
	private static final String ACCESS_TOKEN_SECRET = "sjkdhfjisd8726347jJHHHSDhsgdfhsgdfh";
	
	private final UserDetailServiceImpl userDetailServiceImpl;
	
	private final AppProperties appProperties;
	
	@Autowired
	public JWTService(UserDetailServiceImpl userDetailServiceImpl, AppProperties appProperties) {
		this.userDetailServiceImpl = userDetailServiceImpl;
		this.appProperties = appProperties;
	}

	public String createToken(String username, HttpServletRequest request) throws UsernameNotFoundException {
		long expirationTime = appProperties.getAccessTokenValiditySeconds() * 1_000;
		Date expirationDate = new Date(System.currentTimeMillis() + expirationTime);
		
		User userSession = (User) request.getSession().getAttribute(appProperties.getSessionUserName());
		Map<String, Object> extra = new HashMap<>();
		extra.put("branchOffice", userSession == null?"":userSession.getBranchOffice().getDescription());
		
		List<MenuDTO> listMenus = new ArrayList<>();
		List<Module> modulesChilds = userDetailServiceImpl.findModulesChildByUsername(username);
		List<Module> modules = userDetailServiceImpl.findModulesFatherByUsername(username);
		if (modules != null) {
			modules.stream().forEach(m -> {
				List<MenuDTO> listMenusChilds = new ArrayList<>();
				m.getChilds().forEach(c -> {
					if (modulesChilds.contains(c)) {
					    listMenusChilds.add(new MenuDTO(c.getDescription(), c.getUrlMenu(), c.getIcon()));
					}
				});
				listMenusChilds.sort(Comparator.comparing(MenuDTO::getN));
				listMenus.add(new MenuDTO(m.getDescription(), m.getUrlMenu(), m.getIcon(), listMenusChilds));
			});
		}
		extra.put("menu", listMenus);
		
		return Jwts.builder()
				.setSubject(username)
				.addClaims(extra)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(expirationDate)
				.signWith(Keys.hmacShaKeyFor(ACCESS_TOKEN_SECRET.getBytes()))
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
			UserDetails userDetails = userDetailServiceImpl.loadUserByUsername(username);
			
			return new UsernamePasswordAuthenticationToken(username, null, userDetails.getAuthorities());
		} catch (JwtException e) {
			return null;
		}
	}

}
