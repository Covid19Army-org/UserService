package com.covid19army.UserService.mex.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;

import com.covid19army.UserService.dtos.TokenDto;
import com.covid19army.UserService.services.UserService;
import com.covid19army.UserService.services.UserSessionService;
import com.covid19army.core.dtos.OtpVerificationRequestDto;
import com.covid19army.core.exceptions.ResourceNotFoundException;

public class RabbitMQConsumer {

	@Autowired
	UserService _userService;
	
	@Autowired
	UserSessionService _userSessionService;

	@RabbitListener(queues = "${covid19army.rabbitmq.onscreen.otp.validation.queue}")
	public void recievedMessage(OtpVerificationRequestDto message) {		
		System.out.println("Recieved Message From RabbitMQ: " + message.getMobilenumber());
		TokenDto result;
		try {
			result = _userService.validateOtp(message, null);
			System.out.println("Recieved Message From RabbitMQ: " + result);
		} catch (ResourceNotFoundException e) {
			// TODO Auto-generated catch block
			_userSessionService.updateError(message.getMobilenumber(), message.getOtp());
			e.printStackTrace();
		}	
	}
}
