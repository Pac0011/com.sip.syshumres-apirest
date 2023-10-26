package com.sip.syshumres_apirest.controllers.common;

/**
 * Clase. Controlador comun
 * 
 * @author Prong
 * @version 2.0
 */
//@CrossOrigin({"http://localhost:4200"})
public class CommonController {
	
	public static final String ACTIVE = "/active";
	public static final String PAGE = "/page";
	public static final String PAGEORDER = "/page-order";
	public static final String PAGEFILTER = "/page-filter";
	public static final String PAGEFILTERORDER = "/page-filter-order";
	public static final String ID = "/{id}";
	public static final String ERROR = "/error";
	public static final String FILTERTERM = "/filter/{term}";
	
    protected String filter;
    
}
