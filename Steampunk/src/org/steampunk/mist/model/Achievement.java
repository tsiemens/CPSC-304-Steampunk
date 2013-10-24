package org.steampunk.mist.model;

public class Achievement {

	private int mPoints;
	private String mAchievementName;
	private String mAchievementDesc;
	
	public Achievement() {
		mPoints = 0;
		mAchievementName = null;
		mAchievementDesc = null;
	}
	
	public Achievement(int points, String achievementName, String achievementDesc) {
		mPoints = points;
		mAchievementName = achievementName;
		mAchievementDesc = achievementDesc;
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
	
	public void setPoints(int points){
		mPoints = points;
	}
	
	public void setAchievementName(String achievementName){
		mAchievementName = achievementName;
	}
	
	public void setAchievementDesc(String achievementDesc){
		mAchievementDesc = achievementDesc;
	}
}
