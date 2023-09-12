package com.sip.syshumres_apirest.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sip.syshumres_entities.User;
import com.sip.syshumres_exceptions.ChangePasswordException;
import com.sip.syshumres_exceptions.UserSessionNotFoundException;
import com.sip.syshumres_services.UserService;
import com.sip.syshumres_utils.StringTrim;


@RestController
@RequestMapping(UserSettingsController.URLENDPOINT)
public class UserSettingsController {
	
	public static final String URLENDPOINT = "user-settings";
	public static final String CHANGE = "/change-password";
	
	private UserService service;
	
	@Value("${SESSION.USER.NAME}")
	private String sessionUserName;
	
	@Autowired
	public UserSettingsController(UserService service) {
		this.service = service;
	}
	
	@PatchMapping(CHANGE)
	public ResponseEntity<Map<String, Object>> changePassword(@RequestParam String passwordOld,
			@RequestParam String passwordNew, 
			@RequestParam String passwordNewConfirm, 
			HttpSession session) throws UserSessionNotFoundException, ChangePasswordException {
		User userSession = (User) session.getAttribute(this.sessionUserName);
		if (userSession == null) {
			throw new UserSessionNotFoundException("Su sesión ha caducado, vuelva a logearse");
		}
		
		String passOld = StringTrim.trimAndRemoveDiacriticalMarks(passwordOld);
		String passNew = StringTrim.trimAndRemoveDiacriticalMarks(passwordNew);
		String passConfirm = StringTrim.trimAndRemoveDiacriticalMarks(passwordNewConfirm);
		Map<String, Object> errorsCustomFields = this.service.validChangePassword(userSession.getPassword(), passOld, 
				passNew, passConfirm);
		if (errorsCustomFields != null) {
			return ResponseEntity.badRequest().body(errorsCustomFields);
		}
		
		if (this.service.saveNewPassword(userSession, passNew) == null) {
			throw new ChangePasswordException();
		}
		Map<String, Object> response = new HashMap<>();
		response.put("response", "La contraseña fue actualizada con éxito");
		
		return ResponseEntity.ok().body(response);
	}

}
