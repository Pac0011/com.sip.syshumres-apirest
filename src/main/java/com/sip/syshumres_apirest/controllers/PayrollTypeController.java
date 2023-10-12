package com.sip.syshumres_apirest.controllers;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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
import com.sip.syshumres_entities.PayrollType;
import com.sip.syshumres_entities.dtos.PayrollTypeDTO;
import com.sip.syshumres_entities.dtos.common.EntitySelectDTO;
import com.sip.syshumres_exceptions.EntityIdNotFoundException;
import com.sip.syshumres_exceptions.IdsEntityNotEqualsException;
import com.sip.syshumres_exceptions.utils.ErrorsBindingFields;
import com.sip.syshumres_services.PayrollTypeService;
import com.sip.syshumres_utils.StringTrim;


@RestController
@RequestMapping(PayrollTypeController.URLENDPOINT)
public class PayrollTypeController extends CommonCatalogController {
	
	public static final String URLENDPOINT = "payroll-types";
	public static final String NORMAL = "/normal";
	
	private final PayrollTypeService service;
	
	@Autowired
	public PayrollTypeController(PayrollTypeService service, 
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
				.collect(Collectors.toList()));
	}
	
	@GetMapping(NORMAL + ACTIVE)
	public ResponseEntity<List<EntitySelectDTO>> listNormalActive() {
		return ResponseEntity.ok().body(service.findNormalByEnabledTrueOrderByDescription().stream()
				.map(entity -> modelMapper.map(entity, EntitySelectDTO.class))
				.collect(Collectors.toList()));
	}
	
	@PostMapping
	public ResponseEntity<?> create(@Valid @RequestBody PayrollTypeDTO entity, BindingResult result) {
		if (result.hasErrors()) {
			return ErrorsBindingFields.validate(result);
		}
		
		PayrollType e = new PayrollType();
		e.setDescription(StringTrim.trimAndRemoveDiacriticalMarks(entity.getDescription()));
		e.setEnabled(entity.isEnabled());
		PayrollType e2 = this.service.save(e);
		
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(modelMapper.map(e2, PayrollTypeDTO.class));
	}
	
	@GetMapping(ID)
	public ResponseEntity<PayrollTypeDTO> formEdit(@PathVariable Long id) 
			throws EntityIdNotFoundException, IllegalArgumentException {
		if (id <= 0) {
			throw new IllegalArgumentException("Id no puede ser cero o negativo");
		}
		Optional<PayrollType> entity = this.service.findById(id);
		if(entity.isEmpty()) {
			throw new EntityIdNotFoundException("Id rol " + id + " no encontrado");
		}
		
		return ResponseEntity.ok(modelMapper.map(entity.get(), PayrollTypeDTO.class));
	}
	
	@PutMapping(ID)
	public ResponseEntity<?> edit(@Valid @RequestBody PayrollTypeDTO entity, BindingResult result, @PathVariable Long id) 
			throws EntityIdNotFoundException, IdsEntityNotEqualsException, IllegalArgumentException {
		if (result.hasErrors()) {
			return ErrorsBindingFields.validate(result);
		}
		
		if (id <= 0) {
			throw new IllegalArgumentException("Id no puede ser cero o negativo");
		}
		if(!Objects.equals(id, entity.getId())){
			throw new IdsEntityNotEqualsException("Ids de documento de contratación no coinciden para actualización");
        }
		Optional<PayrollType> o = this.service.findById(id);
		if (!o.isPresent()) {
			throw new EntityIdNotFoundException("Id documento de contratación " + id + " no encontrado");
		}
		
		PayrollType e = o.get();
		e.setDescription(StringTrim.trimAndRemoveDiacriticalMarks(entity.getDescription()));
		e.setEnabled(entity.isEnabled());
		PayrollType e2 = this.service.save(e);
		
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(modelMapper.map(e2, PayrollTypeDTO.class));
	}
	
}
