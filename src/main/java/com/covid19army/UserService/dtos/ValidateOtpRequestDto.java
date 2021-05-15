package com.covid19army.UserService.dtos;

public class ValidateOtpRequestDto {

	private int otp;
	private long mobileNumber;
	
	public int getOtp() {
		return otp;
	}
	public void setOtp(int otp) {
		this.otp = otp;
	}
	public long getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(long mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	
	
}
