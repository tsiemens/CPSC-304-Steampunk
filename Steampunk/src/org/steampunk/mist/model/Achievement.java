package org.steampunk.mist.model;

import org.steampunk.mist.AccountManager;
import org.steampunk.mist.repository.AchievementRepository;
import org.steampunk.mist.repository.RepositoryErrorException;
import org.steampunk.mist.util.MistTextFormatter;

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
	
	@Override
	public String toString() {
		String returnString = this.getAchievementName();
		User cUser = AccountManager.getInstance().getCurrentUser();
		if (cUser instanceof Player) {
			try {
				boolean hasEarned = AchievementRepository.playerHasEarnedAchievement(this,
						cUser.getUsername());
				if (hasEarned) {
					returnString = returnString+" [Earned]";
				}
			} catch (RepositoryErrorException e) {
				e.printStackTrace();
			}
		}
		return returnString;
	}
}
