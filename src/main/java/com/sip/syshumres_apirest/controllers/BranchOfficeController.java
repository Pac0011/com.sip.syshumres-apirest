package com.sip.syshumres_apirest.controllers;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sip.syshumres_apirest.controllers.common.CommonController;
import com.sip.syshumres_apirest.mappers.BranchOfficeMapper;
import com.sip.syshumres_entities.BranchOffice;
import com.sip.syshumres_entities.dtos.BranchOfficeDTO;
import com.sip.syshumres_entities.dtos.common.EntitySelectDTO;
import com.sip.syshumres_exceptions.EntityIdNotFoundException;
import com.sip.syshumres_exceptions.FatherAssignException;
import com.sip.syshumres_exceptions.IdsEntityNotEqualsException;
import com.sip.syshumres_exceptions.MalFormedHeaderException;
import com.sip.syshumres_exceptions.utils.ErrorsBindingFields;
import com.sip.syshumres_services.BranchOfficeService;
import com.sip.syshumres_utils.StringTrim;

import org.springframework.beans.factory.annotation.Autowired;


@RestController
@RequestMapping(BranchOfficeController.URLENDPOINT)
public class BranchOfficeController extends CommonController {
	
	public static final String URLENDPOINT = "branch-offices";
	
	private BranchOfficeService service;
	
	private BranchOfficeMapper customMapper;

	@Autowired
	public BranchOfficeController(BranchOfficeService service, 
			BranchOfficeMapper customMapper) {
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
	public ResponseEntity<Page<BranchOfficeDTO>> list(Pageable pageable) {
		Page<BranchOffice> entities = this.service.findByFilterSession(this.filter, pageable);
		
		Page<BranchOfficeDTO> entitiesPageDTO = entities.map(entity -> {
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
	public ResponseEntity<Page<BranchOfficeDTO>> list(Pageable pageable, Sort sort) {
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
	public ResponseEntity<Page<BranchOfficeDTO>> list(String text, Pageable pageable) {
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
	public ResponseEntity<Page<BranchOfficeDTO>> list(String text, Pageable pageable, Sort sort) {
		Pageable pageableOrder = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
		return this.list(text, pageableOrder);
	}
	
	@PostMapping
	public ResponseEntity<?> create(@Valid @RequestBody BranchOfficeDTO entity, BindingResult result) {
		if (result.hasErrors()) {
			return ErrorsBindingFields.validate(result);
		}
		
		BranchOffice e = service.save(customMapper.toCreateEntity(entity));
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(customMapper.toDto(e));
	}
	
	@GetMapping(ID)
	public ResponseEntity<BranchOfficeDTO> formEdit(@PathVariable Long id) 
			throws EntityIdNotFoundException, IllegalArgumentException {
		if (id <= 0) {
			throw new IllegalArgumentException("Id no puede ser cero o negativo");
		}
		Optional<BranchOffice> entity = this.service.findById(id);
		if(entity.isEmpty()) {
			throw new EntityIdNotFoundException("Id sucursal " + id + " no encontrado");
		}
		
		return ResponseEntity.ok(customMapper.toDto(entity.get()));
	}
	
	@PutMapping(ID)
	public ResponseEntity<?> edit(@Valid @RequestBody BranchOfficeDTO entity, BindingResult result, @PathVariable Long id) 
			throws EntityIdNotFoundException, IdsEntityNotEqualsException, FatherAssignException, IllegalArgumentException {
		if (result.hasErrors()) {
			return ErrorsBindingFields.validate(result);
		}
		
		if (id <= 0) {
			throw new IllegalArgumentException("Id no puede ser cero o negativo");
		}
		if (!Objects.equals(id, entity.getId())) {
			throw new IdsEntityNotEqualsException("Ids de sucursal no coinciden para actualización");
        }
		Optional<BranchOffice> o = this.service.findById(id);
		if (!o.isPresent()) {
			throw new EntityIdNotFoundException("Id sucursal " + id + " no encontrado");
		}
		
		BranchOffice entityDb = o.get();
		if (entity.getFather() != null && entityDb.getId() == entity.getFather().getId()) {
			throw new FatherAssignException("El padre de la sucursal no puede ser ella misma");
		}
		
		BranchOffice e = this.service.save(customMapper.toEditEntity(entityDb, entity));
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(customMapper.toDto(e));
	}
	
	@GetMapping(ERROR + ID)
	public BranchOfficeDTO error(@RequestHeader String Authorization, @PathVariable Long id) 
			throws EntityIdNotFoundException, MalFormedHeaderException, IllegalArgumentException {
		if(id <= 0) {
			throw new IllegalArgumentException("Id no puede ser cero o negativo");
		}
		if(Authorization.equals("kk")) {
			throw new MalFormedHeaderException("token: " + Authorization);
		}
		
		Optional<BranchOffice> entity = this.service.findById(id);
		if(entity.isEmpty()) {
			throw new EntityIdNotFoundException("Id sucursal " + id + " no encontrado");
		}
		
		return customMapper.toDto(entity.get());
	}

}
