package com.org.Shopping_App.Dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductsDto {

	private int id;
	private String title;
	private String description;
	private String catagory;
	private double price;
	private boolean status;
	private int stock;
	private String imageName;
	private int discount;
	private double discountPrice;
}
