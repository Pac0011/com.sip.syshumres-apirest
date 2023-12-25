package com.sip.syshumres_apirest.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sip.syshumres_apirest.enums.StatusMessages;
import com.sip.syshumres_entities.User;
import com.sip.syshumres_entities.dtos.ResponseDTO;
import com.sip.syshumres_exceptions.UsernameNotFoundException;
import com.sip.syshumres_services.UserService;


@RestController
public class LogoutController {
	
	public static final String LOGOUT = "logout";
	
	private final UserService service;
		
	@Autowired
	public LogoutController(UserService service) {
		this.service = service;
	}

	@PostMapping(LOGOUT)
	public ResponseEntity<ResponseDTO> logoutUser() throws UsernameNotFoundException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        
        Optional<User> entity = service.findOneByUsername(username);
        if(entity.isEmpty()) {
			throw new UsernameNotFoundException();
		}
        //service.logout(session, appProperties.getSessionUserName());
        //User e = entity.get()
        //user.setOnline(false)
        //userServ.saveUser(user)
        
        //BranchOffice branchOfficeSession2 = (BranchOffice) session.getAttribute(this.sessionUserName)
        //System.out.println("branchOfficeSession 2: " + branchOfficeSession2.getDescription())
        
        ResponseDTO response = new ResponseDTO();
		response.addEntry(StatusMessages.MESSAGE_KEY.getMessage(), 
				StatusMessages.SUCCESS_LOGOUT.getMessage());
        return ResponseEntity.ok().body(response);
    }

}
