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
import com.sip.syshumres_entities.Module;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

@Service
public class JWTService {
	private final static String ACCESS_TOKEN_SECRET = "sjkdhfjisd8726347jJHHHSDhsgdfhsgdfh";
	
	private UserDetailServiceImpl userDetailServiceImpl;
	
	@Value("${SESSION.USER.NAME}")
	private String sessionUserName;
	
	@Value("${ACCESS_TOKEN_VALIDITY_SECONDS}")
	private Long accessTokenValiditySeconds;
	
	@Autowired
	public JWTService(UserDetailServiceImpl userDetailServiceImpl) {
		this.userDetailServiceImpl = userDetailServiceImpl;
	}

	public String createToken(String name, String username, HttpServletRequest request) throws UsernameNotFoundException {
		long expirationTime = this.accessTokenValiditySeconds * 1_000;
		Date expirationDate = new Date(System.currentTimeMillis() + expirationTime);
		
		User userSession = (User) request.getSession().getAttribute(this.sessionUserName);
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

    /*public Boolean hasTokenExpired(String token) {
        return Jwts.parser()
                .setSigningKey(ACCESS_TOKEN_SECRET)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return (userDetails.getUsername().equals(username) && !hasTokenExpired(token));

    }

    public String extractUsername(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public Collection<? extends GrantedAuthority> getAuthorities(String token) {
        Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
        return (Collection<? extends GrantedAuthority>) claims.get(AUTHORITIES);
    }*/
}
