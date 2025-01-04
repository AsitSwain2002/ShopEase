package com.org.Shopping_App.Service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.org.Shopping_App.Dto.UserDto;

public interface AdminService {

	Page<UserDto> fetchAllAdmin(String roleString , int pageNum , int pageSize);
}
