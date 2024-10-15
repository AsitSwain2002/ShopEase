package com.org.Shopping_App.Service;

import org.springframework.stereotype.Repository;

import com.org.Shopping_App.Dto.ProductOrderDto;
import com.org.Shopping_App.Dto.UserAddressDto;

@Repository
public interface ProductOrderService {

	void saveProductOrder(UserAddressDto userAddressDto, int id , String paymentType);
}
