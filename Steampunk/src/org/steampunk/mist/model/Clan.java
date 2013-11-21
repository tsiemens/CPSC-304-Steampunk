package org.steampunk.mist.model;

public class Clan {

	public String mClanName; // Part of primary key, in addition to foreign key gameID
	public String mMoD;
	public String mClanOwner;
	public int mGameID; 	// Primary key and foreign key
	
	// constructors
	public Clan() {
		mClanName = null;
		mMoD = null;
		mClanOwner = null;
		mGameID = 0;
	}
	
	public Clan(String clanName, String MoD, String clanOwner, int gameID) {
		mClanName = clanName;
		mMoD = MoD;
		mClanOwner = clanOwner;
		mGameID = gameID;
	}
	
	// accessors
	public String getClanName() {
		return mClanName;
	}
	
	public String getMoD() {
		return mMoD;
	}
	
	public String getClanOwner() {
		return mClanOwner;
	}
	
	public int getClanGameID() {
		return mGameID;
	}
	
	
	// mutators
	public void setClanName(String clanName) {
		mClanName = clanName;
	}
	
	public void setMoD(String MoD) {
		mMoD = MoD;
	}
	
	public void setClanOwner(String clanOwner) {
		mClanOwner = clanOwner;
	}
	
	public void setGameID(int gameID) {
		mGameID = gameID;
	}
	
}
