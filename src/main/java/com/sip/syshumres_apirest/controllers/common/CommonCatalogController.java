package com.sip.syshumres_apirest.controllers.common;

import java.util.Objects;
import java.util.Optional;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.sip.syshumres_entities.common.BaseEntityCatalog;
import com.sip.syshumres_exceptions.EntityIdNotFoundException;
import com.sip.syshumres_exceptions.IdsEntityNotEqualsException;
import com.sip.syshumres_services.common.CommonService;
import com.sip.syshumres_utils.ErrorsBindingFields;
import com.sip.syshumres_utils.StringTrim;


/**
 * Clase. Controlador generico para los Catalogos
 * 
 * @author Prong
 * @version 2.0
 */
public class CommonCatalogController<E extends BaseEntityCatalog, S extends CommonService<E>> {
	
	public static final String ACTIVE = "/active";
	public static final String PAGE = "/page";
	public static final String PAGEORDER = "/page-order";
	public static final String PAGEFILTER = "/page-filter";
	public static final String PAGEFILTERORDER = "/page-filter-order";
	public static final String ID = "/{id}";
	
	@Autowired
	protected S service;
	
	@Autowired
	protected ModelMapper modelMapper;
	
    protected String filter;
	
	@GetMapping
	public ResponseEntity<?> list() {
		return ResponseEntity.ok().body(service.findAll());
	}
	
	@GetMapping(ID)
	public ResponseEntity<?> detail(@PathVariable Long id) 
			throws EntityIdNotFoundException, IllegalArgumentException {
		if (id <= 0) {
			throw new IllegalArgumentException("Id no puede ser cero o negativo");
		}
		Optional<E> entity = service.findById(id);
		if(entity.isEmpty()) {
			throw new EntityIdNotFoundException();
		}
		return ResponseEntity.ok(entity.get());
	}
	
	@PostMapping
	public ResponseEntity<?> save(@Valid @RequestBody E entity, BindingResult result) {
		if (result.hasErrors()) {
			return ErrorsBindingFields.validate(result);
		}
		E s = service.create(entity);
		return ResponseEntity.status(HttpStatus.CREATED).body(s);
	}
	
	@PutMapping(ID)
	public ResponseEntity<?> edit(@Valid @RequestBody E entity, BindingResult result, @PathVariable Long id) 
			throws EntityIdNotFoundException, IdsEntityNotEqualsException, IllegalArgumentException {
		if (result.hasErrors()) {
			return ErrorsBindingFields.validate(result);
		}
		
		if (id <= 0) {
			throw new IllegalArgumentException("Id no puede ser cero o negativo");
		}
		if(!Objects.equals(id, entity.getId())){
			throw new IdsEntityNotEqualsException();
        }
		Optional<E> o = service.findById(id);
		if (!o.isPresent()) {
			throw new EntityIdNotFoundException();
		}
		
		E c = o.get();
		c.setDescription(StringTrim.trimAndRemoveDiacriticalMarks(entity.getDescription()));
		c.setEnabled(entity.isEnabled());
		return ResponseEntity.status(HttpStatus.CREATED).body(service.create(c));
	}

}
