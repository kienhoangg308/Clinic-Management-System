package com.clinic;

import com.clinic.common.entity.User;
import com.clinic.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class FrontMainController {

	@Autowired
	private UserService userService;

	@GetMapping("")
	public String viewHomePage(Model model) {
		List<User> listDoctors = userService.listAllDoctor();
		model.addAttribute("listDoctors", listDoctors);

		return "index";
	}

	@GetMapping("/login")
	public String viewLoginPage() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
			return "login";
		}

		return "redirect:/";
	}

//	@GetMapping("/treatment")
//	public String viewTreatment(Model model) {
//		List<Category> listCategories = categoryService.listTreatmentCategories();
//		model.addAttribute("listCategories", listCategories);
//
//		return "index";
//	}
//
//	@GetMapping("/cosmetic")
//	public String viewCosmetic(Model model) {
//		List<Category> listCategories = categoryService.listCosmeticCategories();
//		model.addAttribute("listCategories", listCategories);
//
//		return "index";
//	}

}
