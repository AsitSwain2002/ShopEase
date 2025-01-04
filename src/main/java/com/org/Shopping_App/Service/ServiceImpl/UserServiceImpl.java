package com.org.Shopping_App.Service.ServiceImpl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;

import com.org.Shopping_App.Dto.UserDto;
import com.org.Shopping_App.Entity.User;
import com.org.Shopping_App.Repo.UserRepo;
import com.org.Shopping_App.Service.UserService;
import com.org.Shopping_App.exceptionHandler.ResourceNotFound;
import com.org.Shopping_App.util.AppConstant;

import jakarta.servlet.http.HttpSession;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepo userRepo;
	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private PasswordEncoder encoder;

	@Override
	public UserDto saveUser(UserDto userDto,String role) {
		userDto.setPassword(encoder.encode(userDto.getPassword()));
		userDto.setActive(true);
		if(role.equals("ADMIN")) {
			userDto.setRole("ADMIN");
		}else {
		userDto.setRole("USER");
		}
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
	public Page<UserDto> fetchAllUser(String user,Integer pageNum,Integer pageSize) {		
		Pageable of = PageRequest.of(pageNum, pageSize);
		 Page<User> findByRole = userRepo.findByRole(user,of);
		 List<User> content = findByRole.getContent();
		 List<UserDto> collect = content.stream().map((e) -> modelMapper.map(e, UserDto.class)).collect(Collectors.toList());
		 return new PageImpl<UserDto>(collect,of,findByRole.getTotalElements());
	}

	@Override
	public UserDto updateStatus(boolean status, int id) {
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
	public UserDto updatePassword(int id, String firstPas) {
		User user = userRepo.findById(id).orElseThrow(() -> new ResourceNotFound("User Not Found"));
		String encode = encoder.encode(firstPas);
		user.setPassword(encode);
		user.setToken(null);
		userRepo.save(user);
		return modelMapper.map(user, UserDto.class);
	}

	@Override
	public UserDto findById(int id) {
		User user = userRepo.findById(id).orElseThrow(() -> new ResourceNotFound("User Not Found"));
		return modelMapper.map(user, UserDto.class);
	}

	@Override
	public UserDto updateUser(UserDto userDto, int Id) {
		User user = userRepo.findById(Id).orElseThrow(() -> new ResourceNotFound("User Not Found"));
		user.setName(userDto.getName());
		user.setEmail(userDto.getEmail());
		user.setMobile(userDto.getMobile());
		userRepo.save(user);
		return modelMapper.map(user, UserDto.class);
	}

	@Override
	public UserDto updatePassword(String oldPassword, String newPassword, String reEnterPassword, int userId,
			HttpSession session) {
		User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFound("User Not Found"));

		if (encoder.matches(oldPassword, user.getPassword())) {
			if (newPassword.equals(reEnterPassword)) {
				String encodePas = encoder.encode(newPassword);
				user.setPassword(encodePas);
				userRepo.save(user);
				session.setAttribute("successMsg", "Password Update Successfully");
			} else {
				session.setAttribute("errorMsg", "Password Did Not Match");
			}
		} else {
			session.setAttribute("errorMsg", "New Password Did Not Match");
		}
		return modelMapper.map(user, UserDto.class);
	}

	@Override
	public List<UserDto> fetchAllUserByName(String name) {
		List<User> names = userRepo.findAllByName(name);
		System.out.println();
		System.out.println();
		System.out.println(names);
		System.out.println();
		System.out.println();
		return names.stream().map((n) -> modelMapper.map(n, UserDto.class)).collect(Collectors.toList());
	}

	@Override
	public UserDto saveAdmin(UserDto userDto) {
		userDto.setPassword(encoder.encode(userDto.getPassword()));
		userDto.setActive(true);
		userDto.setRole("ADMIN");
		User user = modelMapper.map(userDto, User.class);
		user.setFailedAttemp(0);
		userRepo.save(user);
		return modelMapper.map(user, UserDto.class);
	}

}
