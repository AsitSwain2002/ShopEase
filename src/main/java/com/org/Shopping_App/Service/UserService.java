package com.org.Shopping_App.Service;

import java.util.List;

import com.org.Shopping_App.Dto.UserDto;

public interface UserService {

	UserDto saveUser(UserDto userDto);

	public UserDto findByEmail(String email);
	
	List<UserDto> fetchAllUser(String user);

	UserDto updateStatus(Boolean status, int id);
}
