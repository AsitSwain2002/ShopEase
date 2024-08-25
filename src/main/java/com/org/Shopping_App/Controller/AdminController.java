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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.org.Shopping_App.Dto.CatagoryDto;
import com.org.Shopping_App.Entity.Catagory;
import com.org.Shopping_App.Service.CatagoryService;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.Session;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private CatagoryService catagoryService;

	@GetMapping("/")
	public String viewIndex() {
		return "admin/index";
	}

	@GetMapping("/loadAddProduct")
	public String viewaddProduct() {
		return "admin/addProduct";
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

				File saveFile = new ClassPathResource("static/img").getFile();
				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "category_img" + File.separator
						+ file.getOriginalFilename());
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
	public String updateCatagory(@ModelAttribute CatagoryDto catagoryDto, @RequestParam int id , @RequestParam ("file") MultipartFile file , HttpSession session) {
		catagoryService.updateCatagory(id, catagoryDto, file);
		session.setAttribute("successMsg", "Updated Successfully");
		session.setAttribute("errorMsg", "USomething Went Wrong");
		return"redirect:/admin/updateCatagoryPage/"+catagoryDto.getId();
	}

	@GetMapping("/deleteCatagory/{id}")
	public String deleteCatagory(@PathVariable int id) {
		catagoryService.deleteCatagory(id);
		return "redirect:/admin/catagory";
	}
}
