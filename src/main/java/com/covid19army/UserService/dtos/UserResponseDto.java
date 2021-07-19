package com.covid19army.UserService.dtos;

import java.util.UUID;

public class UserResponseDto {
	private long userid;
	private String mobileNumber;	
	private boolean isactive;
	private int Otp;
	private UUID loginRequestId; 
	
	public int getOtp() {
		return Otp;
	}
	public void setOtp(int otp) {
		Otp = otp;
	}
	public long getUserid() {
		return userid;
	}
	public void setUserid(long userid) {
		this.userid = userid;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public boolean isIsactive() {
		return isactive;
	}
	public void setIsactive(boolean isactive) {
		this.isactive = isactive;
	}
	public UUID getLoginRequestId() {
		return loginRequestId;
	}
	public void setLoginRequestId(UUID loginRequestId) {
		this.loginRequestId = loginRequestId;
	}	
}
