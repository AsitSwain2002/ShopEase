package com.org.Shopping_App.Service.ServiceImpl;

import java.util.List;

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
import com.org.Shopping_App.Service.CartService;
import com.org.Shopping_App.Service.ProductService;
import com.org.Shopping_App.Service.UserService;

@Service
public class CartServiceImpl implements CartService {

	@Autowired
	private UserService userService;

	@Autowired
	private ProductService productService;

	@Autowired
	private CartRepo cartRepo;

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
	public List<CartDto> fetchAllCart(int cartId) {
		// TODO Auto-generated method stub
		return null;
	}

}
