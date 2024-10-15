package com.org.Shopping_App.Service;

import java.util.List;

import com.org.Shopping_App.Dto.CartDto;

public interface CartService {

	public CartDto saveCart(int pId, int uId);

	public List<CartDto> fetchAllCart(int cartId);

	public CartDto removeItem(int id);

	public CartDto fetchById(int id);

	public CartDto incrementProd(int id);

	public CartDto decrementProd(int id);

	void removeAllCartItem(int userId);
}
