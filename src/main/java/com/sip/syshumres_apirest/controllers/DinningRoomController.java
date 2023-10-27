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
import com.sip.syshumres_entities.DinningRoom;
import com.sip.syshumres_entities.dtos.DinningRoomDTO;
import com.sip.syshumres_entities.dtos.common.EntitySelectDTO;
import com.sip.syshumres_exceptions.EntityIdNotFoundException;
import com.sip.syshumres_exceptions.IdsEntityNotEqualsException;
import com.sip.syshumres_exceptions.InvalidIdException;
import com.sip.syshumres_exceptions.utils.ErrorsBindingFields;
import com.sip.syshumres_services.DinningRoomService;
import com.sip.syshumres_utils.StringTrim;

@RestController
@RequestMapping(DinningRoomController.URLENDPOINT)
public class DinningRoomController extends CommonCatalogController {
	
	public static final String URLENDPOINT = "dinning-room";
	
	private final DinningRoomService service;
	
	@Autowired
	public DinningRoomController(DinningRoomService service, 
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
	public ResponseEntity<List<DinningRoomDTO>> list() {
		return ResponseEntity.ok()
				.body(listMapper.mapList(listMapper.toList(service.findAll())
						, DinningRoomDTO.class));
	}
	
	@GetMapping(ID)
	public ResponseEntity<DinningRoomDTO> formEdit(@PathVariable Long id) 
			throws EntityIdNotFoundException, InvalidIdException {
		if (id <= 0) {
			throw new InvalidIdException();
		}
		Optional<DinningRoom> entity = service.findById(id);
		if(entity.isEmpty()) {
			throw new EntityIdNotFoundException();
		}
		
		return ResponseEntity.ok(this
				.modelMapper.map(entity.get(), DinningRoomDTO.class));
	}
	
	@PostMapping
	public ResponseEntity<?> create(@Valid @RequestBody DinningRoomDTO entity, BindingResult result) {
		if (result.hasErrors()) {
			return ErrorsBindingFields.validate(result);
		}
		
		DinningRoom e = service.save(this.modelMapper.map(entity, DinningRoom.class));
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(this.modelMapper.map(e, DinningRoomDTO.class));
	}
	
	@PutMapping(ID)
	public ResponseEntity<?> edit(@Valid @RequestBody DinningRoomDTO entity, BindingResult result, @PathVariable Long id) 
			throws EntityIdNotFoundException, IdsEntityNotEqualsException, InvalidIdException {
		if (result.hasErrors()) {
			return ErrorsBindingFields.validate(result);
		}
		
		if (id <= 0) {
			throw new InvalidIdException();
		}
		if(!Objects.equals(id, entity.getId())){
			throw new IdsEntityNotEqualsException("Ids de comedor no coinciden para actualización");
        }
		Optional<DinningRoom> o = this.service.findById(id);
		if (!o.isPresent()) {
			throw new EntityIdNotFoundException("Id comedor " + id + " no encontrado");
		}
		
		DinningRoom e = o.get();
		e.setDescription(StringTrim.trimAndRemoveDiacriticalMarks(entity.getDescription()));
		e.setPrice(entity.getPrice());
		e.setEnabled(entity.isEnabled());
		
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(this.modelMapper.map(service.save(e), DinningRoomDTO.class));
	}

}
