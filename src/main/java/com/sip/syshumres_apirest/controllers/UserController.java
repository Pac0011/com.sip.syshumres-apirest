package com.sip.syshumres_apirest.controllers;

import java.util.List;
import java.util.Map;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sip.syshumres_apirest.config.SwaggerConfig;
import com.sip.syshumres_apirest.controllers.common.CommonController;
import com.sip.syshumres_apirest.enums.StatusMessages;
import com.sip.syshumres_apirest.mappers.AuthorityMapper;
import com.sip.syshumres_apirest.mappers.ListMapper;
import com.sip.syshumres_apirest.mappers.UserMapper;
import com.sip.syshumres_entities.Authority;
import com.sip.syshumres_entities.User;
import com.sip.syshumres_entities.dtos.AuthorityDTO;
import com.sip.syshumres_entities.dtos.ResponseDTO;
import com.sip.syshumres_entities.dtos.UserDTO;
import com.sip.syshumres_entities.utils.ErrorsBindingFields;
import com.sip.syshumres_exceptions.ChangePasswordException;
import com.sip.syshumres_exceptions.CreateRegisterException;
import com.sip.syshumres_exceptions.EntityIdNotFoundException;
import com.sip.syshumres_exceptions.IdsEntityNotEqualsException;
import com.sip.syshumres_exceptions.InvalidIdException;
import com.sip.syshumres_exceptions.UpdateRegisterException;
import com.sip.syshumres_services.UserService;
import com.sip.syshumres_utils.StringTrim;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags = {SwaggerConfig.USER_TAG})
@RestController
@RequestMapping(UserController.URLENDPOINT)
public class UserController extends CommonController {
	
	public static final String URLENDPOINT = "users";
	public static final String CHANGE = "/change-password";
	public static final String AUTHORITIES = "/authorities";
	public static final String AAUTHORITIES = "/assign-authorities";
	public static final String RAUTHORITY = "/remove-authority";
	
	private static final String MSG_ID = "Id usuario ";
	private static final String MSG_NOT_FOUND = " no encontrado";
	
	private final UserService service;
	
	private final UserMapper customMapper;
	
	private final AuthorityMapper customMapper2;
	
	private final ListMapper listMapper;
			
	@Autowired
	public UserController(UserService service, 
			UserMapper customMapper,
			AuthorityMapper customMapper2,
			ListMapper listMapper) {
		this.service = service;
		this.customMapper = customMapper;
		this.customMapper2 = customMapper2;
		this.listMapper = listMapper;
		this.filter = "";
	}
	
	@GetMapping(PAGE)
	public ResponseEntity<Page<UserDTO>> list(Pageable pageable) {
		Page<User> entities = this.service.findByFilterSession(this.filter, pageable);
		
		Page<UserDTO> entitiesPageDTO = entities.map(customMapper::toDto);

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
	public ResponseEntity<Page<UserDTO>> list(@RequestParam String q, Pageable pageable) {
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
	public ResponseEntity<Page<UserDTO>> list(@RequestParam String q, Pageable pageable, Sort sort) {
		Pageable pageableOrder = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
		return this.list(q, pageableOrder);
	}
	
	@PostMapping
	@ApiOperation(value = "Save new User", response = Iterable.class, tags = "save")
	@ApiResponses(value = { 
	      @ApiResponse(code = 201, message = "Success|Create"),
	      @ApiResponse(code = 400, message = "Bad request"), 
	      @ApiResponse(code = 403, message = "forbidden!!!"),
	      @ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<ResponseDTO> create(@Valid @RequestBody UserDTO entity, BindingResult result) 
	throws CreateRegisterException {
		if (result.hasErrors()) {
			return ResponseEntity.badRequest()
					.body(ErrorsBindingFields.getErrors(result));
		}
		User user = customMapper.toSaveEntity(entity);
		
		Map<String, String> errorsCustomFields = this.service.validEntity(user, 0L);
		if (!errorsCustomFields.isEmpty()) {
			return ResponseEntity.badRequest()
					.body(ErrorsBindingFields.getErrors(errorsCustomFields));
		}
		
		//valid password and encode
		entity.setPassword(this.service.encodePassword(entity.getPassword()));
		
		User e = service.save(user);
		if (e == null) {
			throw new CreateRegisterException();
		}
		ResponseDTO response = new ResponseDTO();
		response.addEntry(StatusMessages.MESSAGE_KEY.getMessage(), 
				StatusMessages.SUCCESS_CREATE.getMessage());
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(response);
	}
	
	@ApiOperation(value = "Form to edit User", response = Iterable.class, tags = "formEdit")
	@ApiResponses(value = { 
	      @ApiResponse(code = 200, message = "Success|OK"),
	      @ApiResponse(code = 400, message = "Bad request"), 
	      @ApiResponse(code = 403, message = "forbidden!!!"),
	      @ApiResponse(code = 404, message = "not found!!!") })
	@GetMapping(ID)
	public ResponseEntity<UserDTO> formEdit(@PathVariable Long id) 
			throws EntityIdNotFoundException, InvalidIdException {
		if (id <= 0) {
			throw new InvalidIdException();
		}
		Optional<User> entity = this.service.findById(id);
		if(entity.isEmpty()) {
			throw new EntityIdNotFoundException(MSG_ID + id + MSG_NOT_FOUND);
		}
		
		return ResponseEntity.ok(customMapper.toDto(entity.get()));
	}
	
	@PutMapping(ID)
	public ResponseEntity<ResponseDTO> edit(@Valid @RequestBody UserDTO entity, BindingResult result, @PathVariable Long id) 
			throws EntityIdNotFoundException, IdsEntityNotEqualsException, InvalidIdException
	, UpdateRegisterException {
		//Se quito validacion result.hasErrors pq el password se actualiza independiente
		if (id <= 0) {
			throw new InvalidIdException();
		}
		if(!Objects.equals(id, entity.getId())){
			throw new IdsEntityNotEqualsException("Ids de usuario no coinciden para actualización");
        }
		Optional<User> o = this.service.findById(id);
		if (!o.isPresent()) {
			throw new EntityIdNotFoundException(MSG_ID + id + MSG_NOT_FOUND);
		}
		User entityDb = o.get(); 
		entityDb = customMapper.toEditEntity(entityDb, entity);
		
		Map<String, String> errorsCustomFields = this.service.validEntity(entityDb, id);
		if (!errorsCustomFields.isEmpty()) {
			return ResponseEntity.badRequest()
					.body(ErrorsBindingFields.getErrors(errorsCustomFields));
		}
		
		User e = this.service.save(entityDb);
		if (service.save(e) == null) {
			throw new UpdateRegisterException();
		}
		ResponseDTO response = new ResponseDTO();
		response.addEntry(StatusMessages.MESSAGE_KEY.getMessage(), 
				StatusMessages.SUCCESS_UPDATE.getMessage());
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(response);
	}
	
	@PatchMapping(ID + CHANGE)
	public ResponseEntity<ResponseDTO> changePassword(@PathVariable Long id,
			@RequestParam String passwordNew, 
			@RequestParam String passwordNewConfirm) 
					throws EntityIdNotFoundException, ChangePasswordException, InvalidIdException {
		if (id <= 0) {
			throw new InvalidIdException();
		}
		Optional<User> o = this.service.findById(id);
		if (!o.isPresent()) {
			throw new EntityIdNotFoundException(MSG_ID + id + MSG_NOT_FOUND);
		}
		
		String passN = StringTrim.trimAndRemoveDiacriticalMarksPassword(passwordNew);
		String passC = StringTrim.trimAndRemoveDiacriticalMarksPassword(passwordNewConfirm);
		Map<String, String> errorsCustomFields = this.service.validNewPassword(passN, passC);
		if (!errorsCustomFields.isEmpty()) {
			return ResponseEntity.badRequest()
					.body(ErrorsBindingFields.getErrors(errorsCustomFields));
		}
		
		if (this.service.saveNewPassword(o.get(), passN) == null) {
			throw new ChangePasswordException();
		}
		
		ResponseDTO response = new ResponseDTO();
		response.addEntry(StatusMessages.MESSAGE_KEY.getMessage(), 
				StatusMessages.SUCCESS_CHANGE_PASSWORD.getMessage());
		return ResponseEntity.ok().body(response);
	}
	
	@GetMapping(ID + AUTHORITIES)
	public ResponseEntity<UserDTO> getAuthorities(@PathVariable Long id) 
			throws EntityIdNotFoundException, InvalidIdException {
		if (id <= 0) {
			throw new InvalidIdException();
		}
		Optional<User> entity = service.findById(id);
		if(entity.isEmpty()) {
			throw new EntityIdNotFoundException(MSG_ID + id + MSG_NOT_FOUND);
		}
				
		return ResponseEntity.ok(customMapper.toDto(entity.get()));
	}
	
	@PatchMapping(ID + AAUTHORITIES)
	public ResponseEntity<ResponseDTO> assignAuthorities(@RequestBody List<AuthorityDTO> authorities, @PathVariable Long id) 
			throws EntityIdNotFoundException, InvalidIdException, UpdateRegisterException {
		if (id <= 0) {
			throw new InvalidIdException();
		}
		Optional<User> o = this.service.findById(id);
		if (!o.isPresent()) {
			throw new EntityIdNotFoundException(MSG_ID + id + MSG_NOT_FOUND);
		}
		
		User e = this.service.assignAuthorities(o.get(), listMapper.mapList(authorities, Authority.class));
		if (e == null) {
			throw new UpdateRegisterException(StatusMessages.ERROR_ADD.getMessage());
		}
		ResponseDTO response = new ResponseDTO();
		response.addEntry(StatusMessages.MESSAGE_KEY.getMessage(), 
				StatusMessages.SUCCESS_ADD.getMessage());
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@PatchMapping(ID + RAUTHORITY)
	public ResponseEntity<ResponseDTO> removeAuthority(@RequestBody AuthorityDTO authority, @PathVariable Long id) 
			throws EntityIdNotFoundException, InvalidIdException, UpdateRegisterException {
		if (id <= 0) {
			throw new InvalidIdException();
		}
		Optional<User> o = this.service.findById(id);
		if (!o.isPresent()) {
			throw new EntityIdNotFoundException(MSG_ID + id + MSG_NOT_FOUND);
		}

		User e = this.service.removeAuthority(o.get(), customMapper2.toEntity(authority));	
		if (e == null) {
			throw new UpdateRegisterException(StatusMessages.ERROR_REMOVE.getMessage());
		}
		ResponseDTO response = new ResponseDTO();
		response.addEntry(StatusMessages.MESSAGE_KEY.getMessage(), 
				StatusMessages.SUCCESS_REMOVE.getMessage());
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

}
