package com.sip.syshumres_apirest.controllers;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sip.syshumres_apirest.enums.StatusMessages;
import com.sip.syshumres_entities.User;
import com.sip.syshumres_entities.dtos.ResponseDTO;
import com.sip.syshumres_entities.utils.ErrorsBindingFields;
import com.sip.syshumres_exceptions.ChangePasswordException;
import com.sip.syshumres_exceptions.UserSessionNotFoundException;
import com.sip.syshumres_services.UserService;
import com.sip.syshumres_utils.StringTrim;


@RestController
@RequestMapping(UserSettingsController.URLENDPOINT)
public class UserSettingsController {
	
	public static final String URLENDPOINT = "user-settings";
	public static final String CHANGE = "/change-password";
	
	private final UserService service;
	
	@Value("${SESSION.USER.NAME}")
	private String sessionUserName;
	
	@Autowired
	public UserSettingsController(UserService service) {
		this.service = service;
	}
	
	@PatchMapping(CHANGE)
	public ResponseEntity<ResponseDTO> changePassword(@RequestParam String passwordOld,
			@RequestParam String passwordNew, 
			@RequestParam String passwordNewConfirm, 
			HttpSession session) throws UserSessionNotFoundException, ChangePasswordException {
		User userSession = (User) session.getAttribute(this.sessionUserName);
		if (userSession == null) {
			throw new UserSessionNotFoundException("Su sesión ha caducado, vuelva a logearse");
		}
		
		String passOld = StringTrim.trimAndRemoveDiacriticalMarksPassword(passwordOld);
		String passNew = StringTrim.trimAndRemoveDiacriticalMarksPassword(passwordNew);
		String passConfirm = StringTrim.trimAndRemoveDiacriticalMarksPassword(passwordNewConfirm);
		Map<String, String> errorsCustomFields = this.service.validChangePassword(userSession.getPassword(), passOld, 
				passNew, passConfirm);
		if (!errorsCustomFields.isEmpty()) {
			return ResponseEntity.badRequest()
					.body(ErrorsBindingFields.getErrors(errorsCustomFields));
		}
		
		if (this.service.saveNewPassword(userSession, passNew) == null) {
			throw new ChangePasswordException();
		}
		
		ResponseDTO response = new ResponseDTO();
		response.addEntry(StatusMessages.MESSAGE_KEY.getMessage(), 
				StatusMessages.SUCCESS_CHANGE_PASSWORD.getMessage());
		return ResponseEntity.ok().body(response);
	}

}
