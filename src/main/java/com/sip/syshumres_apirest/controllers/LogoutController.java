package com.sip.syshumres_apirest.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sip.syshumres_entities.User;
import com.sip.syshumres_exceptions.UsernameNotFoundException;
import com.sip.syshumres_services.UserService;


@RestController
public class LogoutController {
	
	public static final String LOGOUT = "logout";
	
	private UserService service;
	
	@Value("${SESSION.USER.NAME}")
	private String sessionUserName;
	
	@Autowired
	public LogoutController(UserService service) {
		this.service = service;
	}

	@PostMapping(LOGOUT)
	public ResponseEntity<Map<String, Object>> logout(HttpSession session) throws UsernameNotFoundException {
		Map<String, Object> response = new HashMap<>();
		response.put("response", "Logout exitoso");
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        
        Optional<User> entity = service.findOneByUsername(username);
        if(entity.isEmpty()) {
			throw new UsernameNotFoundException();
		}
        session = service.logout(session, sessionUserName);
        //User e = entity.get();
        //user.setOnline(false);
        //userServ.saveUser(user);
        
        //BranchOffice branchOfficeSession2 = (BranchOffice) session.getAttribute(this.sessionUserName);
        //System.out.println("branchOfficeSession 2: " + branchOfficeSession2.getDescription());
        
        return ResponseEntity.ok().body(response);
    }

}
