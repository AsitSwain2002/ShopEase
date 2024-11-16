package com.org.Shopping_App.Entity;

import java.time.LocalDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
public class ProductOrder {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String orderId;
	private LocalDate orderDate;
	private String status;
	private String paymentType;
	private double price;
	private int quantity;
	@ManyToOne
	private User user;
	@ManyToOne
	private Products product;
	@OneToOne(cascade = CascadeType.ALL)
	private UserAddress userAddress;

}
