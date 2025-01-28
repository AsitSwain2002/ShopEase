package com.org.Shopping_App.Controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
import com.org.Shopping_App.Entity.User;
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
	private ModelMapper modelMapper;

	@Autowired
	private ProductService productService;

	@Autowired
	private UserService userService;

	@Autowired
	private ProductOrderService productOrderService;

	@Autowired
	private MailUtil mailUtil;

	@Autowired
	private BCryptPasswordEncoder encoder;

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
	public String updateCatagoryPage(@PathVariable int id, HttpSession session, Model m) {
		CatagoryDto catagoryDto = catagoryService.findById(id);
		m.addAttribute("catagory", catagoryDto);
		session.setAttribute("catagory", catagoryDto);
		return "/admin/editCatagory";
	}

	@PostMapping("/updateCatagory")
	public String updateCatagory(@ModelAttribute CatagoryDto catagoryDto, @RequestParam int id,
			@RequestParam("file") MultipartFile file, HttpSession session) {
		CatagoryDto updateCatagory = catagoryService.updateCatagory(id, catagoryDto, file);
		if (ObjectUtils.isEmpty(updateCatagory)) {
			session.setAttribute("errorMsg", "Something Went Wrong");
		}
		session.setAttribute("successMsg", "Updated Successfully");
		return "redirect:/admin/updateCatagoryPage/" + id;
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

//	 View Product
	@GetMapping("/product")
	public String viewProduct(Model m, @RequestParam(name = "pageNum", defaultValue = "0") Integer pageNum,
			@RequestParam(name = "pageSize", defaultValue = "5") Integer pageSize) {
		Page<ProductsDto> fetchAllProduct = productService.fetchAllProduct(pageNum, pageSize);
		List<ProductsDto> content = fetchAllProduct.getContent();
		m.addAttribute("size", content.size());
		m.addAttribute("pageSize", fetchAllProduct.getSize());
		m.addAttribute("totalPage", fetchAllProduct.getTotalPages());
		m.addAttribute("pagenumber", fetchAllProduct.getNumber());
		m.addAttribute("totalElement", fetchAllProduct.getTotalElements());
		m.addAttribute("isFirst", fetchAllProduct.isFirst());
		m.addAttribute("isLast", fetchAllProduct.isLast());
		m.addAttribute("allProducts", fetchAllProduct);
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
		return "redirect:/admin/editProduct/"+id;
	}

	// Fetch all User
	@GetMapping("/users")
	public String fetchAllUser(Model m, @RequestParam(name = "pageNum", defaultValue = "0") Integer pageNum,
			@RequestParam(name = "pageSize", defaultValue = "8") Integer pageSize) {
		Page<UserDto> fetchAllUser = userService.fetchAllUser("USER", pageNum, pageSize);
		List<UserDto> content = fetchAllUser.getContent();
		m.addAttribute("size", content.size());
		m.addAttribute("pageSize", fetchAllUser.getSize());
		m.addAttribute("totalPage", fetchAllUser.getTotalPages());
		m.addAttribute("pagenumber", fetchAllUser.getNumber());
		m.addAttribute("totalElement", fetchAllUser.getTotalElements());
		m.addAttribute("isFirst", fetchAllUser.isFirst());
		m.addAttribute("isLast", fetchAllUser.isLast());

		m.addAttribute("allUser", fetchAllUser);
		return "admin/showAllUser";
	}

	@GetMapping("/admins")
	public String fetchAllAdmins(Model m, @RequestParam(name = "pageNum", defaultValue = "0") Integer pageNum,
			@RequestParam(name = "pageSize", defaultValue = "8") Integer pageSize) {
		Page<UserDto> fetchAllUser = userService.fetchAllUser("ADMIN", pageNum, pageSize);
		List<UserDto> content = fetchAllUser.getContent();
		m.addAttribute("size", content.size());
		m.addAttribute("pageSize", fetchAllUser.getSize());
		m.addAttribute("totalPage", fetchAllUser.getTotalPages());
		m.addAttribute("pagenumber", fetchAllUser.getNumber());
		m.addAttribute("totalElement", fetchAllUser.getTotalElements());
		m.addAttribute("isFirst", fetchAllUser.isFirst());
		m.addAttribute("isLast", fetchAllUser.isLast());

		m.addAttribute("allUser", fetchAllUser);
		return "admin/showAllAdmin";
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
	public String orderUpdatePage(Model m, @RequestParam(name = "pageNum", defaultValue = "0") Integer pageNum,
			@RequestParam(name = "pageSize", defaultValue = "8") Integer pageSize) {
		Page<ProductOrderDto> fetchAllOrder = productOrderService.fetchAllOrder(pageNum, pageSize);
		List<ProductOrderDto> content = fetchAllOrder.getContent();
		m.addAttribute("size", content.size());
		m.addAttribute("pageSize", fetchAllOrder.getSize());
		m.addAttribute("totalPage", fetchAllOrder.getTotalPages());
		m.addAttribute("pagenumber", fetchAllOrder.getNumber());
		m.addAttribute("totalElement", fetchAllOrder.getTotalElements());
		m.addAttribute("isFirst", fetchAllOrder.isFirst());
		m.addAttribute("isLast", fetchAllOrder.isLast());
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
//				System.out.println();
//				System.out.println("Mail Send Successfully");
//				System.out.println();
			} else {
				session.setAttribute("mailErr", "Mail Not Send");
			}
		}
		return "redirect:/admin/orders";
	}

	@PostMapping("/searchId")
	public String searchId(@RequestParam String search, Model m) {
		List<ProductOrderDto> searchId = productOrderService.searchId(search);
		m.addAttribute("allOrder", searchId);
		return "admin/OrderUpdate";
	}

	@PostMapping("/searchProductByName")
	public String searchProductByName(@RequestParam String name, Model m,
			@RequestParam(name = "pageNum", defaultValue = "0") Integer pageNum,
			@RequestParam(name = "pageSize", defaultValue = "8") Integer pageSize) {
		Page<ProductsDto> allProducts = productService.searchByName(name, pageNum, pageSize);
		List<ProductsDto> content = allProducts.getContent();
		m.addAttribute("size", content.size());
		m.addAttribute("pageSize", allProducts.getSize());
		m.addAttribute("totalPage", allProducts.getTotalPages());
		m.addAttribute("pagenumber", allProducts.getNumber());
		m.addAttribute("totalElement", allProducts.getTotalElements());
		m.addAttribute("isFirst", allProducts.isFirst());
		m.addAttribute("isLast", allProducts.isLast());
		m.addAttribute("allProducts", allProducts);
		return "/admin/viewProduct";
	}

	@PostMapping("/searchUserByName")
	public String searchUserByName(@RequestParam String name, Model m) {
		List<UserDto> fetchAllByName = userService.fetchAllUserByName(name);
		m.addAttribute("allUser", fetchAllByName);
		return "admin/showAllUser";
	}

	@GetMapping("/addAdmin")
	public String addAdminPage(Model m) {
		m.addAttribute("RoleAdmin", "ADMIN");
		m.addAttribute("user", new UserDto());
		return "register";
	}

}
