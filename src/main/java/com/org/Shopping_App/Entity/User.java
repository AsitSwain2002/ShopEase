package com.org.Shopping_App.Entity;

import java.util.Date;
import java.util.List;

import org.springframework.validation.annotation.Validated;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String name;
	private String mobile;
	private String email;
	private String address;
	private String city;
	private String state;
	private int pincode;
	private String password;
	private String confirmPassword;
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
