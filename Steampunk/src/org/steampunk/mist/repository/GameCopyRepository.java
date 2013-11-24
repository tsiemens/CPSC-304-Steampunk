package org.steampunk.mist.repository;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;

import org.steampunk.mist.jdbc.DatabaseManager;
import org.steampunk.mist.jdbc.DatabaseSchema;
import org.steampunk.mist.model.GameCopy;

public class GameCopyRepository {
	public static final String FIELD_GAMEKEY = "gameKey";
	public static final String FIELD_GAMEID = "gameID";
	public static final String FIELD_OWNER = "ownerUsername";
	public static final String FIELD_PURCHASE_DATE = "purchaseDate";
	
	/**
	 * Retrieves game copy from database, with game key
	 * @param username
	 * @return the user, or null if an error occurred
	 * @throws RepositoryErrorException
	 * @throws UserNotFoundException
	 */
	public static GameCopy getGameCopy(String gameKey)  throws RepositoryErrorException, GameCopyNotFoundException {
		GameCopy copy = new GameCopy();
		setGameCopyData(gameKey, copy);
		return copy;
	}
	
	/**
	 * Gets the game copy keys that a user owns
	 * @param username
	 * @return
	 * @throws RepositoryErrorException
	 */
	public static String[] getGameCopyKeysOwnedByUser(String username) throws RepositoryErrorException {
		DatabaseManager dbm = DatabaseManager.getInstance();
		ResultSet rs;
		try {
			rs = dbm.queryPrepared("SELECT "+FIELD_GAMEKEY
				+" FROM "+DatabaseSchema.TABLE_NAME_GAME_COPIES
				+" WHERE "+FIELD_OWNER+" = ?", username);
		} catch (SQLException e) {
			 throw new RepositoryErrorException(e.getMessage());
		}
		
		ArrayList<String> copies = new ArrayList<String>();
		
		try{
			while (rs.next()){
				copies.add(rs.getString(FIELD_GAMEKEY));
			}
		} catch (SQLException e) {
			throw new RepositoryErrorException("Error reading game copy data: "+e);
		}
		return (String[]) copies.toArray();
	}
	
	/**
	 * Gets the game copy keys that belong to a specific game
	 * @param gameID
	 * @return
	 * @throws RepositoryErrorException
	 */
	public static String[] getGameCopyKeysForGame(int gameID) throws RepositoryErrorException {
		DatabaseManager dbm = DatabaseManager.getInstance();
		ResultSet rs;
		try {
			rs = dbm.queryPrepared("SELECT "+FIELD_GAMEKEY
				+" FROM "+DatabaseSchema.TABLE_NAME_GAME_COPIES
				+" WHERE "+FIELD_GAMEID+" = ?", gameID);
		} catch (SQLException e) {
			 throw new RepositoryErrorException(e.getMessage());
		}
		
		ArrayList<String> copies = new ArrayList<String>();
		
		try{
			while (rs.next()){
				copies.add(rs.getString(FIELD_GAMEKEY));
			}
		} catch (SQLException e) {
			throw new RepositoryErrorException("Error reading game copy data: "+e);
		}
		return (String[]) copies.toArray();
	}
	
	/**
	 * Checks if the game copy exists
	 * @param gameKey. this cannot be null
	 * @return true if user exists, false otherwise
	 * @throws RepositoryErrorException
	 */
	public static boolean gameCopyExists(String gameKey) throws RepositoryErrorException {
		DatabaseManager dbm = DatabaseManager.getInstance();
		ResultSet rs;
		try {
			rs = dbm.queryPrepared("SELECT "+FIELD_GAMEKEY
				+" FROM "+DatabaseSchema.TABLE_NAME_GAME_COPIES
				+" WHERE "+FIELD_GAMEKEY+" = ?", gameKey);
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
			throw new RepositoryErrorException("Error reading game copy data: "+e);
		}
	}
	

	/**
	 * populates the game copy with data from the database
	 * @param gameKey
	 * @param copy
	 * @throws RepositoryErrorException
	 * @throws GameCopyNotFoundException
	 */
	public static void setGameCopyData(String gameKey, GameCopy copy) throws RepositoryErrorException,
		GameCopyNotFoundException
	{				
		DatabaseManager dbm = DatabaseManager.getInstance();
		ResultSet rs;
		try {
			rs = dbm.queryPrepared("SELECT gameKey, gameID, ownerUsername, purchaseDate"
				+" FROM "+DatabaseSchema.TABLE_NAME_GAME_COPIES
				+" WHERE gameKey = ?", gameKey);
		} catch (SQLException e) {
			 throw new RepositoryErrorException(e.getMessage());
		}
		
		try{
			if (rs.next()){
				copy.setGameKey(rs.getString(FIELD_GAMEKEY));
				copy.setGameID(rs.getInt(FIELD_GAMEID));
				copy.setOwnerUsername(rs.getString(FIELD_OWNER));
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(rs.getDate(FIELD_PURCHASE_DATE).getTime());
				copy.setPurchaseDate(cal);
			} else {
				throw new GameCopyNotFoundException("Game copy does not exist");
			}
		} catch (SQLException e) {
			throw new RepositoryErrorException("Error reading game copy data: "+e);
		}
	}
	
	public static void updateOwner(String gameKey, String username) throws RepositoryErrorException
	{
		updateField(gameKey, FIELD_OWNER, username);
	}
	
	private static void updateField(String gameKey, String field, Object newVal) throws RepositoryErrorException
	{
		DatabaseManager dbm = DatabaseManager.getInstance();
		try {
			dbm.updatePrepared("UPDATE "+DatabaseSchema.TABLE_NAME_GAME_COPIES
				+" SET "+field+" = ?"
				+" WHERE gameKey = ?", newVal, gameKey);
		} catch (SQLException e) {
			 throw new RepositoryErrorException(e.getMessage());
		}
	}
	
	public static void addGameCopy(GameCopy copy) throws RepositoryErrorException{
		DatabaseManager dbm = DatabaseManager.getInstance();
		try {
			dbm.updatePrepared("INSERT INTO " + DatabaseSchema.TABLE_NAME_GAME_COPIES +"(gameKey, gameID, ownerUsername, purchaseDate)"
					+" VALUES(?, ?, ?, ?)", copy.getGameKey(), copy.getGameID(), copy.getOwnerUsername(),
					new Date(copy.getPurchaseDate().getTimeInMillis()));
		} catch (SQLException e) {
			throw new RepositoryErrorException(e.getMessage());
		}
	}
	
	public static void addGameAdministered(String adminName, int gameID) throws RepositoryErrorException{
		DatabaseManager dbm = DatabaseManager.getInstance();
		try {
			dbm.updatePrepared("INSERT INTO " + DatabaseSchema.TABLE_NAME_ADMINISTRATES +"(username, gameID)"
					+" VALUES(?, ?)", adminName, gameID);
		} catch (SQLException e) {
			throw new RepositoryErrorException(e.getMessage());
		}
	}
	
	
	// still not done
	public static Vector<String> getGameAdministered(String adminName) throws RepositoryErrorException{
		DatabaseManager dbm = DatabaseManager.getInstance();
		ResultSet rs;
		try {
			rs = dbm.queryPrepared("SELECT gameID"
				+" FROM "+DatabaseSchema.TABLE_NAME_ADMINISTRATES
				+" WHERE username = ?", adminName);
		} catch (SQLException e) {
			 throw new RepositoryErrorException(e.getMessage());
		}
		
		Vector<String> gameNames = new Vector<String>();
				
		try{
			while (rs.next()){
				gameNames.add(rs.getString(1));
				
			}

		} catch (SQLException e) {
			throw new RepositoryErrorException("Error reading player data: "+e);
		}
		return gameNames;
	}
	
	public static class GameCopyNotFoundException extends Exception{	
		
		private static final long serialVersionUID = -4327940496520103396L;

		GameCopyNotFoundException(String reason) {
			super(reason);
		}
	}
}
