package br.com.ifind.unittests.mapper.mocks;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.ifind.data.vo.v1.BookVO;
import br.com.ifind.model.Book;

public class MockBook {
	
	public Book mockEntity() {
		return mockEntity(0);
	}
	
	public BookVO mockVO() {
		return mockVO(0);
	}
	
	public List<Book> mockEntityList() {
		List<Book> books = new ArrayList<Book>();
		for (int i = 0; i < 14; i++) {
			books.add(mockEntity(i));
		}
		return books;
	}
	
	public List<BookVO> mockVOList() {
		List<BookVO> books = new ArrayList<BookVO>();
		for (int i = 0; i < 14; i++) {
			books.add(mockVO(i));
		}
		return books;
	}
	
	public Book mockEntity(Integer number) {
		Book book = new Book();
		book.setId(number.longValue());
		book.setAuthor("Autor" + number);
		book.setLaunchDate(new Date());
		book.setPrice(number.doubleValue());
		book.setTitle("Title" + number);
		return book;
	}
	
	public BookVO mockVO(Integer number) {
		BookVO book = new BookVO();
		book.setKey(number.longValue());
		book.setAuthor("Autor" + number);
		book.setLaunchDate(new Date());
		book.setPrice(number.doubleValue());
		book.setTitle("Title" + number);
		return book;
	}
}
