package org.steampunk.mist.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import org.steampunk.mist.jdbc.DatabaseManager;
import org.steampunk.mist.jdbc.DatabaseSchema;
import org.steampunk.mist.model.User;

public class UserRepository {
	
	/**
	 * Checks the authenticity of the user
	 * @param username
	 * @param password
	 * @return true if the password is correct for the username
	 * @throws UserNotFoundException
	 * @throws RepositoryErrorException
	 */
	public static boolean authorizeUser(String username, String password) throws UserNotFoundException,
		RepositoryErrorException {
		if (username == null)
			throw new UserNotFoundException("Username cannot be null");
		else if (password == null)
			return false;
		
		DatabaseManager dbm = DatabaseManager.getInstance();
		ResultSet rs;
		try {
			rs = dbm.queryPrepared("SELECT password, passSalt"
				+" FROM "+DatabaseSchema.TABLE_NAME_USERS
				+" WHERE username = ?", username);
			if (rs.next()){
				byte[] dbPassHash = rs.getBytes("password");
				byte[] dbSalt = rs.getBytes("passSalt");
				
				byte[] passHash = User.getHash(password, dbSalt);
				
				if ( java.util.Arrays.equals(passHash, dbPassHash))
					return true;
			} else {
				throw new UserNotFoundException("User does not exist");
			}
		} catch (SQLException e) {
			 throw new RepositoryErrorException(e.getMessage());
		}
		return false;
	}

	/**
	 * Retrieves user from database, with username
	 * @param username
	 * @return the user, or null if an error occurred
	 * @throws RepositoryErrorException
	 * @throws UserNotFoundException
	 */
	public static User getUser(String username)  throws RepositoryErrorException, UserNotFoundException {
		User user = new User();
		setUserData(username, user);
		return user;
	}
	
	/**
	 * Checks if the user exists already
	 * @param username. this cannot be null
	 * @return true if user exists, false otherwise
	 * @throws RepositoryErrorException
	 */
	public static boolean userExists(String username) throws RepositoryErrorException {
		DatabaseManager dbm = DatabaseManager.getInstance();
		ResultSet rs;
		try {
			rs = dbm.queryPrepared("SELECT username"
				+" FROM "+DatabaseSchema.TABLE_NAME_USERS
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
			throw new RepositoryErrorException("Error reading user data: "+e);
		}
	}
	
	/**
	 * Modifies user to match the data contained in the database, for username
	 * @param username
	 * @param user
	 * @return true if user was successfully set to the correct data, false otherwise
	 * @throws RepositoryErrorException
	 * @throws UserNotFoundException
	 */
	public static void setUserData(String username, User user) throws RepositoryErrorException,
		UserNotFoundException
	{
		if (username == null)
			throw new UserNotFoundException("Username cannot be null");
		
		DatabaseManager dbm = DatabaseManager.getInstance();
		ResultSet rs;
		try {
			rs = dbm.queryPrepared("SELECT username, password, passSalt, email, dateJoined"
				+" FROM "+DatabaseSchema.TABLE_NAME_USERS
				+" WHERE username = ?", username);
		} catch (SQLException e) {
			 throw new RepositoryErrorException(e.getMessage());
		}
		
		try{
			if (rs.next()){
				user.setUsername(rs.getString("username"));
				user.setPasswordHash(rs.getBytes("password"));
				user.setPasswordSalt(rs.getBytes("passSalt"));
				user.setEmail(rs.getString("email"));
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(rs.getDate("dateJoined").getTime());
				user.setDateJoined(cal);
			} else {
				throw new UserNotFoundException("User does not exist");
			}
		} catch (SQLException e) {
			throw new RepositoryErrorException("Error reading user data: "+e);
		}
	}
	
	public static void addUser(User user) throws RepositoryErrorException{
		DatabaseManager dbm = DatabaseManager.getInstance();
		try {
			dbm.updatePrepared("INSERT INTO " + DatabaseSchema.TABLE_NAME_USERS +"(username, password, passSalt, email, dateJoined)"
					+" VALUES(?, ?, ?, ?, ?)", user.getUsername(), user.getPasswordHash(), user.getPasswordSalt(),
					user.getEmail(), new java.sql.Date(user.getDateJoined().getTimeInMillis()));
		} catch (SQLException e) {
			throw new RepositoryErrorException(e.getMessage());
		}
	}
	
	public static class UserNotFoundException extends Exception{

		/**
		 * 
		 */
		private static final long serialVersionUID = 3762770524381364592L;	
		
		UserNotFoundException(String reason) {
			super(reason);
		}
	}
}
