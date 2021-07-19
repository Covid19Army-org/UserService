package com.covid19army.UserService.services;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.covid19army.UserService.client.TokenServiceClient;
import com.covid19army.UserService.dtos.LoginRequestDto;
import com.covid19army.UserService.dtos.TokenDto;
import com.covid19army.UserService.dtos.UserTokenRequestDto;
import com.covid19army.UserService.models.User;
import com.covid19army.UserService.repositories.UserRepository;
import com.covid19army.core.common.clients.OtpServiceClient;
import com.covid19army.core.dtos.MobileVerificationQueueDto;
import com.covid19army.core.dtos.MobileVerificationResponseDto;
import com.covid19army.core.dtos.OtpVerificationRequestDto;
import com.covid19army.core.exceptions.NotAuthorizedException;
import com.covid19army.core.exceptions.ResourceNotFoundException;
import com.covid19army.core.mex.rabbitmq.RabbitMQSender;

import java.util.Date;
import java.util.Optional;

@Service
public class UserService {
	
	Logger logger = LoggerFactory.getLogger(UserService.class);

	@Autowired
	UserRepository _userRepository;
	
	@Autowired
	ModelMapper _mapper;
	
	@Autowired
	@Qualifier("otpExchangeSender")			
	RabbitMQSender _otpExchangeSender;
	
	@Autowired
	OtpServiceClient _otpServiceClient;
	
	@Autowired
	TokenServiceClient _tokenServiceClient;	
	
	@Autowired
	UserSessionService _userSessionService;
	
	@Value("${covid19army.otp.onscreen.enable:true}")
	boolean isOnscreenOtpEnabled;
	
	@Value("${covid19army.user.login.max.attempt:3}")
	int maxAllowedLoginAttempt;
	
	@Value("${covid19army.user.login.max.waittime:30}")
	int maxLoginWaitTime;
	
	@Value("${covid19army.user.login.passcode.enable:false}")
	boolean enablePasscodeAuth;
	
	public boolean userExists(String mobileNumber) {
		return _userRepository.findByMobilenumber(mobileNumber) != null;
	}	
	
	public boolean isUserMobileVerified(String mobileNumber) {
		return _userRepository.existsByMobilenumberAndIsmobileverifiedIsTrue(mobileNumber);
	}
	
	public Optional<User> getUserByMobileNumber(String mobileNumber) {
		return _userRepository.findByMobilenumber(mobileNumber);		
	}
	
	public User login(LoginRequestDto loginRequestDto, String clientIp) throws ResourceNotFoundException, NotAuthorizedException {
		Optional<User> optionalUser = this.getUserByMobileNumber(loginRequestDto.getMobileNumber());
		User user = null ;
		if(optionalUser.isEmpty()) {
			user = new User();
			user.setMobilenumber(loginRequestDto.getMobileNumber());
			user.setClientip(clientIp);
			user.setPasscode(loginRequestDto.getPasscode());
			
			user = _userRepository.save(user);			
		}else {
			user = optionalUser.get();
			
			if(enablePasscodeAuth) {
				Date currDate = new Date();
				if(user.getLoginattempt() >= maxAllowedLoginAttempt && 
						(currDate.getTime() - user.getDateLastFailedLogin()) < maxLoginWaitTime ) {
					throw new NotAuthorizedException( String.format("You have exceeded the max attemps. Please wait for %d mins.", maxLoginWaitTime));
				}
				
				if(user.getPasscode() != loginRequestDto.getPasscode()) {
					
					int loginAttempt = user.getLoginattempt() >= maxAllowedLoginAttempt ? 0 : user.getLoginattempt();					
					user.setLoginattempt(loginAttempt);
					user.setDateLastFailedLogin(currDate.getTime());
					_userRepository.save(user);
					throw new ResourceNotFoundException("mobilenumber or passcode is invalid.");
				}
			}
		}
		
		return user;
	}
	
	public MobileVerificationResponseDto generateOtp(User user) {
		MobileVerificationQueueDto otpdto = new MobileVerificationQueueDto();
		otpdto.setMobilenumber(user.getMobilenumber());
		otpdto.setEntityid(user.getUserid());
		otpdto.setEntitytype("USR");
		
		MobileVerificationResponseDto otpResponseDto = null;
		
		if(!isOnscreenOtpEnabled)
			_otpExchangeSender.<MobileVerificationQueueDto>send(otpdto);
		else
		{
			otpResponseDto = _otpServiceClient.createOtp(otpdto);			
		}
		
		return otpResponseDto;
	}
	
	public void updateIsMobileVerified(String mobileNumber) throws ResourceNotFoundException {
		if(!this.isUserMobileVerified(mobileNumber)) {
			var userObject = getUserByMobileNumber(mobileNumber);
			if(userObject.isEmpty())
				throw new ResourceNotFoundException("User not found.");
			var user = userObject.get();
			user.setIsmobileverified(true);
			_userRepository.save(user);
		}
	}
	
	public TokenDto validateOtp(OtpVerificationRequestDto otpRequestDto, String clientAddr) throws ResourceNotFoundException {
			//call otp validation client api
		 	TokenDto data = null;
			var result = _otpServiceClient.validateOtp(otpRequestDto);
			if(result) {
				this.updateIsMobileVerified(otpRequestDto.getMobilenumber());
				UserTokenRequestDto userTokenRequestDto  = new UserTokenRequestDto();
				userTokenRequestDto.setUserName(otpRequestDto.getMobilenumber());//_mapper.map(otpRequestDto, UserTokenRequestDto.class);			
				userTokenRequestDto.setClientIp(clientAddr);
				
				// return access token
				  data = _tokenServiceClient.GetToken(userTokenRequestDto);
				  _userSessionService.updateTokenData(otpRequestDto.getMobilenumber(), otpRequestDto.getOtp(), data);
				 logger.info(data.getToken());
				 return data;
			}
		throw new ResourceNotFoundException("Invaid Otp.");
	}
}
