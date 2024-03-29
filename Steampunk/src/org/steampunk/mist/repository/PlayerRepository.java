package org.steampunk.mist.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

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
	
	
	public static boolean isFriend(String inviter, String invitee) throws RepositoryErrorException{
		DatabaseManager dbm = DatabaseManager.getInstance();
		ResultSet rs;
		try {
			rs = dbm.queryPrepared("SELECT inviterUsername, inviteeUsername"
				+" FROM "+DatabaseSchema.TABLE_NAME_ARE_FRIENDS
				+" WHERE inviterUsername = ?"
				+ " AND inviteeUsername = ?", inviter, invitee);
		} catch (SQLException e) {
			 throw new RepositoryErrorException(e.getMessage());
		}
		
		try{
			if (rs.next()){
				System.out.println("true");
				return true;
			} else {
				System.out.println("false");
				return false;
			}
		} catch (SQLException e) {
			throw new RepositoryErrorException("Error reading player data: "+e);
		}

	}
	
	public static void addFriend(String inviter, String invitee) throws RepositoryErrorException{
		DatabaseManager dbm = DatabaseManager.getInstance();
		try {
			dbm.updatePrepared("INSERT INTO " + DatabaseSchema.TABLE_NAME_ARE_FRIENDS
					+" VALUES(?, ?)", inviter, invitee);
		} catch (SQLException e) {
			throw new RepositoryErrorException(e.getMessage());
		}
		
	}

	public static void removeFriend(String inviter, String invitee) throws RepositoryErrorException{
		DatabaseManager dbm = DatabaseManager.getInstance();
		try {
			dbm.updatePrepared("DELETE FROM " + DatabaseSchema.TABLE_NAME_ARE_FRIENDS
					+" WHERE inviterUsername = ? AND inviteeUsername = ?", inviter, invitee);
		} catch (SQLException e) {
			throw new RepositoryErrorException(e.getMessage());
		}
	}

	public static Vector<String> getFriends(String inviter) throws RepositoryErrorException{
		DatabaseManager dbm = DatabaseManager.getInstance();
		ResultSet rs;
		try{
			rs = dbm.queryPrepared("SELECT inviteeUsername"
					+ " FROM " + DatabaseSchema.TABLE_NAME_ARE_FRIENDS
					+ " WHERE inviterUsername = ?", inviter);
		} catch (SQLException e) {
			throw new RepositoryErrorException(e.getMessage());
		}
		
		Vector<String> friends = new Vector<String>();
		
		try{
			while (rs.next()){
				friends.add(rs.getString(1));
			}
		} catch (SQLException e) {
			throw new RepositoryErrorException("Error reading friends data: " + e);
		}
		return friends;
	}
	
}
