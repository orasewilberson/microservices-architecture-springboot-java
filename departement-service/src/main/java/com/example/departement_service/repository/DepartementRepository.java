package com.example.departement_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.departement_service.model.Departement;

public interface DepartementRepository extends JpaRepository<Departement, Long> {

}
