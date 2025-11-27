package com.example.webapp.repository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.example.webapp.model.Employee;


import com.example.webapp.CustomProperties;
import com.example.webapp.dto.EmployeePageDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class EmployeeProxy {
	
	@Autowired
	private CustomProperties customProperties;
	
    private RestTemplate restTemplate = new RestTemplate(); 
    	
	/**
	 * Get all employees
	 * @return 
	 */
	public EmployeePageDTO getEmployees(String token, int page, int size, String sortBy) {
		String baseApiUrl = customProperties.getApiUrl();
//		String getEmployeesUrl = baseApiUrl + "/employees";
		String getEmployeesUrl = baseApiUrl 
                + "/employees?page=" + page 
                + "&size=" + size 
                + "&sortBy=" + sortBy;
				
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(token);
		HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<EmployeePageDTO> response = restTemplate.exchange(
            getEmployeesUrl,
            HttpMethod.GET,
            entity,
            new ParameterizedTypeReference<EmployeePageDTO>() {}
        );

        log.debug("Get Employees call " + response.getStatusCode());

        return response.getBody();
	}
	
	/**
	 * Get an employee by the id
	 * @param id The id of the employee
	 * @return The employee which matches the id
	 */
	  public Employee getEmployee(int id, String token) {
		  String baseApiUrl = customProperties.getApiUrl();
		  String getEmployeeUrl = baseApiUrl + "/employee/" + id;
		  
		  HttpHeaders headers = new HttpHeaders();
		  headers.setContentType(MediaType.APPLICATION_JSON);
		  headers.setBearerAuth(token);
		  HttpEntity<Void> request = new HttpEntity<>(headers);
		  
		  ResponseEntity<Employee> response = restTemplate.exchange(
				  getEmployeeUrl, 
				  HttpMethod.GET, 
				  request, 
				  Employee.class);
		  log.debug("Get Employee call " + response.getStatusCode().toString());
		  
		  return response.getBody();
	  }
	  
	  /**
	   * Add a new Employee
	   * @param e A new employee (without an id)
	   * @return The employee full filled (with an id)
	   */
	  public Employee createEmployee(Employee e, String token) {
		  String baseApiUrl = customProperties.getApiUrl();
		  String createEmployeeUrl = baseApiUrl + "/employee";
		  
		  //Recupere le token depuis la session
		  HttpHeaders headers = new HttpHeaders();
		  headers.setContentType(MediaType.APPLICATION_JSON);
		  headers.setBearerAuth(token);
		  
		  HttpEntity<Employee> request = new HttpEntity<Employee>(e, headers);
		  ResponseEntity<Employee> response = restTemplate.exchange(
				  createEmployeeUrl,
				  HttpMethod.POST,
				  request,
				  Employee.class);
		  
		  log.debug("create employee call" + response.getStatusCode().toString());
		  
		  return response.getBody();
	  }
	  
	  /**
	   * Update an employee - using the PUT HTTP Method
	   * @param e Existing employee to update
	   * @return 
	   */
	  public Employee updateEmployee(Employee e, String token) {
		  String baseApiUrl = customProperties.getApiUrl();
		  String updateEmployee = baseApiUrl + "/employee/" + e.getId();
		  
		  //Recupere le token depuis la session
		  HttpHeaders headers = new HttpHeaders();
		  headers.setContentType(MediaType.APPLICATION_JSON);
		  headers.setBearerAuth(token);
		  
		  HttpEntity<Employee> request = new HttpEntity<Employee>(e, headers);
		  ResponseEntity<Employee> response = restTemplate.exchange(
				  updateEmployee, 
				  HttpMethod.PUT, 
				  request, 
				  Employee.class);
		  
		  log.debug("Update Employee call " + response.getStatusCode().toString());
		  
		  return response.getBody();
	  }
	  
	  /**
	   * Delete an employee using exchange method of RestTemplate
	   * instead of delete method in order to log the response status code
	   * @param id The employee to delete
	   */
	 public void deleteEmployee(int id, String token) {
		 String baseApiUrl = customProperties.getApiUrl();
		 String deleteEmployee = baseApiUrl + "/employee/" + id;
		  
		 HttpHeaders headers = new HttpHeaders();
		  headers.setContentType(MediaType.APPLICATION_JSON);
		  headers.setBearerAuth(token);
		  HttpEntity<Void> request = new HttpEntity<>(headers);
		  
		 ResponseEntity<Void> response = restTemplate.exchange(
				 deleteEmployee, 
				 HttpMethod.DELETE, 
				 request, 
				 Void.class);
		 
		 log.debug("Delete Employee call " + response.getStatusCode().toString());
	 }

}
