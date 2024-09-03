package com.org.Shopping_App.Service;

import com.org.Shopping_App.Dto.UserDto;

public interface UserService {

	UserDto saveUser(UserDto userDto);
	public UserDto findByEmail(String email);
}
