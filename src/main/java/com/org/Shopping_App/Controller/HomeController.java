package com.org.Shopping_App.Controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.org.Shopping_App.Dto.UserDto;
import com.org.Shopping_App.Service.CatagoryService;
import com.org.Shopping_App.Service.ProductService;
import com.org.Shopping_App.Service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
	@Autowired
	private UserService userService;
	@Autowired
	private CatagoryService catagoryServ;

	@Autowired
	private ProductService productService;

	@GetMapping("/")
	public String indexPage(Model m) {
		m.addAttribute("catagories", catagoryServ.activCatagory());
		m.addAttribute("products", productService.fetchAllProduct());
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
	public String productPage(Model m, HttpSession session) {
		session.setAttribute("products", productService.fetchAllProduct());
		session.setAttribute("catagories", catagoryServ.activCatagory());
		return "products";
	}

	@PostMapping("/saveUser")
	public String saveUser(@ModelAttribute UserDto userDto, @RequestParam("img") MultipartFile file,
			HttpSession session) throws IOException {

		String imageName = file.isEmpty() ? "default.png" : file.getOriginalFilename();
		userDto.setImage(imageName);
		UserDto saveUser = userService.saveUser(userDto);

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
		return "register";
	}
}
