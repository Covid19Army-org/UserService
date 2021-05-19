package com.covid19army.UserService.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.covid19army.UserService.models.User;
import com.covid19army.UserService.repositories.UserRepository;
import java.util.Optional;

@Service
public class UserService {

	@Autowired
	UserRepository _userRepository;
	
	public boolean userExists(String mobileNumber) {
		return _userRepository.findByMobilenumber(mobileNumber) != null;
	}	
	
	public Optional<User> getUserByMobileNumber(String mobileNumber) {
		return _userRepository.findByMobilenumber(mobileNumber);		
	}
	
	public User login(String mobileNumber, String clientIp) {
		Optional<User> optionalUser = this.getUserByMobileNumber(mobileNumber);
		User user = null;
		if(!optionalUser.isPresent()) {
			user = new User();
			user.setMobilenumber(mobileNumber);
			user.setClientip(clientIp);
			
			user = _userRepository.save(user);			
		}
		
		return optionalUser.orElse(user);
	}
}
