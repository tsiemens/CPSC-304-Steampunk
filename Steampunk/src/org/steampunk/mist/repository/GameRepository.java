package org.steampunk.mist.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.steampunk.mist.jdbc.DatabaseManager;
import org.steampunk.mist.jdbc.DatabaseSchema;
import org.steampunk.mist.model.Game;

public class GameRepository {
	
	public static final String FIELD_GAMEID = "gameID";
	public static final String FIELD_NAME = "gameName";
	public static final String FIELD_PUBLISHER = "publisher";
	public static final String FIELD_RATING = "rating";
	public static final String FIELD_DESCRIPTION = "description";
	
	/**
	 * Retrieves game from database, with game id
	 * @param gameID
	 * @return
	 * @throws RepositoryErrorException
	 * @throws GameNotFoundException
	 */
	public static Game getGame(int gameID)  throws RepositoryErrorException, GameNotFoundException {
		Game game = new Game();
		setGameData(gameID, game);
		return game;
	}
	
	/**
	 * Returns pairs of game names and ids only, that are owned by the user, sorted alphabetically
	 * @param username
	 * @return
	 * @throws RepositoryErrorException
	 */
	public static Vector<GameNameIdPair> getGameNamesOwnedByUser(String username) throws RepositoryErrorException {
		DatabaseManager dbm = DatabaseManager.getInstance();
		ResultSet rs;
		try {
			rs = dbm.queryPrepared("SELECT DISTINCT g."+FIELD_GAMEID+", g."+FIELD_NAME
				+" FROM "+DatabaseSchema.TABLE_NAME_GAMES+" g, "+DatabaseSchema.TABLE_NAME_GAME_COPIES+" gc"
				+" WHERE g."+FIELD_GAMEID+" = gc."+GameCopyRepository.FIELD_GAMEID
				+" AND gc."+GameCopyRepository.FIELD_OWNER+" = ?"
				+" ORDER BY g."+FIELD_NAME+" ASC", username);
		} catch (SQLException e) {
			 throw new RepositoryErrorException(e.getMessage());
		}
		
		Vector<GameNameIdPair> games = new Vector<GameNameIdPair>();
		
		try{
			while (rs.next()){
				games.add(new GameNameIdPair(rs.getInt(FIELD_GAMEID), rs.getString(FIELD_NAME)));
			}
		} catch (SQLException e) {
			throw new RepositoryErrorException("Error reading game data: "+e);
		}
		return games;
	}
	
	/**
	 * Returns pairs of game names and ids only, that match the search term, sorted alphabetically
	 * @param username
	 * @return
	 * @throws RepositoryErrorException
	 */
	public static Vector<GameNameIdPair> searchGames(String searchterm) throws RepositoryErrorException {
		Vector<GameNameIdPair> games = new Vector<GameNameIdPair>();
		
		searchterm = searchterm.replace("%", "");
		System.out.println("Searching '"+searchterm+"'");
		if (searchterm.isEmpty()) { return games; }
		
		DatabaseManager dbm = DatabaseManager.getInstance();
		ResultSet rs;
		try {
			rs = dbm.queryPrepared("SELECT DISTINCT "+FIELD_GAMEID+", "+FIELD_NAME
				+" FROM "+DatabaseSchema.TABLE_NAME_GAMES
				+" WHERE UPPER("+FIELD_NAME+") LIKE UPPER('%'||?||'%')"
				+" ORDER BY "+FIELD_NAME+" ASC", searchterm);
		} catch (SQLException e) {
			 throw new RepositoryErrorException(e.getMessage());
		}
		
		try{
			while (rs.next()){
				games.add(new GameNameIdPair(rs.getInt(FIELD_GAMEID), rs.getString(FIELD_NAME)));
			}
		} catch (SQLException e) {
			throw new RepositoryErrorException("Error reading game data: "+e);
		}
		return games;
	}
	
	/**
	 * Checks if the game exists
	 * @param gameID
	 * @return true if game exists, false otherwise
	 * @throws RepositoryErrorException
	 */
	public static boolean gameExists(int gameID) throws RepositoryErrorException {
		DatabaseManager dbm = DatabaseManager.getInstance();
		ResultSet rs;
		try {
			rs = dbm.queryPrepared("SELECT "+FIELD_GAMEID
				+" FROM "+DatabaseSchema.TABLE_NAME_GAMES
				+" WHERE "+FIELD_GAMEID+" = ?", new Integer(gameID));
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
			throw new RepositoryErrorException("Error reading game data: "+e);
		}
	}
	
	public static String getGameName(int gameID) throws RepositoryErrorException,
	GameNotFoundException
	{				
		DatabaseManager dbm = DatabaseManager.getInstance();
		ResultSet rs;
		try {
			rs = dbm.queryPrepared("SELECT gameName"
					+" FROM "+DatabaseSchema.TABLE_NAME_GAMES
					+" WHERE gameID = ?", gameID);
		} catch (SQLException e) {
			throw new RepositoryErrorException(e.getMessage());
		}

		try{
			if (rs.next()){
				return rs.getString(FIELD_NAME);
			} else {
				throw new GameNotFoundException("Game does not exist");
			}
		} catch (SQLException e) {
			throw new RepositoryErrorException("Error reading game data: "+e);
		}
	}

	/**
	 * populates game with the info in database, associated with the game with gameID
	 * @param gameID
	 * @param game
	 * @throws RepositoryErrorException
	 * @throws GameNotFoundException
	 */
	public static void setGameData(int gameID, Game game) throws RepositoryErrorException,
		GameNotFoundException
	{				
		DatabaseManager dbm = DatabaseManager.getInstance();
		ResultSet rs;
		try {
			rs = dbm.queryPrepared("SELECT gameID, gameName, publisher, rating, description"
				+" FROM "+DatabaseSchema.TABLE_NAME_GAMES
				+" WHERE gameID = ?", gameID);
		} catch (SQLException e) {
			 throw new RepositoryErrorException(e.getMessage());
		}
		
		try{
			if (rs.next()){
				game.setGameID(rs.getInt(FIELD_GAMEID));
				game.setGameName(rs.getString(FIELD_NAME));
				game.setPublisher(rs.getString(FIELD_PUBLISHER));
				game.setRating(rs.getString(FIELD_RATING));
				game.setDescription(rs.getString(FIELD_DESCRIPTION));
			} else {
				throw new GameNotFoundException("Game does not exist");
			}
		} catch (SQLException e) {
			throw new RepositoryErrorException("Error reading game data: "+e);
		}
	}
	
	public static void updateName(int gameID, String name) throws RepositoryErrorException
	{
		updateField(gameID, FIELD_NAME, name);
	}
	
	public static void updatePublisher(int gameID, String publisher) throws RepositoryErrorException
	{
		updateField(gameID, FIELD_PUBLISHER, publisher);
	}
	
	public static void updateRating(int gameID, String rating) throws RepositoryErrorException
	{
		updateField(gameID, FIELD_RATING, rating);
	}
	
	public static void updateDescription(int gameID, String desc) throws RepositoryErrorException
	{
		updateField(gameID, FIELD_DESCRIPTION, desc);
	}
	
	private static void updateField(int gameID, String field, Object newVal) throws RepositoryErrorException
	{
		DatabaseManager dbm = DatabaseManager.getInstance();
		try {
			dbm.updatePrepared("UPDATE "+DatabaseSchema.TABLE_NAME_GAMES
				+" SET "+field+" = ?"
				+" WHERE gameID = ?", newVal, gameID);
		} catch (SQLException e) {
			 throw new RepositoryErrorException(e.getMessage());
		}
	}
	
	public static void addGame(Game game) throws RepositoryErrorException{
		DatabaseManager dbm = DatabaseManager.getInstance();
		try {
			dbm.updatePrepared("INSERT INTO " + DatabaseSchema.TABLE_NAME_GAMES +"(gameID, gameName, publisher, rating, description)"
					+" VALUES(?, ?, ?, ?, ?)", game.getGameID(), game.getGameName(), game.getPublisher(), 
					game.getRating(), game.getDescription());
		} catch (SQLException e) {
			throw new RepositoryErrorException(e.getMessage());
		}
	}
	
	public static boolean gameAdminedByUser(String username, int gameID) throws RepositoryErrorException {
		DatabaseManager dbm = DatabaseManager.getInstance();
		ResultSet rs;
		try {
			rs = dbm.queryPrepared("SELECT gameID"
					+" FROM "+DatabaseSchema.TABLE_NAME_ADMINISTRATES
					+" WHERE username = ?", username);
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
			throw new RepositoryErrorException("Error reading admin data: "+e);
		}
	}
	
	public static class GameNotFoundException extends Exception{	
		
		private static final long serialVersionUID = -4327940496520103396L;

		GameNotFoundException(String reason) {
			super(reason);
		}
	}
	
	public static class GameNameIdPair {
		public String title;
		public int id;
		
		public GameNameIdPair(int id, String title) {
			this.title = title;
			this.id = id;
		}
		
		@Override
		public String toString() {
			return title;
		}
	}
}
