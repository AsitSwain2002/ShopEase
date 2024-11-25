package com.org.Shopping_App.Dto;

import java.time.LocalDate;

import com.org.Shopping_App.Entity.Products;
import com.org.Shopping_App.Entity.User;
import com.org.Shopping_App.Entity.UserAddress;

import lombok.Data;

@Data
public class ProductOrderDto {

	private int id;
	private String orderId;
	private LocalDate orderDate;
	private String orderStatus;
	private String paymentType;
	private double price;
	private int quantity;
	private UserAddress userAddress;
	private User user;
	private Products product;
}
