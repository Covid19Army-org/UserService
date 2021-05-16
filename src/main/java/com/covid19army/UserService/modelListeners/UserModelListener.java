package com.covid19army.UserService.modelListeners;

import com.covid19army.UserService.models.User;

import java.util.Date;

import javax.persistence.PrePersist;

public class UserModelListener {

	@PrePersist
	public void onCreating(User user) {
		Date currentDate = new Date();
		
		user.setCountrycode(91);
		user.setDateCreated(currentDate);
		user.setDateLastlogin(currentDate);
		user.setDateNotificationLastSeen(currentDate);
		user.setIsactive(true);
		user.setIsmobileverified(false);
		user.setIsvolunteer(false);
		user.setName(null);		
	}
}
