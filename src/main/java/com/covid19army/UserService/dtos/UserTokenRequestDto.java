package com.covid19army.UserService.dtos;

import java.util.UUID;

public class UserTokenRequestDto {
	private String userName;
	private String clientIp;

	public String getClientIp() {
		return clientIp;
	}
	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
}
