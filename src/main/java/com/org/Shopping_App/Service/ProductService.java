package com.org.Shopping_App.Service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.org.Shopping_App.Dto.ProductOrderDto;
import com.org.Shopping_App.Dto.ProductsDto;

public interface ProductService {

	ProductsDto saveProduct(ProductsDto productDto);

	List<ProductsDto> fetchAllProduct(Integer pageNum, Integer pageSize);

	void removeProduct(int id);

	ProductsDto findById(int id);

	ProductsDto updateProduct(ProductsDto productsDto, int id, MultipartFile file);

	List<ProductsDto> fetchAllByName(String name);

	List<ProductsDto> fetchAllProduct(String keyword, String CatName);

	List<ProductsDto> searchByName(String name);
}
