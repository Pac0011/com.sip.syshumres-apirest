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
import com.sip.syshumres_entities.SchoolGradeComplete;
import com.sip.syshumres_entities.dtos.SchoolGradeCompleteDTO;
import com.sip.syshumres_entities.dtos.common.EntitySelectDTO;
import com.sip.syshumres_exceptions.EntityIdNotFoundException;
import com.sip.syshumres_exceptions.IdsEntityNotEqualsException;
import com.sip.syshumres_exceptions.utils.ErrorsBindingFields;
import com.sip.syshumres_services.SchoolGradeCompleteService;
import com.sip.syshumres_utils.StringTrim;


@RestController
@RequestMapping(SchoolGradeCompleteController.URLENDPOINT)
public class SchoolGradeCompleteController extends CommonCatalogController {
	
	public static final String URLENDPOINT = "school-grades-complete";
	
	private final SchoolGradeCompleteService service;
	
	@Autowired
	public SchoolGradeCompleteController(SchoolGradeCompleteService service, 
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
	
	@GetMapping
	public ResponseEntity<List<SchoolGradeCompleteDTO>> list() {
		return ResponseEntity.ok()
				.body(listMapper.mapList(listMapper.toList(service.findAll())
						, SchoolGradeCompleteDTO.class));
	}
	
	@GetMapping(ID)
	public ResponseEntity<SchoolGradeCompleteDTO> formEdit(@PathVariable Long id) 
			throws EntityIdNotFoundException, IllegalArgumentException {
		if (id <= 0) {
			throw new IllegalArgumentException("Id no puede ser cero o negativo");
		}
		Optional<SchoolGradeComplete> entity = service.findById(id);
		if(entity.isEmpty()) {
			throw new EntityIdNotFoundException();
		}
		
		return ResponseEntity.ok(this
				.modelMapper.map(entity.get(), SchoolGradeCompleteDTO.class));
	}
	
	@PostMapping
	public ResponseEntity<?> create(@Valid @RequestBody SchoolGradeCompleteDTO entity, BindingResult result) {
		if (result.hasErrors()) {
			return ErrorsBindingFields.validate(result);
		}
		
		SchoolGradeComplete e = service.save(this.modelMapper.map(entity, SchoolGradeComplete.class));
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(this.modelMapper.map(e, SchoolGradeCompleteDTO.class));
	}
	
	@PutMapping(ID)
	public ResponseEntity<?> edit(@Valid @RequestBody SchoolGradeCompleteDTO entity, BindingResult result, @PathVariable Long id) 
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
		Optional<SchoolGradeComplete> o = service.findById(id);
		if (!o.isPresent()) {
			throw new EntityIdNotFoundException();
		}
		
		SchoolGradeComplete e = o.get();
		e.setDescription(StringTrim.trimAndRemoveDiacriticalMarks(entity.getDescription()));
		e.setEnabled(entity.isEnabled());
		
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(this.modelMapper.map(service.save(e), SchoolGradeCompleteDTO.class));
	}

}
