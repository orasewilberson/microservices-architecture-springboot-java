package com.example.api.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


@SpringBootTest
@AutoConfigureMockMvc // désactive Spring Security pour MockMvc
public class EmployeeControllerTest {

	@Autowired
	public MockMvc mockMvc;
	
	@Test
	@WithMockUser(username = "testUser", roles = {"USER"})
	public void testGetEmployees() throws Exception {
		mockMvc.perform(get("/employees")
                .param("page", "0")
                .param("size", "5")
                .param("sortBy", "id"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Laurent")));
    }		
	
	@Test
	@WithMockUser(username = "testAdmin", roles = {"ADMIN"})
	public void testGetEmployeeById() throws Exception {
		mockMvc.perform(get("/employee/{id}", 1))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.id", is(1)))
		.andExpect(jsonPath("$.firstName", is("Laurent")));		
	}
	
	@Test
	@WithMockUser(username = "testAdmin", roles = {"USER"})
	public void testGetEmployeeByIdNotFound() throws Exception {
		mockMvc.perform(get("/employee/{id}", 999))
		.andExpect(status().isOk())
		.andExpect(content().string(""));	
	}
	
}
