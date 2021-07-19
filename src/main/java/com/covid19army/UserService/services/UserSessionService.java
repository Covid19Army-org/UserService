package com.covid19army.UserService.services;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.covid19army.UserService.dtos.TokenDto;
import com.covid19army.UserService.models.UserSession;
import com.covid19army.UserService.repositories.UserSessionRepository;

@Service
public class UserSessionService {
	
	@Value("${covid19army.otp.onscreen.enable:true}")
	boolean isOnscreenOtpEnabled;
	
	@Autowired
	UserSessionRepository _userSessionRepository;

	private UUID generateUuidFromMobileNumberAndOtp(String mobileNumber, int otp) {
		
		String data = mobileNumber+otp;
		return UUID.nameUUIDFromBytes(data.getBytes());
	}
	
	public UserSession create(long userid, String mobileNumber, int otp) {
		
		if(!isOnscreenOtpEnabled)
			return null;
		
		UserSession userSession = new UserSession();
		userSession.setLoginrequestid(this.generateUuidFromMobileNumberAndOtp(mobileNumber, otp));
		userSession.setUserid(userid);
		userSession = _userSessionRepository.save(userSession);
		return userSession;		
	}
	
	public void updateTokenData(String mobileNumber, int otp, TokenDto tokenDto) {
		if(!isOnscreenOtpEnabled)
			return;
		
		UUID loginRequestId = this.generateUuidFromMobileNumberAndOtp(mobileNumber, otp);
		Optional<UserSession> userSessionOpt = _userSessionRepository.findByLoginrequestid(loginRequestId);
		if(userSessionOpt.isPresent()) {
			var userSession = userSessionOpt.get();
			JSONObject tokenJson = new JSONObject(tokenDto);
			userSession.setTokenData(tokenJson.toString());
			_userSessionRepository.save(userSession);		
		}
	}
	
	public void updateError(String mobileNumber, int otp) {
		UUID loginRequestId = this.generateUuidFromMobileNumberAndOtp(mobileNumber, otp);
		Optional<UserSession> userSessionOpt = _userSessionRepository.findByLoginrequestid(loginRequestId);
		if(userSessionOpt.isPresent()) {
			var userSession = userSessionOpt.get();
			Date currDate = new Date();
			userSession.setError(true);
			userSession.setDateUpdated(currDate);
			_userSessionRepository.save(userSession);		
		}
	}
	
	public Optional<UserSession> getUserSession(UUID loginRequestId) {
		if(!isOnscreenOtpEnabled)
			return null;
		return _userSessionRepository.findByLoginrequestid(loginRequestId);		 
	}
}
