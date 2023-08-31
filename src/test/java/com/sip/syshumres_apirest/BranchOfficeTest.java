package com.sip.syshumres_apirest;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;

import com.sip.syshumres_apirest.controllers.BranchOfficeController;
import com.sip.syshumres_entities.BranchOffice;
import com.sip.syshumres_services.BranchOfficeService;


@PropertySource(
	    ignoreResourceNotFound = false,
	    value = "classpath:application.properties")//test/resources/application.properties
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)//no aplica filtros de autenticacion
public class BranchOfficeTest {
	
	  @Autowired
	  private BranchOfficeService service;
	  
	  @Autowired
	  private BranchOfficeController controller;
	 
	  @Test
	  public void testContextLoads() {
	      Assertions.assertThat(controller).isNotNull();
	      //System.out.println(controller.toString());
	  }
	  
	  //Checar esta busqueda assertTrue(listGet.contains(branch));
	  @Test
	  void testGetListBranchs() {
	      List<BranchOffice> listGet = service.findByEnabledTrueOrderByDescription();
	      BranchOffice branch = new BranchOffice();
		  branch.setId(1L);
		  branch.setDescription("Corporativo");
		  assertTrue(listGet.size() > 0);
		  assertTrue(listGet.contains(branch));
	      //assertEquals("Corporativo", listGet.get(1).getDescription());
	  }

}
