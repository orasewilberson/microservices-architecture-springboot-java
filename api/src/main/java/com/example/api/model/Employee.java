package com.example.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity
@Table(name = "employees", uniqueConstraints = {
	    @UniqueConstraint(columnNames = "mail")
	})
public class Employee {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@NotBlank(message = "le prenom doit est obligatoire")
    @Column(name="first_name")
    private String firstName;

	@NotBlank(message = "le nom doit est obligatoire")
    @Column(name="last_name")
    private String lastName;

    @Email
    @Column(nullable = false, unique = true)
    private String mail;

	@NotBlank(message = "le password doit est obligatoire")
    private String password;
	
	@Column(name = "departement_id")
	private Long departementId;


//    private String role;
}
