package com.org.Shopping_App.Service.ServiceImpl;

import java.time.LocalDate;
import java.util.ArrayList;
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
import com.org.Shopping_App.util.MailUtil;

import jakarta.servlet.http.HttpSession;

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
	@Autowired
	private MailUtil mailUtil;

	@Override
	public void saveProductOrder(UserAddressDto userAddressDto, int id, String paymentType, HttpSession session) {
		List<CartDto> carts = cartService.fetchAllCart(id);
		double totalPrice = 0.0;
		List<ProductOrderDto> productOrderDtos = new ArrayList<>();
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
			address.setProductorder(order);
			userAddressRepo.save(address);
			order.setUserAddress(address);
			ProductOrder save = productOrderRepo.save(order);
			userAddressDto.setProductorder(save);
			mailUtil.sendMailForProductOrders(modelMapper.map(order, ProductOrderDto.class), userAddressDto.getEmail());
		}
		session.setAttribute("AllMailOrder", productOrderDtos);
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
	public List<ProductOrderDto> searchId(String id) {
		List<ProductOrder> findByOrderId = productOrderRepo.findByOrderIdContainingIgnoreCase(id);
		return findByOrderId.stream().map((e) -> modelMapper.map(e, ProductOrderDto.class)).collect(Collectors.toList());
		}

}
