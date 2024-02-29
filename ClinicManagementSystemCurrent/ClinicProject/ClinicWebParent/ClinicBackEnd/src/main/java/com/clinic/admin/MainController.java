package com.clinic.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
	
	@GetMapping("")
	public String viewHomePage() {
		return "index";
	}



//	@GetMapping("/record")
//	public String viewRecord() {
//		return "medical records/medical_form";
//	}

}
