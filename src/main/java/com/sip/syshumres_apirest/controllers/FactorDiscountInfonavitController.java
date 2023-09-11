package com.sip.syshumres_apirest.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sip.syshumres_apirest.controllers.common.CommonCatalogController;
import com.sip.syshumres_entities.FactorDiscountInfonavit;
import com.sip.syshumres_entities.dtos.EntitySelectDTO;
import com.sip.syshumres_services.FactorDiscountInfonavitService;


@RestController
@RequestMapping(FactorDiscountInfonavitController.URLENDPOINT)
public class FactorDiscountInfonavitController extends CommonCatalogController<FactorDiscountInfonavit, 
	FactorDiscountInfonavitService> {
	
	public static final String URLENDPOINT = "factors-discount-infonavit";
	
	@GetMapping(ACTIVE)
	public ResponseEntity<List<EntitySelectDTO>> listActive() {
		return ResponseEntity.ok().body(service.findByEnabledTrueOrderByDescription().stream()
				.map(entity -> modelMapper.map(entity, EntitySelectDTO.class))
				.collect(Collectors.toList()));
	}

}
