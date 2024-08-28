package com.org.Shopping_App.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.org.Shopping_App.Service.CatagoryService;
import com.org.Shopping_App.Service.ProductService;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

	@Autowired
	private CatagoryService catagoryServ;

	@Autowired
	private ProductService productService;

	@GetMapping("/")
	public String indexPage(Model m) {
		m.addAttribute("catagories", catagoryServ.fetchAllCatagory());
		m.addAttribute("products", productService.fetchAllProduct());
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
	public String productPage(Model m , HttpSession session) {
      session.setAttribute("products", productService.fetchAllProduct());
      session.setAttribute("catagories", catagoryServ.fetchAllCatagory());
		return "products";
	}
}
