package com.sip.syshumres_apirest.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sip.syshumres_apirest.controllers.common.CommonCatalogController;
import com.sip.syshumres_entities.Turn;
import com.sip.syshumres_services.TurnService;


@RestController
@RequestMapping(TurnController.URLENDPOINT)
public class TurnController extends CommonCatalogController<Turn, TurnService> {
	
	public static final String URLENDPOINT = "turns";

}
