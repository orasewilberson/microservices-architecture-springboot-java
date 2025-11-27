package com.example.webapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.webapp.dto.EmployeePageDTO;
import com.example.webapp.model.Employee;
import com.example.webapp.repository.EmployeeProxy;

import lombok.Data;

@Data
@Service
public class EmployeeService {

	@Autowired
	private EmployeeProxy employeeProxy;
	
	public EmployeePageDTO getEmployees(String token, int page, int size, String sortBy) {
		return employeeProxy.getEmployees(token, page, size, sortBy);
	}
	
	public Employee getEmployee(final int id, String token) {
		return employeeProxy.getEmployee(id, token);
	}
	
	public void deleteEmployee( final int id, String token) {
		employeeProxy.deleteEmployee(id, token);
	}
	
	public Employee saveEmployee(Employee employee, String token ) {
		Employee saveEmployee;
		
		//Nom de la famille doit etre en majiscule
		employee.setLastName(employee.getLastName().toUpperCase());
		
		if (employee.getId() == null) {
			saveEmployee = employeeProxy.createEmployee(employee, token);
		} else {
			saveEmployee = employeeProxy.updateEmployee(employee, token);
		}
		
		return saveEmployee;
	}
}
