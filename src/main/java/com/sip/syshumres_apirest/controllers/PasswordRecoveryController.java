package com.sip.syshumres_apirest.controllers;

import java.util.Optional;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sip.syshumres_apirest.enums.StatusMessages;
import com.sip.syshumres_entities.PasswordRecovery;
import com.sip.syshumres_entities.User;
import com.sip.syshumres_entities.dtos.ResponseDTO;
import com.sip.syshumres_exceptions.CreateRegisterException;
import com.sip.syshumres_exceptions.EmailUserNotFoundException;
import com.sip.syshumres_exceptions.EntityIdNotFoundException;
import com.sip.syshumres_exceptions.InvalidFieldException;
import com.sip.syshumres_exceptions.SendEmailException;
import com.sip.syshumres_exceptions.UpdateRegisterException;
import com.sip.syshumres_exceptions.UrlInvalidException;
import com.sip.syshumres_services.EmailService;
import com.sip.syshumres_services.PasswordRecoveryService;
import com.sip.syshumres_services.UserService;
import com.sip.syshumres_utils.StringTrim;

import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping(PasswordRecoveryController.URLENDPOINT)
public class PasswordRecoveryController {
	
	public static final String URLENDPOINT = "password-recovery";
	public static final String UUID = "/{uuid}";
	public static final String CHANGE = "/change-password";
	
	private static final String MSG_NOT_EXIST = " no existe";
	
	private final PasswordRecoveryService service;
	
	private final UserService serviceU;
	
	private final EmailService serviceEmail;
	
	@Autowired
	public PasswordRecoveryController(PasswordRecoveryService service, UserService serviceU,
			EmailService serviceEmail) {
		this.service = service;
		this.serviceU = serviceU;
		this.serviceEmail = serviceEmail;
	}

	@PostMapping
	public ResponseEntity<ResponseDTO> passwordRecovery(@RequestParam String email) 
			throws EmailUserNotFoundException, CreateRegisterException, SendEmailException {
		Optional<User> o = this.serviceU.findOneByEmail(StringTrim.trimAndRemoveDiacriticalMarks(email));
		if (!o.isPresent()) {
			throw new EmailUserNotFoundException("El email no existe, valide que este correcto");
		}
		User u = o.get();
		if (!u.isEnabled()) {
			throw new EmailUserNotFoundException("El usuario asociado a este email está deshabilitado");
		}
		
		PasswordRecovery e = this.service.savePasswordRecovery(email);
		if (e == null) {
			throw new CreateRegisterException("No se pudo crear recuperación de password, valide la info enviada");
		}
		
		ResponseDTO response = new ResponseDTO();
		try {
			String link = this.serviceEmail.sendHtmlEmailRecoveryPassword(email, "Recuperación contraseña Sysrh", e);
			//response.addEntry(message, "Se envió un email con una liga para que pueda cambiar su contraseña, valide sus bandejas de entrada")
			response.addEntry("message", link);
		} catch (MessagingException ex) {
			throw new SendEmailException("No se pudo enviar el email con la liga de recuperación, valide con el administrador");
		}
		
		return ResponseEntity.ok().body(response);
	}
	
	@GetMapping(UUID)
	public ResponseEntity<PasswordRecovery> formEdit(@PathVariable String uuid) 
			throws EntityIdNotFoundException, UrlInvalidException {
		String tmp = StringTrim.trimAndRemoveDiacriticalMarks(uuid);
		if (tmp.equals("")) {
			throw new EntityIdNotFoundException("Cadena vacia");
		}
		
		Optional<PasswordRecovery> o = this.service.findOneByUuidAndEnabledTrue(tmp);
		if (!o.isPresent()) {
			throw new EntityIdNotFoundException("La cadena de recuperación " + tmp + MSG_NOT_EXIST);
		}
		PasswordRecovery passwordRecovery = o.get();
		if (this.service.expirationLinkRecovery(passwordRecovery)) {
			throw new UrlInvalidException("La url de recuperación ha caducado, vuelva a generar otra nueva");
		}
		
		return ResponseEntity.ok(passwordRecovery);
	}
	
	@PatchMapping(UUID + CHANGE)
	public ResponseEntity<ResponseDTO> changePassword(@PathVariable String uuid,
			@RequestParam String passwordNew, 
			@RequestParam String passwordNewConfirm, 
			HttpSession session) throws EntityIdNotFoundException, EmailUserNotFoundException, InvalidFieldException, 
	UpdateRegisterException {
		String tmp = StringTrim.trimAndRemoveDiacriticalMarks(uuid);
		if (tmp.equals("")) {
			throw new EntityIdNotFoundException("Cadena vacia");
		}
		Optional<PasswordRecovery> o = this.service.findOneByUuidAndEnabledTrue(tmp);
		if (!o.isPresent()) {
			throw new EntityIdNotFoundException("La cadena de recuperación " + tmp + MSG_NOT_EXIST);
		}
		PasswordRecovery passwordRecovery = o.get();
		
		Optional<User> o2 = this.serviceU.findOneByEmail(passwordRecovery.getEmail());
		if (!o2.isPresent()) {
			throw new EmailUserNotFoundException("El email de recuperación " + passwordRecovery.getEmail() + MSG_NOT_EXIST);
		}
		User user = o2.get();
		
		String passN = StringTrim.trimAndRemoveDiacriticalMarks(passwordNew);
		if (passN.equals("")) {
			throw new InvalidFieldException("La contraseña no debe estar vacia");
		}
		
		String passC = StringTrim.trimAndRemoveDiacriticalMarks(passwordNewConfirm);
		if (!passN.equals(passC)) {
			throw new InvalidFieldException("La confirmación de la contraseña no es la misma");
		}
		
		passwordRecovery.setEnabled(false);
		if (this.service.save(passwordRecovery) == null) {
			throw new UpdateRegisterException("No se pudo actualizar el password de recuperación, valide con el administrador");
		}
		
		if (this.serviceU.saveNewPassword(user, passN) == null) {
			throw new UpdateRegisterException("No se pudo actualizar el password, valide con el administrador");
		} 
		
		ResponseDTO response = new ResponseDTO();
		response.addEntry(StatusMessages.MESSAGE_KEY.getMessage(), 
				StatusMessages.SUCCESS_CHANGE_PASSWORD.getMessage());
		return ResponseEntity.ok().body(response);
	}

}
