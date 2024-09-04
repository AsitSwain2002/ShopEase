package com.org.Shopping_App.Service.ServiceImpl;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.org.Shopping_App.Dto.UserDto;
import com.org.Shopping_App.Entity.User;
import com.org.Shopping_App.Repo.UserRepo;
import com.org.Shopping_App.Service.UserService;
import com.org.Shopping_App.exceptionHandler.ResourceNotFound;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepo userRepo;
	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private PasswordEncoder encoder;

	@Override
	public UserDto saveUser(UserDto userDto) {
		userDto.setPassword(encoder.encode(userDto.getPassword()));
		userDto.setActive(true);
		User user = modelMapper.map(userDto, User.class);
		userRepo.save(user);
		return modelMapper.map(user, UserDto.class);
	}

	public UserDto findByEmail(String email) {
		return modelMapper.map(userRepo.findByEmail(email), UserDto.class);
	}

	@Override
	public List<UserDto> fetchAllUser(String user) {
		List<User> user1 = userRepo.findByRole(user);
		return user1.stream().map((e) -> modelMapper.map(e, UserDto.class)).collect(Collectors.toList());
	}

	@Override
	public UserDto updateStatus(Boolean status, int id) {
		User user = userRepo.findById(id).orElseThrow(() -> new ResourceNotFound("User Not Found"));
		user.setActive(status);
		userRepo.save(user);
		return modelMapper.map(user, UserDto.class);
	}

}
