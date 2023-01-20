package br.com.ifind.integrationtests.repositories;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import br.com.ifind.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.ifind.model.Person;
import br.com.ifind.repositories.PersonRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) //não altera o banco
@TestMethodOrder(OrderAnnotation.class)
public class PersonRepositoryTest extends AbstractIntegrationTest {
	
	@Autowired
	public PersonRepository repository;
	
	private static Person person; //precisa ser a entidade
	
	@BeforeAll
	public static void setup() {
		person = new Person();
	}
	
	@Test
	@Order(0)
	public void testFindByName() throws JsonMappingException, JsonProcessingException {
		
		Pageable pageable = PageRequest.of(0, 10, Sort.by(Direction.ASC, "firstName"));
		person =  repository.findPersonsByName("Marietta", pageable).getContent().get(0);
		
		assertNotNull(person.getId());
		assertNotNull(person.getFirstName());
		assertNotNull(person.getLastName());
		assertNotNull(person.getAddress());
		assertNotNull(person.getGender());
		assertEquals(72, person.getId());
		
		assertEquals("Marietta", person.getFirstName());
		assertEquals("Flello", person.getLastName());
		assertEquals("021 Onsgard Plaza", person.getAddress());
		assertEquals("Male", person.getGender());
		assertFalse(person.getEnabled());
	}
	
	@Test
	@Order(2)
	public void testDisabledPersonById() throws JsonMappingException, JsonProcessingException {
		
		repository.disablePerson(person.getId());
		Pageable pageable = PageRequest.of(0, 10, Sort.by(Direction.ASC, "firstName"));
		person =  repository.findPersonsByName("Marietta", pageable).getContent().get(0);
		
		assertNotNull(person);
		assertNotNull(person.getId());
		assertNotNull(person.getFirstName());
		assertNotNull(person.getLastName());
		assertNotNull(person.getAddress());
		assertNotNull(person.getGender());
		assertFalse(person.getEnabled());

		assertEquals(72, person.getId());
		
		assertEquals("Marietta", person.getFirstName());
		assertEquals("Flello", person.getLastName());
		assertEquals("021 Onsgard Plaza", person.getAddress());
		assertEquals("Male", person.getGender());
		assertFalse(person.getEnabled());
	}
}
