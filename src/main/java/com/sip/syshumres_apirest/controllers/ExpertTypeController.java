package com.sip.syshumres_apirest.controllers;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sip.syshumres_apirest.controllers.common.CommonCatalogController;
import com.sip.syshumres_apirest.enums.StatusMessages;
import com.sip.syshumres_apirest.mappers.ListMapper;
import com.sip.syshumres_entities.ExpertType;
import com.sip.syshumres_entities.dtos.ExpertTypeDTO;
import com.sip.syshumres_entities.dtos.ResponseDTO;
import com.sip.syshumres_entities.dtos.common.EntitySelectDTO;
import com.sip.syshumres_entities.utils.ErrorsBindingFields;
import com.sip.syshumres_exceptions.CreateRegisterException;
import com.sip.syshumres_exceptions.EntityIdNotFoundException;
import com.sip.syshumres_exceptions.IdsEntityNotEqualsException;
import com.sip.syshumres_exceptions.InvalidIdException;
import com.sip.syshumres_exceptions.UpdateRegisterException;
import com.sip.syshumres_services.ExpertTypeService;
import com.sip.syshumres_utils.StringTrim;


@RestController
@RequestMapping(ExpertTypeController.URLENDPOINT)
public class ExpertTypeController extends CommonCatalogController {
	
	public static final String URLENDPOINT = "expert-types";
	
	private final ExpertTypeService service;
	
	@Autowired
	public ExpertTypeController(ExpertTypeService service, 
			ModelMapper modelMapper,
			ListMapper listMapper) {
		this.service = service;
		this.modelMapper = modelMapper;
		this.listMapper = listMapper;
	}
	
	@GetMapping(ACTIVE)
	public ResponseEntity<List<EntitySelectDTO>> listActive() {
		return ResponseEntity.ok().body(service.findByEnabledTrueOrderByDescription().stream()
				.map(entity -> modelMapper.map(entity, EntitySelectDTO.class))
				.toList());
	}
	
	@GetMapping
	public ResponseEntity<List<ExpertTypeDTO>> list() {
		return ResponseEntity.ok()
				.body(listMapper.mapList(listMapper.toList(service.findAll())
						, ExpertTypeDTO.class));
	}
	
	@GetMapping(ID)
	public ResponseEntity<ExpertTypeDTO> formEdit(@PathVariable Long id) 
			throws EntityIdNotFoundException, InvalidIdException {
		if (id <= 0) {
			throw new InvalidIdException();
		}
		Optional<ExpertType> entity = service.findById(id);
		if(entity.isEmpty()) {
			throw new EntityIdNotFoundException();
		}
		
		return ResponseEntity.ok(this
				.modelMapper.map(entity.get(), ExpertTypeDTO.class));
	}
	
	@PostMapping
	public ResponseEntity<ResponseDTO> create(@Valid @RequestBody ExpertTypeDTO entity, BindingResult result) 
	throws CreateRegisterException {
		if (result.hasErrors()) {
			return ResponseEntity.badRequest()
					.body(ErrorsBindingFields.getErrors(result));
		}
		
		ExpertType e = service.save(this.modelMapper.map(entity, ExpertType.class));
		if (e == null) {
			throw new CreateRegisterException();
		}
		ResponseDTO response = new ResponseDTO();
		response.addEntry(StatusMessages.MESSAGE_KEY.getMessage(), 
				StatusMessages.SUCCESS_CREATE.getMessage());
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(response);
	}
	
	@PutMapping(ID)
	public ResponseEntity<ResponseDTO> edit(@Valid @RequestBody ExpertTypeDTO entity, BindingResult result, @PathVariable Long id) 
			throws EntityIdNotFoundException, IdsEntityNotEqualsException, InvalidIdException
	, UpdateRegisterException {
		if (result.hasErrors()) {
			return ResponseEntity.badRequest()
					.body(ErrorsBindingFields.getErrors(result));
		}
		
		if (id <= 0) {
			throw new InvalidIdException();
		}
		if(!Objects.equals(id, entity.getId())){
			throw new IdsEntityNotEqualsException();
        }
		Optional<ExpertType> o = service.findById(id);
		if (!o.isPresent()) {
			throw new EntityIdNotFoundException();
		}
		
		ExpertType e = o.get();
		e.setDescription(StringTrim.trimAndRemoveDiacriticalMarks(entity.getDescription()));
		e.setEnabled(entity.isEnabled());
		
		if (service.save(e) == null) {
			throw new UpdateRegisterException();
		}
		ResponseDTO response = new ResponseDTO();
		response.addEntry(StatusMessages.MESSAGE_KEY.getMessage(), 
				StatusMessages.SUCCESS_UPDATE.getMessage());
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(response);
	}

}
