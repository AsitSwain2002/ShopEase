package com.org.Shopping_App.Service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.org.Shopping_App.Dto.UserDto;

import jakarta.servlet.http.HttpSession;

public interface UserService {

	UserDto saveUser(UserDto userDto,String role,HttpSession session);

	public UserDto findByEmail(String email);

	Page<UserDto> fetchAllUser(String user,Integer pageNum,Integer pageSize);

	UserDto updateStatus(boolean status, int id);

	void increaseFailedAttemp(UserDto userDto);

	void userAccountLock(UserDto userDto);

	boolean unlockAccountTimeExpaired(UserDto userDto);

	void resetAttempt(int id);

	void updateToken(String email, String token);

	UserDto findByToken(String token);

	UserDto updatePassword(int id, String firstPas);

	UserDto findById(int id);

	UserDto updateUser(UserDto userDto, int Id);

	UserDto updatePassword(String oldPassword, String newPassword , String reEnterPassword, int userId , HttpSession session);
	
	List<UserDto> fetchAllUserByName(String name);
	UserDto saveAdmin(UserDto userDto);
}
