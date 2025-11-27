package com.example.api.model;

import lombok.Data;

@Data
public class EmployeeWithDepartement {
	
	private Long id;
	private String firstName;
	private String lastName;
	private String mail;
	private Departement departement;

	public EmployeeWithDepartement(Long id, String firstName, String lastName, String mail, Departement departement) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.mail = mail;
		this.departement = departement;
	}
}
