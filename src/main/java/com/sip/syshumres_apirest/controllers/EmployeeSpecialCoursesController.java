package com.sip.syshumres_apirest.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sip.syshumres_apirest.controllers.common.CommonCatalogController;
import com.sip.syshumres_entities.EmployeeSpecialCourses;
import com.sip.syshumres_services.EmployeeSpecialCoursesService;


@RestController
@RequestMapping(EmployeeSpecialCoursesController.URLENDPOINT)
public class EmployeeSpecialCoursesController extends CommonCatalogController<EmployeeSpecialCourses, 
	EmployeeSpecialCoursesService>  {
	
	public static final String URLENDPOINT = "employee-special-courses";

}
