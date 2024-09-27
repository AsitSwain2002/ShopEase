package com.org.Shopping_App.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.org.Shopping_App.Dto.CartDto;
import com.org.Shopping_App.Service.CartService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private CartService cartService;

	@GetMapping("/")
	public String home() {
		return "user/index";
	}

	@GetMapping("/addCart")
	public String addToCart(@RequestParam int pId, @RequestParam int uId, HttpSession session) {

		CartDto saveCart = cartService.saveCart(pId, uId);
		if (ObjectUtils.isEmpty(saveCart)) {
			session.setAttribute("errorMsg", "Something Went Wrong");
		} else {
			session.setAttribute("succMsg", "Added To Cart");
		}
		return "redirect:/viewDetails/" + pId;
	}

}
