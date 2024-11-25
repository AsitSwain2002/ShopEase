package com.org.Shopping_App.Service;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.org.Shopping_App.Dto.ProductOrderDto;
import com.org.Shopping_App.Dto.UserAddressDto;

@Repository
public interface ProductOrderService {

	void saveProductOrder(UserAddressDto userAddressDto, int id, String paymentType);

	ProductOrderDto searchById(int id);

	List<ProductOrderDto> findAllById(int id);

	void cancelOrder(int orderId);

	List<ProductOrderDto> fetchAllOrder();

	ProductOrderDto updateOrderStatus(String status, int orderId);

	ProductOrderDto searchId(String id);
}
