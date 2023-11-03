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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sip.syshumres_apirest.controllers.common.CommonController;
import com.sip.syshumres_apirest.enums.StatusMessages;
import com.sip.syshumres_apirest.mappers.EmployeePositionProfileMapper;
import com.sip.syshumres_entities.EmployeePositionProfile;
import com.sip.syshumres_entities.dtos.EmployeePositionProfileDTO;
import com.sip.syshumres_entities.dtos.ResponseDTO;
import com.sip.syshumres_entities.dtos.common.EntitySelectDTO;
import com.sip.syshumres_entities.utils.ErrorsBindingFields;
import com.sip.syshumres_exceptions.CreateRegisterException;
import com.sip.syshumres_exceptions.EntityIdNotFoundException;
import com.sip.syshumres_exceptions.IdsEntityNotEqualsException;
import com.sip.syshumres_exceptions.InvalidIdException;
import com.sip.syshumres_exceptions.UpdateRegisterException;
import com.sip.syshumres_services.EmployeePositionProfileService;
import com.sip.syshumres_utils.StringTrim;


@RestController
@RequestMapping(EmployeePositionProfileController.URLENDPOINT)
public class EmployeePositionProfileController extends CommonController {
	
	public static final String URLENDPOINT = "employee-position-profiles";
	
	private final EmployeePositionProfileService service;
	
	private final EmployeePositionProfileMapper customMapper;
		
	@Autowired
	public EmployeePositionProfileController(EmployeePositionProfileService service, 
			EmployeePositionProfileMapper customMapper) {
		this.service = service;
		this.customMapper = customMapper;
		this.filter = "";
	}
	
	@GetMapping(ACTIVE)
	public ResponseEntity<List<EntitySelectDTO>> listActive() {
		return ResponseEntity.ok().body(service.findByEnabledTrueOrderByDescription().stream()
				.map(customMapper::toSelectDto)
				.toList());
	}
	
	@GetMapping(PAGE)
	public ResponseEntity<Page<EmployeePositionProfileDTO>> list(Pageable pageable) {
		Page<EmployeePositionProfile> entities = this.service.findByFilterSession(this.filter, pageable);
		
		Page<EmployeePositionProfileDTO> entitiesPageDTO = entities.map(customMapper::toDto);

		return ResponseEntity.ok().body(entitiesPageDTO);
	}
	
	/**
     * @param page          number of the page returned
     * @param size          number of entries in each page
     * @param sort  		Sort object
     * @return Page object with entitys after sorting
     */
	@GetMapping(PAGEORDER)
	public ResponseEntity<Page<EmployeePositionProfileDTO>> list(Pageable pageable, Sort sort) {
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
	public ResponseEntity<Page<EmployeePositionProfileDTO>> list(@RequestParam String q, Pageable pageable) {
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
	public ResponseEntity<Page<EmployeePositionProfileDTO>> list(@RequestParam String q, Pageable pageable, Sort sort) {
		Pageable pageableOrder = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
		return this.list(q, pageableOrder);
	}
	
	@PostMapping
	public ResponseEntity<ResponseDTO> create(@Valid @RequestBody EmployeePositionProfileDTO entity, BindingResult result) 
	throws CreateRegisterException {
		if (result.hasErrors()) {
			return ResponseEntity.badRequest()
					.body(ErrorsBindingFields.getErrors(result));
		}
		
		EmployeePositionProfile e = service.save(customMapper.toSaveEntity(entity));
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
	public ResponseEntity<EmployeePositionProfileDTO> formEdit(@PathVariable Long id) 
			throws EntityIdNotFoundException, InvalidIdException {
		if (id <= 0) {
			throw new InvalidIdException();
		}
		Optional<EmployeePositionProfile> entity = this.service.findById(id);
		if(entity.isEmpty()) {
			throw new EntityIdNotFoundException("Id perfil puesto " + id + " no encontrado");
		}
		
		return ResponseEntity.ok(customMapper.toDto(entity.get()));
	}
	
	@PutMapping(ID)
	public ResponseEntity<ResponseDTO> edit(@Valid @RequestBody EmployeePositionProfileDTO entity, BindingResult result, @PathVariable Long id) 
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
			throw new IdsEntityNotEqualsException("Ids de perfil puesto no coinciden para actualización");
        }
		Optional<EmployeePositionProfile> o = this.service.findById(id);
		if (!o.isPresent()) {
			throw new EntityIdNotFoundException("Id perfil puesto " + id + " no encontrado");
		}
		
		if (this.service.save(customMapper.toEditEntity(o.get(), entity)) == null) {
			throw new UpdateRegisterException();
		}
		
		ResponseDTO response = new ResponseDTO();
		response.addEntry(StatusMessages.MESSAGE_KEY.getMessage(), 
				StatusMessages.SUCCESS_UPDATE.getMessage());
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(response);
	}

}
