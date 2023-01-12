package br.com.ifind.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.ifind.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>{}
