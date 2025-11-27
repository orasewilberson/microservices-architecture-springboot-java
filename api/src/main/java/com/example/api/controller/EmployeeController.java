package com.example.api.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.api.client.DepartementClient;
import com.example.api.dto.EmployeePageDTO;
import com.example.api.model.Departement;
import com.example.api.model.Employee;
import com.example.api.model.EmployeeWithDepartement;
import com.example.api.service.EmployeeService;

import jakarta.validation.Valid;


@RestController
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private DepartementClient departementClient;
	
	/**
	 * create - An a new Employee
	 * @param employee An object employee
	 * @return the employed object save
	 */
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/employee")
	public Employee createEmployee(@Valid @RequestBody Employee employee) {
		return employeeService.saveEmployee(employee);
	}
	
	/**
	 * Read - get one Employee
	 * @param id - the id of employee
	 * @return An Employee full filled
	 */
	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	@GetMapping("employee/{id}")
	public Employee getEmployee(@PathVariable("id") final long id) {
		Optional<Employee> employee = employeeService.getEmployee(id);
		if(employee.isPresent()) {
			return employee.get();
		}else {
			return null; //un retour notFound sera adapter plus tard
		}
	}
	
	/**
	 * Read - Get all employees
	 * @return - An EmployeePageDTO object of employee full filled
	 */
	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	@GetMapping("/employees")
	public EmployeePageDTO getEmployees(@RequestParam(defaultValue = "0") int page,
										   @RequestParam(defaultValue = "5") int size,
										   @RequestParam(defaultValue = "id") String sortBy) {
		
		Page<Employee> employeesPage = employeeService.getEmployees(page, size, sortBy);
		
		EmployeePageDTO dto = new EmployeePageDTO();
        dto.setContent(employeesPage.getContent());
        dto.setTotalPages(employeesPage.getTotalPages());
        dto.setTotalElements(employeesPage.getTotalElements());
        dto.setNumber(employeesPage.getNumber());
        dto.setSize(employeesPage.getSize());
        dto.setFirst(employeesPage.isFirst());
        dto.setLast(employeesPage.isLast());

        return dto;
	}
	

	/**
	 * Delete - Delete an employee
	 * @param id - The id of employee to delete
	 */
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/employee/{id}")
	public void deleteEmployee(@PathVariable("id") final long id) {
		employeeService.deleteEmployee(id);
	}
	
	/**
	 * 
	 * @param id
	 * @param employee
	 * @return
	 */
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/employee/{id}")
	public Employee updateEmployee(@PathVariable("id") final long id, @RequestBody Employee employee) {
		Optional<Employee> employeeExisting = employeeService.getEmployee(id);
		if(employeeExisting.isPresent()) {
			Employee currentEmployee = employeeExisting.get();
			
			String firstName = employee.getFirstName();
			if (firstName != null) {
				currentEmployee.setFirstName(firstName);
			}
			
			String lastName = employee.getLastName();
			if (lastName != null) {
				currentEmployee.setLastName(lastName);
			}
			
			String mail = employee.getMail();
			if (mail != null) {
				currentEmployee.setMail(mail);
			}
			
			String password = employee.getPassword();
			if (password != null) {
				currentEmployee.setPassword(password);
			}
			employeeService.updateEmployee(currentEmployee);
			return currentEmployee;
		} else {
			return null;
		}
	}
	
	
	@GetMapping("/internal/employees/{id}/with-departement")
	public EmployeeWithDepartement getEmployeeWithDepartement(@PathVariable Long id) {
		Employee employee = employeeService.getEmployee(id).orElseThrow(() -> new RuntimeException("Employee not found"));
		
		Departement departement = departementClient.getDepartementById(employee.getDepartementId());
		
		return new EmployeeWithDepartement(
				employee.getId(),
				employee.getFirstName(),
				employee.getLastName(),
				employee.getMail(),
				departement
				);
	}
	
}
