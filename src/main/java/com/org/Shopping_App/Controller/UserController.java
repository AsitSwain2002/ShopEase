package com.org.Shopping_App.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.org.Shopping_App.Dto.CartDto;
import com.org.Shopping_App.Entity.Cart;
import com.org.Shopping_App.Service.CartService;
import com.org.Shopping_App.Service.UserService;

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

	@GetMapping("/viewCart/{id}")
	public String viewCart(@PathVariable int id, Model m) {
		List<CartDto> fetchAllCart = cartService.fetchAllCart(id);
		m.addAttribute("Carts", fetchAllCart);
		m.addAttribute("cartLength", fetchAllCart.size());
		double totalPrice = 0.0;
		double totalDiscountPrice = 0;
		double totalAmount = 0.0;
		for (CartDto cart : fetchAllCart) {
			totalPrice += cart.getProducts().getPrice() * cart.getQuantity();
			if (cart.getProducts().getDiscount() > 0) {
				totalDiscountPrice += (cart.getProducts().getPrice() - cart.getProducts().getDiscountPrice())
						* cart.getQuantity();
			}
			totalAmount += totalPrice - totalDiscountPrice;
		}
		m.addAttribute("totalPrice", totalPrice);
		m.addAttribute("totalDiscountPrice", totalDiscountPrice);
		m.addAttribute("totalAmount", totalAmount);
		return "user/addCart";
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

	@GetMapping("/removeCart/{id}")
	public String removecartItem(@PathVariable int id, HttpSession session) {
		CartDto cart = cartService.removeItem(id);
		return "redirect:/user/viewCart/" + cart.getUser().getId();
	}

	@GetMapping("/incrementQuantity/{id}")
	public String incQuantitySize(@PathVariable int id, HttpSession session) {

		CartDto cart = cartService.incrementProd(id);
		if (!ObjectUtils.isEmpty(cart)) {
			session.setAttribute("errorMsg", "Something Went Wrong");
		}
		return "redirect:/user/viewCart/" + cart.getUser().getId();
	}

	@GetMapping("/decrementQuantity/{id}")
	public String decQuantitySize(@PathVariable int id, HttpSession session) {
		CartDto cart = cartService.decrementProd(id);
		if (!ObjectUtils.isEmpty(cart)) {
			session.setAttribute("errorMsg", "Something Went Wrong");
		}
		return "redirect:/user/viewCart/" + cart.getUser().getId();
	}

}
