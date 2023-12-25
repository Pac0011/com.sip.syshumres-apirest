package com.sip.syshumres_apirest.controllers;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sip.syshumres_apirest.config.AppProperties;
import com.sip.syshumres_apirest.config.UploadProperties;
import com.sip.syshumres_apirest.controllers.common.CommonController;
import com.sip.syshumres_apirest.enums.StatusMessages;
import com.sip.syshumres_apirest.mappers.EmployeeProfileMapper;
import com.sip.syshumres_apirest.security.JwtTokenDecoder;
import com.sip.syshumres_entities.EmployeeProfile;
import com.sip.syshumres_entities.dtos.EmployeeProfileDTO;
import com.sip.syshumres_entities.dtos.EmployeeProfileViewDTO;
import com.sip.syshumres_entities.dtos.ResponseDTO;
import com.sip.syshumres_entities.dtos.UserTokenExtraDTO;
import com.sip.syshumres_entities.enums.EmployeeTypeEnum;
import com.sip.syshumres_entities.utils.ErrorsBindingFields;
import com.sip.syshumres_exceptions.CreateRegisterException;
import com.sip.syshumres_exceptions.EntityIdNotFoundException;
import com.sip.syshumres_exceptions.IdsEntityNotEqualsException;
import com.sip.syshumres_exceptions.InvalidIdException;
import com.sip.syshumres_exceptions.JwtNotFoundException;
import com.sip.syshumres_exceptions.UnknownOptionException;
import com.sip.syshumres_exceptions.UpdateRegisterException;
import com.sip.syshumres_exceptions.UploadFileException;
import com.sip.syshumres_exceptions.UploadFormatsAllowException;
import com.sip.syshumres_exceptions.UserSessionNotFoundException;
import com.sip.syshumres_exceptions.UserTokenExtraNotFoundException;
import com.sip.syshumres_services.EmployeeProfileService;
import com.sip.syshumres_utils.StringTrim;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;


@RestController
@RequestMapping(EmployeeProfileController.URLENDPOINT)
public class EmployeeProfileController extends CommonController {
	
	public static final String URLENDPOINT = "employee-profiles";
	public static final String PAGE = "/page";
	public static final String OPER = "/oper";
	public static final String ADM = "/adm";
	public static final String UPLOADFILE = "/upload-file";
	public static final String DOCUMENT = "/document";
	public static final String SEARCHSTRING = "/filter";
	public static final String SEARCHNAMERELATIONSHIP = "/search-name-relationship";
	
	private static final String MSG_ID = "Id empleado ";
	private static final String MSG_NOT_FOUND = " no encontrado";
	
    private final EmployeeProfileService service;
    
    private final EmployeeProfileMapper customMapper;
    
    private final JwtTokenDecoder jwtTokenDecoder;
    	
	@Autowired
	public EmployeeProfileController(EmployeeProfileService service,
			EmployeeProfileMapper customMapper, AppProperties appProperties
			, UploadProperties uploadProperties, JwtTokenDecoder jwtTokenDecoder) {
		this.service = service;
		this.customMapper = customMapper;
		this.service.configBasePaths(uploadProperties.getBaseDocumentsEmployees()
				, uploadProperties.getPathDocumentsEmployees() 
				, uploadProperties.getUrlDocumentsEmployees()
				, uploadProperties.getListFormatsAllow()
				, appProperties.getSizeEmployeeNumber());
		this.filter = "";
		this.jwtTokenDecoder = jwtTokenDecoder;
	}
	
    /*
     * 2 Patrones de url para filtrar
     * 
     */
	@GetMapping(OPER + PAGE)
	public ResponseEntity<Page<EmployeeProfileViewDTO>> listEmployeeTypeOper(Pageable pageable, 
			@RequestHeader("Authorization") String jwtToken) 
			throws UserTokenExtraNotFoundException, ParseException, JwtNotFoundException {
		UserTokenExtraDTO userToken = jwtTokenDecoder.getExtraFromToken(jwtToken);
		
		Page<EmployeeProfile> entities = this.service.findByFilterSession(this.filter, 
				userToken, EmployeeTypeEnum.OPER.getValue(), pageable);
		
		Page<EmployeeProfileViewDTO> entitiesPageDTO = entities.map(customMapper::toViewDto);

		return ResponseEntity.ok().body(entitiesPageDTO);
	}
	
	/**
     * @param page          number of the page returned
     * @param size          number of entries in each page
     * @param sort  		Sort object
     * @return Page object with entitys after sorting
	 * @throws ParseException 
	 * @throws JwtNotFoundException 
     */
	@GetMapping(OPER + PAGEORDER)
	public ResponseEntity<Page<EmployeeProfileViewDTO>> listOper(Pageable pageable, Sort sort, 
			@RequestHeader("Authorization") String jwtToken) 
					throws UserTokenExtraNotFoundException, ParseException, JwtNotFoundException {
		this.filter = "";
		return this.listEmployeeTypeOper(PageRequest.of(pageable.getPageNumber(), 
				pageable.getPageSize(), sort), jwtToken);
	}
	
	/**
     * @param text			Filter for description, url, detail
     * @param page          number of the page returned
     * @param size          number of entries in each page
     * @return Page object with entitys after filtering
	 * @throws ParseException 
	 * @throws JwtNotFoundException 
     */
	@GetMapping(OPER + PAGEFILTER)
	public ResponseEntity<Page<EmployeeProfileViewDTO>> listOper(@RequestParam String q, 
			Pageable pageable, @RequestHeader("Authorization") String jwtToken) 
					throws UserTokenExtraNotFoundException, ParseException, JwtNotFoundException {
		this.filter = StringTrim.urlDecodingAndTrim(q);
		return this.listEmployeeTypeOper(pageable, jwtToken);
	}
	
	/**
     * @param text			Filter for description, url, detail
     * @param page          number of the page returned
     * @param size          number of entries in each page
     * @param sort  		Sort object
     * @return Page object with entitys after filtering and sorting
	 * @throws ParseException 
	 * @throws JwtNotFoundException 
     */
	@GetMapping(OPER + PAGEFILTERORDER)
	public ResponseEntity<Page<EmployeeProfileViewDTO>> listOper(@RequestParam String q, Pageable pageable, 
			Sort sort, @RequestHeader("Authorization") String jwtToken) 
					throws UserTokenExtraNotFoundException, ParseException, JwtNotFoundException {
		Pageable pageableOrder = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
		return this.listOper(q, pageableOrder, jwtToken);
	}
	
	@PostMapping
	public ResponseEntity<ResponseDTO> create(@Valid @RequestBody EmployeeProfileDTO entity, BindingResult result) 
	throws CreateRegisterException {
		if (result.hasErrors()) {
			return ResponseEntity.badRequest()
					.body(ErrorsBindingFields.getErrors(result));
		}
		
		EmployeeProfile employeeProfile = customMapper.toSaveEntity(entity);
		Map<String, String> errorsCustomFields = this.service.validEntity(employeeProfile, 0L);
		if (!errorsCustomFields.isEmpty()) {
			return ResponseEntity.badRequest()
					.body(ErrorsBindingFields.getErrors(errorsCustomFields));
		}
		
		EmployeeProfile e = service.save(employeeProfile);
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
	public ResponseEntity<EmployeeProfileDTO> formEdit(@PathVariable Long id) 
			throws EntityIdNotFoundException, InvalidIdException {
		if (id <= 0) {
			throw new InvalidIdException();
		}
		Optional<EmployeeProfile> entity = this.service.findById(id);
		if(entity.isEmpty()) {
			throw new EntityIdNotFoundException(MSG_ID + id + MSG_NOT_FOUND);
		}
		
		return ResponseEntity.ok(customMapper.toDto(entity.get()));
	}
	
	@PutMapping(ID)
	public ResponseEntity<ResponseDTO> edit(@Valid @RequestBody EmployeeProfileDTO entity, BindingResult result, @PathVariable Long id) 
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
			throw new IdsEntityNotEqualsException("Ids de Empleado no coinciden para actualización");
        }
		Optional<EmployeeProfile> o = this.service.findById(id);
		if (!o.isPresent()) {
			throw new EntityIdNotFoundException(MSG_ID + id + MSG_NOT_FOUND);
		}
		
		EmployeeProfile entityDb = o.get();
		entityDb = customMapper.toEditEntity(entityDb, entity);
		
		Map<String, String> errorsCustomFields = this.service.validEntity(entityDb, id);
		if (!errorsCustomFields.isEmpty()) {
			return ResponseEntity.badRequest()
					.body(ErrorsBindingFields.getErrors(errorsCustomFields));
		}
		if (service.save(entityDb) == null) {
			throw new UpdateRegisterException();
		}
		
		ResponseDTO response = new ResponseDTO();
		response.addEntry(StatusMessages.MESSAGE_KEY.getMessage(), 
				StatusMessages.SUCCESS_UPDATE.getMessage());
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(response);
	}
	
	@PutMapping(ID + UPLOADFILE)
	public ResponseEntity<ResponseDTO> uploadFileProfile(@PathVariable Long id, 
			@RequestParam String nameInput, 
			@RequestParam MultipartFile fileUpload) 
					throws IOException, EntityIdNotFoundException, UploadFormatsAllowException, 
					UploadFileException, UnknownOptionException, CreateRegisterException, InvalidIdException {		
		if (id <= 0) {
			throw new InvalidIdException();
		}
		
		String urlFile = this.service.uploadFile(id, nameInput, fileUpload);
		ResponseDTO response = new ResponseDTO();
		response.addEntry(StatusMessages.MESSAGE_KEY.getMessage(), 
				StatusMessages.SUCCESS_UPLOAD.getMessage());
		response.addEntry(StatusMessages.URLFILE_KEY.getMessage(), urlFile);
		return ResponseEntity.ok().body(response);
	}
	
	@GetMapping(ID + DOCUMENT)
	public ResponseEntity<Resource> getFileEmployee(@PathVariable Long id, 
			@RequestParam String nameInput) throws EntityIdNotFoundException, InvalidIdException
	, IOException {
		if (id <= 0) {
			throw new InvalidIdException();
		}
		Optional<EmployeeProfile> o = this.service.findById(id);
		if (!o.isPresent()) {	
			throw new EntityIdNotFoundException(MSG_ID + id + MSG_NOT_FOUND);
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
	public ResponseEntity<Page<EmployeeProfileViewDTO>> listEmployeeTypeAdm(Pageable pageable, 
			@RequestHeader("Authorization") String jwtToken) 
			throws UserSessionNotFoundException, ParseException, UserTokenExtraNotFoundException, JwtNotFoundException {		
		UserTokenExtraDTO userToken = jwtTokenDecoder.getExtraFromToken(jwtToken);
		
		Page<EmployeeProfile> entities = this.service.findByFilterSession(this.filter, 
				userToken, EmployeeTypeEnum.ADM.getValue(), pageable);
		
		Page<EmployeeProfileViewDTO> entitiesPageDTO = entities.map(customMapper::toViewDto);

		return ResponseEntity.ok().body(entitiesPageDTO);
	}
	
	/**
     * @param page          number of the page returned
     * @param size          number of entries in each page
     * @param sort  		Sort object
     * @return Page object with entitys after sorting
	 * @throws UserTokenExtraNotFoundException 
	 * @throws ParseException 
	 * @throws JwtNotFoundException 
     */
	@GetMapping(ADM + PAGEORDER)
	public ResponseEntity<Page<EmployeeProfileViewDTO>> listAdm(Pageable pageable, Sort sort, 
			@RequestHeader("Authorization") String jwtToken) throws UserSessionNotFoundException, ParseException, UserTokenExtraNotFoundException, JwtNotFoundException {
		this.filter = "";
		return this.listEmployeeTypeAdm(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort), jwtToken);
	}
	
	/**
     * @param text			Filter for description, url, detail
     * @param page          number of the page returned
     * @param size          number of entries in each page
     * @return Page object with entitys after filtering
	 * @throws UserTokenExtraNotFoundException 
	 * @throws ParseException 
	 * @throws JwtNotFoundException 
     */
	@GetMapping(ADM + PAGEFILTER)
	public ResponseEntity<Page<EmployeeProfileViewDTO>> listAdm(@RequestParam String q, Pageable pageable, 
			@RequestHeader("Authorization") String jwtToken) throws UserSessionNotFoundException, ParseException, UserTokenExtraNotFoundException, JwtNotFoundException {
		this.filter = StringTrim.urlDecodingAndTrim(q);
		return this.listEmployeeTypeAdm(pageable, jwtToken);
	}
	
	/**
     * @param text			Filter for description, url, detail
     * @param page          number of the page returned
     * @param size          number of entries in each page
     * @param sort  		Sort object
     * @return Page object with entitys after filtering and sorting
	 * @throws UserTokenExtraNotFoundException 
	 * @throws ParseException 
	 * @throws JwtNotFoundException 
     */
	@GetMapping(ADM + PAGEFILTERORDER)
	public ResponseEntity<Page<EmployeeProfileViewDTO>> listAdm(@RequestParam String q, Pageable pageable, Sort sort, 
			@RequestHeader("Authorization") String jwtToken) throws UserSessionNotFoundException, ParseException, UserTokenExtraNotFoundException, JwtNotFoundException {
		Pageable pageableOrder = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
		return this.listAdm(q, pageableOrder, jwtToken);
	}
	
	@GetMapping(SEARCHSTRING)
	public ResponseEntity<Page<EmployeeProfileViewDTO>> list(@RequestParam Long idEmployeeType
			, Pageable pageable
			, @RequestHeader("Authorization") String jwtToken) 
			throws UserTokenExtraNotFoundException, ParseException, JwtNotFoundException {
		UserTokenExtraDTO userToken = jwtTokenDecoder.getExtraFromToken(jwtToken);
		
		Page<EmployeeProfileViewDTO> entitiesPageDTO =  service.listEmployeeType(idEmployeeType, pageable, userToken)
			.map(customMapper::toViewDto);
		return ResponseEntity.ok(entitiesPageDTO);
	}
	
	@GetMapping(SEARCHNAMERELATIONSHIP)
	public ResponseEntity<ResponseDTO> searchNameRelationshipEmployee(@RequestParam String lastName,
			@RequestParam String lastNameSecond) {
		String res = "";
		if (this.service.countByNameAnotherEmployee(StringTrim.urlDecodingAndTrim(lastName), 
				StringTrim.urlDecodingAndTrim(lastNameSecond)) > 1) {
			res = "Posible parentesco con otro empleado";
		}
		ResponseDTO response = new ResponseDTO();
		response.addEntry("name", res);
		return ResponseEntity.ok().body(response);
	}

}
