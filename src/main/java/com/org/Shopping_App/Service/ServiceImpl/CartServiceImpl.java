package com.org.Shopping_App.Service.ServiceImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.org.Shopping_App.Dto.CartDto;
import com.org.Shopping_App.Dto.ProductsDto;
import com.org.Shopping_App.Dto.UserDto;
import com.org.Shopping_App.Entity.Cart;
import com.org.Shopping_App.Entity.Products;
import com.org.Shopping_App.Entity.User;
import com.org.Shopping_App.Repo.CartRepo;
import com.org.Shopping_App.Repo.UserRepo;
import com.org.Shopping_App.Service.CartService;
import com.org.Shopping_App.Service.ProductService;
import com.org.Shopping_App.Service.UserService;
import com.org.Shopping_App.exceptionHandler.ResourceNotFound;

@Service
public class CartServiceImpl implements CartService {

	@Autowired
	private UserService userService;

	@Autowired
	private ProductService productService;

	@Autowired
	private CartRepo cartRepo;

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public CartDto saveCart(int pId, int uId) {

		ProductsDto productDto = productService.findById(pId);
		UserDto userDto = userService.findById(uId);
		Cart cart = cartRepo.findByProductsIdAndUserId(pId, uId);
		Cart cart1;
		if (ObjectUtils.isEmpty(cart)) {
			cart1 = new Cart();
			cart1.setProducts(modelMapper.map(productDto, Products.class));
			cart1.setUser(modelMapper.map(userDto, User.class));
			cart1.setQuantity(1);
			cart1.setTotalPrice(1 * productDto.getDiscountPrice());
		} else {
			cart1 = cart;
			cart1.setQuantity(cart1.getQuantity() + 1);
			cart1.setTotalPrice(cart1.getQuantity() * cart1.getProducts().getDiscountPrice());
		}
		Cart cartSave = cartRepo.save(cart1);
		return modelMapper.map(cartSave, CartDto.class);
	}

	@Override
	public List<CartDto> fetchAllCart(int userId) {
		User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFound("User Not Found"));
		List<Cart> carts = user.getCarts();
		return carts.stream().map((e) -> modelMapper.map(e, CartDto.class)).collect(Collectors.toList());
	}

	@Override
	public CartDto removeItem(int id) {
		Cart cart = cartRepo.findById(id).orElseThrow(() -> new ResourceNotFound("Cart Item Not Found"));
		cartRepo.deleteById(id);
		return modelMapper.map(cart, CartDto.class);
	}

	@Override
	public CartDto fetchById(int id) {
		Cart cart = cartRepo.findById(id).orElseThrow(() -> new ResourceNotFound("Cart Not Found"));
		return modelMapper.map(cart, CartDto.class);
	}

	@Override
	public CartDto incrementProd(int id) {
		Cart cart = cartRepo.findById(id).orElseThrow(() -> new ResourceNotFound("Cart Not Found"));
		if(cart.getProducts().getStock() > 0) {
		cart.setQuantity(cart.getQuantity() + 1);
		cart.getProducts().setStock(cart.getProducts().getStock() - 1);
		cartRepo.save(cart);
		}else {
			cart.setQuantity(cart.getQuantity());
		}
		return modelMapper.map(cart, CartDto.class);
	}

	@Override
	public CartDto decrementProd(int id) {
		Cart cart = cartRepo.findById(id).orElseThrow(() -> new ResourceNotFound("Cart Not Found"));
		if (cart.getQuantity() > 1) {
			cart.setQuantity(cart.getQuantity() - 1);
			cart.getProducts().setStock(cart.getProducts().getStock() + 1);
			cartRepo.save(cart);
		} else {
			cart.setQuantity(0);
			cartRepo.save(cart);
		}
		return modelMapper.map(cart, CartDto.class);
	}

}
