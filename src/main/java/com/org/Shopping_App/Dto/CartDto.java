package com.org.Shopping_App.Dto;

import jakarta.persistence.Transient;
import lombok.Data;

@Data
public class CartDto {

	private int id;
	private int quantity;
	@Transient
	private double totalPrice;
}
