package br.com.ifind.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.ifind.model.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long>{}
