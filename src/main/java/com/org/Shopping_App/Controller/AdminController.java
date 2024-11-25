package com.org.Shopping_App.Controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.org.Shopping_App.Dto.CatagoryDto;
import com.org.Shopping_App.Dto.ProductOrderDto;
import com.org.Shopping_App.Dto.ProductsDto;
import com.org.Shopping_App.Dto.UserDto;
import com.org.Shopping_App.Repo.UserRepo;
import com.org.Shopping_App.Service.CatagoryService;
import com.org.Shopping_App.Service.ProductOrderService;
import com.org.Shopping_App.Service.ProductService;
import com.org.Shopping_App.Service.UserService;
import com.org.Shopping_App.util.MailUtil;

import jakarta.servlet.http.HttpSession;
import jakarta.websocket.Session;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private CatagoryService catagoryService;

	@Autowired
	private ProductService productService;

	@Autowired
	private UserService userService;

	@Autowired
	private ProductOrderService productOrderService;

	@Autowired
	private MailUtil mailUtil;

	@GetMapping("/")
	public String viewIndex() {
		return "admin/index";
	}

//Catagory Controller
	@GetMapping("/catagory")
	public String viewCatagory(Model model) {

		model.addAttribute("catagory", catagoryService.fetchAllCatagory());
		return "admin/addCatagory";
	}

	@PostMapping("/saveCatagory")
	public String saveCatagory(@ModelAttribute CatagoryDto catagoryDto, @RequestParam MultipartFile file,
			HttpSession session) throws IOException {

		String image = file != null ? file.getOriginalFilename() : "default.jpg";
		catagoryDto.setImageName(image);
		if (catagoryService.isCatagoryExist(catagoryDto.getName())) {
			session.setAttribute("errorMsg", "CatagoryAlready Exists");
		} else {
			CatagoryDto saveCatagory = catagoryService.saveCatagory(catagoryDto);

			if (ObjectUtils.isEmpty(saveCatagory)) {
				session.setAttribute("errorMsg", "Something Went Wrong ! Internal Server Error");
			} else {

				File saveFile = new ClassPathResource("static/img/category_img").getFile();

				// Create the directory if it doesn't exist
				if (!saveFile.exists()) {
					Files.createDirectories(saveFile.toPath());
				}

				// Define the path where the file will be saved
				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());

				// Copy the file to the target location, replacing existing one if any
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				session.setAttribute("successMsg", "Saved Successfully");
			}
		}
		return "redirect:/admin/catagory";
	}

	@GetMapping("/updateCatagoryPage/{id}")
	public String updateCatagoryPage(@PathVariable int id, HttpSession session) {
		CatagoryDto catagoryDto = catagoryService.findById(id);
		session.setAttribute("catagory", catagoryDto);
		return "/admin/editCatagory";
	}

	@PostMapping("/updateCatagory")
	public String updateCatagory(@ModelAttribute CatagoryDto catagoryDto, @RequestParam int id,
			@RequestParam("file") MultipartFile file, HttpSession session) {
		catagoryService.updateCatagory(id, catagoryDto, file);
		session.setAttribute("successMsg", "Updated Successfully");
		session.setAttribute("errorMsg", "Something Went Wrong");
		return "redirect:/admin/updateCatagoryPage/" + catagoryDto.getId();
	}

	@GetMapping("/deleteCatagory/{id}")
	public String deleteCatagory(@PathVariable int id) {
		catagoryService.deleteCatagory(id);
		return "redirect:/admin/catagory";
	}

	// Save Product Start
	@GetMapping("/loadAddProduct")
	public String viewaddProduct(Model m) {
		List<CatagoryDto> allCatagory = catagoryService.fetchAllCatagory();
		m.addAttribute("allCatagory", allCatagory);
		return "admin/addProduct";
	}

	@PostMapping("/saveProduct")
	public String saveProduct(@ModelAttribute ProductsDto productDto, @RequestParam MultipartFile file,
			HttpSession session) throws IOException {

		String imageName = file != null ? file.getOriginalFilename() : "default.png";
		productDto.setImageName(imageName);

		ProductsDto product = productService.saveProduct(productDto);

		if (!ObjectUtils.isEmpty(product)) {
			// Get the directory where you want to save the file
			File saveFile = new ClassPathResource("static/img/category_img").getFile();

			// Create the directory if it doesn't exist
			if (!saveFile.exists()) {
				Files.createDirectories(saveFile.toPath());
			}

			// Define the path where the file will be saved
			Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());

			// Copy the file to the target location, replacing existing one if any
			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

			// Set a success message in the session
			session.setAttribute("successMsg", "Added Successfully");
		} else {
			session.setAttribute("errorMsg", "Something Went Wrong");
		}
		return "redirect:/admin/loadAddProduct";
	}

	// View Product
	@GetMapping("/product")
	public String viewProduct(Model m) {
		m.addAttribute("allProducts", productService.fetchAllProduct());
		return "/admin/viewProduct";
	}

	// Remove Product
	@GetMapping("/removeProduct/{id}")
	public String removeProduct(@PathVariable int id) {
		productService.removeProduct(id);
		return "redirect:/admin/product";
	}

	// edit Product
	@GetMapping("/editProduct/{id}")
	public String editProduct(@PathVariable int id, Model m) {
		m.addAttribute("product", productService.findById(id));
		m.addAttribute("catagory", catagoryService.fetchAllCatagory());
		return "admin/editProduct";
	}

	@PostMapping("/updateProduct")
	public String updateProduct(@ModelAttribute ProductsDto productsDto, @RequestParam("prodId") int id,
			@RequestParam("file") MultipartFile file, HttpSession session) {
		if (ObjectUtils.isEmpty(productService.updateProduct(productsDto, id, file))) {
			session.setAttribute("errorMsg", "Invalid Discount");
			return "redirect:/admin/editProduct/" + id;
		}
		productService.updateProduct(productsDto, id, file);
		session.setAttribute("successMsg", "Updated Successfully");
		session.setAttribute("errorMsg", "Something Went Wrong");
		return "redirect:/admin/product";
	}

	// Fetch all User
	@GetMapping("/users")
	public String fetchAllUser(Model m) {
		m.addAttribute("allUser", userService.fetchAllUser("USER"));
		return "admin/showAllUser";
	}

	// Set User Status
	@GetMapping("/updateStatus")
	public String updartStatus(@RequestParam Boolean status, @RequestParam int id, HttpSession session) {

		UserDto updateStatus = userService.updateStatus(status, id);
		if (ObjectUtils.isEmpty(updateStatus)) {
			session.setAttribute("errorMsg", "Something Went Wrong On Server");
		} else {
			session.setAttribute("succMsg", "Update Successfully");
		}
		return "redirect:/admin/users";
	}

	@GetMapping("/orders")
	public String orderUpdatePage(Model m) {
		List<ProductOrderDto> fetchAllOrder = productOrderService.fetchAllOrder();
		m.addAttribute("allOrder", fetchAllOrder);
		return "admin/OrderUpdate";
	}

	@PostMapping("/updateStatus")
	public String updateOrderStatus(@RequestParam String status, Model m, @RequestParam int orderId,
			HttpSession session) {
		if (status == null || status.isEmpty()) {
			m.addAttribute("err", "Status cannot be empty");
			return "/admin/orders";
		}
		ProductOrderDto updateOrderStatus = productOrderService.updateOrderStatus(status, orderId);
		if (ObjectUtils.isEmpty(updateOrderStatus)) {
			m.addAttribute("err", "Something Went Wrong In Server");
		} else {
			m.addAttribute("success", "updated Success");
			boolean sendMailForProductOrder = mailUtil.sendMailForProductOrder(updateOrderStatus);
			if (sendMailForProductOrder) {
				session.setAttribute("mailSucc", "Mail Send Successfully");
				System.out.println();
				System.out.println("Mail Send Successfully");
				System.out.println();
			} else {
				session.setAttribute("mailErr", "Mail Not Send");
			}
		}
		return "redirect:/admin/orders";
	}

	@PostMapping("/searchId")
	public String searchId(@RequestParam String search, Model m) {
		ProductOrderDto searchId = productOrderService.searchId(search);
		m.addAttribute("allOrder", searchId);
		return "admin/OrderUpdate";
	}
}
