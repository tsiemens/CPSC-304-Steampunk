package org.steampunk.mist.model;

import java.util.Calendar;

public class GameCopy {

	private String mGameKey; // primary key
	private int mGameID; // foreign key
	private String mOwnerUsername; // foreign key
	private Calendar mPurchaseDate;
	
	// constructors
	public GameCopy() {
		mGameKey = null;
		mGameID = 0;
		mOwnerUsername = null;
		mPurchaseDate = null;
	}
	
	public GameCopy (String gameKey, int gameID, String ownerUsername, Calendar purchaseDate) {
		mGameKey = gameKey;
		mGameID = gameID;
		mOwnerUsername = ownerUsername;
		mPurchaseDate = purchaseDate;
	}
	
	// accessors
	public String getGameKey() {
		return mGameKey;
	}
	
	public int getGameID() {
		return mGameID;
	}
	
	public String getOwnerUsername() {
		return mOwnerUsername;
	}
	
	public Calendar getPurchaseDate() {
		return mPurchaseDate;
	}
	
	
	// mutators
	public void setGameKey(String gameKey) {
		mGameKey = gameKey;
	}
	
	public void setGameID(int gameID) {
		mGameID = gameID;
	}
	
	public void setOwnerUsername(String ownerUsername) {
		mOwnerUsername = ownerUsername;
	}
	
	public void setPurchaseDate(Calendar purchaseDate) {
		mPurchaseDate = purchaseDate;
	}
}
