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

import com.sip.syshumres_apirest.controllers.common.CommonController;
import com.sip.syshumres_apirest.mappers.EmployeePositionMapper;
import com.sip.syshumres_entities.EmployeePosition;
import com.sip.syshumres_entities.dtos.EmployeePositionDTO;
import com.sip.syshumres_entities.dtos.common.EntitySelectDTO;
import com.sip.syshumres_entities.enums.EmployeeTypeEnum;
import com.sip.syshumres_exceptions.EntityIdNotFoundException;
import com.sip.syshumres_exceptions.IdsEntityNotEqualsException;
import com.sip.syshumres_exceptions.utils.ErrorsBindingFields;
import com.sip.syshumres_services.EmployeePositionService;
import com.sip.syshumres_utils.StringTrim;


@RestController
@RequestMapping(EmployeePositionController.URLENDPOINT)
public class EmployeePositionController extends CommonController {
	
	public static final String URLENDPOINT = "employee-positions";
	public static final String ADM = "/adm";
	public static final String OPER = "/oper";
	
	private final EmployeePositionService service;
	
	private final EmployeePositionMapper customMapper;
	
	@Autowired
	public EmployeePositionController(EmployeePositionService service, 
			EmployeePositionMapper customMapper) {
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
	
	@GetMapping(ADM + ACTIVE)
	public ResponseEntity<List<EntitySelectDTO>> listAdmActive() {
		return ResponseEntity.ok().body(service.findByEnabledTrueOrderByDescription(EmployeeTypeEnum.ADM.getValue()).stream()
				.map(entity -> customMapper.toSelectDto(entity))
				.collect(Collectors.toList()));
	}
	
	@GetMapping(OPER + ACTIVE)
	public ResponseEntity<List<EntitySelectDTO>> listOperActive() {
		return ResponseEntity.ok().body(service.findByEnabledTrueOrderByDescription(EmployeeTypeEnum.OPER.getValue()).stream()
				.map(entity -> customMapper.toSelectDto(entity))
				.collect(Collectors.toList()));
	}
	
	@GetMapping(PAGE)
	public ResponseEntity<Page<EmployeePositionDTO>> list(Pageable pageable) {
		Page<EmployeePosition> entities = this.service.findByFilterSession(this.filter, pageable);
		
		Page<EmployeePositionDTO> entitiesPageDTO = entities.map(entity -> {
			EmployeePositionDTO dto = customMapper.toDto(entity);
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
	public ResponseEntity<Page<EmployeePositionDTO>> list(Pageable pageable, Sort sort) {
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
	public ResponseEntity<Page<EmployeePositionDTO>> list(String text, Pageable pageable) {
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
	public ResponseEntity<Page<EmployeePositionDTO>> list(String text, Pageable pageable, Sort sort) {
		Pageable pageableOrder = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
		return this.list(text, pageableOrder);
	}
	
	@PostMapping
	public ResponseEntity<?> create(@Valid @RequestBody EmployeePositionDTO entity, BindingResult result) {
		if (result.hasErrors()) {
			return ErrorsBindingFields.validate(result);
		}
		
		EmployeePosition e = service.save(customMapper.toSaveEntity(entity));
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(customMapper.toDto(e));
	}
	
	@GetMapping(ID)
	public ResponseEntity<EmployeePositionDTO> formEdit(@PathVariable Long id) 
			throws EntityIdNotFoundException, IllegalArgumentException {
		if (id <= 0) {
			throw new IllegalArgumentException("Id no puede ser cero o negativo");
		}
		Optional<EmployeePosition> entity = this.service.findById(id);
		if(entity.isEmpty()) {
			throw new EntityIdNotFoundException("Id rol " + id + " no encontrado");
		}
		
		return ResponseEntity.ok(customMapper.toDto(entity.get()));
	}
	
	@PutMapping(ID)
	public ResponseEntity<?> edit(@Valid @RequestBody  EmployeePositionDTO entity, BindingResult result, @PathVariable Long id) 
			throws EntityIdNotFoundException, IdsEntityNotEqualsException, IllegalArgumentException {
		if (result.hasErrors()) {
			return ErrorsBindingFields.validate(result);
		}
		
		if (id <= 0) {
			throw new IllegalArgumentException("Id no puede ser cero o negativo");
		}
		if(!Objects.equals(id, entity.getId())){
			throw new IdsEntityNotEqualsException("Ids de puesto no coinciden para actualización");
        }
		Optional<EmployeePosition> o = this.service.findById(id);
		if (!o.isPresent()) {
			throw new EntityIdNotFoundException("Id puesto " + id + " no encontrado");
		}
		
		EmployeePosition e = this.service.save(customMapper.toEditEntity(o.get(), entity));
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(customMapper.toDto(e));
	}

}
