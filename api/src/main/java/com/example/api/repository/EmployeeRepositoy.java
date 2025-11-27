package com.example.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.api.model.Employee;

@Repository
public interface EmployeeRepositoy extends JpaRepository<Employee, Long> {
	boolean existsByMail(String mail);
	Employee findByMail(String mail);
}
