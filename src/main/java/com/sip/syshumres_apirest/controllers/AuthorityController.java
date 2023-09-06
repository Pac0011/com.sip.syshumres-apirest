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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sip.syshumres_apirest.mappers.AuthorityMapper;
import com.sip.syshumres_entities.Module;
import com.sip.syshumres_entities.Authority;
import com.sip.syshumres_entities.dtos.AuthorityDTO;
import com.sip.syshumres_entities.dtos.EntitySelectDTO;
import com.sip.syshumres_exceptions.EntityIdNotFoundException;
import com.sip.syshumres_exceptions.IdsEntityNotEqualsException;
import com.sip.syshumres_exceptions.MalFormedHeaderException;
import com.sip.syshumres_exceptions.UnauthorizedException;
import com.sip.syshumres_exceptions.utils.ErrorsBindingFields;
import com.sip.syshumres_services.AuthorityService;
import com.sip.syshumres_utils.StringTrim;

import org.springframework.web.bind.annotation.RequestHeader;


@RestController
@RequestMapping(AuthorityController.URLENDPOINT)
public class AuthorityController {
	
	public static final String URLENDPOINT = "authorities";
	public static final String ACTIVE = "/active";
	public static final String PAGE = "/page";
	public static final String PAGEORDER = "/page-order";
	public static final String PAGEFILTER = "/page-filter";
	public static final String PAGEFILTERORDER = "/page-filter-order";
	public static final String ID = "/{id}";
	public static final String MODULES = "/modules";
	public static final String AMODULES = "/assign-modules";
	public static final String RMODULE = "/remove-module";
	public static final String ERROR = "/error";
	
    private AuthorityService service;
    
    private AuthorityMapper customMapper;
		
    private String filter;
    
    @Autowired
	public AuthorityController(AuthorityService service, 
			AuthorityMapper customMapper) {
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
	
	@GetMapping(PAGE)
	public ResponseEntity<Page<AuthorityDTO>> list(Pageable pageable) {
		Page<Authority> entities = this.service.findByFilterSession(this.filter, pageable);
		
		Page<AuthorityDTO> entitiesPageDTO = entities.map(entity -> {
			AuthorityDTO dto = customMapper.toDto(entity);
			dto.setModulesDtos(this.service.getModulesChilds(entity));
		    return dto;
		});

		return ResponseEntity.ok().body(entitiesPageDTO);
	}
	
	/**
     * @param page          number of the page returned
     * @param size          number of entries in each page
     * @param sort  		Sort object
     * @return Page object with entitys after sorting
     */
	@GetMapping(PAGEORDER)
	public ResponseEntity<Page<AuthorityDTO>> list(Pageable pageable, Sort sort) {
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
	public ResponseEntity<Page<AuthorityDTO>> list(String text, Pageable pageable) {
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
	public ResponseEntity<Page<AuthorityDTO>> list(String text, Pageable pageable, Sort sort) {
		Pageable pageableOrder = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
		return this.list(text, pageableOrder);
	}
	
	@GetMapping(ID + MODULES)
	public ResponseEntity<AuthorityDTO> getModules(@PathVariable Long id) throws EntityIdNotFoundException {
		Optional<Authority> entity = service.findById(id);
		if(entity.isEmpty()) {
			throw new EntityIdNotFoundException("Id rol " + id + " no encontrado");
		}
	
		return ResponseEntity.ok(customMapper.toDto(entity.get()));
	}
	
	@PostMapping
	public ResponseEntity<?> create(@Valid @RequestBody Authority entity, BindingResult result) {
		if (result.hasErrors()) {
			return ErrorsBindingFields.validate(result);
		}
		Authority e = service.save(customMapper.toSaveEntity(entity));
		return ResponseEntity.status(HttpStatus.CREATED).body(e);
	}
	
	@GetMapping(ID)
	public ResponseEntity<AuthorityDTO> formEdit(@PathVariable Long id) 
			throws EntityIdNotFoundException, IllegalArgumentException {
		if (id <= 0) {
			throw new IllegalArgumentException("Id no puede ser cero o negativo");
		}
		Optional<Authority> entity = this.service.findById(id);
		if(entity.isEmpty()) {
			throw new EntityIdNotFoundException("Id rol " + id + " no encontrado");
		}
		
		return ResponseEntity.ok(customMapper.toDto(entity.get()));
	}
	
	//@LogWeb
	@PutMapping(ID)
	public ResponseEntity<?> edit(@Valid @RequestBody Authority entity, BindingResult result, @PathVariable Long id) 
			throws EntityIdNotFoundException, IdsEntityNotEqualsException, IllegalArgumentException {
		if (result.hasErrors()) {
			return ErrorsBindingFields.validate(result);
		}
		
		if (id <= 0) {
			throw new IllegalArgumentException("Id no puede ser cero o negativo");
		}
		if(!Objects.equals(id, entity.getId())){
			throw new IdsEntityNotEqualsException("Ids de rol no coinciden para actualización");
        }
		Optional<Authority> o = this.service.findById(id);
		if (!o.isPresent()) {
			throw new EntityIdNotFoundException("Id rol " + id + " no encontrado");
		}
		
		return ResponseEntity.status(HttpStatus.CREATED).
				body(this.service.save(customMapper.toEditEntity(o.get(), entity)));
	}
	
	@PatchMapping(ID + AMODULES)
	public ResponseEntity<Authority> assignModules(@RequestBody List<Module> modules, @PathVariable Long id) 
			throws EntityIdNotFoundException, IllegalArgumentException {
		if (id <= 0) {
			throw new IllegalArgumentException("Id no puede ser cero o negativo");
		}
		Optional<Authority> o = this.service.findById(id);
		if (!o.isPresent()) {
			throw new EntityIdNotFoundException("Id rol " + id + " no encontrado");
		}
		
		return ResponseEntity.status(HttpStatus.CREATED).
				body(this.service.assignModules(o.get(), modules));
	}
	
	@PatchMapping(ID + RMODULE)
	public ResponseEntity<Authority> removeModule(@RequestBody Module module, @PathVariable Long id) 
			throws EntityIdNotFoundException, IllegalArgumentException {
		if (id <= 0) {
			throw new IllegalArgumentException("Id no puede ser cero o negativo");
		}
		Optional<Authority> o = this.service.findById(id);
		if (!o.isPresent()) {
			throw new EntityIdNotFoundException("Id rol " + id + " no encontrado");
		}
		
		return ResponseEntity.status(HttpStatus.CREATED).
				body(this.service.removeModule(o.get(), module));
	}
	
	@GetMapping(ERROR + ID)
	public AuthorityDTO error(@RequestHeader String Authorization, @PathVariable Long id) 
			throws EntityIdNotFoundException, MalFormedHeaderException, UnauthorizedException, IllegalArgumentException {
		if (id <= 0) {
			throw new IllegalArgumentException("Id no puede ser cero o negativo");
		}
		if(Authorization.equals("kk")) {
			throw new MalFormedHeaderException("token: " + Authorization);
		}
		
		Optional<Authority> entity = this.service.findById(id);
		if (!entity.isPresent()) {
			throw new EntityIdNotFoundException("Id rol " + id + " no encontrado");
		}
		
		return customMapper.toDto(entity.get());
	}

}
