package com.sip.syshumres_apirest.controllers;

import java.io.IOException;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
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

import com.sip.syshumres_apirest.enums.StatusMessages;
import com.sip.syshumres_entities.EmployeeDocument;
import com.sip.syshumres_entities.dtos.EmployeeDocumentDTO;
import com.sip.syshumres_entities.dtos.ResponseDTO;
import com.sip.syshumres_entities.utils.ErrorsBindingFields;
import com.sip.syshumres_exceptions.CreateRegisterException;
import com.sip.syshumres_exceptions.EntityIdNotFoundException;
import com.sip.syshumres_exceptions.InvalidIdException;
import com.sip.syshumres_exceptions.TypeHiringDocumentNotExistException;
import com.sip.syshumres_exceptions.UploadFileException;
import com.sip.syshumres_exceptions.UploadFormatsAllowException;
import com.sip.syshumres_services.EmployeeDocumentService;


@RestController
@RequestMapping(EmployeeDocumentController.URLENDPOINT)
public class EmployeeDocumentController {
	
	public static final String URLENDPOINT = "employee-documents";
	public static final String IDUPLOADFILE = "/{idEmployeeProfile}/upload-file";
	
    private final EmployeeDocumentService service;
    
    private final ModelMapper modelMapper;
	
	@Autowired
	public EmployeeDocumentController(EmployeeDocumentService service, 
			ModelMapper modelMapper) {
		this.service = service;
		this.modelMapper = modelMapper;
	}
	
	@PostMapping
	public ResponseEntity<ResponseDTO> create(@Valid @RequestBody EmployeeDocumentDTO entity, BindingResult result) 
	throws CreateRegisterException {
		if (result.hasErrors()) {
			return ResponseEntity.badRequest()
					.body(ErrorsBindingFields.getErrors(result));
		}
		EmployeeDocument e = service.save(this.modelMapper.map(entity, EmployeeDocument.class));
		if (e == null) {
			throw new CreateRegisterException();
		}
		ResponseDTO response = new ResponseDTO();
		response.addEntry(StatusMessages.MESSAGE_KEY.getMessage(), 
				StatusMessages.SUCCESS_CREATE.getMessage());
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(response);
	}
	
	@PutMapping(IDUPLOADFILE)
	public ResponseEntity<ResponseDTO> uploadFileNew(@PathVariable Long idEmployeeProfile, 
			@RequestParam("nameInput") Long idHiringDocument,
			@RequestParam MultipartFile fileUpload) throws IOException, UploadFormatsAllowException
	, EntityIdNotFoundException, TypeHiringDocumentNotExistException, CreateRegisterException
	, InvalidIdException, UploadFileException {
		if (idEmployeeProfile <= 0) {
			throw new InvalidIdException();
		}
		
		String urlFile = this.service.uploadFile(idEmployeeProfile, idHiringDocument, fileUpload);
		ResponseDTO response = new ResponseDTO();
		response.addEntry(StatusMessages.MESSAGE_KEY.getMessage(), 
				StatusMessages.SUCCESS_UPLOAD.getMessage());
		response.addEntry(StatusMessages.URLFILE_KEY.getMessage(), urlFile);
		return ResponseEntity.ok().body(response);
	}

}
