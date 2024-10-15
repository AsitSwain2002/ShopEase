package com.org.Shopping_App.Repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.org.Shopping_App.Entity.UserAddress;

public interface UserAddressRepo extends JpaRepository<UserAddress, Integer> {

}
