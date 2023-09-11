package com.sip.syshumres_apirest.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sip.syshumres_apirest.controllers.common.CommonCatalogController;
import com.sip.syshumres_entities.PayrollType;
import com.sip.syshumres_entities.dtos.EntitySelectDTO;
import com.sip.syshumres_services.PayrollTypeService;


@RestController
@RequestMapping(PayrollTypeController.URLENDPOINT)
public class PayrollTypeController extends CommonCatalogController<PayrollType, PayrollTypeService> {
	
	public static final String URLENDPOINT = "payroll-types";
	public static final String NORMAL = "/normal";
	
	@GetMapping(ACTIVE)
	public ResponseEntity<List<EntitySelectDTO>> listActive() {
		return ResponseEntity.ok().body(service.findByEnabledTrueOrderByDescription().stream()
				.map(entity -> modelMapper.map(entity, EntitySelectDTO.class))
				.collect(Collectors.toList()));
	}
	
	@GetMapping(NORMAL + ACTIVE)
	public ResponseEntity<List<EntitySelectDTO>> listNormalActive() {
		return ResponseEntity.ok().body(service.findNormalByEnabledTrueOrderByDescription().stream()
				.map(entity -> modelMapper.map(entity, EntitySelectDTO.class))
				.collect(Collectors.toList()));
	}
	
}
