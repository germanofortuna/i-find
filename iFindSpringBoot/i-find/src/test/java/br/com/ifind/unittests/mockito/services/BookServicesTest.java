package br.com.ifind.unittests.mockito.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.List;
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

import br.com.ifind.data.vo.v1.BookVO;
import br.com.ifind.exceptions.RequiredObjectIsNullException;
import br.com.ifind.model.Book;
import br.com.ifind.repositories.BookRepository;
import br.com.ifind.services.BookServices;
import br.com.ifind.unittests.mapper.mocks.MockBook;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class BookServicesTest {
	
	MockBook input;
	
	@InjectMocks
	private BookServices service;
	
	@Mock
	BookRepository repository;
	
	@BeforeEach
	void setUpMocks() throws Exception {
		input = new MockBook();
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	void testFindAll() throws Exception {
		List<Book> list = input.mockEntityList();
		
		when(repository.findAll()).thenReturn(list);
		
		var books = service.findAll();
		assertNotNull(books);
		assertEquals(14, books.size());
		
		var bookOne = books.get(1);
		
		assertTrue(bookOne.toString().contains("links: [</api/book/v1/1>;rel=\"self\"]"));
		assertEquals("Autor1", bookOne.getAuthor());
		assertNotNull(bookOne.getLaunchDate());
		assertEquals(1.0, bookOne.getPrice());
		assertEquals("Title1", bookOne.getTitle());
		
		var bookFive = books.get(5);
		assertTrue(bookFive.toString().contains("links: [</api/book/v1/5>;rel=\"self\"]"));
		assertEquals("Autor5", bookFive.getAuthor());
		assertNotNull(bookOne.getLaunchDate());
		assertEquals(5.0, bookFive.getPrice());
		assertEquals("Title5", bookFive.getTitle());
	}
	
	@Test
	void testFindById() throws Exception {
		Book entity = input.mockEntity(1);
		entity.setId(1L);
		
		when(repository.findById(1L)).thenReturn(Optional.of(entity));
		
		var result = service.findById(1L);
		assertNotNull(result);
		assertNotNull(result.getKey());
		assertNotNull(result.getLinks());
		
		assertTrue(result.toString().contains("links: [</api/book/v1/1>;rel=\"self\"]"));
		assertEquals("Autor1", result.getAuthor());
		assertNotNull(result.getLaunchDate());
		assertEquals(1.0, result.getPrice());
		assertEquals("Title1", result.getTitle());
	}
	
	@Test
	void testCreate() throws Exception {
		Book entity = input.mockEntity(1);
		Book persisted = entity;
		persisted.setId(1L);
		
		BookVO vo = input.mockVO(1);
		vo.setKey(1L);
		
		when(repository.save(entity)).thenReturn(persisted);
		
		var result = service.create(vo);
		assertNotNull(result);
		assertNotNull(result.getKey());
		assertNotNull(result.getLinks());
		
		assertTrue(result.toString().contains("links: [</api/book/v1/1>;rel=\"self\"]"));
		assertEquals("Autor1", result.getAuthor());
		assertNotNull(result.getLaunchDate());
		assertEquals(1.0, result.getPrice());
		assertEquals("Title1", result.getTitle());
	}
	
	@Test
	void testUpdate() throws Exception {
		Book entity = input.mockEntity(1);
		entity.setId(1L);
		
		Book persisted = entity;
		persisted.setId(1L);
		
		BookVO vo = input.mockVO(1);
		vo.setKey(1L);
		
		when(repository.findById(1L)).thenReturn(Optional.of(entity));
		when(repository.save(entity)).thenReturn(persisted);
		
		var result = service.update(vo);
		assertNotNull(result);
		assertNotNull(result.getKey());
		assertNotNull(result.getLinks());
		
		assertTrue(result.toString().contains("links: [</api/book/v1/1>;rel=\"self\"]"));
		assertEquals("Autor1", result.getAuthor());
		assertNotNull(result.getLaunchDate());
		assertEquals(1.0, result.getPrice());
		assertEquals("Title1", result.getTitle());
	}
	
	@Test
	void testCreateWIthNullBook() throws Exception {		
		Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
			service.create(null);
		});
		
		String expectedMessage = "It's not allowed to persist an null object";
		String actualMessage = exception.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));
	}
	
	@Test
	void testUpdateWIthNullBook() throws Exception {		
		Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
			service.update(null);
		});
		
		String expectedMessage = "It's not allowed to persist an null object";
		String actualMessage = exception.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));
	}
	
	@Test
	void testDelete() throws Exception {
		Book entity = input.mockEntity(1);
		entity.setId(1L);
		
		when(repository.findById(1L)).thenReturn(Optional.of(entity));
		
		service.delete(1L);
	}
}
