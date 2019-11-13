package com.cgi.exampleProject.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.cgi.exampleProject.domain.Example;
import com.cgi.exampleProject.exceptions.MemberAlreadyFoundException;
import com.cgi.exampleProject.exceptions.MemberNotFoundException;
import com.cgi.exampleProject.service.ExampleProjectService;

@RestController
@RequestMapping("api/v1")
public class ExampleController {

	private ExampleProjectService exampleProjectService;
	
	@Autowired
	public ExampleController(ExampleProjectService exampleProjectService) {
		this.exampleProjectService = exampleProjectService;
	}
	
	@PostMapping("post")
	public ResponseEntity<?> addMember(@RequestBody Example example) throws Exception, MemberAlreadyFoundException{
		
      return  new ResponseEntity<Example>(exampleProjectService.saveMember(example), HttpStatus.CREATED);
	}
	
	@GetMapping("get")
	public ResponseEntity<?> getAll() throws Exception {
  
        return new ResponseEntity<List<Example>>(exampleProjectService.getAllMembers(),HttpStatus.OK);
	}
	
	@PutMapping("update")
	public ResponseEntity<?> updateMember(@RequestBody Example example) throws Exception, MemberNotFoundException{
		
		return  new ResponseEntity<Example>(exampleProjectService.updateMember(example),HttpStatus.OK);
	}
	
	@DeleteMapping("delete")
	public ResponseEntity<?> deleteMember(@RequestBody Example example) throws Exception, MemberNotFoundException{
				
		return  new ResponseEntity<Optional<Example>>(exampleProjectService.deleteMember(example),HttpStatus.OK);
		
	}
	
	
	
	@GetMapping("get/{id}")
	public ResponseEntity<?> getById(@PathVariable int id) throws Exception, MemberNotFoundException{
		
		return new ResponseEntity<Example>(exampleProjectService.getMemberById(id),HttpStatus.OK);
	
	}
	
		
	@DeleteMapping("delete/{id}")
	public ResponseEntity<?> deleteById(@PathVariable int id) throws Exception, MemberNotFoundException{
		
		return new ResponseEntity<Optional<Example>>(exampleProjectService.deleteMemberById(id),HttpStatus.OK);
	}
	
	
	
	
	
	
	
	
	
	
}
