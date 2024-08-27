package com.org.Shopping_App.Service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.org.Shopping_App.Dto.ProductsDto;

import jakarta.servlet.http.HttpSession;

public interface ProductService {

	ProductsDto saveProduct(ProductsDto productDto);
	
	List<ProductsDto> fetchAllProduct();
	 
	void removeProduct(int id);
	
	ProductsDto findById(int id);
	
	ProductsDto updateProduct(ProductsDto productsDto , int id , MultipartFile file);
}
