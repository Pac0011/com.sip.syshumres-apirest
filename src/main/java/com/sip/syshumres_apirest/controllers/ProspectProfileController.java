package com.sip.syshumres_apirest.controllers;

import java.util.Objects;
import java.util.Optional;

import javax.servlet.http.HttpSession;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sip.syshumres_apirest.controllers.common.CommonController;
import com.sip.syshumres_apirest.mappers.EmployeeProfileMapper;
import com.sip.syshumres_apirest.mappers.ProspectProfileMapper;
import com.sip.syshumres_apirest.producer.JmsProducer;
import com.sip.syshumres_entities.BranchOffice;
import com.sip.syshumres_entities.EmployeeProfile;
import com.sip.syshumres_entities.EmployeeStatus;
import com.sip.syshumres_entities.ProspectProfile;
import com.sip.syshumres_entities.ProspectStatus;
import com.sip.syshumres_entities.User;
import com.sip.syshumres_entities.dtos.EmployeeProfileDTO;
import com.sip.syshumres_entities.dtos.ProspectProfileDTO;
import com.sip.syshumres_entities.enums.EmployeeStatusEnum;
import com.sip.syshumres_entities.enums.ProspectStatusEnum;
import com.sip.syshumres_exceptions.BranchOfficeUserNotFoundException;
import com.sip.syshumres_exceptions.CreateEmployeeProfileException;
import com.sip.syshumres_exceptions.EmployeeFieldsAlreadyExistException;
import com.sip.syshumres_exceptions.EntityIdNotFoundException;
import com.sip.syshumres_exceptions.IdsEntityNotEqualsException;
import com.sip.syshumres_exceptions.ProspectFieldsAlreadyExistException;
import com.sip.syshumres_exceptions.StatusCatalogNotFoundException;
import com.sip.syshumres_exceptions.UserSessionNotFoundException;
import com.sip.syshumres_exceptions.utils.ErrorsBindingFields;
import com.sip.syshumres_services.BranchOfficeService;
import com.sip.syshumres_services.EmployeeProfileService;
import com.sip.syshumres_services.EmployeeStatusService;
import com.sip.syshumres_services.ProspectProfileService;
import com.sip.syshumres_services.ProspectStatusService;
import com.sip.syshumres_utils.StringTrim;


@RestController
@RequestMapping(ProspectProfileController.URLENDPOINT)
public class ProspectProfileController extends CommonController {
	
	public static final String URLENDPOINT = "prospect-profiles";
	public static final String NEWHIRE = "/new-hire";
	
	private ProspectProfileService service;
	
	private ProspectProfileMapper customMapper;
		
	private EmployeeProfileMapper customMapper2;
		
	@Value("${SESSION.USER.NAME}")
	private String sessionUserName;
	
	private ProspectStatusService serviceS;
	
	private EmployeeStatusService serviceES;
	
	private BranchOfficeService serviceB;
	
	private EmployeeProfileService serviceP;
	
    private JmsProducer jmsProducer;
	
	@Autowired
	public ProspectProfileController(ProspectProfileService service, ProspectStatusService serviceS, 
			BranchOfficeService serviceB, EmployeeProfileService serviceP, EmployeeStatusService serviceES, 
			ProspectProfileMapper customMapper, 
			EmployeeProfileMapper customMapper2, 
			JmsProducer jmsProducer) {
		this.service = service;
		this.serviceS = serviceS;
		this.serviceB = serviceB;
		this.serviceP = serviceP;
		this.serviceES = serviceES;
		this.jmsProducer = jmsProducer;
		this.filter = "";
		this.customMapper = customMapper;
		this.customMapper2 = customMapper2;
	}
	
	/*
     * 2 Patrones de url para filtrar
     * 
     */
	@GetMapping(PAGE)
	public ResponseEntity<Page<ProspectProfileDTO>> list(Pageable pageable, HttpSession session) throws UserSessionNotFoundException {
		User userSession = (User) session.getAttribute(this.sessionUserName);
		if (userSession == null) {
			throw new UserSessionNotFoundException();
		}
		
		Page<ProspectProfile> entities = this.service.findByFilterSession(this.filter, userSession, pageable);
		
		Page<ProspectProfileDTO> entitiesPageDTO = entities.map(entity -> {
		    return customMapper.toDto(entity);
		});

		return ResponseEntity.ok().body(entitiesPageDTO);
	}
	
	/**
     * @param page          number of the page returned
     * @param size          number of entries in each page
     * @param sort  		Sort object
     * @return Page object with entitys after sorting
	 * @throws UserSessionNotFoundException 
     */
	@GetMapping(PAGEORDER)
	public ResponseEntity<Page<ProspectProfileDTO>> list(Pageable pageable, Sort sort, HttpSession session) throws UserSessionNotFoundException {
		this.filter = "";
		return this.list(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort), session);
	}
	
	/**
     * @param text			Filter for description, url, detail
     * @param page          number of the page returned
     * @param size          number of entries in each page
     * @return Page object with entitys after filtering
	 * @throws UserSessionNotFoundException 
     */
	@GetMapping(PAGEFILTER)
	public ResponseEntity<Page<ProspectProfileDTO>> list(String text, Pageable pageable, HttpSession session) throws UserSessionNotFoundException {
		this.filter = StringTrim.trimAndRemoveDiacriticalMarks(text);
		return this.list(pageable, session);
	}
	
	/**
     * @param text			Filter for description, url, detail
     * @param page          number of the page returned
     * @param size          number of entries in each page
     * @param sort  		Sort object
     * @return Page object with entitys after filtering and sorting
	 * @throws UserSessionNotFoundException 
     */
	@GetMapping(PAGEFILTERORDER)
	public ResponseEntity<Page<ProspectProfileDTO>> list(String text, Pageable pageable, Sort sort, HttpSession session) throws UserSessionNotFoundException {
		Pageable pageableOrder = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
		return this.list(text, pageableOrder, session);
	}
	
	@PostMapping
	public ResponseEntity<?> create(@Valid @RequestBody ProspectProfileDTO entity, BindingResult result, HttpSession session) 
		throws UserSessionNotFoundException, BranchOfficeUserNotFoundException, StatusCatalogNotFoundException, 
		ProspectFieldsAlreadyExistException, EmployeeFieldsAlreadyExistException {
		if (result.hasErrors()) {
			return ErrorsBindingFields.validate(result);
		}
		
		User userSession = (User) session.getAttribute(this.sessionUserName);
		if (userSession == null) {
			throw new UserSessionNotFoundException();
		}
		Optional<BranchOffice> optionalB = this.serviceB.findById(userSession.getBranchOffice().getId());
		if(optionalB.isEmpty()) {
			throw new BranchOfficeUserNotFoundException();
		}
		Optional<ProspectStatus> optional = this.serviceS.findById(ProspectStatusEnum.INICIO_PROCESO.getValue());
		if(optional.isEmpty()) {
			throw new StatusCatalogNotFoundException("Estatus de Prospecto INICIO_PROCESO no encontrado");
		}
		
		ProspectProfile prospectProfile = customMapper.toSaveEntity(entity);
		this.service.validEntity(prospectProfile, 0L);

		ProspectProfile e = this.service.save(prospectProfile, optionalB.get(), optional.get());
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(customMapper.toDto(e));
	}
	
	@GetMapping(ID)
	public ResponseEntity<ProspectProfileDTO> formEdit(@PathVariable Long id) 
			throws EntityIdNotFoundException, IllegalArgumentException {
		if (id <= 0) {
			throw new IllegalArgumentException("Id no puede ser cero o negativo");
		}
		Optional<ProspectProfile> entity = this.service.findById(id);
		if(entity.isEmpty()) {
			throw new EntityIdNotFoundException("Id Prospecto no encontrado");
		}
		
		return ResponseEntity.ok(customMapper.toDto(entity.get()));
	}
	
	@PutMapping(ID)
	public ResponseEntity<?> edit(@Valid @RequestBody ProspectProfileDTO entity, BindingResult result, @PathVariable Long id) 
			throws IdsEntityNotEqualsException, EntityIdNotFoundException, ProspectFieldsAlreadyExistException, 
			EmployeeFieldsAlreadyExistException, IllegalArgumentException {
		if (result.hasErrors()) {
			return ErrorsBindingFields.validate(result);
		}
		
		if (id <= 0) {
			throw new IllegalArgumentException("Id no puede ser cero o negativo");
		}
		if (!Objects.equals(id, entity.getId())) {
			throw new IdsEntityNotEqualsException("Ids de Prospecto no coinciden para actualización");
        }
		Optional<ProspectProfile> o = this.service.findById(id);
		if (!o.isPresent()) {
			throw new EntityIdNotFoundException("Id Prospecto no encontrado");
		}
		ProspectProfile entityDb = o.get();
		entityDb = customMapper.toEditEntity(entityDb, entity);
		
		this.service.validEntity(entityDb, id);
		
		ProspectProfile e = this.service.save(entityDb);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(customMapper.toDto(e));
	}
	
	@PutMapping(ID + NEWHIRE)
	public ResponseEntity<EmployeeProfileDTO> hire(@RequestBody ProspectProfileDTO entity, @PathVariable Long id) 
		throws IdsEntityNotEqualsException, EntityIdNotFoundException, StatusCatalogNotFoundException, 
		CreateEmployeeProfileException, IllegalArgumentException {
		if (id <= 0) {
			throw new IllegalArgumentException("Id no puede ser cero o negativo");
		}
		if (!Objects.equals(id, entity.getId())) {
			throw new IdsEntityNotEqualsException("Ids de Prospecto no coinciden para actualización");
        }
		Optional<ProspectProfile> o = this.service.findById(id);
		if (!o.isPresent()) {
			throw new EntityIdNotFoundException("Id Prospecto no encontrado");
		}
		
		ProspectProfile p = o.get();
		//this.service.validEntity(c, id);
		
		Optional<ProspectStatus> optional = this.serviceS.findById(ProspectStatusEnum.CONTRATADO.getValue());
		if(optional.isEmpty()) {
			throw new StatusCatalogNotFoundException("Estatus de Prospecto CONTRATADO no encontrado");
		}
		Optional<EmployeeStatus> optional2 = this.serviceES.findById(EmployeeStatusEnum.CONTRATADO.getValue());
		if(optional2.isEmpty()) {
			throw new StatusCatalogNotFoundException("Estatus de Empleado CONTRATADO no encontrado");
		}
		
		//Generate employee number
		String employeeNumber = this.serviceP.generateEmployeeNumber(p.getEmployeePosition());
		//System.out.println("employeeNumber:" + employeeNumber);
		EmployeeProfile e = this.service.saveNewHire(p, employeeNumber, optional.get(), optional2.get());
		if (e == null) {
			throw new CreateEmployeeProfileException("No se pudo crear Empleado desde el Prospecto, valide la info");
		}
		
		//Send message to Queue
		//jmsProducer.sendMessage(c);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(customMapper2.toDto(e));
	}

}
