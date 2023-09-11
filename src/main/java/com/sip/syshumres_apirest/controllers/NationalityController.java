package com.sip.syshumres_apirest.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sip.syshumres_apirest.controllers.common.CommonCatalogController;
import com.sip.syshumres_entities.Nationality;
import com.sip.syshumres_services.NationalityService;


@RestController
@RequestMapping(NationalityController.URLENDPOINT)
public class NationalityController extends CommonCatalogController<Nationality, NationalityService> {
	
	public static final String URLENDPOINT = "nationality";

}
