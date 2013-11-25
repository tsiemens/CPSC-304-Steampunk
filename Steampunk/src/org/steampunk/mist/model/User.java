package org.steampunk.mist.model;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Calendar;

public class User {

	private byte[] mPasswordHash;
	private byte[] mPasswordSalt;
	private String mUsername;
	private String mEmail;
	private Calendar mDateJoined;
	
	public User() {
		mUsername = null;
		mPasswordHash = null;
		mPasswordSalt = null;
		mEmail = null;
		mDateJoined = null;
	}
	
	public User(String username, byte[] passwordHash, byte[] salt, String email, Calendar dateJoined) {
		mUsername = username;
		mPasswordHash = passwordHash;
		mPasswordSalt = salt;
		mEmail = email;
		mDateJoined = dateJoined;
	}
	
	/**
	 * Creates a copy of user
	 * @param user
	 */
	public User(User user) {
		mUsername = user.getUsername();
		mPasswordHash = user.getPasswordHash();
		mPasswordSalt = user.getPasswordSalt();
		mEmail = user.getEmail();
		mDateJoined = user.getDateJoined();
	}
	
	public String getUsername(){
		return mUsername;
	}
	
	public byte[] getPasswordHash(){
		return mPasswordHash;
	}
	
	public byte[] getPasswordSalt(){
		return mPasswordSalt;
	}
	
	public String getEmail(){
		return mEmail;
	}
	
	public Calendar getDateJoined() {
		return mDateJoined;
	}

	public void setDateJoined(Calendar dateJoined) {
		mDateJoined = dateJoined;
	}

	public void setPasswordHash(byte[] passwordHash) {
		mPasswordHash = passwordHash;
	}
	
	public void setPassword(String pass) {
		mPasswordHash = User.getHash(pass, mPasswordSalt);
	}
	
	public void setPasswordSalt(byte[] passwordSalt) {
		mPasswordSalt = passwordSalt;
	}

	public void setUsername(String username) {
		mUsername = username;
	}

	public void setEmail(String email) {
		mEmail = email;
	}
	
	public static byte[] getHash(String password, byte[] salt){
		try{
	       MessageDigest digest = MessageDigest.getInstance("SHA-256");
	       digest.reset();
	       digest.update(salt);
	       return digest.digest(password.getBytes("UTF-8"));
		} catch (Exception e) {
			System.err.println("Unexpected error hashing password "+e);
		}
		return null;
	 }
	
	public static byte[] generateSalt(){
		return new BigInteger(32, new SecureRandom()).toByteArray();
	}
}
