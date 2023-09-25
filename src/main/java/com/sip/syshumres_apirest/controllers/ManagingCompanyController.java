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
import org.springframework.web.bind.annotation.RestController;

import com.sip.syshumres_apirest.controllers.common.CommonController;
import com.sip.syshumres_apirest.mappers.BranchOfficeMapper;
import com.sip.syshumres_apirest.mappers.ListMapper;
import com.sip.syshumres_apirest.mappers.ManagingCompanyMapper;
import com.sip.syshumres_entities.BranchOffice;
import com.sip.syshumres_entities.ManagingCompany;
import com.sip.syshumres_entities.dtos.BranchOfficeDTO;
import com.sip.syshumres_entities.dtos.ManagingCompanyDTO;
import com.sip.syshumres_exceptions.EntityIdNotFoundException;
import com.sip.syshumres_exceptions.IdsEntityNotEqualsException;
import com.sip.syshumres_exceptions.utils.ErrorsBindingFields;
import com.sip.syshumres_services.ManagingCompanyService;
import com.sip.syshumres_utils.StringTrim;


@RestController
@RequestMapping(ManagingCompanyController.URLENDPOINT)
public class ManagingCompanyController extends CommonController {
	
	public static final String URLENDPOINT = "managing-companies";
	public static final String BRANCHOFFICES = "/branch-offices";
	public static final String ABRANCHOFFICES = "/assign-branch-offices";
	public static final String RBRANCHOFFICE = "/remove-branch-office";
	
	private ManagingCompanyService service;
	
	private ManagingCompanyMapper customMapper;
	
	private BranchOfficeMapper customMapper2;
	
	private ListMapper listMapper;
		
	@Autowired
	public ManagingCompanyController(ManagingCompanyService service, 
			ManagingCompanyMapper customMapper,
			BranchOfficeMapper customMapper2,
			ListMapper listMapper) {
		this.service = service;
		this.customMapper = customMapper;
		this.customMapper2 = customMapper2;
		this.listMapper = listMapper;
		this.filter = "";
	}
	
	@GetMapping(ID + BRANCHOFFICES)
	public ResponseEntity<ManagingCompanyDTO> getBranchOffices(@PathVariable Long id) 
			throws EntityIdNotFoundException, IllegalArgumentException {
		if (id <= 0) {
			throw new IllegalArgumentException("Id no puede ser cero o negativo");
		}
		Optional<ManagingCompany> entity = service.findById(id);
		if(entity.isEmpty()) {
			throw new EntityIdNotFoundException("Id administradora " + id + " no encontrado");
		}
				
		return ResponseEntity.ok(customMapper.toDto(entity.get()));
	}

	@GetMapping(PAGE)
	public ResponseEntity<Page<ManagingCompanyDTO>> list(Pageable pageable) {
		Page<ManagingCompany> entities = this.service.findByFilterSession(this.filter, pageable);
		
		Page<ManagingCompanyDTO> entitiesPageDTO = entities.map(entity -> {
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
	public ResponseEntity<Page<ManagingCompanyDTO>> list(Pageable pageable, Sort sort) {
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
	public ResponseEntity<Page<ManagingCompanyDTO>> list(String text, Pageable pageable) {
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
	public ResponseEntity<Page<ManagingCompanyDTO>> list(String text, Pageable pageable, Sort sort) {
		Pageable pageableOrder = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
		return this.list(text, pageableOrder);
	}
	
	@PostMapping
	public ResponseEntity<?> create(@Valid @RequestBody ManagingCompanyDTO entity, BindingResult result) {
		if (result.hasErrors()) {
			return ErrorsBindingFields.validate(result);
		}
		
		ManagingCompany e = service.save(customMapper.toSaveEntity(entity));
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(customMapper.toDto(e));
	}
	
	@GetMapping(ID)
	public ResponseEntity<ManagingCompanyDTO> formEdit(@PathVariable Long id) 
			throws EntityIdNotFoundException, IllegalArgumentException {
		if (id <= 0) {
			throw new IllegalArgumentException("Id no puede ser cero o negativo");
		}
		Optional<ManagingCompany> entity = this.service.findById(id);
		if(entity.isEmpty()) {
			throw new EntityIdNotFoundException("Id administradora " + id + " no encontrado");
		}
		
		return ResponseEntity.ok(customMapper.toDto(entity.get()));
	}
	
	@PutMapping(ID)
	public ResponseEntity<?> edit(@Valid @RequestBody ManagingCompanyDTO entity, BindingResult result, @PathVariable Long id) 
			throws EntityIdNotFoundException, IdsEntityNotEqualsException, IllegalArgumentException {
		if (result.hasErrors()) {
			return ErrorsBindingFields.validate(result);
		}
		
		if (id <= 0) {
			throw new IllegalArgumentException("Id no puede ser cero o negativo");
		}
		if(!Objects.equals(id, entity.getId())){
			throw new IdsEntityNotEqualsException("Ids de la administradora no coinciden para actualización");
        }
		Optional<ManagingCompany> o = this.service.findById(id);
		if (!o.isPresent()) {
			throw new EntityIdNotFoundException("Id administradora " + id + " no encontrado");
		}
		
		ManagingCompany e = this.service.save(customMapper.toEditEntity(o.get(), entity));
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(customMapper.toDto(e));
	}
	
	@PatchMapping(ID + ABRANCHOFFICES)
	public ResponseEntity<ManagingCompanyDTO> assignBranchOffices(@RequestBody List<BranchOfficeDTO> branchOffices, @PathVariable Long id) 
			throws EntityIdNotFoundException, IllegalArgumentException {
		if (id <= 0) {
			throw new IllegalArgumentException("Id no puede ser cero o negativo");
		}
		Optional<ManagingCompany> o = this.service.findById(id);
		if (!o.isPresent()) {
			throw new EntityIdNotFoundException("Id administradora " + id + " no encontrado");
		}
		
		ManagingCompany e = this.service.assignBranchOffices(o.get(), listMapper.mapList(branchOffices, BranchOffice.class));
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(customMapper.toDto(e));
	}
	
	@PatchMapping(ID + RBRANCHOFFICE)
	public ResponseEntity<ManagingCompanyDTO> removeBranchOffice(@RequestBody BranchOfficeDTO branchOffice, @PathVariable Long id) 
			throws EntityIdNotFoundException, IllegalArgumentException {
		if (id <= 0) {
			throw new IllegalArgumentException("Id no puede ser cero o negativo");
		}
		Optional<ManagingCompany> o = this.service.findById(id);
		if (!o.isPresent()) {
			throw new EntityIdNotFoundException("Id administradora " + id + " no encontrado");
		}

		ManagingCompany e = this.service.removeBranchOffice(o.get(), customMapper2.toEntity(branchOffice));
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(customMapper.toDto(e));
	}

}
