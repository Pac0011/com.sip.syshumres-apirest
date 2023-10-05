package com.sip.syshumres_apirest.resources;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice("com.sip.syshumres_apirest.controllers")
public class CustomStringCleanAdvice {
	
	@InitBinder
    public void initBinder(WebDataBinder dataBinder) {
		System.out.println("==================================");
    	System.out.println("=============== Entre a initBinder2 ===================");
        dataBinder.registerCustomEditor(String.class, new StringCleanerEditor());
        
        //StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        //dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

}
