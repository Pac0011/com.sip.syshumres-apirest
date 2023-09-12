package com.sip.syshumres_apirest.controllers;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sip.syshumres_apirest.controllers.common.CommonCatalogController;
import com.sip.syshumres_entities.ReasonQuitJob;
import com.sip.syshumres_entities.dtos.EntitySelectDTO;
import com.sip.syshumres_exceptions.EntityIdNotFoundException;
import com.sip.syshumres_exceptions.IdsEntityNotEqualsException;
import com.sip.syshumres_exceptions.utils.ErrorsBindingFields;
import com.sip.syshumres_services.ReasonQuitJobService;


@RestController
@RequestMapping(ReasonQuitJobController.URLENDPOINT)
public class ReasonQuitJobController extends CommonCatalogController<ReasonQuitJob, ReasonQuitJobService> {
	
	public static final String URLENDPOINT = "reason-quit-job";
	
	@GetMapping(ACTIVE)
	public ResponseEntity<List<EntitySelectDTO>> listActive() {
		return ResponseEntity.ok().body(service.findByEnabledTrueOrderByDescription().stream()
				.map(entity -> modelMapper.map(entity, EntitySelectDTO.class))
				.collect(Collectors.toList()));
	}
	
	@Override
	@PutMapping(ID)
	public ResponseEntity<?> edit(@Valid @RequestBody ReasonQuitJob entity, BindingResult result, @PathVariable Long id) 
			throws EntityIdNotFoundException, IdsEntityNotEqualsException, IllegalArgumentException {
		if (result.hasErrors()) {
			return ErrorsBindingFields.validate(result);
		}
		
		if (id <= 0) {
			throw new IllegalArgumentException("Id no puede ser cero o negativo");
		}
		if(!Objects.equals(id, entity.getId())){
			throw new IdsEntityNotEqualsException("Ids razón de baja no coinciden para actualización");
        }
		Optional<ReasonQuitJob> o = this.service.findById(id);
		if (!o.isPresent()) {
			throw new EntityIdNotFoundException("Id razón de baja " + id + " no encontrado");
		}
		
		ReasonQuitJob e = o.get();
		e.setDescription(entity.getDescription());
		e.setRehire(entity.isRehire());
		e.setEnabled(entity.isEnabled());
		return ResponseEntity.status(HttpStatus.CREATED).body(this.service.save(e));
	}

}
