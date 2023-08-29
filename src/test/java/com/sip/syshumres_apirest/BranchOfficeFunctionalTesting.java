package com.sip.syshumres_apirest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sip.syshumres_apirest.controllers.BranchOfficeController;
import com.sip.syshumres_apirest.resources.RestBuilder;
import com.sip.syshumres_entities.BranchOffice;
import com.sip.syshumres_entities.dtos.EntitySelectDTO;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

//@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@SpringBootTest
//@TestPropertySource(locations = "classpath:test.properties")
public class BranchOfficeFunctionalTesting {
	
	//@Value("${local.server.port}")
	//private int port;
	
	private String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImJyYW5jaE9mZmljZSI6IkNhbmN1biIsIm1lbnUiOlt7Im4iOiJBZG1vbiBzdWN1cnNhbGVzIiwidSI6IiMiLCJpIjoiZmEgZmEtYmFycyIsImMiOlt7Im4iOiJBZG1pbmlzdHJhZG9yYXMiLCJ1IjoibWFuYWdpbmctY29tcGFuaWVzIiwiaSI6ImZhIGZhLXVuaXZlcnNpdHkiLCJjIjpudWxsfSx7Im4iOiJSZWdpb25lcyIsInUiOiJyZWdpb25zIiwiaSI6ImZhIGZhLXBpZS1jaGFydCIsImMiOm51bGx9LHsibiI6IlN1Y3Vyc2FsZXMiLCJ1IjoiYnJhbmNoLW9mZmljZXMiLCJpIjoiZmEgZmEtYnVpbGRpbmciLCJjIjpudWxsfSx7Im4iOiJUaXBvIGRlIHN1Y3Vyc2FsZXMiLCJ1IjoiYnJhbmNoLW9mZmljZXMtdHlwZXMiLCJpIjoiZmEgZmEtaW5kdXN0cnkiLCJjIjpudWxsfV19LHsibiI6IkNhdGFsb2dvcyIsInUiOiIjIiwiaSI6ImZhIGZhLWJhcnMiLCJjIjpbeyJuIjoiQXJlYXMiLCJ1IjoiZW1wbG95ZWUtYXJlYXMiLCJpIjoiZmEgZmEtc2l0ZW1hcCIsImMiOm51bGx9LHsibiI6IkJhbmNvcyBlbXBsZWFkbyIsInUiOiJlbXBsb3llZS1iYW5rcyIsImkiOiJmYSBmYS11bml2ZXJzaXR5IiwiYyI6bnVsbH0seyJuIjoiQ2VudHJvcyBkZSBjb3N0b3MiLCJ1IjoiY29zdC1jZW50ZXJzIiwiaSI6ImZhIGZhLXVzZCIsImMiOm51bGx9LHsibiI6IkNvbWVkb3IiLCJ1IjoiZGlubmluZy1yb29tIiwiaSI6ImZhIGZhLWJpcnRoZGF5LWNha2UiLCJjIjpudWxsfSx7Im4iOiJEb2N1bWVudG9zIGNvbnRyYXRhY2lvbiIsInUiOiJoaXJpbmctZG9jdW1lbnRzIiwiaSI6ImZhIGZhLWJvb2siLCJjIjpudWxsfSx7Im4iOiJPdHJhcyByYXpvbmVzIGRlIGJhamEiLCJ1Ijoib3RoZXItcmVhc29uLXF1aXQtam9iIiwiaSI6ImZhIGZhLW91dGRlbnQiLCJjIjpudWxsfSx7Im4iOiJQZXJmaWwgcHVlc3RvcyIsInUiOiJlbXBsb3llZS1wb3NpdGlvbi1wcm9maWxlcyIsImkiOiJmYSBmYS10aC1saXN0IiwiYyI6bnVsbH0seyJuIjoiUHVlc3RvcyIsInUiOiJlbXBsb3llZS1wb3NpdGlvbnMiLCJpIjoiZmEgZmEtc2l0ZW1hcCIsImMiOm51bGx9LHsibiI6IlJhem9uZXMgZGUgYmFqYSIsInUiOiJyZWFzb24tcXVpdC1qb2IiLCJpIjoiZmEgZmEtdGgtbGlzdCIsImMiOm51bGx9LHsibiI6IlRpcG8gcGxhbnRpbGxhIiwidSI6InR5cGUtc3RhZmYiLCJpIjoiZmEgZmEtdXNlci1jaXJjbGUiLCJjIjpudWxsfSx7Im4iOiJUaXBvIHNhbHVkIGVtcGxlYWRvIiwidSI6ImVtcGxveWVlLXR5cGVzLWhlYWx0aCIsImkiOiJmYSBmYS11c2VyLW1kIiwiYyI6bnVsbH0seyJuIjoiVGlwb3MgZGUgZG9jdW1lbnRvcyIsInUiOiJoaXJpbmctZG9jdW1lbnRzLXR5cGUiLCJpIjoiZmEgZmEtYm9vayIsImMiOm51bGx9LHsibiI6IlRpcG9zIGRlIGVtcGxlYWRvIiwidSI6ImVtcGxveWVlLXR5cGVzIiwiaSI6ImZhIGZhLXVzZXItY2lyY2xlIiwiYyI6bnVsbH0seyJuIjoiVHVybm9zIiwidSI6InR1cm5zIiwiaSI6ImZhIGZhLWhvdXJnbGFzcy1oYWxmIiwiYyI6bnVsbH0seyJuIjoiVmFjdW5hcyBjb3ZpZCIsInUiOiJjb3ZpZC12YWNjaW5lcyIsImkiOiJmYSBmYS1tZWRraXQiLCJjIjpudWxsfV19LHsibiI6IkVtcGxlYWRvcyIsInUiOiIjIiwiaSI6ImZhIGZhLXVzZXJzIiwiYyI6W3sibiI6IkFkbWluaXN0cmF0aXZvcyIsInUiOiJlbXBsb3llZS1wcm9maWxlcy1hZG0iLCJpIjoiZmEgZmEtYWRkcmVzcy1jYXJkIiwiYyI6bnVsbH0seyJuIjoiT3BlcmF0aXZvcyIsInUiOiJlbXBsb3llZS1wcm9maWxlcy1vcGVyIiwiaSI6ImZhIGZhLXVzZXItY2lyY2xlIiwiYyI6bnVsbH1dfSx7Im4iOiJNb2R1bG9zIiwidSI6Im1vZHVsZXMiLCJpIjoiZmEgZmEtdGFza3MiLCJjIjpbXX0seyJuIjoiUHJvc3BlY3RvcyIsInUiOiIvcHJvc3BlY3QtcHJvZmlsZXMiLCJpIjoiZmEgZmEtdXNlcnMiLCJjIjpbXX0seyJuIjoiUm9sZXMiLCJ1IjoiYXV0aG9yaXRpZXMiLCJpIjoiZmEgZmEtYWRkcmVzcy1jYXJkIiwiYyI6W119LHsibiI6IlVzdWFyaW9zIiwidSI6InVzZXJzIiwiaSI6ImZhIGZhLXVzZXJzIiwiYyI6W119XSwiaWF0IjoxNjkyMzg3OTM2LCJleHAiOjE2OTIzOTUxMzZ9.gpwwwr8Wpmc8MKqiKjGV3fHZwwZCUKw2cxFESi4P9qA";
	
	private long idEdit = 1L;
	private long idError = 0;
	
	@Test
	public void listActive() {
		ResponseEntity<?> response = new RestBuilder<String>()
				.clazz(String.class)
				.basicAuthToken(token)
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
	public void page() {
		//?page=0&size=5&sort=
		ResponseEntity<?> response = new RestBuilder<String>()
				.clazz(String.class)
				.basicAuthToken(token)
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
	public void formEdit() {
		ResponseEntity<?> response = new RestBuilder<String>()
				.clazz(String.class)
				.basicAuthToken(token)
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
	public void error() {
		//Falla recupera BADREQUEST
		//Throwable exception = 
		assertThrows(IllegalArgumentException.class, () -> {
	    	new RestBuilder<String>()
			.clazz(String.class)
			.basicAuthToken(token)
			.path(BranchOfficeController.URLENDPOINT)
			.path(BranchOfficeController.ERROR)
			.path(BranchOfficeController.ID)
			.expand(this.idError)
			.get().builder();
	    });
		
		//Assert.assertEquals("Username cannot be blank", exception.getMessage());
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
