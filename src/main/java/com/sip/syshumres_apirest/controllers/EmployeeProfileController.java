package com.sip.syshumres_apirest.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sip.syshumres_apirest.mappers.EmployeeProfileMapper;
import com.sip.syshumres_entities.EmployeeProfile;
import com.sip.syshumres_entities.User;
import com.sip.syshumres_entities.dtos.EmployeeProfileDTO;
import com.sip.syshumres_entities.dtos.EmployeeProfileViewDTO;
import com.sip.syshumres_entities.enums.EmployeeTypeEnum;
import com.sip.syshumres_exceptions.CreateRegisterException;
import com.sip.syshumres_exceptions.EntityIdNotFoundException;
import com.sip.syshumres_exceptions.IdsEntityNotEqualsException;
import com.sip.syshumres_exceptions.UnknownOptionException;
import com.sip.syshumres_exceptions.UploadFileException;
import com.sip.syshumres_exceptions.UploadFormatsAllowException;
import com.sip.syshumres_exceptions.UserSessionNotFoundException;
import com.sip.syshumres_exceptions.utils.ErrorsBindingFields;
import com.sip.syshumres_services.EmployeeProfileService;
import com.sip.syshumres_utils.StringTrim;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;


@RestController
@RequestMapping(EmployeeProfileController.URLENDPOINT)
public class EmployeeProfileController {
	
	public static final String URLENDPOINT = "employee-profiles";
	public static final String ACTIVE = "/active";
	public static final String PAGE = "/page";
	public static final String OPER = "/oper";
	public static final String ADM = "/adm";
	public static final String PAGEORDER = "/page-order";
	public static final String PAGEFILTER = "/page-filter";
	public static final String PAGEFILTERORDER = "/page-filter-order";
	public static final String ID = "/{id}";
	public static final String UPLOADFILE = "/upload-file";
	public static final String DOCUMENT = "/document";
	public static final String FILTER = "/filter";
	public static final String SEARCHNAMERELATIONSHIP = "/search-name-relationship";
	
    private EmployeeProfileService service;
    
    private EmployeeProfileMapper customMapper;
		
	private String filter;
	
	@Value("${SESSION.USER.NAME}")
	private String sessionUserName;
	
	@Autowired
	public EmployeeProfileController(EmployeeProfileService service,
			EmployeeProfileMapper customMapper) {
		this.service = service;
		this.customMapper = customMapper;
		this.filter = "";
	}
	
    /*
     * 2 Patrones de url para filtrar
     * 
     */
	@GetMapping(OPER + PAGE)
	public ResponseEntity<Page<EmployeeProfileViewDTO>> listEmployeeTypeOper(Pageable pageable, HttpSession session) 
			throws UserSessionNotFoundException {
		User userSession = (User) session.getAttribute(this.sessionUserName);
		if (userSession == null) {
			throw new UserSessionNotFoundException();
		}
		//System.out.println("branchOfficeSession: " + userSession.getBranchOffice().getDescription());
		Page<EmployeeProfile> entities = this.service.findByFilterSession(this.filter, 
			 userSession, EmployeeTypeEnum.OPER.getValue(), pageable);
		
		Page<EmployeeProfileViewDTO> entitiesPageDTO = entities.map(entity -> {
		    return customMapper.toViewDto(entity);
		});

		return ResponseEntity.ok().body(entitiesPageDTO);
	}
	
	/**
     * @param page          number of the page returned
     * @param size          number of entries in each page
     * @param sort  		Sort object
     * @return Page object with entitys after sorting
     */
	@GetMapping(OPER + PAGEORDER)
	public ResponseEntity<Page<EmployeeProfileViewDTO>> listOper(Pageable pageable, Sort sort, HttpSession session) throws UserSessionNotFoundException {
		this.filter = "";
		return this.listEmployeeTypeOper(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort), session);
	}
	
	/**
     * @param text			Filter for description, url, detail
     * @param page          number of the page returned
     * @param size          number of entries in each page
     * @return Page object with entitys after filtering
     */
	@GetMapping(OPER + PAGEFILTER)
	public ResponseEntity<Page<EmployeeProfileViewDTO>> listOper(String text, Pageable pageable, HttpSession session) throws UserSessionNotFoundException {
		this.filter = StringTrim.trimAndRemoveDiacriticalMarks(text);
		return this.listEmployeeTypeOper(pageable, session);
	}
	
	/**
     * @param text			Filter for description, url, detail
     * @param page          number of the page returned
     * @param size          number of entries in each page
     * @param sort  		Sort object
     * @return Page object with entitys after filtering and sorting
     */
	@GetMapping(OPER + PAGEFILTERORDER)
	public ResponseEntity<Page<EmployeeProfileViewDTO>> listOper(String text, Pageable pageable, Sort sort, HttpSession session) throws UserSessionNotFoundException {
		Pageable pageableOrder = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
		return this.listOper(text, pageableOrder, session);
	}
	
	@PostMapping
	public ResponseEntity<?> create(@Valid @RequestBody EmployeeProfileDTO entity, BindingResult result) {
		if (result.hasErrors()) {
			return ErrorsBindingFields.validate(result);
		}
		
		EmployeeProfile employeeProfile = customMapper.toSaveEntity(entity);
		Map<String, Object> errorsCustomFields = this.service.validEntity(employeeProfile, 0L);
		if (errorsCustomFields != null) {
			return ResponseEntity.badRequest().body(errorsCustomFields);
		}
		
		return ResponseEntity.status(HttpStatus.CREATED).
				body(service.save(employeeProfile));
	}
	
	@GetMapping(ID)
	public ResponseEntity<EmployeeProfileDTO> formEdit(@PathVariable Long id) 
			throws EntityIdNotFoundException, IllegalArgumentException {
		if (id <= 0) {
			throw new IllegalArgumentException("Id no puede ser cero o negativo");
		}
		Optional<EmployeeProfile> entity = this.service.findById(id);
		if(entity.isEmpty()) {
			throw new EntityIdNotFoundException("Id Empleado " + id + " no encontrado");
		}
		
		return ResponseEntity.ok(customMapper.toDto(entity.get()));
	}
	
	@PutMapping(ID)
	public ResponseEntity<?> edit(@Valid @RequestBody EmployeeProfileDTO entity, BindingResult result, @PathVariable Long id) 
			throws EntityIdNotFoundException, IdsEntityNotEqualsException, IllegalArgumentException {
		if (result.hasErrors()) {
			return ErrorsBindingFields.validate(result);
		}
		
		if (id <= 0) {
			throw new IllegalArgumentException("Id no puede ser cero o negativo");
		}
		if(!Objects.equals(id, entity.getId())){
			throw new IdsEntityNotEqualsException("Ids de Empleado no coinciden para actualización");
        }
		Optional<EmployeeProfile> o = this.service.findById(id);
		if (!o.isPresent()) {
			throw new EntityIdNotFoundException("Id Empleado " + id + " no encontrado");
		}
		
		EmployeeProfile entityDb = o.get();
		entityDb = customMapper.toEditEntity(entityDb, entity);
		
		Map<String, Object> errorsCustomFields = this.service.validEntity(entityDb, id);
		if (errorsCustomFields != null) {
			return ResponseEntity.badRequest().body(errorsCustomFields);
		}
		
		return ResponseEntity.status(HttpStatus.CREATED).
				body(this.service.save(entityDb));
	}
	
	@PutMapping(ID + UPLOADFILE)
	public ResponseEntity<Map<String, Object>> uploadFile(@PathVariable Long id, 
			@RequestParam String nameInput, 
			@RequestParam MultipartFile fileUpload) 
					throws IOException, EntityIdNotFoundException, UploadFormatsAllowException, 
					UploadFileException, UnknownOptionException, CreateRegisterException, IllegalArgumentException {
		Map<String, Object> response = new HashMap<>();
		
		if (id <= 0) {
			throw new IllegalArgumentException("Id no puede ser cero o negativo");
		}
		
		response = this.service.uploadFile(id, nameInput, fileUpload);
		
		return ResponseEntity.ok().body(response);
	}
	
	@GetMapping(ID + DOCUMENT)
	public ResponseEntity<Resource> getFileEmployee(@PathVariable Long id, 
			@RequestParam String nameInput) throws EntityIdNotFoundException, IllegalArgumentException {
		if (id <= 0) {
			throw new IllegalArgumentException("Id no puede ser cero o negativo");
		}
		Optional<EmployeeProfile> o = this.service.findById(id);
		if (!o.isPresent()) {	
			throw new EntityIdNotFoundException("Id Empleado " + id + " no encontrado");
		}
		if (o.get().getFileCurp() == null) {	
			throw new EntityIdNotFoundException("Archivo de la Curp no encontrado de empleado id " + id);
		}
		Resource resource = this.service.getFileEmployee(o.get(), nameInput);
		
		if (resource == null) {
			throw new EntityIdNotFoundException("Archivo " + nameInput + " no encontrado del empleado id " + id);
		}
		
		return ResponseEntity.ok()
				.contentType(MediaType.IMAGE_JPEG)
				.body(resource);
	}
	
	@GetMapping(ADM + PAGE)
	public ResponseEntity<Page<EmployeeProfileViewDTO>> listEmployeeTypeAdm(Pageable pageable, HttpSession session) 
			throws UserSessionNotFoundException {		
		User userSession = (User) session.getAttribute(this.sessionUserName);
		//System.out.println(branchOfficeSession.getDescription());
		if (userSession == null) {
			throw new UserSessionNotFoundException();
		}
		Page<EmployeeProfile> entities = this.service.findByFilterSession(this.filter, 
				 userSession, EmployeeTypeEnum.ADM.getValue(), pageable);
		
		Page<EmployeeProfileViewDTO> entitiesPageDTO = entities.map(entity -> {
		    return customMapper.toViewDto(entity);
		});

		return ResponseEntity.ok().body(entitiesPageDTO);
	}
	
	/**
     * @param page          number of the page returned
     * @param size          number of entries in each page
     * @param sort  		Sort object
     * @return Page object with entitys after sorting
     */
	@GetMapping(ADM + PAGEORDER)
	public ResponseEntity<Page<EmployeeProfileViewDTO>> listAdm(Pageable pageable, Sort sort, HttpSession session) throws UserSessionNotFoundException {
		this.filter = "";
		return this.listEmployeeTypeAdm(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort), session);
	}
	
	/**
     * @param text			Filter for description, url, detail
     * @param page          number of the page returned
     * @param size          number of entries in each page
     * @return Page object with entitys after filtering
     */
	@GetMapping(ADM + PAGEFILTER)
	public ResponseEntity<Page<EmployeeProfileViewDTO>> listAdm(String text, Pageable pageable, HttpSession session) throws UserSessionNotFoundException {
		this.filter = StringTrim.trimAndRemoveDiacriticalMarks(text);
		return this.listEmployeeTypeAdm(pageable, session);
	}
	
	/**
     * @param text			Filter for description, url, detail
     * @param page          number of the page returned
     * @param size          number of entries in each page
     * @param sort  		Sort object
     * @return Page object with entitys after filtering and sorting
     */
	@GetMapping(ADM + PAGEFILTERORDER)
	public ResponseEntity<Page<EmployeeProfileViewDTO>> listAdm(String text, Pageable pageable, Sort sort, HttpSession session) throws UserSessionNotFoundException {
		Pageable pageableOrder = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
		return this.listAdm(text, pageableOrder, session);
	}
	
	@GetMapping(FILTER)
	public ResponseEntity<Page<EmployeeProfile>> list(@RequestParam Long idEmployeeType, Pageable pageable, HttpSession session) 
			throws UserSessionNotFoundException {
		User userSession = (User) session.getAttribute(this.sessionUserName);
		//System.out.println(branchOfficeSession.getDescription());
		if (userSession == null) {
			throw new UserSessionNotFoundException();
		}
		if (userSession.isSeeAllBranchs()) {
			return ResponseEntity.ok(service.listEmployeeType(idEmployeeType, pageable));
		}
		return ResponseEntity.ok(service.listEmployeeType(userSession.getBranchOffice().getId(), idEmployeeType, pageable));
	}
	
	@GetMapping(SEARCHNAMERELATIONSHIP)
	public ResponseEntity<Map<String, Object>> searchNameRelationship(@RequestParam String lastName,
			@RequestParam String lastNameSecond) {
		Map<String, Object> response = new HashMap<>();
		response.put("name", "");
		if (this.service.countByNameAnotherEmployee(StringTrim.trimAndRemoveDiacriticalMarks(lastName), 
				StringTrim.trimAndRemoveDiacriticalMarks(lastNameSecond)) > 1) {
			response.replace("name", "Posible parentesco con otro empleado");
		}
		return ResponseEntity.ok().body(response);
	}

}
