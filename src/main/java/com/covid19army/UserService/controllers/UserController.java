package com.covid19army.UserService.controllers;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.covid19army.UserService.client.TokenServiceClient;
import com.covid19army.UserService.dtos.LoginRequestDto;
import com.covid19army.UserService.dtos.TokenDto;
import com.covid19army.UserService.dtos.UserResponseDto;
import com.covid19army.UserService.dtos.UserTokenRequestDto;
import com.covid19army.UserService.dtos.ValidateOtpRequestDto;
import com.covid19army.UserService.models.User;
import com.covid19army.UserService.services.UserService;
import com.covid19army.UserService.services.UserSessionService;
import com.covid19army.core.common.clients.OtpServiceClient;
import com.covid19army.core.dtos.OtpVerificationRequestDto;
import com.covid19army.core.exceptions.NotAuthorizedException;
import com.covid19army.core.exceptions.ResourceNotFoundException;
import com.covid19army.core.extensions.HttpServletRequestExtension;



@RestController
@RequestMapping("user")
public class UserController {
	Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	UserService _userService;
	
	@Autowired
	TokenServiceClient _tokenServiceClient;
	
	@Autowired
	OtpServiceClient _otpServiceClient;
	
	@Autowired
	ModelMapper _mapper;
	
	@Autowired
	HttpServletRequestExtension _requestExtension;
	
	@Autowired
	UserSessionService _userSessionService;
	
	@GetMapping("/health")
	public String health() {
		return "am running!";
	}
	
	@GetMapping("/header")
	public String header(HttpServletRequest request) {
		return "received" + _requestExtension.getAuthenticatedUser();
	}
	
	@PostMapping("/login")
	public ResponseEntity<UserResponseDto> findOrCreateUser(@RequestBody LoginRequestDto loginRequestDto,  HttpServletRequest request) throws ResourceNotFoundException, NotAuthorizedException{
		logger.info("mobile number entered is: " + loginRequestDto.getMobileNumber());
		User user = _userService.login(loginRequestDto, request.getRemoteAddr());
		var otpResponse = _userService.generateOtp(user);
		UserResponseDto userDto = _mapper.map(user, UserResponseDto.class);
		if(otpResponse != null) {
			userDto.setOtp(otpResponse.getOtp());
			var userSession = _userSessionService.create(user.getUserid(), user.getMobilenumber(), userDto.getOtp());
			if(userSession != null)
				userDto.setLoginRequestId(userSession.getLoginrequestid());
		}
		return new ResponseEntity<>(userDto, HttpStatus.OK);
	}
	
	@GetMapping("/{mobileNumber}")
	public UserResponseDto get(@PathVariable String mobileNumber){
		Optional<User> optionalUser = _userService.getUserByMobileNumber(mobileNumber);
		
		
		UserResponseDto userDto = optionalUser.isPresent()? 
				_mapper.map(optionalUser.get(), UserResponseDto.class): null;
		
		return userDto;
	}
	
	@PostMapping("/validateotp")
	public TokenDto validateOtp(@RequestBody OtpVerificationRequestDto otpRequestDto, HttpServletRequest request) 
			throws ResourceNotFoundException{
		//call otp validation client api
		 TokenDto data = _userService.validateOtp(otpRequestDto, request.getRemoteAddr());
		 return data;
		
	}
}
