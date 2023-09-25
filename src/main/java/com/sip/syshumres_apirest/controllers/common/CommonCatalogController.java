package com.sip.syshumres_apirest.controllers.common;

import org.modelmapper.ModelMapper;

import com.sip.syshumres_apirest.mappers.ListMapper;

public class CommonCatalogController {
	
	public static final String ACTIVE = "/active";
	public static final String PAGE = "/page";
	public static final String PAGEORDER = "/page-order";
	public static final String PAGEFILTER = "/page-filter";
	public static final String PAGEFILTERORDER = "/page-filter-order";
	public static final String ID = "/{id}";
	public static final String ERROR = "/error";
	public static final String FILTERTERM = "/filter/{term}";
	
	protected ModelMapper modelMapper;
	
	protected ListMapper listMapper;
		
    protected String filter;

}
