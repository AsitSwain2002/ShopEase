package com.org.Shopping_App.Service;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.org.Shopping_App.Dto.ProductOrderDto;
import com.org.Shopping_App.Dto.UserAddressDto;

import jakarta.servlet.http.HttpSession;

@Repository
public interface ProductOrderService {

	void saveProductOrder(UserAddressDto userAddressDto, int id, String paymentType, HttpSession session);

	ProductOrderDto searchById(int id);

	List<ProductOrderDto> findAllById(int id);

	void cancelOrder(int orderId);

	List<ProductOrderDto> fetchAllOrder();

	ProductOrderDto updateOrderStatus(String status, int orderId);

	List<ProductOrderDto> searchId(String id);

}
