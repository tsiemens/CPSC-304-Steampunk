package org.steampunk.mist.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.steampunk.mist.jdbc.DatabaseManager;
import org.steampunk.mist.jdbc.DatabaseSchema;
import org.steampunk.mist.model.Player;

public class PlayerRepository extends UserRepository{

	/**
	 * Retrieves player from database, with username
	 * @param username
	 * @return the user, or null if an error occurred
	 * @throws RepositoryErrorException
	 * @throws UserNotFoundException
	 */
	public static Player getPlayer(String username)  throws RepositoryErrorException, UserNotFoundException {
		Player player = new Player();
		setPlayerData(username, player);
		return player;
	}
	
	/**
	 * Checks if the player exists already
	 * @param username. this cannot be null
	 * @return true if user exists, false otherwise
	 * @throws RepositoryErrorException
	 */
	public static boolean playerExists(String username) throws RepositoryErrorException {
		DatabaseManager dbm = DatabaseManager.getInstance();
		ResultSet rs;
		try {
			rs = dbm.queryPrepared("SELECT username"
				+" FROM "+DatabaseSchema.TABLE_NAME_PLAYERS
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
			throw new RepositoryErrorException("Error reading player data: "+e);
		}
	}
	
	/**
	 * Modifies player to match the data contained in the database, for username
	 * @param username
	 * @param user
	 * @return true if user was successfully set to the correct data, false otherwise
	 * @throws RepositoryErrorException
	 * @throws UserNotFoundException
	 */
	public static void setPlayerData(String username, Player player) throws RepositoryErrorException,
		UserNotFoundException
	{
		if (username == null) {
			throw new UserNotFoundException("Username cannot be null");
		} else if (!playerExists(username)) {
			throw new UserNotFoundException("Player does not exist");
		}
		
		UserRepository.setUserData(username, player);
	}
	
	public static void addPlayer(Player player) throws RepositoryErrorException{
		DatabaseManager dbm = DatabaseManager.getInstance();
		UserRepository.addUser(player);
		try {
			dbm.updatePrepared("INSERT INTO " + DatabaseSchema.TABLE_NAME_PLAYERS +"(username)"
					+" VALUES(?)", player.getUsername());
		} catch (SQLException e) {
			throw new RepositoryErrorException(e.getMessage());
		}
	}
}
