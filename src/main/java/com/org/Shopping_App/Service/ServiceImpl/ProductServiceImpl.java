package com.org.Shopping_App.Service.ServiceImpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.org.Shopping_App.Dto.ProductsDto;
import com.org.Shopping_App.Entity.Products;
import com.org.Shopping_App.Repo.ProductRepo;
import com.org.Shopping_App.Service.ProductService;

import jakarta.servlet.http.HttpSession;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepo productRepo;
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public ProductsDto saveProduct(ProductsDto productDto) {
		Products product = modelMapper.map(productDto, Products.class);
		product.setTitle(productDto.getTitle());
		product.setDescription(productDto.getDescription());
		product.setCatagory(productDto.getCatagory());
		product.setPrice(productDto.getPrice());
		product.setStatus(productDto.isStatus());
		product.setStock(productDto.getStock());
		product.setImageName(productDto.getImageName());

		productRepo.save(product);
		return modelMapper.map(product, ProductsDto.class);
	}

}
