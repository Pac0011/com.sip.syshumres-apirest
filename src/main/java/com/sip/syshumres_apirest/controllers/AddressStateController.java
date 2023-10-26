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
import com.sip.syshumres_entities.AddressState;
import com.sip.syshumres_exceptions.EntityIdNotFoundException;
import com.sip.syshumres_exceptions.IdsEntityNotEqualsException;
import com.sip.syshumres_exceptions.utils.ErrorsBindingFields;
import com.sip.syshumres_entities.dtos.AddressStateDTO;
import com.sip.syshumres_entities.dtos.common.EntitySelectDTO;
import com.sip.syshumres_services.AddressStateService;
import com.sip.syshumres_utils.StringTrim;


@RestController
@RequestMapping(AddressStateController.URLENDPOINT)
public class AddressStateController extends CommonCatalogController {
	
	public static final String URLENDPOINT = "addresses-states";
	
	private final AddressStateService service;
	
	@Autowired
	public AddressStateController(AddressStateService service, 
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
	public ResponseEntity<List<AddressStateDTO>> list() {
		return ResponseEntity.ok()
				.body(listMapper.mapList(listMapper.toList(service.findAll())
						, AddressStateDTO.class));
	}
	
	@GetMapping(ID)
	public ResponseEntity<AddressStateDTO> formEdit(@PathVariable Long id) 
			throws EntityIdNotFoundException, IllegalArgumentException {
		if (id <= 0) {
			throw new IllegalArgumentException("Id no puede ser cero o negativo");
		}
		Optional<AddressState> entity = service.findById(id);
		if(entity.isEmpty()) {
			throw new EntityIdNotFoundException();
		}
		
		return ResponseEntity.ok(this
				.modelMapper.map(entity.get(), AddressStateDTO.class));
	}
	
	@PostMapping
	public ResponseEntity<?> create(@Valid @RequestBody AddressStateDTO entity, BindingResult result) {
		if (result.hasErrors()) {
			return ErrorsBindingFields.validate(result);
		}
		
		AddressState e = service.save(this.modelMapper.map(entity, AddressState.class));
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(this.modelMapper.map(e, AddressStateDTO.class));
	}
	
	@PutMapping(ID)
	public ResponseEntity<?> edit(@Valid @RequestBody AddressStateDTO entity, BindingResult result, @PathVariable Long id) 
			throws EntityIdNotFoundException, IdsEntityNotEqualsException, IllegalArgumentException {
		if (result.hasErrors()) {
			return ErrorsBindingFields.validate(result);
		}
		
		if (id <= 0) {
			throw new IllegalArgumentException("Id no puede ser cero o negativo");
		}
		if(!Objects.equals(id, entity.getId())){
			throw new IdsEntityNotEqualsException();
        }
		Optional<AddressState> o = service.findById(id);
		if (!o.isPresent()) {
			throw new EntityIdNotFoundException();
		}
		
		AddressState e = o.get();
		e.setDescription(StringTrim.trimAndRemoveDiacriticalMarks(entity.getDescription()));
		e.setEnabled(entity.isEnabled());
		
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(this.modelMapper.map(service.save(e), AddressStateDTO.class));
	}

}
