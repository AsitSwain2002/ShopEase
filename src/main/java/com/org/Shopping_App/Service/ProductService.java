package com.org.Shopping_App.Service;

import com.org.Shopping_App.Dto.ProductsDto;

import jakarta.servlet.http.HttpSession;

public interface ProductService {

	ProductsDto saveProduct(ProductsDto productDto);
}
