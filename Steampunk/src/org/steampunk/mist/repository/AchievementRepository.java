package org.steampunk.mist.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.steampunk.mist.jdbc.DatabaseManager;
import org.steampunk.mist.jdbc.DatabaseSchema;
import org.steampunk.mist.model.Achievement;

public class AchievementRepository {

	public static final String FIELD_ACHIEVEMENT_NAME = "achievementName";			//Primary Key
	public static final String FIELD_ACHIEVEMENT_DESCRIPTION = "achievementDesc";
	public static final String FIELD_GAME_ID = "gameID";							//Primary Key
	public static final String FIELD_POINTS = "points";
		
	/**
	 * Retrieves achievement from database, with gameID and achievementName
	 * @param gameID
	 * @param achievementName. this cannot be null
	 * @return the achievement, or null if an error occurred
	 * @throws RepositoryErrorException
	 * @throws AchievementNotFoundException
	 */
	public static Achievement getAchievement(int gameID, String achievementName)  throws RepositoryErrorException, AchievementNotFoundException {
		Achievement achievement = new Achievement();
		setAchievementData(gameID, achievementName, achievement);
		return achievement;
	}
	
	/**
	 * Checks if the achievement exists already
	 * @param gameID
	 * @param achievemntName. this cannot be null
	 * @return true if user exists, false otherwise
	 * @throws RepositoryErrorException
	 */
	public static boolean achievementExists(int gameID, String achievementName) throws RepositoryErrorException {
		DatabaseManager dbm = DatabaseManager.getInstance();
		ResultSet rs;
		try {
			rs = dbm.queryPrepared("SELECT "+FIELD_ACHIEVEMENT_NAME+FIELD_GAME_ID
				+" FROM "+DatabaseSchema.TABLE_NAME_ACHIEVEMENTS
				+" WHERE gameID = ?", gameID +" AND achievementName = ?", achievementName);
		} catch (SQLException e) {
			 throw new RepositoryErrorException(e.getMessage());
		}
		
		try{
			if (rs.next()){
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			throw new RepositoryErrorException("Error reading user data: "+e);
		}
	}
	
	/**
	 * Modifies achievement to match the data contained in the database, for gameID
	 * @param gameID
	 * @param achievementName 
	 * @param achievement
	 * @return true if achievement was successfully set to the correct data, false otherwise
	 * @throws RepositoryErrorException
	 * @throws AchievementNotFoundException
	 */
	public static void setAchievementData(int gameID, String achievementName, Achievement achievement) throws RepositoryErrorException,
		AchievementNotFoundException
	{
		if (achievementName == null){
			throw new AchievementNotFoundException("AchievementName cannot be null");
		}
		
		DatabaseManager dbm = DatabaseManager.getInstance();
		ResultSet rs;
		try {
			rs = dbm.queryPrepared("SELECT achievementName, achievementDescription, gameID, points"
				+" FROM "+DatabaseSchema.TABLE_NAME_ACHIEVEMENTS
				+" WHERE gameID = ?", gameID);
		} catch (SQLException e) {
			 throw new RepositoryErrorException(e.getMessage());
		}
		
		try{
			if (rs.next()){
				achievement.setAchievementName(rs.getString(FIELD_ACHIEVEMENT_NAME));
				achievement.setAchievementDesc(rs.getString(FIELD_ACHIEVEMENT_DESCRIPTION));
				achievement.setPoints(rs.getInt(FIELD_POINTS));
			} else {
				throw new AchievementNotFoundException("Achievement does not exist");
			}
		} catch (SQLException e) {
			throw new RepositoryErrorException("Error reading user data: "+e);
		}
	}
	
	public static void addAchievement(Achievement achievement) throws RepositoryErrorException{
		DatabaseManager dbm = DatabaseManager.getInstance();
		try {
			dbm.updatePrepared("INSERT INTO " + DatabaseSchema.TABLE_NAME_ACHIEVEMENTS +"("+FIELD_ACHIEVEMENT_NAME+
					", "+FIELD_ACHIEVEMENT_DESCRIPTION+", "+FIELD_GAME_ID+", "+FIELD_POINTS+")"
					+" VALUES(?, ?, ?, ?)", achievement.getAchievementName() , achievement.getAchievementDesc(), achievement.getGameID(),
					achievement.getPoints());
		} catch (SQLException e) {
			throw new RepositoryErrorException(e.getMessage());
		}
	}
	
	public static void earnAchievement(Achievement achievement, String username) throws RepositoryErrorException{
		DatabaseManager dbm = DatabaseManager.getInstance();
		try {
			dbm.updatePrepared("INSERT INTO " + DatabaseSchema.TABLE_NAME_HAS_EARNED +"("+FIELD_ACHIEVEMENT_NAME+", "
					+FIELD_GAME_ID+", "+FIELD_USERNAME+")"
					+" VALUES(?, ?, ?)", achievement.getAchievementName() , achievement.getGameID(),
					username);
		} catch (SQLException e) {
			throw new RepositoryErrorException(e.getMessage());
		}
	}
	
	public static class AchievementNotFoundException extends Exception{

		/**
		 * 
		 */
		private static final long serialVersionUID = 3762770524381364592L;	
		
		AchievementNotFoundException(String reason) {
			super(reason);
		}
	}
	
	/*
	 * HasEarnedRepository Addendum
	 */
	
	public static final String FIELD_USERNAME = "username";							//Primary Key
		
	/**
	 * Retrieves player's achievements from database, with username
	 * @param username. this cannot be null
	 * @param gameID
	 * @param achievementName. this cannot be null
	 * @return the achievement, or null if an error occurred
	 * @throws RepositoryErrorException
	 * @throws AchievementNotFoundException
	 */
	public static List<Achievement> getPlayerAchievements(String username)  throws RepositoryErrorException, AchievementNotFoundException {
		List<Achievement> achievementList = new ArrayList<Achievement>();
		setAchievementListData(username, achievementList);
		return achievementList;
	}
	
	/**
	 * Retrieves player's achievements in a from database, with username and gameID
	 * @param username. this cannot be null
	 * @param gameID
	 * @return list of achievements of player in game, or null if an error occurred
	 * @throws RepositoryErrorException
	 * @throws AchievementNotFoundException
	 */
	public static List<Achievement> getPlayerGameAchievements(String username, int gameID)  throws RepositoryErrorException, AchievementNotFoundException {
		List<Achievement> achievementList = new ArrayList<Achievement>();
		setAchievementListData(username, gameID, achievementList);
		return achievementList;
	}
	
	/**
	 * Retrieves game's achievements in a from database, with gameID
	 * @param gameID
	 * @return list of achievements of game, or null if an error occurred
	 * @throws RepositoryErrorException
	 * @throws AchievementNotFoundException
	 */
	public static List<Achievement> getGameAchievements(int gameID)  throws RepositoryErrorException, AchievementNotFoundException {
		List<Achievement> achievementList = new ArrayList<Achievement>();
		setAchievementListData(gameID, achievementList);
		return achievementList;
	}
	
	/**
	 * Modifies achievement list to match the data contained in the database, for username
	 * @param username
	 * @param achievemnetList
	 * @throws RepositoryErrorException
	 * @throws AchievementNotFoundException
	 */
	public static void setAchievementListData(String username, List<Achievement> achievementList) throws RepositoryErrorException,
		AchievementNotFoundException
	{
		if (username == null){
			throw new AchievementNotFoundException("Username cannot be null");
		}
		DatabaseManager dbm = DatabaseManager.getInstance();
		ResultSet rs;
		try {
			rs = dbm.queryPrepared("SELECT gameID, achievementName"
				+" FROM "+DatabaseSchema.TABLE_NAME_HAS_EARNED
				+" WHERE username = ?", username);
		} catch (SQLException e) {
			 throw new RepositoryErrorException(e.getMessage());
		}
		
		Achievement achievement;
		
		try{
			while (rs.next()){
				achievement = new Achievement();
				setAchievementData(rs.getInt(FIELD_GAME_ID), rs.getString(FIELD_ACHIEVEMENT_NAME), achievement);
				achievementList.add(achievement);
			}
		} catch (SQLException e) {
			throw new RepositoryErrorException("Error reading user data: "+e);
		}
	}

	/**
	 * Modifies achievement list to match the data contained in the database, for username and gameID
	 * @param username
	 * @param gameID
	 * @param achievementList
	 * @throws RepositoryErrorException
	 * @throws AchievementNotFoundException
	 */
	public static void setAchievementListData(String username, int gameID, List<Achievement> achievementList) throws RepositoryErrorException,
		AchievementNotFoundException
	{
		if (username == null){
			throw new AchievementNotFoundException("username cannot be null");
		}
		DatabaseManager dbm = DatabaseManager.getInstance();
		ResultSet rs;
		try {
			rs = dbm.queryPrepared("SELECT username, gameID, achievementName"
				+" FROM "+DatabaseSchema.TABLE_NAME_HAS_EARNED
				+" WHERE username = ?"
				+" AND gameID = ?", username, gameID);
		} catch (SQLException e) {
			 throw new RepositoryErrorException(e.getMessage());
		}
		
		Achievement achievement;
		
		try{
			while (rs.next()){
				achievement = new Achievement();
				setAchievementData(rs.getInt(FIELD_GAME_ID), rs.getString(FIELD_ACHIEVEMENT_NAME), achievement);
				achievementList.add(achievement);
			}
		} catch (SQLException e) {
			throw new RepositoryErrorException("Error reading user data: "+e);
		}
	}
	
	/**
	 * Modifies achievement list to match the data contained in the database, for gameID
	 * @param gameID
	 * @param achievementList
	 * @return list of achievements of game or null if an error occurred
	 * @throws RepositoryErrorException
	 * @throws AchievementNotFoundException
	 */
	public static void setAchievementListData(int gameID, List<Achievement> achievementList) throws RepositoryErrorException,
		AchievementNotFoundException
	{
		DatabaseManager dbm = DatabaseManager.getInstance();
		ResultSet rs;
		try {
			rs = dbm.queryPrepared("SELECT gameID, achievementName, achievementDesc, points"
				+" FROM "+DatabaseSchema.TABLE_NAME_ACHIEVEMENTS
				+" WHERE gameID = ?", gameID);
		} catch (SQLException e) {
			 throw new RepositoryErrorException(e.getMessage());
		}
		
		Achievement achievement = new Achievement();
		
		try{
			while (rs.next()){
				achievement = new Achievement(rs.getInt(FIELD_POINTS), rs.getString(FIELD_ACHIEVEMENT_NAME),
						rs.getString(FIELD_ACHIEVEMENT_DESCRIPTION), rs.getInt(FIELD_GAME_ID));
				achievementList.add(achievement);
			}
		} catch (SQLException e) {
			throw new RepositoryErrorException("Error reading user data: "+e);
		}
	}
	
	/**
	 * Modifies sum to match the data contained in the database, for username
	 * @param username
	 * @return sum player achievement points or null if an error occurred
	 * @throws RepositoryErrorException
	 * @throws AchievementNotFoundException
	 */
	public static int sumPlayerAchievementPoints(String username) throws RepositoryErrorException,
		AchievementNotFoundException
	{
		if (username == null){
			throw new AchievementNotFoundException("Username cannot be null");
		}
		DatabaseManager dbm = DatabaseManager.getInstance();
		ResultSet rs;
		try {
			rs = dbm.queryPrepared("SELECT SUM(points) AS sum"
				+" FROM "+DatabaseSchema.TABLE_NAME_HAS_EARNED
				+", "+DatabaseSchema.TABLE_NAME_ACHIEVEMENTS
				+" WHERE username = ?"
				+" AND "+DatabaseSchema.TABLE_NAME_HAS_EARNED+".gameID = "+DatabaseSchema.TABLE_NAME_ACHIEVEMENTS+".gameID"
				+" AND "+DatabaseSchema.TABLE_NAME_HAS_EARNED+".achievementName = "+DatabaseSchema.TABLE_NAME_ACHIEVEMENTS+".achievementName", username);
		} catch (SQLException e) {
			 throw new RepositoryErrorException(e.getMessage());
		}
		
		int sum = 0;
		
		try{
			if(rs.next()){
				sum = rs.getInt(0);
			}
		} catch (SQLException e) {
			throw new RepositoryErrorException("Error reading user data: "+e);
		}
		
		return sum;
	}
	
	/**
	 * Modifies sum to match the data contained in the database, for username and gameID
	 * @param username
	 * @param gameID
	 * @return sum player achievement points for game or null if an error occurred
	 * @throws RepositoryErrorException
	 * @throws AchievementNotFoundException
	 */
	public static int sumPlayerGameAchievementPoints(String username, int gameID) throws RepositoryErrorException,
		AchievementNotFoundException
	{
		if (username == null){
			throw new AchievementNotFoundException("Username cannot be null");
		}
		DatabaseManager dbm = DatabaseManager.getInstance();
		ResultSet rs;
		try {
			rs = dbm.queryPrepared("SELECT SUM(points) AS sum"
				+" FROM "+DatabaseSchema.TABLE_NAME_HAS_EARNED
				+", "+DatabaseSchema.TABLE_NAME_ACHIEVEMENTS
				+" WHERE username = ?"
				+" AND gameID = ?"
				+" AND "+DatabaseSchema.TABLE_NAME_HAS_EARNED+".gameID = "+DatabaseSchema.TABLE_NAME_ACHIEVEMENTS+".gameID"
				+" AND "+DatabaseSchema.TABLE_NAME_HAS_EARNED+".achievementName = "+DatabaseSchema.TABLE_NAME_ACHIEVEMENTS+".achievementName", username, gameID);
		} catch (SQLException e) {
			 throw new RepositoryErrorException(e.getMessage());
		}
		
		int sum = 0;
		
		try{
			if(rs.next()){
				sum = rs.getInt(0);
			}
		} catch (SQLException e) {
			throw new RepositoryErrorException("Error reading user data: "+e);
		}
		
		return sum;
	}
	
	public static boolean playerHasEarnedAchievement(Achievement achmt, String username) 
			throws RepositoryErrorException {
		if (username == null){
			return false;
		}
		DatabaseManager dbm = DatabaseManager.getInstance();
		ResultSet rs;
		try {
			rs = dbm.queryPrepared("SELECT a."+FIELD_ACHIEVEMENT_NAME
				+" FROM "+DatabaseSchema.TABLE_NAME_HAS_EARNED+" he"
				+", "+DatabaseSchema.TABLE_NAME_ACHIEVEMENTS+" a"
				+" WHERE he.gameID = a.gameID"
				+" AND a.achievementName = he.achievementName"
				+" AND a.gameID = ?"
				+" AND he.username = ?"
				+" AND a.achievementName = ?", achmt.getGameID(), username,
				achmt.getAchievementName());
		} catch (SQLException e) {
			 throw new RepositoryErrorException(e.getMessage());
		}
		
		try{
			if(rs.next()){
				return true;
			}
		} catch (SQLException e) {
			throw new RepositoryErrorException("Error reading achievement data: "+e);
		}
		return false;
	}

}
