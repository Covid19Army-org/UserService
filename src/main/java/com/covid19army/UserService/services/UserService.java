package com.covid19army.UserService.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.covid19army.UserService.models.User;
import com.covid19army.UserService.repositories.UserRepository;
import com.covid19army.core.dtos.MobileVerificationQueueDto;
import com.covid19army.core.dtos.OtpVerificationRequestDto;
import com.covid19army.core.mex.rabbitmq.RabbitMQSender;

import java.util.Optional;

@Service
public class UserService {

	@Autowired
	UserRepository _userRepository;
	
	@Autowired
	ModelMapper _mapper;
	
	@Autowired
	@Qualifier("otpExchangeSender")
	RabbitMQSender _otpExchangeSender;
	
	public boolean userExists(String mobileNumber) {
		return _userRepository.findByMobilenumber(mobileNumber) != null;
	}	
	
	public Optional<User> getUserByMobileNumber(String mobileNumber) {
		return _userRepository.findByMobilenumber(mobileNumber);		
	}
	
	public User login(String mobileNumber, String clientIp) {
		Optional<User> optionalUser = this.getUserByMobileNumber(mobileNumber);
		User user = null ;
		if(optionalUser.isEmpty()) {
			user = new User();
			user.setMobilenumber(mobileNumber);
			user.setClientip(clientIp);
			
			user = _userRepository.save(user);			
		}else {
			user = optionalUser.get();
		}
		
		MobileVerificationQueueDto otpdto = new MobileVerificationQueueDto();
		otpdto.setMobilenumber(user.getMobilenumber());
		otpdto.setEntityid(user.getUserid());
		otpdto.setEntitytype("USR");
		
		_otpExchangeSender.<MobileVerificationQueueDto>send(otpdto);
		
		return user;
	}
}
