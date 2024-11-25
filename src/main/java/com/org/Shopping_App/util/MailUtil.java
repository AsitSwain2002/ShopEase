package com.org.Shopping_App.util;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import org.springframework.stereotype.Service;

import com.org.Shopping_App.Dto.ProductOrderDto;
import com.org.Shopping_App.Dto.UserDto;
import com.org.Shopping_App.Service.UserService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class MailUtil {

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private UserService userService;

	public String generateUrl(HttpServletRequest req) {

		String url = req.getRequestURL().toString();
		return url.replace(req.getServletPath(), "");
	}

	public boolean sendEmail(String url, String email) throws UnsupportedEncodingException, MessagingException {

		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo(email);

		UserDto user = userService.findByEmail(email);
		String content = "Hello," + user.getName() + "  You have requested to reset your password.\n"
				+ "Click the link below to change your password:\n" + url + "  Change your password";

		mail.setSubject("Password Reset");
		mail.setText(content);
		javaMailSender.send(mail);
		return true;
	}

	public boolean sendMailForProductOrder(ProductOrderDto productOrderDto) {
		try {
			MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

			helper.setFrom("asitoctafx@gmail.com");
			helper.setTo(productOrderDto.getUserAddress().getEmail());
			helper.setSubject("Order Status");

			String content = "<p>Your Order Status: <strong>" + productOrderDto.getOrderStatus() + "</strong></p>"
					+ "<p><strong>Product Details:</strong></p>" + "<ul>" + "<li>Name: "
					+ productOrderDto.getProduct().getTitle() + "</li>" + "<li>Category: "
					+ productOrderDto.getProduct().getCatagory() + "</li>" + "<li>Quantity: "
					+ productOrderDto.getQuantity() + "</li>" + "<li>Price: "
					+ (productOrderDto.getProduct().getDiscountPrice() * productOrderDto.getQuantity()) + "</li>"
					+ "<li>Payment Type: " + productOrderDto.getPaymentType() + "</li>" + "</ul>" + "<br>" + "<br>"
					+ "<p> <strong>Thank You For Shopping With Us  : SHOPEASE</strong></p>";

			helper.setText(content, true);

			javaMailSender.send(mimeMessage);
			return true;
		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		}
	}

}
