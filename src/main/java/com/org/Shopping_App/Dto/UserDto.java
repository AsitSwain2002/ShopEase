package com.org.Shopping_App.Dto;

import java.util.Date;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDto {
	private int id;
	@NotBlank(message = "Name Can Not Be Null")
	private String name;
	@NotBlank(message = "Mobile Number Can Not Be Null")
	@Size(max = 10, min = 10, message = "Invaid Number")
	private String mobile;
	@Email
	private String email;
	private String address;
	private String city;
	private String state;
	private int pincode;
	@NotBlank(message = "Password Can Not Be Null")
	@Size(max = 8, min = 6, message = "Invaid Password")
	private String password;
	@NotBlank(message = "Password Can Not Be Null")
	@Size(max = 8, min = 6, message = "Invaid Password")
	private String confirmPassword;
	private String image;
	private String role;
	private boolean isActive;
	private boolean accountLocked;
	private int failedAttemp;
	private Date lockTime;
	private String token;

}
