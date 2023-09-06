package com.sip.syshumres_apirest.controllers;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

import com.sip.syshumres_apirest.mappers.ModuleCustomMapper;
import com.sip.syshumres_entities.Module;
import com.sip.syshumres_entities.dtos.EntitySelectDTO;
import com.sip.syshumres_entities.dtos.ModuleDTO;
import com.sip.syshumres_exceptions.EntityIdNotFoundException;
import com.sip.syshumres_exceptions.FatherAssignException;
import com.sip.syshumres_exceptions.IdsEntityNotEqualsException;
import com.sip.syshumres_exceptions.utils.ErrorsBindingFields;
import com.sip.syshumres_services.ModuleService;
import com.sip.syshumres_utils.StringTrim;



@RestController
@RequestMapping(ModuleController.URLENDPOINT)
public class ModuleController {
	
	public static final String URLENDPOINT = "modules";
	public static final String ACTIVE = "/active";
	public static final String PAGE = "/page";
	public static final String PAGEORDER = "/page-order";
	public static final String PAGEFILTER = "/page-filter";
	public static final String PAGEFILTERORDER = "/page-filter-order";
	public static final String ID = "/{id}";
	
	private ModuleService service;
	
	private ModuleCustomMapper customMapper;
		
	private String filter;

	@Autowired
	public ModuleController(ModuleService service, 
			ModuleCustomMapper customMapper) {
		this.service = service;
		this.customMapper = customMapper;
		this.filter = "";
	}
	
	@GetMapping(ACTIVE)
	public ResponseEntity<List<EntitySelectDTO>> listActive() {
		return ResponseEntity.ok().body(service.findByEnabledTrueOrderByDescription().stream()
				.map(entity -> customMapper.toSelectDto(entity))
				.collect(Collectors.toList()));
	}
	
	/**
     * @param page          number of the page returned
     * @param size          number of entries in each page
     * @return Page object with entitys
     */
	@GetMapping(PAGE)
	public ResponseEntity<Page<ModuleDTO>> list(Pageable pageable) {
		Page<Module> entities = this.service.findByFilterSession(this.filter, pageable);
		
		Page<ModuleDTO> entitiesPageDTO = entities.map(entity -> {
		    return customMapper.toDto(entity);
		});

		return ResponseEntity.ok().body(entitiesPageDTO);
	}
	
	/**
     * @param page          number of the page returned
     * @param size          number of entries in each page
     * @param sort  		Sort object
     * @return Page object with entitys after sorting
     */
	//@LogWeb
	@GetMapping(PAGEORDER)
	public ResponseEntity<Page<ModuleDTO>> list(Pageable pageable, Sort sort) {
		this.filter = "";
		return this.list(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort));
	}
	
	/**
     * @param text			Filter for description, url, detail
     * @param page          number of the page returned
     * @param size          number of entries in each page
     * @return Page object with entitys after filtering
     */
	@GetMapping(PAGEFILTER)
	public ResponseEntity<Page<ModuleDTO>> list(String text, Pageable pageable) {
		this.filter = StringTrim.trimAndRemoveDiacriticalMarks(text);
		return this.list(pageable);
	}
	
	/**
     * @param text			Filter for description, url, detail
     * @param page          number of the page returned
     * @param size          number of entries in each page
     * @param sort  		Sort object
     * @return Page object with entitys after filtering and sorting
     */
	@GetMapping(PAGEFILTERORDER)
	public ResponseEntity<Page<ModuleDTO>> list(String text, Pageable pageable, Sort sort) {
		Pageable pageableOrder = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
		return this.list(text, pageableOrder);
	}
	
	@PostMapping
	public ResponseEntity<?> create(@Valid @RequestBody Module entity, BindingResult result) {
		if (result.hasErrors()) {
			return ErrorsBindingFields.validate(result);
		}
		
		return ResponseEntity.status(HttpStatus.CREATED).
				body(service.save(customMapper.toSaveEntity(entity)));
	}
	
	@GetMapping(ID)
	public ResponseEntity<ModuleDTO> formEdit(@PathVariable Long id) 
			throws EntityIdNotFoundException, IllegalArgumentException {
		if (id <= 0) {
			throw new IllegalArgumentException("Id no puede ser cero o negativo");
		}
		Optional<Module> entity = this.service.findById(id);
		if(entity.isEmpty()) {
			throw new EntityIdNotFoundException("Id modulo " + id + " no encontrado");
		}
		
		return ResponseEntity.ok(customMapper.toDto(entity.get()));
	}
	
	@PutMapping(ID)
	public ResponseEntity<?> edit(@Valid @RequestBody Module entity, BindingResult result, @PathVariable Long id) 
			throws EntityIdNotFoundException, IdsEntityNotEqualsException, FatherAssignException, IllegalArgumentException {
		if (result.hasErrors()) {
			return ErrorsBindingFields.validate(result);
		}
		
		if (id <= 0) {
			throw new IllegalArgumentException("Id no puede ser cero o negativo");
		}
		if(!Objects.equals(id, entity.getId())){
			throw new IdsEntityNotEqualsException("Ids de modulo no coinciden para actualización");
        }
		Optional<Module> o = this.service.findById(id);
		if (!o.isPresent()) {
			throw new EntityIdNotFoundException("Id modulo " + id + " no encontrado");
		}
		
		Module e = o.get();
		if (entity.getFather() != null && e.getId() == entity.getFather().getId()) {
			throw new FatherAssignException("El padre del modulo no puede ser el mismo");
		}

		return ResponseEntity.status(HttpStatus.CREATED).
				body(this.service.save(customMapper.toEditEntity(e, entity)));
	}

}
