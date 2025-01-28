package com.org.Shopping_App.Service.ServiceImpl;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.org.Shopping_App.Dto.CatagoryDto;
import com.org.Shopping_App.Dto.ProductOrderDto;
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
		product.setDiscount(0);
		product.setDiscountPrice(productDto.getPrice());

		productRepo.save(product);
		return modelMapper.map(product, ProductsDto.class);
	}

	@Override
	public Page<ProductsDto> fetchAllProduct(Integer pageNum, Integer pageSize) {
		Pageable pageable = PageRequest.of(pageNum, pageSize);
		Page<Products> findAll = productRepo.findAll(pageable);
		List<ProductsDto> dtoList = findAll.stream().map(e -> modelMapper.map(e, ProductsDto.class))
				.collect(Collectors.toList());

		return new PageImpl<>(dtoList, pageable, findAll.getTotalElements());
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
		if (productDto.getDiscount() >= 0 && productDto.getDiscount() <= 100) {
			product.setTitle(productDto.getTitle());
			product.setDescription(productDto.getDescription());
			product.setCatagory(productDto.getCatagory());

			product.setStatus(productDto.isStatus());
			product.setStock(productDto.getStock());
			product.setImageName(imageName);

			// discount
			Double discountPrice = (productDto.getDiscount() * productDto.getPrice()) / 100;
			Double originalPrice = productDto.getPrice() - discountPrice;
			BigDecimal bd = new BigDecimal(originalPrice).setScale(2, RoundingMode.HALF_UP);
			originalPrice = bd.doubleValue();
			// discount End
			product.setDiscount(productDto.getDiscount());
			product.setDiscountPrice(originalPrice);
			productRepo.save(product);
			if (!file.isEmpty()) {
				try {
					File saveFile = new ClassPathResource("static/img/category_img").getFile();
					Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
					Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
			return modelMapper.map(product, ProductsDto.class);
		} else {
			System.out.println("Else Block Execute");
			return null;
		}
	}

	@Override
	public Page<ProductsDto> fetchAllByName(String name, Integer pageNum, Integer pageSize) {
		Pageable of = PageRequest.of(pageNum, pageSize);
		Page<Products> findAllByCatagory = productRepo.findAllByCatagory(name, of);
		List<ProductsDto> collect = findAllByCatagory.stream().map((e) -> modelMapper.map(e, ProductsDto.class))
				.collect(Collectors.toList());
		return new PageImpl<>(collect, of, findAllByCatagory.getTotalElements());
	}

	@Override
	public Page<ProductsDto> fetchAllProduct(String keyword, String CatName, Integer pageNum, Integer pageSize) {
		Pageable of = PageRequest.of(pageNum, pageSize);
		Page<Products> products = productRepo.findByTitleContainingIgnoreCaseOrCatagoryContainingIgnoreCase(keyword,
				CatName, of);
		List<ProductsDto> collect = products.stream().map((m) -> modelMapper.map(m, ProductsDto.class))
				.collect(Collectors.toList());
		return new PageImpl<>(collect, of, products.getTotalElements());
	}

	@Override
	public Page<ProductsDto> searchByName(String name, Integer pageNum, Integer pageSize) {
		Pageable of = PageRequest.of(pageNum, pageSize);
		Page<Products> products = productRepo.findByTitleContainingIgnoreCaseOrCatagoryContainingIgnoreCase(name, name,
				of);
		List<ProductsDto> collect = products.stream().map((m) -> modelMapper.map(m, ProductsDto.class))
				.collect(Collectors.toList());
		return new PageImpl<>(collect, of, products.getTotalElements());
	}

}
