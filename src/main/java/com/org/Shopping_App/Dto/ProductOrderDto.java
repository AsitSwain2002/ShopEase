package com.org.Shopping_App.Dto;

import java.util.Date;

import com.org.Shopping_App.Entity.User;
import com.org.Shopping_App.Entity.UserAddress;

import lombok.Data;

@Data
public class ProductOrderDto {
	private int id;
	private String orderId;
	private Date orderDate;
	private String status;
	private String paymentType;
	private double price;
	private int quantity;
	private UserAddress userAddres;
	private User user;
}
