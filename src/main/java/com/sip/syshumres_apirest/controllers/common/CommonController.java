package com.sip.syshumres_apirest.controllers.common;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.sip.syshumres_exceptions.utils.ErrorsBindingFields;
import com.sip.syshumres_services.common.CommonService;

/**
 * Clase. Controlador comun
 * 
 * @author Prong
 * @version 2.0
 */
//@CrossOrigin({"http://localhost:4200"})
public class CommonController<E, S extends CommonService<E>> {
	
	@Autowired
	protected S service;
	
	@Autowired
	protected ModelMapper modelMapper;
	
    protected String filter;
	
	@PostMapping
	public ResponseEntity<?> create(@Valid @RequestBody E entity, BindingResult result) {
		if (result.hasErrors()) {
			return ErrorsBindingFields.validate(result);
		}
		E s = service.save(entity);
		return ResponseEntity.status(HttpStatus.CREATED).body(s);
	}
	
	/*@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		service.deleteById(id);
		return ResponseEntity.noContent().build();
	}*/
}
