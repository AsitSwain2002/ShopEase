package com.org.Shopping_App.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import com.org.Shopping_App.Dto.ProductsDto;
import com.org.Shopping_App.Service.ProductService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ProductController {

	@Autowired
	private ProductService productService;

	@GetMapping("/viewDetails/{id}")
	public String viewDetails(@PathVariable int id, Model m , HttpSession sesssion) {
		ProductsDto product = productService.findById(id);
		if(!ObjectUtils.isEmpty(product)) {
		m.addAttribute("productDetails",product);
		}else {
			sesssion.setAttribute("errorMsg", "Something WentWrong");
		}
		return "viewProduct";
	}
	
	@GetMapping("/productCatgory/{name}")
	public String productCatagory(@PathVariable String name , HttpSession session , Model m) {
		m.addAttribute("products", productService.fetchAllByName(name));
		session.setAttribute("products", productService.fetchAllByName(name));
//		session.setAttribute("catgoryName", name);
		m.addAttribute("catgoryName", name);
		return "/products";
	}
}
