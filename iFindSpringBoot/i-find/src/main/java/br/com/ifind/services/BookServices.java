package br.com.ifind.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import br.com.ifind.controllers.BookControler;
import br.com.ifind.data.vo.v1.BookVO;
import br.com.ifind.exceptions.RequiredObjectIsNullException;
import br.com.ifind.exceptions.ResourceNotFoundException;
import br.com.ifind.mapper.DozerMapper;
import br.com.ifind.model.Book;
import br.com.ifind.repositories.BookRepository;

@Service
public class BookServices {
	
	private Logger logger = Logger.getLogger(PersonServices.class.getName());
	
	@Autowired
	BookRepository repository;
	
	@Autowired
	PagedResourcesAssembler<BookVO> assembler;
	
	public PagedModel<EntityModel<BookVO>> findAll(Pageable pageable) {
		logger.info("Searching for all books!");
		
		var bookPage = repository.findAll(pageable);
		
		var bookVosPage = bookPage.map(p -> DozerMapper.parseObject(p, BookVO.class));
		
		bookVosPage.map(p -> 
				{
					try {
						return p.add(linkTo(methodOn(BookControler.class)
								.findById(p.getKey())).withSelfRel());
					} catch (Exception e) {
						e.printStackTrace();
					}
					return p;
				});
		
		Link link = linkTo(methodOn(BookControler.class)
				.findAll(pageable.getPageNumber(), 
						pageable.getPageSize(), 
						"asc")).withSelfRel();
		return assembler.toModel(bookVosPage, link);
	}
	
	public BookVO findById(Long id) throws Exception {
		logger.info("Searching one book by id");
		
		var entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records foudn for this id"));
		var vo = DozerMapper.parseObject(entity, BookVO.class);
		vo.add(linkTo(methodOn(BookControler.class).findById(id)).withSelfRel());
		return vo;
	}
	
	public BookVO create(BookVO book) throws Exception {
		logger.info("Creating a book");
		
		if(book == null) throw new RequiredObjectIsNullException();
		
		var entity = DozerMapper.parseObject(book, Book.class);
		var vo = DozerMapper.parseObject(repository.save(entity), BookVO.class);
		vo.add(linkTo(methodOn(BookControler.class).findById(vo.getKey())).withSelfRel());
		return vo;
	}
	
	public BookVO update(BookVO book) throws Exception {
		logger.info("Updating person!");
		
		if(book == null) throw new RequiredObjectIsNullException();
		
		var entity = repository.findById(book.getKey()).orElseThrow(() -> new ResourceNotFoundException("No records found for this id"));
		
		entity.setAuthor(book.getAuthor());
		entity.setLaunchDate(book.getLaunchDate());
		entity.setPrice(book.getPrice());
		entity.setTitle(book.getTitle());
		
		var vo = DozerMapper.parseObject(repository.save(entity), BookVO.class);
		vo.add(linkTo(methodOn(BookControler.class).findById(vo.getKey())).withSelfRel());
		return vo;
	}
	
	public void delete(Long id) {
		logger.info("Deleting a book!");
		
		var entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this id"));
		
		repository.delete(entity);
	}
}
