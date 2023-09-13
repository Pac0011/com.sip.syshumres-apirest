package com.sip.syshumres_apirest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sip.syshumres_apirest.controllers.BranchOfficeController;
import com.sip.syshumres_entities.BranchOffice;
import com.sip.syshumres_entities.dtos.BranchOfficeDTO;
import com.sip.syshumres_entities.dtos.EntitySelectDTO;
import com.sip.syshumres_utils.RandomString;


@SpringBootTest
@TestPropertySource(locations = "/application-test.properties")
@WebAppConfiguration
@AutoConfigureMockMvc(addFilters = false)//no aplica filtros de autenticacion
public class BranchOfficeIT {
		
    private MockMvc mockMvc;
	
	@Autowired
	private WebApplicationContext wac;
	 
	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}
	 
	@Test
	public void listActive() throws Exception {
		MvcResult mockMvcResult = mockMvc.perform(MockMvcRequestBuilders.get(
				"/" + BranchOfficeController.URLENDPOINT + BranchOfficeController.ACTIVE)
				.accept(MediaType.APPLICATION_JSON))
				.andReturn();
		assertEquals(HttpStatus.OK.value(), mockMvcResult.getResponse().getStatus());
		
		List<EntitySelectDTO> listGet = mapToEntity(mockMvcResult.getResponse().getContentAsString());
		listGet.forEach((b) -> System.out.println(b));
		assertTrue(listGet.size() > 0);
	}
	
	@Test
	public void page() throws Exception {
		MvcResult mockMvcResult = mockMvc.perform(MockMvcRequestBuilders.get(
				"/" + BranchOfficeController.URLENDPOINT + BranchOfficeController.PAGE)
				.queryParam("page", "0")
				.queryParam("size", "5")
				.queryParam("sort", "id,asc")
				.accept(MediaType.APPLICATION_JSON))
				.andReturn();
		assertEquals(HttpStatus.OK.value(), mockMvcResult.getResponse().getStatus());
	}
	
	@Test
	public void create() throws Exception {
		BranchOfficeDTO b = buildBranchOffice();
		MvcResult mockMvcResult = mockMvc.perform(MockMvcRequestBuilders.post(
				"/" + BranchOfficeController.URLENDPOINT)
				.accept(MediaType.APPLICATION_JSON_VALUE)
					.contentType(MediaType.APPLICATION_JSON_VALUE)
					.content(mapToJson(b)))
				.andReturn();
		assertEquals(HttpStatus.CREATED.value(), mockMvcResult.getResponse().getStatus());
	}
	
	@Test
	public void formEdit() throws Exception {
		MvcResult mockMvcResult = mockMvc.perform(MockMvcRequestBuilders.get(
				"/" + BranchOfficeController.URLENDPOINT + BranchOfficeController.ID, 1L)
				.accept(MediaType.APPLICATION_JSON))
				.andReturn();
		assertEquals(HttpStatus.OK.value(), mockMvcResult.getResponse().getStatus());
	}
	
	@Test
	public void createWithError() throws Exception {
		BranchOfficeDTO b = buildBranchOffice();
		b.setEmail(null);
		MvcResult mockMvcResult = mockMvc.perform(MockMvcRequestBuilders.post(
				"/" + BranchOfficeController.URLENDPOINT)
				.accept(MediaType.APPLICATION_JSON_VALUE)
					.contentType(MediaType.APPLICATION_JSON_VALUE)
					.content(mapToJson(b)))
				.andReturn();
		assertEquals(HttpStatus.BAD_REQUEST.value(), mockMvcResult.getResponse().getStatus());
	}
	
	@Test
	public void formEditWithError() throws Exception {
		MvcResult mockMvcResult = mockMvc.perform(MockMvcRequestBuilders.get(
				"/" + BranchOfficeController.URLENDPOINT + BranchOfficeController.ID, 666L)
				.accept(MediaType.APPLICATION_JSON))
				.andReturn();
		assertEquals(HttpStatus.NOT_FOUND.value(), mockMvcResult.getResponse().getStatus());
	}
	
	@Test
	public void methodNotSupportedError() throws Exception {
		MvcResult mockMvcResult = mockMvc.perform(MockMvcRequestBuilders.get(
				"/" + BranchOfficeController.URLENDPOINT)
				.accept(MediaType.APPLICATION_JSON))
				.andReturn();
		assertEquals(HttpStatus.BAD_REQUEST.value(), mockMvcResult.getResponse().getStatus());
	}
	
	private String mapToJson(Object object) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(object);
	}
	
	private BranchOfficeDTO buildBranchOffice() {
		String nameTest = "Prueba" + RandomString.getRandomNumber(6);
		BranchOfficeDTO b = new BranchOfficeDTO();
		b.setDescription(nameTest);
		b.setEmail(nameTest + "@prueba.com");
		b.setEnabled(true);
		return  b;
	}
	
	private List<EntitySelectDTO> mapToEntity(String body) throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return Arrays.asList(mapper.readValue(body, EntitySelectDTO[].class));
	}

}
