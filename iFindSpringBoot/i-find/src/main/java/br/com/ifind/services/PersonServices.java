package br.com.ifind.services;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ifind.data.vo.v1.PersonVO;
import br.com.ifind.data.vo.v2.PersonVOV2;
import br.com.ifind.exceptions.ResourceNotFoundException;
import br.com.ifind.mapper.DozerMapper;
import br.com.ifind.mapper.custom.PersonMapper;
import br.com.ifind.model.Person;
import br.com.ifind.repositories.PersonRepository;

//a anotação @Service serve para que o SpringBoot identifique a classe como um objecto que será 
//injetado em RunTime nas outras classes da aplicação, substituindo o "new" da instanciação

@Service
public class PersonServices {
	
	private Logger logger = Logger.getLogger(PersonServices.class.getName());
	
	@Autowired
	PersonRepository repository;
	
	@Autowired
	PersonMapper mapper;
	
	public List<PersonVO> findAll() {
		logger.info("Searching All people!");
		
		return DozerMapper.parseListObjects(repository.findAll(), PersonVO.class);
	}

	public PersonVO findById(Long id) {
		logger.info("Searching one person!");
		
		var entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this id"));
		return DozerMapper.parseObject(entity, PersonVO.class);
	}
	
	public PersonVO create(PersonVO person) {
		logger.info("Creating person!");
		var entity = DozerMapper.parseObject(person, Person.class);
		var vo = DozerMapper.parseObject(repository.save(entity), PersonVO.class);
		return vo;
	}

	public PersonVOV2 createV2(PersonVOV2 person) {
		logger.info("Creating person with V2!");
		var entity = mapper.convertVoTOEntity(person);
		var vo = mapper.convertEntityToVo(repository.save(entity));
		return vo;
	}
	
	public PersonVO update(PersonVO person) {
		logger.info("Updating person!");
		
		var entity = repository.findById(person.getId()).orElseThrow(() -> new ResourceNotFoundException("No records found for this id"));
		
		entity.setFirstName(person.getFirstName());
		entity.setLastName(person.getLastName());
		entity.setAddress(person.getAddress());
		entity.setGender(person.getGender());
		
		var vo = DozerMapper.parseObject(repository.save(entity), PersonVO.class);
		return vo;
	}

	public void delete(Long id) {
		logger.info("Deleting person!");
		
		var entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this id"));
		
		repository.delete(entity);
	}
	
}
