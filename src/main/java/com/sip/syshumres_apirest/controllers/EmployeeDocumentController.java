package com.sip.syshumres_apirest.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sip.syshumres_entities.EmployeeDocument;
import com.sip.syshumres_exceptions.CreateRegisterException;
import com.sip.syshumres_exceptions.EntityIdNotFoundException;
import com.sip.syshumres_exceptions.TypeHiringDocumentNotExistException;
import com.sip.syshumres_exceptions.UploadFormatsAllowException;
import com.sip.syshumres_exceptions.utils.ErrorsBindingFields;
import com.sip.syshumres_services.EmployeeDocumentService;


@RestController
@RequestMapping(EmployeeDocumentController.URLENDPOINT)
public class EmployeeDocumentController {
	
	public static final String URLENDPOINT = "employee-documents";
	public static final String IDUPLOADFILE = "/{idEmployeeProfile}/upload-file";
	
    private EmployeeDocumentService service;
	
	@Autowired
	public EmployeeDocumentController(EmployeeDocumentService service) {
		this.service = service;
	}
	
	@PostMapping
	public ResponseEntity<?> save(@Valid @RequestBody EmployeeDocument entity, BindingResult result) {
		if (result.hasErrors()) {
			return ErrorsBindingFields.validate(result);
		}
		EmployeeDocument s = service.save(entity);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(s);
	}
	
	@PutMapping(IDUPLOADFILE)
	public ResponseEntity<Map<String, Object>> uploadFileNew(@PathVariable Long idEmployeeProfile, 
			@RequestParam("nameInput") Long idHiringDocument,
			@RequestParam MultipartFile fileUpload) throws IOException, UploadFormatsAllowException, 
			EntityIdNotFoundException, TypeHiringDocumentNotExistException, CreateRegisterException, IllegalArgumentException {
		Map<String, Object> response = new HashMap<>();
		
		if (idEmployeeProfile <= 0) {
			throw new IllegalArgumentException("Id no puede ser cero o negativo");
		}
		
		response = this.service.uploadFile(idEmployeeProfile, idHiringDocument, fileUpload);
		
		return ResponseEntity.ok().body(response);
	}

}
