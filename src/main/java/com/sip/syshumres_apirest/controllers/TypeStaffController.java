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
import com.sip.syshumres_entities.TypeStaff;
import com.sip.syshumres_entities.dtos.TypeStaffDTO;
import com.sip.syshumres_entities.dtos.common.EntitySelectDTO;
import com.sip.syshumres_exceptions.EntityIdNotFoundException;
import com.sip.syshumres_exceptions.IdsEntityNotEqualsException;
import com.sip.syshumres_exceptions.InvalidIdException;
import com.sip.syshumres_exceptions.utils.ErrorsBindingFields;
import com.sip.syshumres_services.TypeStaffService;
import com.sip.syshumres_utils.StringTrim;


@RestController
@RequestMapping(TypeStaffController.URLENDPOINT)
public class TypeStaffController extends CommonCatalogController {
	
	public static final String URLENDPOINT = "types-staff";
	
	private final TypeStaffService service;
	
	@Autowired
	public TypeStaffController(TypeStaffService service, 
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
	public ResponseEntity<List<TypeStaffDTO>> list() {
		return ResponseEntity.ok()
				.body(listMapper.mapList(listMapper.toList(service.findAll())
						, TypeStaffDTO.class));
	}
	
	@GetMapping(ID)
	public ResponseEntity<TypeStaffDTO> formEdit(@PathVariable Long id) 
			throws EntityIdNotFoundException, InvalidIdException {
		if (id <= 0) {
			throw new InvalidIdException();
		}
		Optional<TypeStaff> entity = service.findById(id);
		if(entity.isEmpty()) {
			throw new EntityIdNotFoundException();
		}
		
		return ResponseEntity.ok(this
				.modelMapper.map(entity.get(), TypeStaffDTO.class));
	}
	
	@PostMapping
	public ResponseEntity<?> create(@Valid @RequestBody TypeStaffDTO entity, BindingResult result) {
		if (result.hasErrors()) {
			return ErrorsBindingFields.validate(result);
		}
		
		TypeStaff e = service.save(this.modelMapper.map(entity, TypeStaff.class));
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(this.modelMapper.map(e, TypeStaffDTO.class));
	}
	
	@PutMapping(ID)
	public ResponseEntity<?> edit(@Valid @RequestBody TypeStaffDTO entity, BindingResult result, @PathVariable Long id) 
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
		Optional<TypeStaff> o = service.findById(id);
		if (!o.isPresent()) {
			throw new EntityIdNotFoundException();
		}
		
		TypeStaff e = o.get();
		e.setDescription(StringTrim.trimAndRemoveDiacriticalMarks(entity.getDescription()));
		e.setEnabled(entity.isEnabled());
		
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(this.modelMapper.map(service.save(e), TypeStaffDTO.class));
	}

}
