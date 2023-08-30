package com.sip.syshumres_apirest.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sip.syshumres_apirest.mappers.UserMapper;
import com.sip.syshumres_entities.Authority;
import com.sip.syshumres_entities.User;
import com.sip.syshumres_entities.dtos.UserDTO;
import com.sip.syshumres_exceptions.CreateRegisterException;
import com.sip.syshumres_exceptions.EntityIdNotFoundException;
import com.sip.syshumres_exceptions.IdsEntityNotEqualsException;
import com.sip.syshumres_exceptions.utils.ErrorsBindingFields;
import com.sip.syshumres_services.UserService;
import com.sip.syshumres_utils.StringTrim;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "UserController", description = "REST APIs related to User Entity")
@RestController
@RequestMapping(UserController.URLENDPOINT)
public class UserController {
	
	public static final String URLENDPOINT = "users";
	public static final String ACTIVE = "/active";
	public static final String PAGE = "/page";
	public static final String PAGEORDER = "/page-order";
	public static final String PAGEFILTER = "/page-filter";
	public static final String PAGEFILTERORDER = "/page-filter-order";
	public static final String ID = "/{id}";
	public static final String CHANGE = "/change-password";
	public static final String AUTHORITIES = "/authorities";
	public static final String AAUTHORITIES = "/assign-authorities";
	public static final String RAUTHORITY = "/remove-authority";
	
	private UserService service;
	
	private UserMapper customMapper;
		
	private String filter;
	
	@Value("${SESSION.USER.NAME}")
	private String sessionUserName;
	
	@Autowired
	public UserController(UserService service) {
		this.service = service;
		this.customMapper = new UserMapper();
		this.filter = "";
	}
	
	@GetMapping(PAGE)
	public ResponseEntity<Page<UserDTO>> list(Pageable pageable) {
		Page<User> entities = this.service.findByFilterSession(this.filter, pageable);
		
		Page<UserDTO> entitiesPageDTO = entities.map(entity -> {
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
	@GetMapping(PAGEORDER)
	public ResponseEntity<Page<UserDTO>> list(Pageable pageable, Sort sort) {
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
	public ResponseEntity<Page<UserDTO>> list(String text, Pageable pageable) {
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
	public ResponseEntity<Page<UserDTO>> list(String text, Pageable pageable, Sort sort) {
		Pageable pageableOrder = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
		return this.list(text, pageableOrder);
	}
	
	@PostMapping
	@ApiOperation(value = "Save new User", response = Iterable.class, tags = "save")
	@ApiResponses(value = { 
	      @ApiResponse(code = 201, message = "Success|Create"),
	      @ApiResponse(code = 400, message = "Bad request"), 
	      @ApiResponse(code = 403, message = "forbidden!!!"),
	      @ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<?> save(@Valid @RequestBody User entity, BindingResult result) {
		if (result.hasErrors()) {
			return ErrorsBindingFields.validate(result);
		}
		
		Map<String, Object> errorsCustomFields = this.service.validEntity(entity, 0L);
		if (errorsCustomFields != null) {
			return ResponseEntity.badRequest().body(errorsCustomFields);
		}
		
		//valid password and encode
		entity.setPassword(this.service.encodePassword(entity.getPassword()));
		
		return ResponseEntity.status(HttpStatus.CREATED).
				body(service.save(customMapper.toSaveEntity(entity)));
	}
	
	@ApiOperation(value = "Form to edit User", response = Iterable.class, tags = "formEdit")
	@ApiResponses(value = { 
	      @ApiResponse(code = 200, message = "Success|OK"),
	      @ApiResponse(code = 400, message = "Bad request"), 
	      @ApiResponse(code = 403, message = "forbidden!!!"),
	      @ApiResponse(code = 404, message = "not found!!!") })
	@GetMapping(ID)
	public ResponseEntity<UserDTO> formEdit(@PathVariable Long id) 
			throws EntityIdNotFoundException, IllegalArgumentException {
		if (id <= 0) {
			throw new IllegalArgumentException("Id no puede ser cero o negativo");
		}
		Optional<User> entity = this.service.findById(id);
		if(entity.isEmpty()) {
			throw new EntityIdNotFoundException("Id usuario " + id + " no encontrado");
		}
		
		return ResponseEntity.ok(customMapper.toDto(entity.get()));
	}
	
	@PutMapping(ID)
	public ResponseEntity<?> edit(@Valid @RequestBody User entity, BindingResult result, @PathVariable Long id) 
			throws EntityIdNotFoundException, IdsEntityNotEqualsException, IllegalArgumentException {
		//Se quito validacion ya que el password se actualiza de forma independiente.
		//if (result.hasErrors()) {
		//	return this.validate(result);
		//}
		
		if (id <= 0) {
			throw new IllegalArgumentException("Id no puede ser cero o negativo");
		}
		if(!Objects.equals(id, entity.getId())){
			throw new IdsEntityNotEqualsException("Ids de usuario no coinciden para actualización");
        }
		Optional<User> o = this.service.findById(id);
		if (!o.isPresent()) {
			throw new EntityIdNotFoundException("Id usuario " + id + " no encontrado");
		}
		
		Map<String, Object> errorsCustomFields = this.service.validEntity(entity, id);
		if (errorsCustomFields != null) {
			return ResponseEntity.badRequest().body(errorsCustomFields);
		}
		
		return ResponseEntity.status(HttpStatus.CREATED).
				body(this.service.save(customMapper.toEditEntity(o.get(), entity)));
	}
	
	@PatchMapping(ID + CHANGE)
	public ResponseEntity<Map<String, Object>> changePassword(@PathVariable Long id,
			@RequestParam String passwordNew, 
			@RequestParam String passwordNewConfirm) 
					throws EntityIdNotFoundException, CreateRegisterException, IllegalArgumentException {
		if (id <= 0) {
			throw new IllegalArgumentException("Id no puede ser cero o negativo");
		}
		Optional<User> o = this.service.findById(id);
		if (!o.isPresent()) {
			throw new EntityIdNotFoundException("Id usuario " + id + " no encontrado");
		}
		
		String passN = StringTrim.trimAndRemoveDiacriticalMarks(passwordNew);
		String passC = StringTrim.trimAndRemoveDiacriticalMarks(passwordNewConfirm);
		Map<String, Object> errorsCustomFields = this.service.validNewPassword(passN, passC);
		if (errorsCustomFields != null) {
			return ResponseEntity.badRequest().body(errorsCustomFields);
		}
		
		if (this.service.saveNewPassword(o.get(), passN) == null) {
			throw new CreateRegisterException("No se pudo guardar la nueva contraseña");
		}
		Map<String, Object> response = new HashMap<>();
		response.put("response", "La contraseña fue actualizada con éxito");
		
		return ResponseEntity.ok().body(response);
	}
	
	@GetMapping(ID + AUTHORITIES)
	public ResponseEntity<UserDTO> getAuthorities(@PathVariable Long id) 
			throws EntityIdNotFoundException, IllegalArgumentException {
		if (id <= 0) {
			throw new IllegalArgumentException("Id no puede ser cero o negativo");
		}
		Optional<User> entity = service.findById(id);
		if(entity.isEmpty()) {
			throw new EntityIdNotFoundException("Id usuario " + id + " no encontrado");
		}
				
		return ResponseEntity.ok(customMapper.toDto(entity.get()));
	}
	
	@PatchMapping(ID + AAUTHORITIES)
	public ResponseEntity<User> assignAuthorities(@RequestBody List<Authority> authorities, @PathVariable Long id) 
			throws EntityIdNotFoundException, IllegalArgumentException {
		if (id <= 0) {
			throw new IllegalArgumentException("Id no puede ser cero o negativo");
		}
		Optional<User> o = this.service.findById(id);
		if (!o.isPresent()) {
			throw new EntityIdNotFoundException("Id usuario " + id + " no encontrado");
		}
		
		return ResponseEntity.status(HttpStatus.CREATED).body(this.service.assignAuthorities(o.get(), authorities) );
	}
	
	@PatchMapping(ID + RAUTHORITY)
	public ResponseEntity<User> removeAuthority(@RequestBody Authority authority, @PathVariable Long id) 
			throws EntityIdNotFoundException, IllegalArgumentException {
		if (id <= 0) {
			throw new IllegalArgumentException("Id no puede ser cero o negativo");
		}
		Optional<User> o = this.service.findById(id);
		if (!o.isPresent()) {
			throw new EntityIdNotFoundException("Id usuario " + id + " no encontrado");
		}

		return ResponseEntity.status(HttpStatus.CREATED).body(this.service.removeAuthority(o.get(), authority));
	}

}
