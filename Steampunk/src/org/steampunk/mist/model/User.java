package org.steampunk.mist.model;

import java.util.Calendar;

public class User {

	private String mPasswordHash;
	private String mUsername;
	private String mEmail;
	private Calendar mDateJoined;
	
	public User(String username, String passwordHash, String email, Calendar dateJoined) {
		mUsername = username;
		mPasswordHash = passwordHash;
		mEmail = email;
		mDateJoined = dateJoined;
	}
	
	public String getUsername(){
		return mUsername;
	}
	
	public static String hashPassword(String password){
		String hash = new String();
		// do some stuff
		return hash;
	}
}
