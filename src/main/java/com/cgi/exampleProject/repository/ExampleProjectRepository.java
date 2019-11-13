package com.cgi.exampleProject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cgi.exampleProject.domain.Example;

@Repository
public interface ExampleProjectRepository extends JpaRepository<Example, Integer> {

}
