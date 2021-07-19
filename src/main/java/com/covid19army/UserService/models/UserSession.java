package com.covid19army.UserService.models;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.covid19army.UserService.modelListeners.UserSessionModelListener;

@EntityListeners(UserSessionModelListener.class)
@Table(name="usersessions")
@Entity
public class UserSession {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long sessionid;	
	
	private long userid;
	
	private UUID loginrequestid;
	
	private String tokenData;
	
	private boolean isError;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="date_updated")
	private Date dateUpdated;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="date_created")
	private Date dateCreated;

	public long getSessionid() {
		return sessionid;
	}

	public void setSessionid(long sessionid) {
		this.sessionid = sessionid;
	}

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public UUID getLoginrequestid() {
		return loginrequestid;
	}

	public void setLoginrequestid(UUID loginrequestid) {
		this.loginrequestid = loginrequestid;
	}

	public String getTokenData() {
		return tokenData;
	}

	public void setTokenData(String tokenData) {
		this.tokenData = tokenData;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public boolean isError() {
		return isError;
	}

	public void setError(boolean isError) {
		this.isError = isError;
	}

	public Date getDateUpdated() {
		return dateUpdated;
	}

	public void setDateUpdated(Date dateUpdated) {
		this.dateUpdated = dateUpdated;
	}
	
	
}
