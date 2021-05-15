package com.covid19army.UserService.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.covid19army.UserService.dtos.TokenDto;
import com.covid19army.UserService.dtos.UserTokenRequestDto;


@FeignClient(name="TokenServiceClient", url="${app.client.tokenservice.url}")
public interface TokenServiceClient {

	@PostMapping("/auth/token")
	public TokenDto GetToken(@RequestBody UserTokenRequestDto userDetailsDto);
	
}
