package com.covid19army.UserService.dtos;

import java.util.UUID;

public class UserSessionDto {

	private UUID loginRequestDto;
	private boolean isError;
	private TokenDto tokenDto;
	
	public UUID getLoginRequestDto() {
		return loginRequestDto;
	}
	public void setLoginRequestDto(UUID loginRequestDto) {
		this.loginRequestDto = loginRequestDto;
	}
	public TokenDto getTokenDto() {
		return tokenDto;
	}
	public void setTokenDto(TokenDto tokenDto) {
		this.tokenDto = tokenDto;
	}
	public boolean isError() {
		return isError;
	}
	public void setError(boolean isError) {
		this.isError = isError;
	}
	
	
}
