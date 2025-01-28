package com.org.Shopping_App.Controller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.org.Shopping_App.Dto.CartDto;
import com.org.Shopping_App.Dto.ProductOrderDto;
import com.org.Shopping_App.Dto.UserAddressDto;
import com.org.Shopping_App.Dto.UserDto;
import com.org.Shopping_App.Entity.Cart;
import com.org.Shopping_App.Entity.ProductOrder;
import com.org.Shopping_App.Repo.UserRepo;
import com.org.Shopping_App.Service.CartService;
import com.org.Shopping_App.Service.ProductOrderService;
import com.org.Shopping_App.Service.ProductService;
import com.org.Shopping_App.Service.UserService;
import com.org.Shopping_App.util.MailUtil;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private CartService cartService;
	@Autowired
	private ProductOrderService productOrderService;
	@Autowired
	private UserService userService;

	@GetMapping("/")
	public String home() {
		return "user/index";
	}

	@GetMapping("/viewCart/{id}")
	public String viewCart(@PathVariable int id, Model m, HttpSession session) {
		List<CartDto> fetchAllCart = cartService.fetchAllCart(id);
		m.addAttribute("Carts", fetchAllCart);
		m.addAttribute("cartLength", fetchAllCart.size());
		session.setAttribute("cartLength", fetchAllCart.size());
		double totalPrice = 0.0;
		double totalDiscountPrice = 0;
		double totalAmount = 0.0;
		for (CartDto cart : fetchAllCart) {
//			System.out.println();
//			System.out.println();
//			System.out.println(cart.getProducts());
//			System.out.println();
//			System.out.println();
//			System.out.println();
			totalPrice += cart.getProducts().getPrice() * cart.getQuantity();
			if (cart.getProducts().getDiscount() > 0) {
				totalDiscountPrice += (cart.getProducts().getPrice() - cart.getProducts().getDiscountPrice())
						* cart.getQuantity();
			}
		}
		totalAmount += totalPrice - totalDiscountPrice;
		m.addAttribute("totalPrice", totalPrice);
		m.addAttribute("totalDiscountPrice", totalDiscountPrice);
		m.addAttribute("totalAmount", totalAmount);

		session.setAttribute("totalPrice", totalPrice);
		session.setAttribute("totalDiscountPrice", totalDiscountPrice);
		session.setAttribute("totalAmount", totalAmount);
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

	@GetMapping("/orderPage")
	public String orderPage(Model m) {
		m.addAttribute("userAddress", new UserAddressDto());
		return "user/orderPage";
	}

	@PostMapping("/orderNextPage/{id}")
	public String orderNextPage(@RequestParam String paymentType, @Valid @ModelAttribute("userAddress") UserAddressDto userAddressDto,
			BindingResult bindingResult, @PathVariable("id") int userId, Model m, HttpSession session) {

		if (bindingResult.hasErrors()) {
			return "user/orderPage";
		}
		productOrderService.saveProductOrder(userAddressDto, userId, paymentType, session);
		cartService.removeAllCartItem(userId);
		return "user/orderNextPagee";

	}

	@GetMapping("/userOrderPage/{pId}")
	public String userOrderPage(@PathVariable int pId, Model m, HttpSession session,
			@RequestParam(name = "pageNum", defaultValue = "0") Integer pageNum,
			@RequestParam(name = "pageSize", defaultValue = "5") Integer pageSize) {
		Page<ProductOrderDto> findAllById = productOrderService.findAllById(pId, pageNum, pageSize);
		List<ProductOrderDto> content = findAllById.getContent();

		m.addAttribute("size", content.size());
		m.addAttribute("pageSize", findAllById.getSize());
		m.addAttribute("totalPage", findAllById.getTotalPages());
		m.addAttribute("pagenumber", findAllById.getNumber());
		m.addAttribute("totalElement", findAllById.getTotalElements());
		m.addAttribute("isFirst", findAllById.isFirst());
		m.addAttribute("isLast", findAllById.isLast());
		m.addAttribute("AllOrder", findAllById);
		session.setAttribute("AllOrder", findAllById);
		return "user/userOrderPage";
	}

	@GetMapping("/orderCancelPage/{id}")
	public String orderCancelPage(@PathVariable int id, Model m) {
		ProductOrderDto product = productOrderService.searchById(id);
		m.addAttribute("product", product);
		return "user/orderCancelPage";
	}

	@GetMapping("/cancelOrder/{orderId}")
	public String cancleOrder(@PathVariable int orderId, @RequestParam int userId) {
		productOrderService.cancelOrder(orderId);
		return "redirect:/user/userOrderPage/" + userId;
	}

	@GetMapping("/viewProfile")
	public String viewProfile() {
		return "user/profilePage";
	}

	@PostMapping("/updateUser/{id}")
	public String updateUser(@ModelAttribute UserDto userDto, @PathVariable int id, HttpSession session) {
		UserDto updateUser = userService.updateUser(userDto, id);
		session.setAttribute("UserDetails", updateUser);
		return "user/profilePage";
	}

	@PostMapping("/updatePassword/{userId}")
	public String updatePassword(@RequestParam String oldPassword, @RequestParam String newPassword,
			@RequestParam String reEnterPassword, @PathVariable int userId, HttpSession session) {
		UserDto updatePassword = userService.updatePassword(oldPassword, newPassword, reEnterPassword, userId, session);
		if (ObjectUtils.isEmpty(updatePassword)) {
			session.setAttribute("errorMsg", "Something Went Wrong In Server");
		}
		return "user/profilePage";
	}
}
