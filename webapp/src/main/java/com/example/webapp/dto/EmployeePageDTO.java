package com.example.webapp.dto;

import java.util.List;

import com.example.webapp.model.Employee;

import lombok.Data;

@Data
public class EmployeePageDTO {
    private List<Employee> content;
    private int totalPages;
    private long totalElements;
    private int number; // numéro de page en cours
    private int size;   // taille de la page
    private boolean first;
    private boolean last;
}
