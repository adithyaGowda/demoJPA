package com.cgi.exampleProject.service;

import java.util.List;
import java.util.Optional;

import com.cgi.exampleProject.domain.Example;
import com.cgi.exampleProject.exceptions.MemberAlreadyFoundException;
import com.cgi.exampleProject.exceptions.MemberNotFoundException;

public interface ExampleProjectService {

	Example saveMember(Example example) throws MemberAlreadyFoundException;

	Example updateMember(Example example) throws MemberNotFoundException;

	List<Example> getAllMembers() throws Exception;

	Optional<Example> deleteMember(Example example) throws MemberNotFoundException;

	Example getMemberById(int id) throws MemberNotFoundException;

	Optional<Example> deleteMemberById(int id) throws MemberNotFoundException;


}
