package com.covid19army.UserService.dtos;

public class UserResponseDto {
	private long userid;
	private long mobileNumber;	
	public long getUserid() {
		return userid;
	}
	public void setUserid(long userid) {
		this.userid = userid;
	}
	public long getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(long mobileNumber) {
		this.mobileNumber = mobileNumber;
	}	
}
