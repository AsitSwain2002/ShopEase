package com.org.Shopping_App.Entity;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String name;
	private long mobile;
	private String email;
	private String address;
	private String city;
	private String state;
	private int pincode;
	private String password;
	private String image;
	private String role;
	private boolean isActive;
	private boolean accountLocked;
	private int failedAttemp;
	private Date lockTime;
	private String token;
	@OneToMany(mappedBy = "user")
	private List<Cart> carts;
}
