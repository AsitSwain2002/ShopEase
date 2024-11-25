package com.org.Shopping_App.Service.ServiceImpl;

import java.time.LocalDate;
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
			order.setPrice(totalPrice);
			order.setOrderDate(LocalDate.now());
			order.setOrderId(UUID.randomUUID().toString());
			order.setPaymentType(paymentType);
			order.setOrderStatus(AppConstant.OrderReceived);
			UserAddress address = modelMapper.map(userAddressDto, UserAddress.class);
			userAddressRepo.save(address);
			order.setUserAddress(address);
			productOrderRepo.save(order);
		}
	}

	public ProductOrderDto searchById(int id) {
		ProductOrder order = productOrderRepo.findById(id).orElseThrow(() -> new ResourceNotFound("Product Not Found"));
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

	@Override
	public void cancelOrder(int orderId) {
		ProductOrder order = productOrderRepo.findById(orderId)
				.orElseThrow(() -> new ResourceNotFound("Product Not Found"));
		productOrderRepo.delete(order);

	}

	@Override
	public List<ProductOrderDto> fetchAllOrder() {
		List<ProductOrder> findAll = productOrderRepo.findAll();
		return findAll.stream().map((e) -> modelMapper.map(e, ProductOrderDto.class)).collect(Collectors.toList());
	}

	@Override
	public ProductOrderDto updateOrderStatus(String status, int orderId) {
		ProductOrder order = productOrderRepo.findById(orderId)
				.orElseThrow(() -> new ResourceNotFound("Order Not Found"));
		order.setOrderStatus(status);
		productOrderRepo.save(order);
		return modelMapper.map(order, ProductOrderDto.class);
	}

	@Override
	public ProductOrderDto searchId(String id) {
		ProductOrder findByOrderId = productOrderRepo.findByOrderId(id);
		return modelMapper.map(findByOrderId, ProductOrderDto.class);
	}

}
