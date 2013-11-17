package org.steampunk.mist.model;

public class Achievement {

	private int mPoints;
	private String mAchievementName;
	private String mAchievementDesc;
	private int mGameID;
	
	public Achievement() {
		mPoints = 0;
		mAchievementName = null;
		mAchievementDesc = null;
		mGameID = 0;
	}
	
	public Achievement(int points, String achievementName, String achievementDesc, int gameID) {
		mPoints = points;
		mAchievementName = achievementName;
		mAchievementDesc = achievementDesc;
		mGameID = gameID;
	}
	
	public int getPoints(){
		return mPoints;
	}	
	
	public String getAchievementName(){
		return mAchievementName;
	}
	
	public String getAchievementDesc(){
		return mAchievementDesc;
	}
	
	public int getGameID(){
		return mGameID;
	}
	
	public void setPoints(int points){
		mPoints = points;
	}
	
	public void setAchievementName(String achievementName){
		mAchievementName = achievementName;
	}
	
	public void setAchievementDesc(String achievementDesc){
		mAchievementDesc = achievementDesc;
	}
	
	public void setGameID(int gameID){
		mGameID = gameID;
	}
	
}
