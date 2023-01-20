 package br.com.ifind.unittests.mockito.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.ifind.data.vo.v1.PersonVO;
import br.com.ifind.exceptions.RequiredObjectIsNullException;
import br.com.ifind.model.Person;
import br.com.ifind.repositories.PersonRepository;
import br.com.ifind.services.PersonServices;
import br.com.ifind.unittests.mapper.mocks.MockPerson;

@TestInstance(Lifecycle.PER_CLASS) // um instancia por classe
@ExtendWith(MockitoExtension.class)
class PersonServicesTest {
	
	MockPerson input;
	
	@InjectMocks
	private PersonServices service; //injetar os mocks para o repository em personservices, 
	
	@Mock
	PersonRepository repository;
	
	@BeforeEach
	void setUpMocks() throws Exception {
		input = new MockPerson();
		MockitoAnnotations.openMocks(this);
	}
	
	
	@Test
	void testFindById() throws Exception {
		Person entity = input.mockEntity(1);
		entity.setId(1L);
		
		when(repository.findById(1L)).thenReturn(Optional.of(entity));
		
		var result = service.findById(1L);
		assertNotNull(result);
		assertNotNull(result.getKey());
		assertNotNull(result.getLinks());
		
		System.out.println(result.toString());
		assertTrue(result.toString().contains("links: [</api/person/v1/1>;rel=\"self\"]"));
		assertEquals("Addres Test1",result.getAddress());
		assertEquals("First Name Test1",result.getFirstName());
		assertEquals("Last Name Test1",result.getLastName());
		assertEquals("Female",result.getGender());
	}

	@Test
	void testCreate() throws Exception {		
		Person entity = input.mockEntity(1);
		Person persisted = entity;		
		persisted.setId(1L);
		
		PersonVO vo = input.mockVO(1);
		vo.setKey(1L);
		
		when(repository.save(entity)).thenReturn(persisted);
		
		var result = service.create(vo);
		assertNotNull(result);
		assertNotNull(result.getKey());
		assertNotNull(result.getLinks());
		assertTrue(result.toString().contains("links: [</api/person/v1/1>;rel=\"self\"]"));
		
		assertEquals("Addres Test1",result.getAddress());
		assertEquals("First Name Test1",result.getFirstName());
		assertEquals("Last Name Test1",result.getLastName());
		assertEquals("Female",result.getGender());
	}
	
	@Test
	void testCreateWIthNullPerson() throws Exception {		
		Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
			service.create(null);
		});
		
		String expectedMessage = "It's not allowed to persist an null object";
		String actualMessage = exception.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testUpdate() throws Exception {
		Person entity = input.mockEntity(1);
		entity.setId(1L);
		
		Person persisted = entity;		
		persisted.setId(1L);
		
		PersonVO vo = input.mockVO(1);
		vo.setKey(1L);
		
		when(repository.findById(1L)).thenReturn(Optional.of(entity));
		when(repository.save(entity)).thenReturn(persisted);
		
		var result = service.update(vo);
		assertNotNull(result);
		assertNotNull(result.getKey());
		assertNotNull(result.getLinks());
		assertTrue(result.toString().contains("links: [</api/person/v1/1>;rel=\"self\"]"));
		
		assertEquals("Addres Test1",result.getAddress());
		assertEquals("First Name Test1",result.getFirstName());
		assertEquals("Last Name Test1",result.getLastName());
		assertEquals("Female",result.getGender());
	}
	
	@Test
	void testUpdateWIthNullPerson() throws Exception {		
		Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
			service.update(null);
		});
		
		String expectedMessage = "It's not allowed to persist an null object";
		String actualMessage = exception.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));
	}
	
	@Test
	void testDelete() throws Exception {
		Person entity = input.mockEntity(1);
		entity.setId(1L);
		
		when(repository.findById(1L)).thenReturn(Optional.of(entity));
		
		service.delete(1L);
	}

}
