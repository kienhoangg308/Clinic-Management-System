package com.clinic.customer;

import com.clinic.common.entity.Customer;
import com.clinic.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CustomerController {
	@Autowired
	private CustomerService service;

	@GetMapping("/register")
	public String showRegisterForm(Model model) {

		model.addAttribute("pageTitle", "Customer Registration");
		model.addAttribute("customer", new Customer());


		return "register/register_form";
	}

	@PostMapping("/create_customer")
	public String saveCustomer(Customer customer, RedirectAttributes redirectAttributes){
		service.save(customer);
		redirectAttributes.addFlashAttribute("message", "The customer has been saved successfully.");
		return "redirect:/";
	}



}