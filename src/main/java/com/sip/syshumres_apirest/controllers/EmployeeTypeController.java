package com.sip.syshumres_apirest.controllers;

import java.util.Objects;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sip.syshumres_apirest.controllers.common.CommonCatalogController;
import com.sip.syshumres_entities.EmployeeType;
import com.sip.syshumres_exceptions.EntityIdNotFoundException;
import com.sip.syshumres_exceptions.IdsEntityNotEqualsException;
import com.sip.syshumres_exceptions.utils.ErrorsBindingFields;
import com.sip.syshumres_services.EmployeeTypeService;
import com.sip.syshumres_utils.StringTrim;


@RestController
@RequestMapping(EmployeeTypeController.URLENDPOINT)
public class EmployeeTypeController extends CommonCatalogController<EmployeeType, EmployeeTypeService>  {
	
	public static final String URLENDPOINT = "employee-types";
	
	@Override
	@PutMapping(ID)
	public ResponseEntity<?> edit(@Valid @RequestBody EmployeeType entity, BindingResult result, @PathVariable Long id) 
			throws EntityIdNotFoundException, IdsEntityNotEqualsException, IllegalArgumentException {
		if (result.hasErrors()) {
			return ErrorsBindingFields.validate(result);
		}
		
		if (id <= 0) {
			throw new IllegalArgumentException("Id no puede ser cero o negativo");
		}
		if(!Objects.equals(id, entity.getId())){
			throw new IdsEntityNotEqualsException("Ids de tipo de empleado no coinciden para actualización");
        }
		Optional<EmployeeType> o = this.service.findById(id);
		if (!o.isPresent()) {
			throw new EntityIdNotFoundException("Id tipo de empleado " + id + " no encontrado");
		}
		
		EmployeeType e = o.get();
		e.setDescription(StringTrim.trimAndRemoveDiacriticalMarks(entity.getDescription()));
		e.setPrefix(StringTrim.trimAndRemoveDiacriticalMarks(entity.getPrefix()));
		e.setEnabled(entity.isEnabled());
		return ResponseEntity.status(HttpStatus.CREATED).body(this.service.save(e));
	}

}
