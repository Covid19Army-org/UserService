package com.covid19army.UserService.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.covid19army.UserService.dtos.TokenDto;
import com.covid19army.UserService.dtos.UserSessionDto;
import com.covid19army.UserService.services.UserSessionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("session")
public class UserSessionsController {

	@Autowired
	UserSessionService _userSessionService;
	
	@GetMapping("/getToken")
	public ResponseEntity<UserSessionDto> getTokenForLoginRequestId(@PathVariable UUID loginRequestId) throws JsonMappingException, JsonProcessingException{
		
		var userSessionOpt = _userSessionService.getUserSession(loginRequestId);
		UserSessionDto dto = new UserSessionDto();
		if(userSessionOpt.isPresent()) {
			var userSession = userSessionOpt.get();
			dto.setLoginRequestDto(loginRequestId);
			ObjectMapper objectMapper = new ObjectMapper();
			var tokenData = objectMapper.readValue(userSession.getTokenData(), TokenDto.class);	
			dto.setTokenDto(tokenData);
			dto.setError(userSession.isError());
		}
		
		return new ResponseEntity<>(dto,HttpStatus.OK);
		
	}
}
