package com.org.Shopping_App.Service;

import java.util.List;
import com.org.Shopping_App.Dto.UserDto;

public interface UserService {

	UserDto saveUser(UserDto userDto);

	public UserDto findByEmail(String email);

	List<UserDto> fetchAllUser(String user);

	UserDto updateStatus(boolean status, int id);

	void increaseFailedAttemp(UserDto userDto);

	void userAccountLock(UserDto userDto);

	boolean unlockAccountTimeExpaired(UserDto userDto);

	void resetAttempt(int id);

	void updateToken(String email, String token);

	UserDto findByToken(String token);

	UserDto updatePassword(int id, String firstPas);

	UserDto findById(int id);
}
