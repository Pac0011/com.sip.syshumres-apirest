package com.sip.syshumres_apirest.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sip.syshumres_apirest.controllers.common.CommonCatalogController;
import com.sip.syshumres_entities.EmployeeStatus;
import com.sip.syshumres_services.EmployeeStatusService;


@RestController
@RequestMapping(EmployeeStatusController.URLENDPOINT)
public class EmployeeStatusController extends CommonCatalogController<EmployeeStatus, EmployeeStatusService>  {
	
	public static final String URLENDPOINT = "employee-status";

}
