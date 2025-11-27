package com.example.webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.webapp.dto.EmployeePageDTO;
import com.example.webapp.model.Employee;
import com.example.webapp.service.EmployeeService;

import jakarta.servlet.http.HttpSession;
import lombok.Data;

@Data
@Controller
@RequestMapping("/employees")
public class EmployeeController {
	
	@Autowired
	private EmployeeService employeeService;

	@GetMapping
	public String home(Model model, HttpSession httpSession,
									@RequestParam(defaultValue = "0") int page,
							        @RequestParam(defaultValue = "5") int size,
							        @RequestParam(defaultValue = "id") String sortBy) {
		
		String token = (String) httpSession.getAttribute("jwtToken");
		if(token == null) {
			return "redirect:/";
		}
		EmployeePageDTO employeePageDTO = employeeService.getEmployees(token, page,size,sortBy);
		
		model.addAttribute("employees", employeePageDTO.getContent());
	    model.addAttribute("currentPage", employeePageDTO.getNumber());
        model.addAttribute("totalPages", employeePageDTO.getTotalPages());
        model.addAttribute("size", employeePageDTO.getSize());
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("isFirstPage", employeePageDTO.isFirst());
        model.addAttribute("isLastPage", employeePageDTO.isLast());
		
		return "home";
	}
	
	@GetMapping("/create")
	public String createEmployee(Model model) {
		Employee e = new Employee();
		model.addAttribute("employee", e);
		return "formNewEmployee";
	}
					
	@GetMapping("/update/{id}")
	public String updateEmployee(@PathVariable("id") final int id, Model model, HttpSession session) {
		//verifier si le token existe
		String token = (String) session.getAttribute("jwtToken");
		if (token == null) {
			return "redirect:/";
		}
		
		Employee e = employeeService.getEmployee(id, token);
		model.addAttribute("employee", e);
		return "formUpdateEmployee";
	}
	
	@GetMapping("delete/{id}")
	public ModelAndView deleteEmployee(@PathVariable("id") final int id, HttpSession session) {
		//verifier si le token existe
		String token = (String) session.getAttribute("jwtToken");
		if (token == null) {
			return new ModelAndView("redirect:/");
		}
		
		employeeService.deleteEmployee(id, token);
		return new ModelAndView("redirect:/employees");
	}
	
	@PostMapping("/save")
	public ModelAndView saveEmployee(@ModelAttribute Employee employee, Model model, RedirectAttributes redirectAttributes, 
			HttpSession session) {
		
		String token = (String) session.getAttribute("jwtToken");
		if(token == null) {
			return new ModelAndView("redirect:/");
		}
		
		try {
			if(employee.getId() != null) {
				// Employee from update form has the password field not filled,
				// so we fill it with the current password.
				Employee current = employeeService.getEmployee(employee.getId(), token);
				employee.setPassword(current.getPassword());
			}
			
			employeeService.saveEmployee(employee, token);
			redirectAttributes.addFlashAttribute("success", "L'enregistrement effectue avec succes !");
			return new ModelAndView("redirect:/employees");

	    } catch (HttpClientErrorException e) { // pour les erreurs 4xx
	        // Récupère le message d'erreur JSON de l'API
	        String errorMessage = e.getResponseBodyAsString();
	        model.addAttribute("error", errorMessage);
	        model.addAttribute("employee", employee); // garder les valeurs saisies
	        return new ModelAndView("formNewEmployee"); // retourne le formulaire avec l'erreur
	    } catch (HttpServerErrorException e) { 
	        String errorMessage = e.getResponseBodyAsString();
	        model.addAttribute("error", errorMessage.substring(16));
	        return new ModelAndView("formNewEmployee");
	    } catch (RestClientException e) {
	        model.addAttribute("error", "Erreur communication avec le serveur.");
	        return new ModelAndView("formNewEmployee");
	    } 

	}
	
}
