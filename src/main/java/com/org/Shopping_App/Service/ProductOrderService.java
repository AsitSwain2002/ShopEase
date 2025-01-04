package com.org.Shopping_App.Service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import com.org.Shopping_App.Dto.ProductOrderDto;
import com.org.Shopping_App.Dto.UserAddressDto;

import jakarta.servlet.http.HttpSession;

@Repository
public interface ProductOrderService {

	void saveProductOrder(UserAddressDto userAddressDto, int id, String paymentType, HttpSession session);

	ProductOrderDto searchById(int id);

	Page<ProductOrderDto> findAllById(int id, Integer pageNum, Integer pageSize);

	void cancelOrder(int orderId);

	Page<ProductOrderDto> fetchAllOrder(Integer pagrNum,Integer pageSize);

	ProductOrderDto updateOrderStatus(String status, int orderId);

	List<ProductOrderDto> searchId(String id);

}
