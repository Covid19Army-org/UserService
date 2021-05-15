package com.covid19army.UserService.controllers;

import javax.servlet.http.HttpServletRequest;

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
import com.covid19army.UserService.dtos.TokenDto;
import com.covid19army.UserService.dtos.UserResponseDto;
import com.covid19army.UserService.dtos.UserTokenRequestDto;
import com.covid19army.UserService.dtos.ValidateOtpRequestDto;



@RestController
@RequestMapping("user")
public class UserController {
	Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	TokenServiceClient _tokenServiceClient;
	
	@GetMapping("/health")
	public String health() {
		return "am running!";
	}
	
	@PostMapping("/login")
	public ResponseEntity<String> findOrCreateUser(@RequestBody long mobileNumber){
		
		return new ResponseEntity<>("success", HttpStatus.OK);
	}
	
	@GetMapping("/{mobileNumber}")
	public UserResponseDto get(@PathVariable long mobileNumber){
		UserResponseDto dto = new UserResponseDto();
		dto.setMobileNumber(9986534946L);
		dto.setUserid(1234567890L);
		return dto;
	}
	
	@PostMapping("/validateotp")
	public TokenDto validateOtp(@RequestBody ValidateOtpRequestDto otpRequestDto, HttpServletRequest request){
		UserResponseDto dto = new UserResponseDto();
		dto.setMobileNumber(9986534946L);
		dto.setUserid(1234567890L);
		
		UserTokenRequestDto userTokenRequestDto = new UserTokenRequestDto();
		userTokenRequestDto.setUserName("9986534946");
		userTokenRequestDto.setClientIp(request.getRemoteAddr());
		
		// return access token
		 TokenDto data = _tokenServiceClient.GetToken(userTokenRequestDto);
		 logger.info(data.getToken());
		return data;
	}
}
