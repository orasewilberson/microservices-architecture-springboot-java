package com.example.api.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.api.model.Departement;

@FeignClient(name = "departement-service")
public interface DepartementClient {
	
	@GetMapping("/departements/{id}")
	Departement getDepartementById(@PathVariable Long id);
}
