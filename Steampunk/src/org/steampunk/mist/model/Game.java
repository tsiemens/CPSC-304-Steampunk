package org.steampunk.mist.model;

public class Game {

	private int mGameID;
	private String mGameName;
	private String mPublisher;
	private String mRating;
	private String mDescription;
	
	// constructors
	public Game() {
		mGameID = 0;
		mGameName = null;
		mPublisher = null;
		mRating = null;
		mDescription = null;
	}
	
	public Game(int gameID, String gameName, String publisher, String rating, String description) {
		mGameID = gameID;
		mGameName = gameName;
		mPublisher = publisher;
		mRating = rating;
		mDescription = description;
	}
	
	// accessors
	public int getGameID(){
		return mGameID;
	}
	
	public String getGameName() {
		return mGameName;
	}
	
	public String getPublisher() {
		return mPublisher;
	}
	
	public String getRating() {
		return mRating;
	}
	
	public String getDescription() {
		return mDescription;
	}
	
	// mutators
	public void setGameID(int gameID) {
		mGameID = gameID;
	}
	
	public void setGameName(String gameName) {
		mGameName = gameName;
	}
	
	public void setPublisher(String publisher) {
		mPublisher = publisher;
	}
	
	public void setRating(String rating) {
		mRating = rating;
	}
	
	public void setDescription(String description) {
		mDescription = description;
	}
	
}
