package com.sip.syshumres_apirest.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sip.syshumres_apirest.controllers.common.CommonCatalogController;
import com.sip.syshumres_entities.BranchOfficeType;
import com.sip.syshumres_entities.dtos.EntitySelectDTO;
import com.sip.syshumres_services.BranchOfficeTypeService;


/*
 * Tipo Sucursales SUC, OOP, ALC
 */
@RestController
@RequestMapping(BranchOfficeTypeController.URLENDPOINT)
public class BranchOfficeTypeController extends CommonCatalogController<BranchOfficeType, BranchOfficeTypeService> {
	
	public static final String URLENDPOINT = "branch-offices-types";
	public static final String FILTERTERM = "/filter/{term}";
	
	@GetMapping(FILTERTERM)
	public ResponseEntity<List<BranchOfficeType>> filter(@PathVariable String term) {
		return ResponseEntity.ok(service.findByDescription(term));
	}
	
	@GetMapping(ACTIVE)
	public ResponseEntity<List<EntitySelectDTO>> listActive() {
		return ResponseEntity.ok().body(service.findByEnabledTrueOrderByDescription().stream()
				.map(entity -> modelMapper.map(entity, EntitySelectDTO.class))
				.collect(Collectors.toList()));
	}

}
