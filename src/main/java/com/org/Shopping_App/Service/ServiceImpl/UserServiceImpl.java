package com.org.Shopping_App.Service.ServiceImpl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.org.Shopping_App.Dto.UserDto;
import com.org.Shopping_App.Entity.User;
import com.org.Shopping_App.Repo.UserRepo;
import com.org.Shopping_App.Service.UserService;
import com.org.Shopping_App.exceptionHandler.ResourceNotFound;
import com.org.Shopping_App.util.AppConstant;

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
		userDto.setRole("USER");
		User user = modelMapper.map(userDto, User.class);
		user.setFailedAttemp(0);
		userRepo.save(user);
		return modelMapper.map(user, UserDto.class);
	}

	public UserDto findByEmail(String email) {
		User userEmail = userRepo.findByEmail(email);
		if (userEmail != null) {
			return modelMapper.map(userEmail, UserDto.class);
		} else {
			return null;
		}
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

	@Override
	public void increaseFailedAttemp(UserDto userDto) {
		User user = modelMapper.map(userDto, User.class);
		int failedAttemp = user.getFailedAttemp() + 1;
		user.setFailedAttemp(failedAttemp);
		userRepo.save(user);
	}

	@Override
	public void userAccountLock(UserDto userDto) {
		User user = modelMapper.map(userDto, User.class);
		user.setAccountLocked(false);
		user.setLockTime(new Date());
		userRepo.save(user);

	}

	@Override
	public boolean unlockAccountTimeExpaired(UserDto userDto) {
		User user = modelMapper.map(userDto, User.class);
		Long lockTime = user.getLockTime().getTime();
		Long unlockTime = lockTime + AppConstant.Unlock_Duration_Time;
		long currentTime = System.currentTimeMillis();
		if (unlockTime < currentTime) {
			user.setAccountLocked(true);
			user.setFailedAttemp(0);
			user.setLockTime(null);
			userRepo.save(user);
			return true;
		}
		return false;
	}

	@Override
	public void resetAttempt(int id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateToken(String email, String token) {
		User user = userRepo.findByEmail(email);
		user.setToken(token);
		userRepo.save(user);
	}

	@Override
	public UserDto findByToken(String token) {
		User user = userRepo.findByToken(token);
		return modelMapper.map(user, UserDto.class);
	}

	@Override
	public UserDto updatePassword( int id , String firstPas) {
		User user = userRepo.findById(id).orElseThrow(() -> new ResourceNotFound("User Not Found"));
		String encode = encoder.encode(firstPas);
		user.setPassword(encode);
		user.setToken(null);
		userRepo.save(user);
		return modelMapper.map(user, UserDto.class);
	}

}
