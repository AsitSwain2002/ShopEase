package com.org.Shopping_App.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.org.Shopping_App.Dto.ProductsDto;
import com.org.Shopping_App.Service.ProductService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ProductController {

	@Autowired
	private ProductService productService;

	@GetMapping("/viewDetails/{id}")
	public String viewDetails(@PathVariable int id, Model m, HttpSession session) {
		ProductsDto product = productService.findById(id);
		if (!ObjectUtils.isEmpty(product)) {
			m.addAttribute("productDetails", product);
		}
		return "viewProduct";
	}

	@GetMapping("/productCatgory/{name}")
	public String productCatagory(@PathVariable String name, HttpSession session, Model m,
			@RequestParam(name = "pageNum", defaultValue = "0") Integer pageNum,
			@RequestParam(name = "pageSize", defaultValue = "12") Integer pageSize) {
		m.addAttribute("products", productService.fetchAllByName(name, pageNum, pageSize));
		Page<ProductsDto> fetchAllProduct = productService.fetchAllByName(name, pageNum, pageSize);
		List<ProductsDto> content = fetchAllProduct.getContent();
		m.addAttribute("size", content.size());
		m.addAttribute("pageSize", fetchAllProduct.getSize());
		m.addAttribute("totalPage", fetchAllProduct.getTotalPages());
		m.addAttribute("pagenumber", fetchAllProduct.getNumber());
		m.addAttribute("totalElement", fetchAllProduct.getTotalElements());
		m.addAttribute("isFirst", fetchAllProduct.isFirst());
		m.addAttribute("isLast", fetchAllProduct.isLast());

		session.setAttribute("products", productService.fetchAllByName(name, pageNum, pageSize));
		m.addAttribute("catgoryName", name);
		return "/products";
	}
}
