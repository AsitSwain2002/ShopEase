package com.org.Shopping_App.Service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import com.org.Shopping_App.Dto.ProductsDto;

public interface ProductService {

	ProductsDto saveProduct(ProductsDto productDto);

	Page<ProductsDto> fetchAllProduct(Integer pageNum, Integer pageSize);

	void removeProduct(int id);

	ProductsDto findById(int id);

	ProductsDto updateProduct(ProductsDto productsDto, int id, MultipartFile file);

	Page<ProductsDto> fetchAllByName(String name, Integer pageNum, Integer pageSize);

	Page<ProductsDto> fetchAllProduct(String keyword, String CatName, Integer pageNum, Integer pageSize);

	Page<ProductsDto> searchByName(String name, Integer pageNum, Integer pageSize);
}
