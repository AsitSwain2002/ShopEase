package com.org.Shopping_App.Dto;

import lombok.Data;

@Data
public class UserDto {
	private int user_Id;
	private String name;
	private long mobile;
	private String email;
	private String address;
	private String city;
	private String state;
	private int pincode;
	private String password;
	private String image;
}
