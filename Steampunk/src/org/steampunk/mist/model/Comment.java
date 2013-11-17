package org.steampunk.mist.model;

import java.util.Calendar;

public class Comment {

	private String mText;
	private Calendar mTimestamp;
	private int mGameID;
	private String mUsername;
	
	public Comment() {
		mText = null;
		mTimestamp = null;
	}
	
	public Comment(String text, String username, int gameID, Calendar timestamp) {
		mText = text;
		mTimestamp = timestamp;
		mUsername = username;
		mGameID = gameID;
	}
	
	public String getText() {
		return mText;
	}
	
	public Calendar getTimestamp() {
		return mTimestamp;
	}
	
	public void setText(String text) {
		mText = text;
	}
	
	public void setTimestamp(Calendar timestamp) {
		mTimestamp = timestamp;
	}

	public int getGameID() {
		return mGameID;
	}

	public void setGameID(int gameID) {
		mGameID = gameID;
	}

	public String getUsername() {
		return mUsername;
	}

	public void setUsername(String username) {
		mUsername = username;
	}
}
