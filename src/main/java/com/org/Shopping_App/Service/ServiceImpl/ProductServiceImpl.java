package com.org.Shopping_App.Service.ServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.org.Shopping_App.Dto.CatagoryDto;
import com.org.Shopping_App.Dto.ProductsDto;
import com.org.Shopping_App.Entity.Catagory;
import com.org.Shopping_App.Entity.Products;
import com.org.Shopping_App.Repo.ProductRepo;
import com.org.Shopping_App.Service.ProductService;
import com.org.Shopping_App.exceptionHandler.ResourceNotFound;

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

	@Override
	public List<ProductsDto> fetchAllProduct() {
		List<Products> findAll = productRepo.findAll();
		return findAll.stream().map((e) -> modelMapper.map(e, ProductsDto.class)).collect(Collectors.toList());
	}

	@Override
	public void removeProduct(int id) {

		Products product = productRepo.findById(id).orElseThrow(() -> new ResourceNotFound("Product Not Found"));
		productRepo.delete(product);
	}

	@Override
	public ProductsDto findById(int id) {
		Products product = productRepo.findById(id).orElseThrow(() -> new ResourceNotFound("product Not Found"));
		return modelMapper.map(product, ProductsDto.class);
	}

	@Override
	public ProductsDto updateProduct(ProductsDto productDto, int id, MultipartFile file) {
		Products product = productRepo.findById(id).orElseThrow(() -> new ResourceNotFound("Product Not Found"));
		String imageName = "";
		if (!file.isEmpty()) {
			imageName = file.getOriginalFilename();
		} else {
			imageName = product.getImageName();
		}
		product.setTitle(productDto.getTitle());
		product.setDescription(productDto.getDescription());
		product.setCatagory(productDto.getCatagory());
		product.setPrice(productDto.getPrice());
		product.setStatus(productDto.isStatus());
		product.setStock(productDto.getStock());
		product.setImageName(imageName);
		productRepo.save(product);
		return modelMapper.map(product, ProductsDto.class);
	}

}
