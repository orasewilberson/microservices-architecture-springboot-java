package com.example.departement_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.departement_service.model.Departement;
import com.example.departement_service.repository.DepartementRepository;

@RestController
@RequestMapping("/departements")
public class DepartementController {
	@Autowired
	private DepartementRepository departmentRepository;

    @GetMapping
    public List<Departement> getAllDepartments() {
        return departmentRepository.findAll();
    }

    @PostMapping
    public Departement createDepartment(@RequestBody Departement department) {
        return departmentRepository.save(department);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Departement> getById(@PathVariable Long id) {
    	return departmentRepository.findById(id)
    			.map(ResponseEntity::ok)
    			.orElse(ResponseEntity.notFound().build());
    }

}
