package com.sip.syshumres_apirest.controllers;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sip.syshumres_apirest.controllers.common.CommonController;
import com.sip.syshumres_apirest.enums.StatusMessages;
import com.sip.syshumres_apirest.mappers.AuthorityMapper;
import com.sip.syshumres_apirest.mappers.ListMapper;
import com.sip.syshumres_apirest.mappers.ModuleCustomMapper;
import com.sip.syshumres_entities.Module;
import com.sip.syshumres_entities.Authority;
import com.sip.syshumres_entities.dtos.AuthorityDTO;
import com.sip.syshumres_entities.dtos.ModuleDTO;
import com.sip.syshumres_entities.dtos.ResponseDTO;
import com.sip.syshumres_entities.dtos.common.EntitySelectDTO;
import com.sip.syshumres_entities.utils.ErrorsBindingFields;
import com.sip.syshumres_exceptions.CreateRegisterException;
import com.sip.syshumres_exceptions.EntityIdNotFoundException;
import com.sip.syshumres_exceptions.IdsEntityNotEqualsException;
import com.sip.syshumres_exceptions.InvalidIdException;
import com.sip.syshumres_exceptions.MalFormedHeaderException;
import com.sip.syshumres_exceptions.UpdateRegisterException;
import com.sip.syshumres_services.AuthorityService;
import com.sip.syshumres_utils.StringTrim;

import org.springframework.web.bind.annotation.RequestHeader;


@RestController
@RequestMapping(AuthorityController.URLENDPOINT)
public class AuthorityController extends CommonController {
	
	public static final String URLENDPOINT = "authorities";
	public static final String MODULES = "/modules";
	public static final String AMODULES = "/assign-modules";
	public static final String RMODULE = "/remove-module";
	
	private static final String MSG_ID = "Id rol ";
	private static final String MSG_NOT_FOUND = " no encontrado";
	
    private final AuthorityService service;
    
    private final AuthorityMapper customMapper;
    
    private final ModuleCustomMapper customMapper2;
    
    private final ListMapper listMapper;
    
    @Autowired
	public AuthorityController(AuthorityService service, 
			AuthorityMapper customMapper,
			ModuleCustomMapper customMapper2,
			ListMapper listMapper) {
		this.service = service;
		this.customMapper = customMapper;
		this.customMapper2 = customMapper2;
		this.listMapper = listMapper;
		this.filter = "";
	}
	
	@GetMapping(ACTIVE)
	public ResponseEntity<List<EntitySelectDTO>> listActive() {
		return ResponseEntity.ok().body(service.findByEnabledTrueOrderByDescription().stream()
				.map(customMapper::toSelectDto)
				.toList());
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
	public ResponseEntity<Page<AuthorityDTO>> list(@RequestParam String q, Pageable pageable) {
		this.filter = StringTrim.urlDecodingAndTrim(q);
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
	public ResponseEntity<Page<AuthorityDTO>> list(@RequestParam String q, Pageable pageable, Sort sort) {
		Pageable pageableOrder = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
		return this.list(q, pageableOrder);
	}
	
	@GetMapping(ID + MODULES)
	public ResponseEntity<AuthorityDTO> getModules(@PathVariable Long id) throws EntityIdNotFoundException {
		Optional<Authority> entity = service.findById(id);
		if(entity.isEmpty()) {
			throw new EntityIdNotFoundException(MSG_ID + id + MSG_NOT_FOUND);
		}
	
		return ResponseEntity.ok(customMapper.toDto(entity.get()));
	}
	
	@PostMapping
	public ResponseEntity<ResponseDTO> create(@Valid @RequestBody AuthorityDTO entity, BindingResult result) 
			throws CreateRegisterException {
		if (result.hasErrors()) {
			return ResponseEntity.badRequest()
					.body(ErrorsBindingFields.getErrors(result));
		}

		Authority e = service.save(customMapper.toSaveEntity(entity));
		if (e == null) {
			throw new CreateRegisterException();
		}
		ResponseDTO response = new ResponseDTO();
		response.addEntry(StatusMessages.MESSAGE_KEY.getMessage(), 
				StatusMessages.SUCCESS_CREATE.getMessage());
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(response);
	}
	
	@GetMapping(ID)
	public ResponseEntity<AuthorityDTO> formEdit(@PathVariable Long id) 
			throws EntityIdNotFoundException, InvalidIdException {
		if (id <= 0) {
			throw new InvalidIdException();
		}
		Optional<Authority> entity = this.service.findById(id);
		if(entity.isEmpty()) {
			throw new EntityIdNotFoundException(MSG_ID + id + MSG_NOT_FOUND);
		}
		
		return ResponseEntity.ok(customMapper.toDto(entity.get()));
	}
	
	//@LogWeb
	@PutMapping(ID)
	public ResponseEntity<ResponseDTO> edit(@Valid @RequestBody AuthorityDTO entity, BindingResult result, @PathVariable Long id) 
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
			throw new IdsEntityNotEqualsException("Ids de rol no coinciden para actualización");
        }
		Optional<Authority> o = this.service.findById(id);
		if (!o.isPresent()) {
			throw new EntityIdNotFoundException(MSG_ID + id + MSG_NOT_FOUND);
		}
		
		Authority e = this.service.save(customMapper.toEditEntity(o.get(), entity));
		if (e == null) {
			throw new UpdateRegisterException();
		}
		ResponseDTO response = new ResponseDTO();
		response.addEntry(StatusMessages.MESSAGE_KEY.getMessage(), 
				StatusMessages.SUCCESS_UPDATE.getMessage());
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@PatchMapping(ID + AMODULES)
	public ResponseEntity<ResponseDTO> assignModules(@RequestBody List<ModuleDTO> modules, @PathVariable Long id) 
			throws EntityIdNotFoundException, InvalidIdException, UpdateRegisterException {
		if (id <= 0) {
			throw new InvalidIdException();
		}
		Optional<Authority> o = this.service.findById(id);
		if (!o.isPresent()) {
			throw new EntityIdNotFoundException(MSG_ID + id + MSG_NOT_FOUND);
		}
		
		Authority e = this.service.assignModules(o.get(), listMapper.mapList(modules, Module.class));
		if (e == null) {
			throw new UpdateRegisterException(StatusMessages.ERROR_ADD.getMessage());
		}
		ResponseDTO response = new ResponseDTO();
		response.addEntry(StatusMessages.MESSAGE_KEY.getMessage(), 
				StatusMessages.SUCCESS_ADD.getMessage());
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@PatchMapping(ID + RMODULE)
	public ResponseEntity<ResponseDTO> removeModule(@RequestBody ModuleDTO module, @PathVariable Long id) 
			throws EntityIdNotFoundException, InvalidIdException, UpdateRegisterException {
		if (id <= 0) {
			throw new InvalidIdException();
		}
		Optional<Authority> o = this.service.findById(id);
		if (!o.isPresent()) {
			throw new EntityIdNotFoundException(MSG_ID + id + MSG_NOT_FOUND);
		}
		
		Authority e = this.service.removeModule(o.get(), customMapper2.toEntity(module));
		if (e == null) {
			throw new UpdateRegisterException(StatusMessages.ERROR_REMOVE.getMessage());
		}
		ResponseDTO response = new ResponseDTO();
		response.addEntry(StatusMessages.MESSAGE_KEY.getMessage(), 
				StatusMessages.SUCCESS_REMOVE.getMessage());
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@GetMapping(ERROR + ID)
	public AuthorityDTO errorTest(@RequestHeader String authorization, @PathVariable Long id) 
			throws EntityIdNotFoundException, MalFormedHeaderException, InvalidIdException {
		if (id <= 0) {
			throw new InvalidIdException();
		}
		if(authorization.equals("kk")) {
			throw new MalFormedHeaderException("token: " + authorization);
		}
		
		Optional<Authority> entity = this.service.findById(id);
		if (!entity.isPresent()) {
			throw new EntityIdNotFoundException(MSG_ID + id + MSG_NOT_FOUND);
		}
		
		return customMapper.toDto(entity.get());
	}

}
