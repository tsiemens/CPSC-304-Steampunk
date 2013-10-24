package org.steampunk.mist.model;

public class Clan {

	public String mClanName; // Part of primary key, in addition to foreign key gameID
	public String mMoD;
	public String mClanOwner;
	// Note: did not include foreign keys
	
	// constructors
	public Clan() {
		mClanName = null;
		mMoD = null;
		mClanOwner = null;
	}
	
	public Clan(String clanName, String MoD, String clanOwner) {
		mClanName = clanName;
		mMoD = MoD;
		mClanOwner = clanOwner;
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
	
}
