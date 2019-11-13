package com.cgi.exampleProject.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.cgi.exampleProject.domain.Example;
import com.cgi.exampleProject.exceptions.GlobalException;
import com.cgi.exampleProject.exceptions.MemberAlreadyFoundException;
import com.cgi.exampleProject.exceptions.MemberNotFoundException;
import com.cgi.exampleProject.repository.ExampleProjectRepository;
import com.cgi.exampleProject.service.ExampleProjectService;
import com.cgi.exampleProject.service.ExampleProjectServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@WebMvcTest
public class ExampleControllerTests {

	@Autowired
	private MockMvc mockMvc;
	
	private Example example;
	private List<Example> list = null;
	
	@MockBean
	private ExampleProjectService exampleProjectService;
	
	@InjectMocks
	private ExampleController exampleController;
	
	@MockBean
	private ExampleProjectRepository exampleProjectRepository;
	
	@InjectMocks
	private ExampleProjectServiceImpl exampleProjectServiceImpl;
	
	
	@Before
	public void setUp() {
		
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(exampleController).setControllerAdvice(GlobalException.class).build();
		
		example = new Example();
		example.setId(100);
		example.setName("Member100");
		example.setSalary(1000.00);
		
		list = new ArrayList<Example>();
		list.add(example);
		
	}
	
    @After
    public void tearDown() {
        this.example = null;
        this.list = null;
    }
    
    @Test
    public void givenPostMappingUrlShouldReturnSavedMember() throws Exception {
    	
    	when(exampleProjectService.saveMember(any(Example.class))).thenReturn(example);
    	when(exampleProjectRepository.save(any(Example.class))).thenReturn(example);
    	
    	mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/post")
    			.contentType(MediaType.APPLICATION_JSON).content(asJsonString(example)))
    			.andExpect(MockMvcResultMatchers.status().isCreated())
    			.andDo(MockMvcResultHandlers.print());
    	
    	Example data = exampleProjectServiceImpl.saveMember(example);
    	assertEquals(example, data);
    	
    	verify(exampleProjectService, times(1)).saveMember(ArgumentMatchers.refEq(example));
    	verify(exampleProjectRepository, times(1)).save(example);
    	 
    }
    
    @Test( expected = MemberAlreadyFoundException.class)
    public void givenPostMappingUrlShouldThrowMemberAlreadyFoundException() throws Exception {

    	when(exampleProjectService.saveMember(any(Example.class))).thenThrow(MemberAlreadyFoundException.class);
    	when(exampleProjectRepository.existsById(example.getId())).thenReturn(true);
    	
    	mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/post")
    			.contentType(MediaType.APPLICATION_JSON).content(asJsonString(example)))
    			.andExpect(MockMvcResultMatchers.status().isConflict())
    			.andDo(MockMvcResultHandlers.print());
    	
    	exampleProjectServiceImpl.saveMember(example);
    	exampleProjectServiceImpl.saveMember(example);
    	
    	verify(exampleProjectService, times(0)).saveMember(example);
    	verify(exampleProjectRepository, times(1)).save(example);
    }
    
   
    @Test
    public void givenGetMappingUrlShouldReturnListOfAllMembers() throws Exception {
    	
    	when(exampleProjectService.getAllMembers()).thenReturn(list);
    	when(exampleProjectRepository.findAll()).thenReturn(list);
    	
    	mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/get")
    			.contentType(MediaType.APPLICATION_JSON))
    			.andExpect(MockMvcResultMatchers.status().isOk())
    			.andDo(MockMvcResultHandlers.print());
    	
    	List<Example> data = exampleProjectServiceImpl.getAllMembers();
    	assertEquals(list, data);
    	
    	verify(exampleProjectService, times(1)).getAllMembers();
    	verify(exampleProjectRepository, times(1)).findAll();
    }
    

    @Test
    public void givenGetMappingUrlWithIdShouldReturnMemberWithThatId() throws Exception {
    	
        when(exampleProjectService.getMemberById(anyInt())).thenReturn(example);
        when(exampleProjectRepository.existsById(example.getId())).thenReturn(true);
        when(exampleProjectRepository.findById(example.getId())).thenReturn(Optional.of(example));
        
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/get/100")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
        
        Example data = exampleProjectServiceImpl.getMemberById(100);
        assertEquals(example, data);
        
        verify(exampleProjectService, times(1)).getMemberById(example.getId());
        verify(exampleProjectRepository, times(1)).existsById(100);
        verify(exampleProjectRepository, times(1)).findById(100);
    }

    @Test( expected = MemberNotFoundException.class )
    public void givenGetMappingUrlAndIdShouldThrowMemberNotFoundException() throws Exception {
        
    	when(exampleProjectService.getMemberById(anyInt())).thenThrow(MemberNotFoundException.class);
	    when(exampleProjectRepository.existsById(example.getId())).thenReturn(true);
        when(exampleProjectRepository.findById(example.getId())).thenReturn(Optional.of(example));

        
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/get/200")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());

        exampleProjectServiceImpl.getMemberById(200);
        
        verify(exampleProjectService, times(0)).getMemberById(example.getId());
        verify(exampleProjectRepository, times(0)).existsById(200);
        verify(exampleProjectRepository, times(1)).findById(200);

    }

    @Test
    public void givenPutMappingUrlShouldReturnUpdatedMember() throws Exception {
    	
    	when(exampleProjectService.updateMember(any(Example.class))).thenReturn(example);
    	when(exampleProjectRepository.existsById(example.getId())).thenReturn(true);
    	when(exampleProjectRepository.findById(example.getId())).thenReturn(Optional.of(example));
    	
    	Example value = new Example(100,"updateMember100",2000.00);
    	
    	mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/update")
    			.contentType(MediaType.APPLICATION_JSON).content(asJsonString(value)))
    			.andExpect(MockMvcResultMatchers.status().isOk())
    			.andDo(MockMvcResultHandlers.print());
    	
    	
    	Example data = exampleProjectServiceImpl.updateMember(value);
    	assertEquals(data, data);
    	
    	verify(exampleProjectService, times(1)).updateMember(ArgumentMatchers.refEq(example));
    	verify(exampleProjectRepository, times(1)).existsById(example.getId());
    	verify(exampleProjectRepository, times(1)).findById(example.getId());
    }
    
    @Test( expected = MemberNotFoundException.class )
    public void givenPutMappingUrlAndTrackShouldReturnTrackNotFoundException() throws Exception {
    	
        when(exampleProjectService.updateMember(any(Example.class))).thenThrow(MemberNotFoundException.class);
    	when(exampleProjectRepository.findById(example.getId())).thenReturn(Optional.of(example));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/update")
                .contentType(MediaType.APPLICATION_JSON).content(asJsonString(example)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
        
    	Example value = new Example(200,"updateMember100",2000.00);
    	exampleProjectServiceImpl.updateMember(value);

        verify(exampleProjectService, times(1)).updateMember(ArgumentMatchers.refEq(example));
    	verify(exampleProjectRepository, times(1)).findById(example.getId());

    }
    
    @Test
    public void givenDeleteMappingUrlAndMemberIdShouldReturnDeletedMember() throws Exception {
    	
        when(exampleProjectService.deleteMemberById(anyInt())).thenReturn(Optional.of(example));
        when(exampleProjectRepository.findById(example.getId())).thenReturn(Optional.of(example));
        
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/delete/100")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

       exampleProjectServiceImpl.deleteMemberById(example.getId());

        verify(exampleProjectService, times(1)).deleteMemberById(example.getId());
    	verify(exampleProjectRepository, times(1)).findById(example.getId());

    }
    
    @Test( expected = MemberNotFoundException.class )
    public void givenDeleteMappingUrlAndMemberIdShouldThrowMemberNotFoundException() throws Exception {
    	
        when(exampleProjectService.deleteMemberById(anyInt())).thenThrow(MemberNotFoundException.class);
        when(exampleProjectRepository.findById(example.getId())).thenReturn(Optional.of(example));
        
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/delete/200")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
        
        exampleProjectServiceImpl.deleteMemberById(200);

        verify(exampleProjectService, times(0)).deleteMemberById(example.getId());
    	verify(exampleProjectRepository, times(1)).findById(example.getId());

    }
    


	private static String asJsonString(final Object object) {
    	try {
    		return new ObjectMapper().writeValueAsString(object);
    	} catch (Exception e) {
    		throw new RuntimeException(e);
    	}
    }

}
