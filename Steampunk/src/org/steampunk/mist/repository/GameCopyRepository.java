
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
import org.steampunk.mist.repository.GameRepository.GameNameIdPair;

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
	
	public static boolean userOwnsGame(String username, int gameid) throws RepositoryErrorException {
		DatabaseManager dbm = DatabaseManager.getInstance();
		ResultSet rs;
		try {
			rs = dbm.queryPrepared("SELECT "+FIELD_GAMEKEY
				+" FROM "+DatabaseSchema.TABLE_NAME_GAME_COPIES
				+" WHERE "+FIELD_GAMEID+" = ? AND "+FIELD_OWNER+" = ?", gameid, username);
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
	
	public static boolean purchaseCopyOfGame(int gameID, String username) throws RepositoryErrorException {
		DatabaseManager dbm = DatabaseManager.getInstance();
		ResultSet rs;
		try {
			rs = dbm.queryPrepared("SELECT "+FIELD_GAMEKEY
				+" FROM "+DatabaseSchema.TABLE_NAME_GAME_COPIES
				+" WHERE "+FIELD_GAMEID+" = ?"
				+" AND "+FIELD_PURCHASE_DATE+" IS NULL", gameID);
		} catch (SQLException e) {
			 throw new RepositoryErrorException(e.getMessage());
		}
		
		try{
			while (rs.next()){
				if (purchaseCopy(rs.getString(FIELD_GAMEKEY), username)) {
					// Successfully purchased the game
					return true;
				}
			}
			return false;
		} catch (SQLException e) {
			throw new RepositoryErrorException("Error reading game copy data: "+e);
		}
	}
	
	public static boolean purchaseCopy(String gameKey, String username) throws RepositoryErrorException
	{
		DatabaseManager dbm = DatabaseManager.getInstance();
		try {
			int rowcount = dbm.updatePrepared("UPDATE "+DatabaseSchema.TABLE_NAME_GAME_COPIES
				+" SET "+FIELD_OWNER+" = ?"
				+", "+FIELD_PURCHASE_DATE+" = ?"
				+" WHERE gameKey = ?"
				+" AND "+FIELD_PURCHASE_DATE+" IS NULL", username, 
				new Date(Calendar.getInstance().getTimeInMillis()), gameKey);
			return rowcount == 1?true:false;
		} catch (SQLException e) {
			 throw new RepositoryErrorException(e.getMessage());
		}
	}
	
	public static void addGameCopy(GameCopy copy) throws RepositoryErrorException{
		DatabaseManager dbm = DatabaseManager.getInstance();
		try {
			if (copy.getPurchaseDate() != null) {
				dbm.updatePrepared("INSERT INTO " + DatabaseSchema.TABLE_NAME_GAME_COPIES +"(gameKey, gameID, ownerUsername, purchaseDate)"
						+" VALUES(?, ?, ?, ?)", copy.getGameKey(), copy.getGameID(), copy.getOwnerUsername(),
						new Date(copy.getPurchaseDate().getTimeInMillis()));
			} else {
				dbm.updatePrepared("INSERT INTO " + DatabaseSchema.TABLE_NAME_GAME_COPIES +"(gameKey, gameID)"
						+" VALUES(?, ?)", copy.getGameKey(), copy.getGameID());
			}
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


	// to get the name of games being administered
	public static Vector<GameNameIdPair> getGameAdministered(String adminName) throws RepositoryErrorException{
		DatabaseManager dbm = DatabaseManager.getInstance();
		ResultSet rs;
		try {
			rs = dbm.queryPrepared("SELECT g.gameName, g.gameID"
					+" FROM "+ DatabaseSchema.TABLE_NAME_GAMES + " g, "
					+ DatabaseSchema.TABLE_NAME_ADMINISTRATES + " a"
					+" WHERE g.gameid = a.gameid and a.username = ?", adminName);
		} catch (SQLException e) {
			throw new RepositoryErrorException(e.getMessage());
		}

		Vector<GameNameIdPair> gameNames = new Vector<GameNameIdPair>();

		try{
			while (rs.next()){
					gameNames.add(new GameNameIdPair(rs.getInt("gameID"), rs.getString("gameName")));
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

