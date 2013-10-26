package org.steampunk.mist;

import org.steampunk.mist.model.User;

public class AccountManager {

	private static AccountManager sInstance; 
	private User mCurrentUser;
	
	public static AccountManager getInstance() {
		if (sInstance == null) {
			sInstance = new AccountManager();
		}
		
		return sInstance;
	}
	
	public AccountManager() {
		// TODO Auto-generated constructor stub
	}
	
	public void setCurrentUser(User user){
		mCurrentUser = user;
	}
	
	public User getCurrentUser(){
		return mCurrentUser;
	}
}
