package com.org.Shopping_App.Repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.org.Shopping_App.Entity.ProductOrder;

public interface ProductOrderRepo extends JpaRepository<ProductOrder, Integer> {

	@Query("SELECT p FROM ProductOrder p WHERE p.user.id = :id")
	Page<ProductOrder> findAllByUserId(@Param("id") int id, Pageable page);

	List<ProductOrder> findByOrderIdContainingIgnoreCase(String id);

}
