package com.org.Shopping_App.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	@GetMapping("/")
	public String indexPage() {
		return "index";
	}

	@GetMapping("/register")
	public String registerPage() {
		return "register";
	}
	@GetMapping("/login")
	public String loginPage() {
		return "login";
	}
	
	@GetMapping("/product")
	public String productPage() {
		return "products";
	}
}
