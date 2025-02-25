package com.org.Shopping_App.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.org.Shopping_App.Entity.Cart;

@Repository
public interface CartRepo extends JpaRepository<Cart, Integer> {

	Cart findByProductsIdAndUserId(int productsId, int userId);

	List<Cart> findAllByUserId(int id);
}
