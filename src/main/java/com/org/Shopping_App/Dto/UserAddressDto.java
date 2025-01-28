package com.org.Shopping_App.Dto;

import com.org.Shopping_App.Entity.ProductOrder;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserAddressDto {
	
	@NotBlank(message = "Name Required")
	private String firstName;
	@NotBlank(message = "Last Name Required")
	private String lastName;
	@NotBlank(message = "Email Required")
	@Email
	private String email;
	@NotBlank(message = "Mobile Number Required")
	@Size(min = 10,max = 10 ,message = "Invalid Number")
	private String mobile;
	@NotBlank(message = "Address Number Required")
	private String address;
	@NotBlank(message = "City Number Required")
	private String city;
	@NotBlank(message = "State Number Required")
	private String state;
	@NotBlank(message = "Pincode Number Required")
	@Size(min = 6,max = 6 ,message = "Invalid PinCode")
	private String pincode;
	private ProductOrder productorder;
}
