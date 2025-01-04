package com.org.Shopping_App.Service.ServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.org.Shopping_App.Dto.UserDto;
import com.org.Shopping_App.Entity.User;
import com.org.Shopping_App.Repo.UserRepo;
import com.org.Shopping_App.Service.AdminService;

@Service
public class AdminServiceImpl implements AdminService {

	@Autowired
	private UserRepo userRepo;
	@Autowired
	private  ModelMapper mapper;
	@Override
	public Page<UserDto> fetchAllAdmin(String roleString, int pageNum, int pageSize) {
		Pageable of = PageRequest.of(pageNum, pageSize);
		Page<User> findByRole = userRepo.findByRole(roleString, of);
		List<User> content = findByRole.getContent();
		List<UserDto> collect = content.stream().map(m -> mapper.map(content, UserDto.class)).collect(Collectors.toList());
		return new PageImpl<UserDto>(collect,of,findByRole.getTotalElements());
	}

}
