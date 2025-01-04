package com.org.Shopping_App.Controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.org.Shopping_App.Dto.ProductsDto;
import com.org.Shopping_App.Dto.UserDto;
import com.org.Shopping_App.Service.CatagoryService;
import com.org.Shopping_App.Service.ProductService;
import com.org.Shopping_App.Service.UserService;
import com.org.Shopping_App.util.MailUtil;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
	@Autowired
	private UserService userService;
	@Autowired
	private CatagoryService catagoryServ;
	@Autowired
	private ProductService productService;

	@Autowired
	private MailUtil mailUtil;

	@GetMapping("/")
	public String indexPage(Model m, @RequestParam(name = "pageNum", defaultValue = "0") Integer pageNum,
			@RequestParam(name = "pageSize", defaultValue = "12") Integer pageSize, HttpSession session) {
		m.addAttribute("catagories", catagoryServ.activCatagory(pageNum, pageSize));
		Page<ProductsDto> fetchAllProduct = productService.fetchAllProduct(pageNum, pageSize);
		List<ProductsDto> content = fetchAllProduct.getContent();
		m.addAttribute("size", content.size());
		m.addAttribute("pageSize", fetchAllProduct.getSize());
		m.addAttribute("totalPage", fetchAllProduct.getTotalPages());
		m.addAttribute("pagenumber", fetchAllProduct.getNumber());
		m.addAttribute("totalElement", fetchAllProduct.getTotalElements());
		m.addAttribute("isFirst", fetchAllProduct.isFirst());
		m.addAttribute("isLast", fetchAllProduct.isLast());
		m.addAttribute("products", productService.fetchAllProduct(pageNum, pageSize));
		return "index";
	}

	@GetMapping("/register")
	public String registerPage() {
		return "register";
	}

	@GetMapping("/signIn")
	public String loginPage() {
		return "login";
	}

	@GetMapping("/product")
	public String productPage(Model m, HttpSession session,
			@RequestParam(name = "pageNum", defaultValue = "0") Integer pageNum,
			@RequestParam(name = "pageSize", defaultValue = "8") Integer pageSize) {
		m.addAttribute("products", productService.fetchAllProduct(pageNum, pageSize));
		m.addAttribute("catagories", catagoryServ.activCatagory(pageNum, pageSize));

		Page<ProductsDto> fetchAllProduct = productService.fetchAllProduct(pageNum, pageSize);
		List<ProductsDto> content = fetchAllProduct.getContent();
		m.addAttribute("size", content.size());
		m.addAttribute("pageSize", fetchAllProduct.getSize());
		m.addAttribute("totalPage", fetchAllProduct.getTotalPages());
		m.addAttribute("pagenumber", fetchAllProduct.getNumber());
		m.addAttribute("totalElement", fetchAllProduct.getTotalElements());
		m.addAttribute("isFirst", fetchAllProduct.isFirst());
		m.addAttribute("isLast", fetchAllProduct.isLast());

		session.setAttribute("products", productService.fetchAllProduct(pageNum, pageSize));
		session.setAttribute("catagories", catagoryServ.activCatagory(pageNum, pageSize));
		return "products";
	}

	@PostMapping("/saveUser")
	public String saveUser(@ModelAttribute UserDto userDto, @RequestParam("img") MultipartFile file,
			HttpSession session ,@RequestParam String role) throws IOException {

		String imageName = file.isEmpty() ? "default.png" : file.getOriginalFilename();
		userDto.setImage(imageName);
		UserDto saveUser = userService.saveUser(userDto,role);

		if (!ObjectUtils.isEmpty(saveUser)) {
			if (!file.isEmpty()) {
				File saveFile = new ClassPathResource("static/img/").getFile();
				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "profile_img" + File.separator
						+ file.getOriginalFilename());
				System.out.println(path);
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			}
			session.setAttribute("succMsg", "Saved SuccessFully");
		} else {
			session.setAttribute("errorMsg", "Something Went  Wrong");
		}
		return "redirect:/register";
	}

	@ModelAttribute
	public void getUSerDetails(Principal p, Model m, HttpSession session) {

		if (p != null) {
			String email = p.getName();
			session.setAttribute("UserDetails", userService.findByEmail(email));
			m.addAttribute("UserDetails", userService.findByEmail(email));
		}
		m.addAttribute("catagories", catagoryServ.fetchAllCatagory());
	}

	// Forget Password

	@GetMapping("/forget")
	public String showForgetPassword() {
		return "forgetPassword";
	}

	@GetMapping("/resetPassword")
	public String showResetPassword(@RequestParam String token) {
		return "resetPassword";
	}

	@PostMapping("/resetPasswordCheck")
	public String checkResetPassword(HttpSession session, String firstPas) {

		String sessionToken = (String) session.getAttribute("token");
		UserDto userToken = userService.findByToken(sessionToken);
		if (sessionToken.equals(userToken.getToken())) {
			UserDto updatePassword = userService.updatePassword(userToken.getId(), firstPas);
			if (!ObjectUtils.isEmpty(updatePassword)) {

				session.setAttribute("succMsg", "Update SuccessFully");
				return "resetPassword";
			} else {
				session.setAttribute("errorMsg", "Something Went Wrong In Server");
				return "resetPassword";
			}
		} else {
			session.setAttribute("errorMsg", "Token Did Not Match");
			return "redirect:/resetPassword";
		}
	}

	@PostMapping("/check_forget")
	public String checkEmail(@RequestParam String email, HttpSession session, HttpServletRequest req)
			throws UnsupportedEncodingException, MessagingException {

		UserDto user = userService.findByEmail(email);
		if (!ObjectUtils.isEmpty(user)) {
			String token = UUID.randomUUID().toString();
			session.setAttribute("token", token);
			userService.updateToken(email, token);

			// Url Generate
			String url = mailUtil.generateUrl(req) + "/resetPassword?token=" + token;

			boolean sendEmail = mailUtil.sendEmail(url, email);

			if (sendEmail) {
				session.setAttribute("succMsg", "Sent SuccessFully");
			}

			return "forgetPassword";
		} else {
			session.setAttribute("errorMsg", "Invalid Email");
			return "forgetPassword";
		}
	}

	@PostMapping("/seachProd")
	public String searchProd(@RequestParam String search, HttpSession session,
			@RequestParam(name = "pageNum", defaultValue = "0") Integer pageNum,
			@RequestParam(name = "pageSize", defaultValue = "8") Integer pageSize, Model m) {
		Page<ProductsDto> fetchAllProduct = productService.fetchAllProduct(search, search, pageNum, pageSize);
		List<ProductsDto> content = fetchAllProduct.getContent();
		m.addAttribute("size", content.size());
		m.addAttribute("pageSize", fetchAllProduct.getSize());
		m.addAttribute("totalPage", fetchAllProduct.getTotalPages());
		m.addAttribute("pagenumber", fetchAllProduct.getNumber());
		m.addAttribute("totalElement", fetchAllProduct.getTotalElements());
		m.addAttribute("isFirst", fetchAllProduct.isFirst());
		m.addAttribute("isLast", fetchAllProduct.isLast());
		session.setAttribute("products", content);
		return "products";
	}
}
