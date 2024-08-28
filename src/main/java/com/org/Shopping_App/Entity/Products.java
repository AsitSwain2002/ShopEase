package com.org.Shopping_App.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Products {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String title;
	@Column(length = 500)
	private String description;
	private String catagory;
	private double price;
	private boolean status;
	private int stock;
	private String imageName;
	private int discount;
	private double discountPrice;
}
