package br.com.ifind.integrationtests.controller.withjson;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.ifind.configs.TestConfigs;
import br.com.ifind.data.vo.v1.security.TokenVO;
import br.com.ifind.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.ifind.integrationtests.vo.AccountCredentialsVO;
import br.com.ifind.integrationtests.vo.PersonVO;
import br.com.ifind.integrationtests.vo.wrappers.WrapperPersonVO;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class PersonControllerJsonTest extends AbstractIntegrationTest {
	
	private static RequestSpecification specification;
	private static ObjectMapper objectMapper;
	
	private static PersonVO person;
	
	private Logger logger = Logger.getLogger(PersonControllerJsonTest.class.getName());
	
	@BeforeAll
	public static void setup() {
		objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES); //desabilitar falhas quando receber o json, ele virá com campos hateos, e o VO não os reconhece
		
		person = new PersonVO();
	}
	
	@Test
	@Order(0)
	public void authorization() throws JsonMappingException, JsonProcessingException {
		AccountCredentialsVO user = new AccountCredentialsVO("leandro", "admin123");
		
		var accessToken = given()
				.basePath("/auth/signin")
					.port(TestConfigs.SERVER_PORT)
					.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.body(user)
					.when()
				.post()
					.then()
						.statusCode(200)
							.extract()
							.body()
								.as(TokenVO.class)
							.getAccessToken();
		
		specification = new RequestSpecBuilder()
				.addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + accessToken)
				.setBasePath("/api/person/v1")
				.setPort(TestConfigs.SERVER_PORT)
					.addFilter(new RequestLoggingFilter(LogDetail.ALL))
					.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
	}
	
	@Test
	@Order(1)
	public void testCreate() throws JsonMappingException, JsonProcessingException {
		mockPerson();
		logger.info("TEST CREATE person: " + person);
		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
					.body(person)
					.when()
					.post()
				.then()
					.statusCode(200)
				.extract()
					.body()
						.asString();
		
		logger.info("TEST CREATE " + content);
		
		PersonVO persistedPerson = objectMapper.readValue(content, PersonVO.class);
		logger.info("TEST CREATE persistedperson: " + persistedPerson);
		person = persistedPerson;
		
		assertNotNull(persistedPerson);
		assertNotNull(persistedPerson.getId());
		assertNotNull(persistedPerson.getFirstName());
		assertNotNull(persistedPerson.getLastName());
		assertNotNull(persistedPerson.getAddress());
		assertNotNull(persistedPerson.getGender());
		assertTrue(persistedPerson.getEnabled());
		
		assertTrue(persistedPerson.getId() > 0);
		
		assertEquals("Tony", persistedPerson.getFirstName());
		assertEquals("Fontana", persistedPerson.getLastName());
		assertEquals("Erechim - RS", persistedPerson.getAddress());
		assertEquals("Male", persistedPerson.getGender());
	}
	
	@Test
	@Order(2)
	public void testUpdate() throws JsonMappingException, JsonProcessingException {
		person.setLastName("Montana");
		
		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
					.body(person)
					.when()
					.post()
				.then()
					.statusCode(200)
					.extract()
					.body()
						.asString();
		
		PersonVO persistedPerson = objectMapper.readValue(content, PersonVO.class);
		person = persistedPerson;
		
		assertNotNull(persistedPerson);
		assertNotNull(persistedPerson.getId());
		assertNotNull(persistedPerson.getFirstName());
		assertNotNull(persistedPerson.getLastName());
		assertNotNull(persistedPerson.getAddress());
		assertNotNull(persistedPerson.getGender());
		assertTrue(persistedPerson.getEnabled());
		
		assertEquals(person.getId(), persistedPerson.getId());
		
		assertEquals("Tony", persistedPerson.getFirstName());
		assertEquals("Montana", persistedPerson.getLastName());
		assertEquals("Erechim - RS", persistedPerson.getAddress());
		assertEquals("Male", persistedPerson.getGender());
	}
	
	@Test
	@Order(3)
	public void testDisabledPersonById() throws JsonMappingException, JsonProcessingException {
		
		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
					.pathParam("id", person.getId())
					.when()
					.patch("{id}")
				.then()
					.statusCode(200)
				.extract()
					.body()
						.asString();
		
		PersonVO persistedPerson = objectMapper.readValue(content, PersonVO.class);
		person = persistedPerson;
		
		assertNotNull(persistedPerson);
		assertNotNull(persistedPerson.getId());
		assertNotNull(persistedPerson.getFirstName());
		assertNotNull(persistedPerson.getLastName());
		assertNotNull(persistedPerson.getAddress());
		assertNotNull(persistedPerson.getGender());
		assertFalse(persistedPerson.getEnabled());

		assertEquals(person.getId(), persistedPerson.getId());
		
		assertEquals("Tony", persistedPerson.getFirstName());
		assertEquals("Montana", persistedPerson.getLastName());
		assertEquals("Erechim - RS", persistedPerson.getAddress());
		assertEquals("Male", persistedPerson.getGender());
	}
	
	@Test
	@Order(4)
	public void testFindById() throws JsonMappingException, JsonProcessingException {
		mockPerson();
		
		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
					.pathParam("id", person.getId())
					.when()
					.get("{id}")
				.then()
					.statusCode(200)
				.extract()
					.body()
						.asString();
		
		PersonVO persistedPerson = objectMapper.readValue(content, PersonVO.class);
		person = persistedPerson;
		
		assertNotNull(persistedPerson);
		assertNotNull(persistedPerson.getId());
		assertNotNull(persistedPerson.getFirstName());
		assertNotNull(persistedPerson.getLastName());
		assertNotNull(persistedPerson.getAddress());
		assertNotNull(persistedPerson.getGender());
		assertFalse(persistedPerson.getEnabled());

		assertEquals(person.getId(), persistedPerson.getId());
		
		assertEquals("Tony", persistedPerson.getFirstName());
		assertEquals("Montana", persistedPerson.getLastName());
		assertEquals("Erechim - RS", persistedPerson.getAddress());
		assertEquals("Male", persistedPerson.getGender());
	}
	
	@Test
	@Order(5)
	public void testFindAll() throws JsonMappingException, JsonProcessingException {
		
		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.queryParams("page",3, "size", 10, "direction", "asc")
					.when()
					.get()
				.then()
					.statusCode(200)
				.extract()
					.body()
						.asString();
		
		WrapperPersonVO wrapper = objectMapper.readValue(content, WrapperPersonVO.class);
		var people = wrapper.getEmbedded().getPersons();
						
		PersonVO foundPersonONe = people.get(0);
		person = foundPersonONe;
		
		assertNotNull(foundPersonONe.getId());
		assertNotNull(foundPersonONe.getFirstName());
		assertNotNull(foundPersonONe.getLastName());
		assertNotNull(foundPersonONe.getAddress());
		assertNotNull(foundPersonONe.getGender());
		assertEquals(672, foundPersonONe.getId());
		
		assertEquals("Alic", foundPersonONe.getFirstName());
		assertEquals("Terbrug", foundPersonONe.getLastName());
		assertEquals("3 Eagle Crest Court", foundPersonONe.getAddress());
		assertEquals("Male", foundPersonONe.getGender());
		assertTrue(foundPersonONe.getEnabled());
		
		PersonVO foundPersonSix = people.get(3);
		person = foundPersonSix;
		
		assertNotNull(foundPersonSix.getId());
		assertNotNull(foundPersonSix.getFirstName());
		assertNotNull(foundPersonSix.getLastName());
		assertNotNull(foundPersonSix.getAddress());
		assertNotNull(foundPersonSix.getGender());
		assertEquals(404, foundPersonSix.getId());
		
		assertEquals("Alister", foundPersonSix.getFirstName());
		assertEquals("Etheridge", foundPersonSix.getLastName());
		assertEquals("333 Lakewood Gardens Street", foundPersonSix.getAddress());
		assertEquals("Male", foundPersonSix.getGender());
		assertFalse(foundPersonSix.getEnabled());
	}
	
	@Test
	@Order(6)
	public void testDelete() throws JsonMappingException, JsonProcessingException {
		
		given().spec(specification)
			.contentType(TestConfigs.CONTENT_TYPE_JSON)
			.pathParam("id", person.getId())
				.when()
				.delete("{id}")
			.then()
				.statusCode(204)
				.extract()
				.body()
			.asString();
	}
	
	@Test
	@Order(7)
	public void testFindAllWithoutToken() throws JsonMappingException, JsonProcessingException {
		
		RequestSpecification specificationWhitoutToken = new RequestSpecBuilder()
			.setBasePath("/api/person/v1")
			.setPort(TestConfigs.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
			.build();
	
	
	 	given().spec(specificationWhitoutToken)
			.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.when()
				.get()
			.then()
				.statusCode(403)
			.extract()
				.body()
					.asString();
	}
	
	@Test
	@Order(8)
	public void testFindByName() throws JsonMappingException, JsonProcessingException {
		
		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.pathParam("firstName", "Marietta")
				.queryParams("page",0, "size", 10, "direction", "asc")
					.when()
					.get("findPersonsByName/{firstName}")
				.then()
					.statusCode(200)
				.extract()
					.body()
						.asString();
		
		WrapperPersonVO wrapper = objectMapper.readValue(content, WrapperPersonVO.class);
		var people = wrapper.getEmbedded().getPersons();
						
		PersonVO foundPersonONe = people.get(0);
		person = foundPersonONe;
		
		assertNotNull(foundPersonONe.getId());
		assertNotNull(foundPersonONe.getFirstName());
		assertNotNull(foundPersonONe.getLastName());
		assertNotNull(foundPersonONe.getAddress());
		assertNotNull(foundPersonONe.getGender());
		assertEquals(72, foundPersonONe.getId());
		
		assertEquals("Marietta", foundPersonONe.getFirstName());
		assertEquals("Flello", foundPersonONe.getLastName());
		assertEquals("021 Onsgard Plaza", foundPersonONe.getAddress());
		assertEquals("Male", foundPersonONe.getGender());
		assertFalse(foundPersonONe.getEnabled());
	}
	
	@Test
	@Order(9)
	public void testHATEOS() throws JsonMappingException, JsonProcessingException {
		
		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.queryParams("page",3, "size", 10, "direction", "asc")
					.when()
					.get()
				.then()
					.statusCode(200)
				.extract()
					.body()
						.asString();
		
		assertTrue(content.contains("_links\":{\"self\":{\"href\":\"http://localhost:8888/api/person/v1/672\"}}}"));
		assertTrue(content.contains("_links\":{\"self\":{\"href\":\"http://localhost:8888/api/person/v1/409\"}}}"));
		assertTrue(content.contains("_links\":{\"self\":{\"href\":\"http://localhost:8888/api/person/v1/841\"}}}"));
		
		assertTrue(content.contains("\"first\":{\"href\":\"http://localhost:8888/api/person/v1?direction=asc&page=0&size=10&sort=firstName,asc\"}"));
		assertTrue(content.contains("\"prev\":{\"href\":\"http://localhost:8888/api/person/v1?direction=asc&page=2&size=10&sort=firstName,asc\"}"));
		assertTrue(content.contains("\"self\":{\"href\":\"http://localhost:8888/api/person/v1?page=3&size=10&direction=asc\"}"));
		assertTrue(content.contains("\"last\":{\"href\":\"http://localhost:8888/api/person/v1?direction=asc&page=100&size=10&sort=firstName,asc\"}}"));
		assertTrue(content.contains("\"next\":{\"href\":\"http://localhost:8888/api/person/v1?direction=asc&page=4&size=10&sort=firstName,asc\"}"));
	
		assertTrue(content.contains("\"page\":{\"size\":10,\"totalElements\":1004,\"totalPages\":101,\"number\":3}}"));
	}
	
	private void mockPerson() {
		person.setFirstName("Tony");
		person.setLastName("Fontana");
		person.setAddress("Erechim - RS");
		person.setGender("Male");
		person.setEnabled(true);
	}

}
