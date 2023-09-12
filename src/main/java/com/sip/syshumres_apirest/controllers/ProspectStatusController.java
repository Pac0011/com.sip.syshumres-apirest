package com.sip.syshumres_apirest.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sip.syshumres_apirest.controllers.common.CommonCatalogController;
import com.sip.syshumres_entities.ProspectStatus;
import com.sip.syshumres_entities.dtos.EntitySelectDTO;
import com.sip.syshumres_services.ProspectStatusService;


@RestController
@RequestMapping(ProspectStatusController.URLENDPOINT)
public class ProspectStatusController extends CommonCatalogController<ProspectStatus, ProspectStatusService>  {
	
	public static final String URLENDPOINT = "prospect-status";
	
	@GetMapping(ACTIVE)
	public ResponseEntity<List<EntitySelectDTO>> listActive() {
		return ResponseEntity.ok().body(service.findByEnabledTrueOrderByDescription().stream()
				.map(entity -> modelMapper.map(entity, EntitySelectDTO.class))
				.collect(Collectors.toList()));
	}

}
