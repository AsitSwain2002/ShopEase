package com.org.Shopping_App.Repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.org.Shopping_App.Entity.ProductOrder;

public interface ProductOrderRepo extends JpaRepository<ProductOrder, Integer>{

}
