package com.org.Shopping_App.Dto;

import lombok.Data;

@Data
public class UserAddressDto {
	private String firstName;
	private String lastName;
	private String email;
	private long mobile;
	private String address;
	private String city;
	private String state;
	private long pincode;
}
