package com.covid19army.UserService.models;

import java.io.Serializable;
import javax.persistence.*;

import com.covid19army.UserService.modelListeners.UserModelListener;

import java.util.Date;

/**
 * The persistent class for the users database table.
 * 
 */
@EntityListeners(UserModelListener.class)
@Entity
@Table(name="users")
@NamedQuery(name="User.findAll", query="SELECT u FROM User u")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long userid;	

	private String clientip;

	private int countrycode;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="date_created")
	private Date dateCreated;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="date_lastlogin")
	private Date dateLastlogin;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="date_notification_last_seen")
	private Date dateNotificationLastSeen;

	private boolean isactive;

	private boolean ismobileverified;

	private boolean isvolunteer;

	private String mobilenumber;

	private String name;	

	public User() {
	}

	public long getUserid() {
		return this.userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public String getClientip() {
		return this.clientip;
	}

	public void setClientip(String clientip) {
		this.clientip = clientip;
	}

	public int getCountrycode() {
		return this.countrycode;
	}

	public void setCountrycode(int countrycode) {
		this.countrycode = countrycode;
	}

	public Date getDateCreated() {
		return this.dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getDateLastlogin() {
		return this.dateLastlogin;
	}

	public void setDateLastlogin(Date dateLastlogin) {
		this.dateLastlogin = dateLastlogin;
	}

	public Date getDateNotificationLastSeen() {
		return this.dateNotificationLastSeen;
	}

	public void setDateNotificationLastSeen(Date dateNotificationLastSeen) {
		this.dateNotificationLastSeen = dateNotificationLastSeen;
	}

	public boolean getIsactive() {
		return this.isactive;
	}

	public void setIsactive(boolean isactive) {
		this.isactive = isactive;
	}

	public boolean getIsmobileverified() {
		return this.ismobileverified;
	}

	public void setIsmobileverified(boolean ismobileverified) {
		this.ismobileverified = ismobileverified;
	}

	public boolean getIsvolunteer() {
		return this.isvolunteer;
	}

	public void setIsvolunteer(boolean isvolunteer) {
		this.isvolunteer = isvolunteer;
	}

	public String getMobilenumber() {
		return this.mobilenumber;
	}

	public void setMobilenumber(String mobilenumber) {
		this.mobilenumber = mobilenumber;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}	
}