package com.org.Shopping_App.util;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import org.springframework.stereotype.Service;

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
		String content = "Hello," +user.getName() +"  You have requested to reset your password.\n"
				+ "Click the link below to change your password:\n" + url + "  Change your password";

		mail.setSubject("Password Reset");
		mail.setText(content);
		javaMailSender.send(mail);
		return true;
	}

}
