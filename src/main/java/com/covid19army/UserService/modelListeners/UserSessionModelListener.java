package com.covid19army.UserService.modelListeners;

import java.util.Date;
import java.util.UUID;

import javax.persistence.PrePersist;

import com.covid19army.UserService.models.UserSession;

public class UserSessionModelListener {

	@PrePersist
	public void onCreating(UserSession userSession) {
		Date currDate = new Date();
		userSession.setDateCreated(currDate);
		userSession.setError(false);
	}
}
