package com.org.Shopping_App.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ProductController {

	@GetMapping("/viewDetails")
	public ModelAndView viewDetails() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("viewProduct");
		return modelAndView;
	}
}
