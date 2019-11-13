package com.cgi.exampleProject.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cgi.exampleProject.domain.Example;
import com.cgi.exampleProject.exceptions.MemberAlreadyFoundException;
import com.cgi.exampleProject.exceptions.MemberNotFoundException;
import com.cgi.exampleProject.repository.ExampleProjectRepository;

@Service
public class ExampleProjectServiceImpl implements ExampleProjectService {
	
	private ExampleProjectRepository exampleProjectRepository;
	
	@Autowired
	public ExampleProjectServiceImpl(ExampleProjectRepository exampleProjectRepository) {
		this.exampleProjectRepository = exampleProjectRepository;
	}
	


	public Example saveMember(Example example) throws MemberAlreadyFoundException{

		  if (exampleProjectRepository.existsById(example.getId())) {
	            throw new MemberAlreadyFoundException();
	        }
		  
	        Example savedMember = exampleProjectRepository.save(example);
	        
	        if (savedMember == null) {
	            throw new MemberAlreadyFoundException();
	        }
	        
	        return savedMember;
		
	}

	public Example updateMember(Example example) throws MemberNotFoundException {
		
		Example update;
		
		if (exampleProjectRepository.existsById(example.getId())) {
			update = exampleProjectRepository.findById(example.getId()).get();
			update.setName(example.getName());
			update.setSalary(example.getSalary());
		}
		else {
			throw new MemberNotFoundException("Member you want to update is not found");
		}
		
		
		return exampleProjectRepository.save(update);
		
	}

	public List<Example> getAllMembers() throws Exception {
		
		return exampleProjectRepository.findAll();
	}
	
	

	public Optional<Example> deleteMember(Example example) throws MemberNotFoundException {

	    Optional<Example> optional = exampleProjectRepository.findById(example.getId());

        if (!optional.isPresent()) {
        	
        	throw new MemberNotFoundException("Member you want to delete is not found");
        }
        
        exampleProjectRepository.deleteById(example.getId());
        return optional;
	}

	

	public Example getMemberById(int id) throws MemberNotFoundException {
		
		
		if (exampleProjectRepository.existsById(id)) {
			
			return exampleProjectRepository.findById(id).get();
		}
		else {
			throw new MemberNotFoundException("Member you want to get is not found");
		}
		
	}



	public Optional<Example> deleteMemberById(int id) throws MemberNotFoundException {
	    Optional<Example> optional = exampleProjectRepository.findById(id);

        if (!optional.isPresent()) {
        	
        	throw new MemberNotFoundException("Member you want to delete is not found");
        }
        
        exampleProjectRepository.deleteById(id);
        return optional;
	}


}
