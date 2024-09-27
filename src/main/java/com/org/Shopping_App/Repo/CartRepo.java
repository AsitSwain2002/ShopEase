package com.org.Shopping_App.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.org.Shopping_App.Entity.Cart;

@Repository
public interface CartRepo extends JpaRepository<Cart, Integer> {

	public Cart findByProductsIdAndUserId(int productsId , int userId);
}
