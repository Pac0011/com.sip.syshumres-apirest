package com.sip.syshumres_apirest.controllers;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
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

import com.sip.syshumres_apirest.controllers.common.CommonCatalogController;
import com.sip.syshumres_apirest.mappers.ListMapper;
import com.sip.syshumres_entities.HiringDocuments;
import com.sip.syshumres_entities.HiringDocumentsType;
import com.sip.syshumres_entities.dtos.HiringDocumentsDTO;
import com.sip.syshumres_entities.dtos.common.EntitySelectDTO;
import com.sip.syshumres_exceptions.EntityIdNotFoundException;
import com.sip.syshumres_exceptions.IdsEntityNotEqualsException;
import com.sip.syshumres_exceptions.utils.ErrorsBindingFields;
import com.sip.syshumres_services.HiringDocumentsService;
import com.sip.syshumres_utils.StringTrim;


@RestController
@RequestMapping(HiringDocumentsController.URLENDPOINT)
public class HiringDocumentsController extends CommonCatalogController {
	
	public static final String URLENDPOINT = "hiring-documents";
	
	private final HiringDocumentsService service;
	
	@Autowired
	public HiringDocumentsController(HiringDocumentsService service, 
			ModelMapper modelMapper,
			ListMapper listMapper) {
		this.service = service;
		this.modelMapper = modelMapper;
		this.listMapper = listMapper;
	}
	
	@GetMapping(ACTIVE)
	public ResponseEntity<List<EntitySelectDTO>> listActive() {
		return ResponseEntity.ok().body(service.findByEnabledTrueOrderByDescription().stream()
				.map(entity -> modelMapper.map(entity, EntitySelectDTO.class))
				.collect(Collectors.toList()));
	}
	
	@GetMapping(PAGE)
	public ResponseEntity<Page<HiringDocumentsDTO>> list(Pageable pageable) {
		Page<HiringDocuments> entities = this.service.findByFilterSession(this.filter, pageable);
		
		Page<HiringDocumentsDTO> entitiesPageDTO = entities.map(entity -> {
			HiringDocumentsDTO dto = modelMapper.map(entity, HiringDocumentsDTO.class);
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
	public ResponseEntity<Page<HiringDocumentsDTO>> list(Pageable pageable, Sort sort) {
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
	public ResponseEntity<Page<HiringDocumentsDTO>> list(String text, Pageable pageable) {
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
	public ResponseEntity<Page<HiringDocumentsDTO>> list(String text, Pageable pageable, Sort sort) {
		Pageable pageableOrder = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
		return this.list(text, pageableOrder);
	}
	
	@PostMapping
	public ResponseEntity<?> create(@Valid @RequestBody HiringDocumentsDTO entity, BindingResult result) {
		if (result.hasErrors()) {
			return ErrorsBindingFields.validate(result);
		}
		
		HiringDocuments e = new HiringDocuments();
		e.setDescription(StringTrim.trimAndRemoveDiacriticalMarks(entity.getDescription()));
		if (entity.getHiringDocumentsType() != null) {
	        e.setHiringDocumentsType(modelMapper.map(entity.getHiringDocumentsType(), 
	        		HiringDocumentsType.class));
	    }
		e.setEnabled(entity.isEnabled());
		HiringDocuments e2 = this.service.save(e);
		
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(modelMapper.map(e2, HiringDocumentsDTO.class));
	}
	
	@GetMapping(ID)
	public ResponseEntity<HiringDocumentsDTO> formEdit(@PathVariable Long id) 
			throws EntityIdNotFoundException, IllegalArgumentException {
		if (id <= 0) {
			throw new IllegalArgumentException("Id no puede ser cero o negativo");
		}
		Optional<HiringDocuments> entity = this.service.findById(id);
		if(entity.isEmpty()) {
			throw new EntityIdNotFoundException("Id rol " + id + " no encontrado");
		}
		
		return ResponseEntity.ok(modelMapper.map(entity.get(), HiringDocumentsDTO.class));
	}
	
	@PutMapping(ID)
	public ResponseEntity<?> edit(@Valid @RequestBody HiringDocumentsDTO entity, BindingResult result, @PathVariable Long id) 
			throws EntityIdNotFoundException, IdsEntityNotEqualsException, IllegalArgumentException {
		if (result.hasErrors()) {
			return ErrorsBindingFields.validate(result);
		}
		
		if (id <= 0) {
			throw new IllegalArgumentException("Id no puede ser cero o negativo");
		}
		if(!Objects.equals(id, entity.getId())){
			throw new IdsEntityNotEqualsException("Ids de documento de contratación no coinciden para actualización");
        }
		Optional<HiringDocuments> o = this.service.findById(id);
		if (!o.isPresent()) {
			throw new EntityIdNotFoundException("Id documento de contratación " + id + " no encontrado");
		}
		
		HiringDocuments e = o.get();
		e.setDescription(StringTrim.trimAndRemoveDiacriticalMarks(entity.getDescription()));
		if (entity.getHiringDocumentsType() != null) {
	        e.setHiringDocumentsType(modelMapper.map(entity.getHiringDocumentsType(), 
	        		HiringDocumentsType.class));
	    }
		e.setEnabled(entity.isEnabled());
		HiringDocuments e2 = this.service.save(e);
		
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(modelMapper.map(e2, HiringDocumentsDTO.class));
	}

}
