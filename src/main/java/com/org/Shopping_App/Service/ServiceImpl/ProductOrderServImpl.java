package com.org.Shopping_App.Service.ServiceImpl;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.org.Shopping_App.Dto.CartDto;
import com.org.Shopping_App.Dto.ProductOrderDto;
import com.org.Shopping_App.Dto.UserAddressDto;
import com.org.Shopping_App.Entity.ProductOrder;
import com.org.Shopping_App.Entity.UserAddress;
import com.org.Shopping_App.Repo.ProductOrderRepo;
import com.org.Shopping_App.Repo.UserAddressRepo;
import com.org.Shopping_App.Service.CartService;
import com.org.Shopping_App.Service.ProductOrderService;
import com.org.Shopping_App.exceptionHandler.ResourceNotFound;
import com.org.Shopping_App.util.AppConstant;

@Service
public class ProductOrderServImpl implements ProductOrderService {

	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private ProductOrderRepo productOrderRepo;

	@Autowired
	private UserAddressRepo userAddressRepo;
	@Autowired
	private CartService cartService;

	@Override
	public void saveProductOrder(UserAddressDto userAddressDto, int id, String paymentType) {
		List<CartDto> carts = cartService.fetchAllCart(id);
		double totalPrice = 0.0;
		for (CartDto cart : carts) {
			ProductOrder order = new ProductOrder();
			totalPrice += cart.getProducts().getDiscountPrice() * cart.getQuantity();
			order.setQuantity(cart.getQuantity());
			order.setUser(cart.getUser());
			order.setProduct(cart.getProducts());
			order.setPrice(totalPrice); // Set total price after looping through carts
			order.setOrderDate(new Date());
			order.setOrderId(UUID.randomUUID().toString());
			order.setPaymentType(paymentType);
			order.setStatus(AppConstant.OrderReceived);
			UserAddress address = modelMapper.map(userAddressDto, UserAddress.class);
			UserAddress savedAddress = userAddressRepo.save(address);
			order.setUserAddress(savedAddress);
			productOrderRepo.save(order);
		}
	}

	public ProductOrderDto searchById(int id) {
		ProductOrder order = productOrderRepo.findById(id).orElseThrow(() -> new ResourceNotFound("User Not Found"));
		return modelMapper.map(order, ProductOrderDto.class);
	}

	@Override
	public List<ProductOrderDto> findAllById(int id) {
		List<ProductOrder> allOrder = productOrderRepo.findAllByUserId(id);
//		System.out.println();
//		System.out.println();
//		System.out.println();
//		System.out.println();
//		System.out.println();
		return allOrder.stream().map((e) -> modelMapper.map(e, ProductOrderDto.class)).collect(Collectors.toList());
	}

}
