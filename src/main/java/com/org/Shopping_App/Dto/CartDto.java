package com.org.Shopping_App.Dto;

import com.org.Shopping_App.Entity.Products;
import com.org.Shopping_App.Entity.User;

import jakarta.persistence.Transient;
import lombok.Data;

@Data
public class CartDto {

	private int id;
	private int quantity;
	@Transient
	private double totalPrice;
	private Products products;
	private User user;
}
