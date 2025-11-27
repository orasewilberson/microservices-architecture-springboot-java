package com.example.webapp.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.example.webapp.dto.EmployeePageDTO;
import com.example.webapp.model.Employee;
import com.example.webapp.service.EmployeeService;

import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("removal")
	@MockBean
    private EmployeeService employeeService; // Mock du service

    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    public void testGetEmployees() throws Exception {
        // Préparer des données factices
        Employee emp = new Employee();
        emp.setId(1);
        emp.setFirstName("Laurent"); // correspond au contenu que tu vérifies
        List<Employee> employees = List.of(emp);

        EmployeePageDTO employeePageDTO = new EmployeePageDTO();
        employeePageDTO.setContent(employees);
        employeePageDTO.setNumber(0);
        employeePageDTO.setTotalPages(1);
        employeePageDTO.setSize(5);
        employeePageDTO.setFirst(true);
        employeePageDTO.setLast(true);

        // Quand le service est appelé, renvoyer ces données
        when(employeeService.getEmployees("", 0, 5, "id")).thenReturn(employeePageDTO);

        // Test MockMvc
        mockMvc.perform(get("/employees")
                .param("page", "0")
                .param("size", "5")
                .param("sortBy", "id"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("home"))
            .andExpect(content().string(containsString("Laurent")));
    }
}
