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

import br.com.ifind.controllers.PersonController;
import br.com.ifind.data.vo.v1.PersonVO;
import br.com.ifind.data.vo.v2.PersonVOV2;
import br.com.ifind.exceptions.RequiredObjectIsNullException;
import br.com.ifind.exceptions.ResourceNotFoundException;
import br.com.ifind.mapper.DozerMapper;
import br.com.ifind.mapper.custom.PersonMapper;
import br.com.ifind.model.Person;
import br.com.ifind.repositories.PersonRepository;
import jakarta.transaction.Transactional;

//a anotação @Service serve para que o SpringBoot identifique a classe como um objecto que será 
//injetado em RunTime nas outras classes da aplicação, substituindo o "new" da instanciação

@Service
public class PersonServices {
	
	private Logger logger = Logger.getLogger(PersonServices.class.getName());
	
	@Autowired
	PersonRepository repository;

	@Autowired
	PagedResourcesAssembler<PersonVO> assembler;
	
	@Autowired
	PersonMapper mapper;
	
	public PagedModel<EntityModel<PersonVO>> findAll(Pageable pageable) {
		logger.info("Searching All people!");
		
		var personPage = repository.findAll(pageable);
		
		var personsVosPage = personPage.map(p -> DozerMapper.parseObject(p, PersonVO.class));
		
		personsVosPage.map(p -> 
				{
					try {
						return p.add(linkTo(methodOn(PersonController.class)
								.findById(p.getKey())).withSelfRel());
					} catch (Exception e) {
						e.printStackTrace();
					}
					return p;
				});
		
		Link link = linkTo(methodOn(PersonController.class)
				.findAll(pageable.getPageNumber(), 
						pageable.getPageSize(), 
						"asc")).withSelfRel();
		return assembler.toModel(personsVosPage, link);
	}

	public PagedModel<EntityModel<PersonVO>> findPersonsByName(String firstName, Pageable pageable) {
		logger.info("Searching people by name!");
		
		var personPage = repository.findPersonsByName(firstName, pageable);
		
		var personsVosPage = personPage.map(p -> DozerMapper.parseObject(p, PersonVO.class));
		
		personsVosPage.map(p -> 
		{
			try {
				return p.add(linkTo(methodOn(PersonController.class)
						.findById(p.getKey())).withSelfRel());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return p;
		});
		
		Link link = linkTo(methodOn(PersonController.class)
				.findAll(pageable.getPageNumber(), 
						pageable.getPageSize(), 
						"asc")).withSelfRel();
		return assembler.toModel(personsVosPage, link);
	}

	public PersonVO findById(Long id) throws Exception {
		logger.info("Searching one person!");
		
		var entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this id"));
		var vo = DozerMapper.parseObject(entity, PersonVO.class);
		vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
		logger.info("WOWOW " + vo.toString());
		return vo;
	}
	
	public PersonVO create(PersonVO person) throws Exception {
		logger.info("Creating person!");
		
		if(person == null) throw new RequiredObjectIsNullException();
		
		var entity = DozerMapper.parseObject(person, Person.class);
		var vo = DozerMapper.parseObject(repository.save(entity), PersonVO.class);
		vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
		return vo;
	}

	public PersonVOV2 createV2(PersonVOV2 person) {
		logger.info("Creating person with V2!");
		var entity = mapper.convertVoTOEntity(person);
		var vo = mapper.convertEntityToVo(repository.save(entity));
		return vo;
	}
	
	public PersonVO update(PersonVO person) throws Exception {
		logger.info("Updating person!");
		
		if(person == null) throw new RequiredObjectIsNullException();
		
		var entity = repository.findById(person.getKey()).orElseThrow(() -> new ResourceNotFoundException("No records found for this id"));
		
		entity.setFirstName(person.getFirstName());
		entity.setLastName(person.getLastName());
		entity.setAddress(person.getAddress());
		entity.setGender(person.getGender());
		
		var vo = DozerMapper.parseObject(repository.save(entity), PersonVO.class);
		vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
		return vo;
	}
	
	/* como não é uma operação default do Spring, 
	 * é necessário adicionar essa @, 
	 * pois o repository foi customizado 
	 * com uma query de update. Basicamente essa @ informa
	 * ao Spring que todos os conceitos de transação (ACID) 
	 * devem ser empregados nos beans que estejam anotados com @Transactional. */
	@Transactional 
	public PersonVO disablePerson(Long id) throws Exception {
		logger.info("Disabling one person!");
		
		repository.disablePerson(id);
		
		var entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this id"));
		var vo = DozerMapper.parseObject(entity, PersonVO.class);
		vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
		return vo;
	}

	public void delete(Long id) {
		logger.info("Deleting person!");
		
		var entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this id"));
		
		repository.delete(entity);
	}
	
}
