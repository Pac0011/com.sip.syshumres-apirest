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
import com.sip.syshumres_apirest.mappers.ListMapper;
import com.sip.syshumres_entities.EmployeeStatus;
import com.sip.syshumres_entities.dtos.EmployeeStatusDTO;
import com.sip.syshumres_exceptions.EntityIdNotFoundException;
import com.sip.syshumres_exceptions.IdsEntityNotEqualsException;
import com.sip.syshumres_exceptions.InvalidIdException;
import com.sip.syshumres_exceptions.utils.ErrorsBindingFields;
import com.sip.syshumres_services.EmployeeStatusService;
import com.sip.syshumres_utils.StringTrim;


@RestController
@RequestMapping(EmployeeStatusController.URLENDPOINT)
public class EmployeeStatusController extends CommonCatalogController {
	
	public static final String URLENDPOINT = "employee-status";
	
	private final EmployeeStatusService service;
	
	@Autowired
	public EmployeeStatusController(EmployeeStatusService service, 
			ModelMapper modelMapper,
			ListMapper listMapper) {
		this.service = service;
		this.modelMapper = modelMapper;
		this.listMapper = listMapper;
	}
	
	@GetMapping
	public ResponseEntity<List<EmployeeStatusDTO>> list() {
		return ResponseEntity.ok()
				.body(listMapper.mapList(listMapper.toList(service.findAll())
						, EmployeeStatusDTO.class));
	}
	
	@GetMapping(ID)
	public ResponseEntity<EmployeeStatusDTO> formEdit(@PathVariable Long id) 
			throws EntityIdNotFoundException, InvalidIdException {
		if (id <= 0) {
			throw new InvalidIdException();
		}
		Optional<EmployeeStatus> entity = service.findById(id);
		if(entity.isEmpty()) {
			throw new EntityIdNotFoundException();
		}
		
		return ResponseEntity.ok(this
				.modelMapper.map(entity.get(), EmployeeStatusDTO.class));
	}
	
	@PostMapping
	public ResponseEntity<?> create(@Valid @RequestBody EmployeeStatusDTO entity, BindingResult result) {
		if (result.hasErrors()) {
			return ErrorsBindingFields.validate(result);
		}
		
		EmployeeStatus e = service.save(this.modelMapper.map(entity, EmployeeStatus.class));
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(this.modelMapper.map(e, EmployeeStatusDTO.class));
	}
	
	@PutMapping(ID)
	public ResponseEntity<?> edit(@Valid @RequestBody EmployeeStatusDTO entity, BindingResult result, @PathVariable Long id) 
			throws EntityIdNotFoundException, IdsEntityNotEqualsException, InvalidIdException {
		if (result.hasErrors()) {
			return ErrorsBindingFields.validate(result);
		}
		
		if (id <= 0) {
			throw new InvalidIdException();
		}
		if(!Objects.equals(id, entity.getId())){
			throw new IdsEntityNotEqualsException();
        }
		Optional<EmployeeStatus> o = service.findById(id);
		if (!o.isPresent()) {
			throw new EntityIdNotFoundException();
		}
		
		EmployeeStatus e = o.get();
		e.setDescription(StringTrim.trimAndRemoveDiacriticalMarks(entity.getDescription()));
		e.setEnabled(entity.isEnabled());
		
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(this.modelMapper.map(service.save(e), EmployeeStatusDTO.class));
	}

}
