package com.sip.syshumres_apirest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sip.syshumres_apirest.controllers.BranchOfficeController;
import com.sip.syshumres_entities.BranchOffice;
import com.sip.syshumres_entities.dtos.EntitySelectDTO;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.HttpClientErrorException.BadRequest;

//@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@SpringBootTest
@ActiveProfiles("test")
//@TestPropertySource(locations = "/application-test.properties")
@TestPropertySource(locations = "classpath:application-test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)//Para evitar que en cada Test inicialice la Clase y elimine el token
public class BranchOfficeFunctionalTesting {
	
	//@Value("${local.server.port}")
	//private int port;
	
	private String token;
	private long idEdit = 1L;
	private long idError = 0;
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Test
	@Order(1)
	public void login() {
		String credentials = "{\"username\": \"admin\", \"password\": \"admin\"}";
		ResponseEntity<?> response = new RestBuilder<String>()
				.clazz(String.class)
				.path("login")
				.post()
				.body(credentials)
				.builder();
		
		List<String> listAuthorization = response.getHeaders().getValuesAsList("Authorization");
		listAuthorization.forEach((String a) -> {
			System.out.println(a);
			assertTrue(a.contains("Bearer"));
			setToken(a.replaceAll("Bearer", "").trim());
		});
		System.out.println("=======token======");
		System.out.println(this.token);
		System.out.println("=======");
		assertEquals(HttpStatus.SC_OK, response.getStatusCodeValue());
	}
	
	@Test
	@Order(2)
	public void listActive() {
		ResponseEntity<?> response = new RestBuilder<String>()
				.clazz(String.class)
				.basicAuthToken(this.token)
				.path(BranchOfficeController.URLENDPOINT)
				.path(BranchOfficeController.ACTIVE)
				.get().builder();
		
		List<EntitySelectDTO> listGet = new ArrayList<>();
		try {
			listGet = mapToEntity(response.getBody().toString());
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		assertEquals(HttpStatus.SC_OK, response.getStatusCodeValue());
		assertTrue(listGet.size() > 0);
	}
	
	@Test
	@Order(3) 
	public void page() {
		//?page=0&size=5&sort=
		ResponseEntity<?> response = new RestBuilder<String>()
				.clazz(String.class)
				.basicAuthToken(getToken())
				.path(BranchOfficeController.URLENDPOINT)
				.path(BranchOfficeController.PAGE)
				.param("page", "0")
				.param("size", "5")
				.param("sort", "id,asc")
				.get().builder();
		   
     	int totalElements = 0;
		try {
			Map<String, Object> jsonAsArrayList = mapJsonAsArrayList(response.getBody().toString());
			
			totalElements = (int) jsonAsArrayList.get("totalElements");
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		assertEquals(HttpStatus.SC_OK, response.getStatusCodeValue());
		assertTrue(totalElements > 0);
	}
	
	@Test
	@Order(4) 
	public void formEdit() {
		ResponseEntity<?> response = new RestBuilder<String>()
				.clazz(String.class)
				.basicAuthToken(getToken())
				.path(BranchOfficeController.URLENDPOINT)
				.path(BranchOfficeController.ID)
				.expand(this.idEdit)
				.get().builder();
		
		BranchOffice branchOfficeGet = null;
		try {
			branchOfficeGet = mapToEntity2(response.getBody().toString());
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		assertEquals(HttpStatus.SC_OK, response.getStatusCodeValue());
		assertTrue(branchOfficeGet.getDescription().length() > 0);
		assertTrue(branchOfficeGet.getId() == this.idEdit);
	}
		
	@Test
	@Order(5)
	public void error() {
		Throwable exception = assertThrows(BadRequest.class, () -> {
	    	new RestBuilder<String>()
			.clazz(String.class)
			.basicAuthToken(getToken())
			.path(BranchOfficeController.URLENDPOINT)
			.path(BranchOfficeController.ERROR)
			.path(BranchOfficeController.ID)
			.expand(this.idError)
			.get().builder();
	    });
		
		//Recupera BadRequest y en el mensaje viene el json del la Exception IllegalArgumentException
		assertTrue(exception.getMessage().indexOf("IllegalArgumentException") > 0); 
	}
	
	private List<EntitySelectDTO> mapToEntity(String body) throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return Arrays.asList(mapper.readValue(body, EntitySelectDTO[].class));
	}
	
	private BranchOffice mapToEntity2(String body) throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);//Unrecognized field "fullPhoneNumber" transient
		return mapper.readValue(body, BranchOffice.class);
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, Object> mapJsonAsArrayList(String body) throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);//Unrecognized field "fullPhoneNumber" transient
		mapper.setDefaultSetterInfo(JsonSetter.Value.forContentNulls(Nulls.AS_EMPTY));
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		
        return mapper.readValue(body, Map.class);
	}

}
