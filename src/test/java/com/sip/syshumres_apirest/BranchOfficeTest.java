package com.sip.syshumres_apirest;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import com.sip.syshumres_apirest.controllers.BranchOfficeController;
import com.sip.syshumres_entities.BranchOffice;
import com.sip.syshumres_services.BranchOfficeService;

@SpringBootTest
@WebAppConfiguration
//@TestPropertySource("/application-test.properties")
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
