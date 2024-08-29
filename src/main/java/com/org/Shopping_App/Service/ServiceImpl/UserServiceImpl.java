package com.org.Shopping_App.Service.ServiceImpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.org.Shopping_App.Dto.UserDto;
import com.org.Shopping_App.Entity.User;
import com.org.Shopping_App.Repo.UserRepo;
import com.org.Shopping_App.Service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepo userRepo;
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public UserDto saveUser(UserDto userDto) {
		User user = modelMapper.map(userDto, User.class);
		userRepo.save(user);
		return modelMapper.map(user, UserDto.class);
	}

}
