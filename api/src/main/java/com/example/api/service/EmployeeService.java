package com.example.api.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.api.model.Employee;
import com.example.api.repository.EmployeeRepositoy;

import lombok.Data;

@Data
@Service
public class EmployeeService {
	
	@Autowired
	private EmployeeRepositoy employeeRepositoy;
	
	public Optional<Employee> getEmployee(final long id) {
		return employeeRepositoy.findById(id);
	}
	
	public Page<Employee> getEmployees(int page,
										   int size,
										    String sortBy) {
		return employeeRepositoy.findAll(PageRequest.of(page, size, Sort.by(sortBy)));
	}
	
	public void deleteEmployee(final long id) {
		employeeRepositoy.deleteById(id);
	}
	
	public Employee saveEmployee(Employee employee) {
		if(employeeRepositoy.existsByMail(employee.getMail())) {
		    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email déjà utilisé");
		}
		Employee e = employeeRepositoy.save(employee);
		return e;
	}
	
	public Employee updateEmployee(Employee employee) {
		return employeeRepositoy.save(employee);
	}

}
