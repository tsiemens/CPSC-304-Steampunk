package org.steampunk.mist.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.steampunk.mist.jdbc.DatabaseManager;
import org.steampunk.mist.jdbc.DatabaseSchema;
import org.steampunk.mist.model.Admin;

public class AdminRepository extends UserRepository{

	/**
	 * Retrieves admin from database, with username
	 * @param username
	 * @return the user, or null if an error occurred
	 * @throws RepositoryErrorException
	 * @throws UserNotFoundException
	 */
	public static Admin getAdmin(String username)  throws RepositoryErrorException, UserNotFoundException {
		Admin admin = new Admin();
		setAdminData(username, admin);
		return admin;
	}
	
	/**
	 * Checks if the admin exists already
	 * @param username. this cannot be null
	 * @return true if user exists, false otherwise
	 * @throws RepositoryErrorException
	 */
	public static boolean adminExists(String username) throws RepositoryErrorException {
		DatabaseManager dbm = DatabaseManager.getInstance();
		ResultSet rs;
		try {
			rs = dbm.queryPrepared("SELECT username"
				+" FROM "+DatabaseSchema.TABLE_NAME_ADMINS
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
	
	/**
	 * Modifies admin to match the data contained in the database, for username
	 * @param username
	 * @param user
	 * @return true if user was successfully set to the correct data, false otherwise
	 * @throws RepositoryErrorException
	 * @throws UserNotFoundException
	 */
	public static void setAdminData(String username, Admin admin) throws RepositoryErrorException,
		UserNotFoundException
	{
		if (username == null)
			throw new UserNotFoundException("Username cannot be null");
		
		DatabaseManager dbm = DatabaseManager.getInstance();
		ResultSet rs;
		try {
			rs = dbm.queryPrepared("SELECT permissionTier"
				+" FROM "+DatabaseSchema.TABLE_NAME_ADMINS
				+" WHERE username = ?", username);
		} catch (SQLException e) {
			 throw new RepositoryErrorException(e.getMessage());
		}
		
		try{
			if (rs.next()){
				admin.setPermissionTier(rs.getInt("permissionTier"));
			} else {
				throw new UserNotFoundException("Admin does not exist");
			}
		} catch (SQLException e) {
			throw new RepositoryErrorException("Error reading admin data: "+e);
		}
		
		UserRepository.setUserData(username, admin);
	}
	
	public static void addAdmin(Admin admin) throws RepositoryErrorException{
		DatabaseManager dbm = DatabaseManager.getInstance();
		UserRepository.addUser(admin);
		try {
			dbm.updatePrepared("INSERT INTO " + DatabaseSchema.TABLE_NAME_ADMINS +"(username, permissionTier)"
					+" VALUES(?, ?)", admin.getUsername(), admin.getPermissionTier());
		} catch (SQLException e) {
			throw new RepositoryErrorException(e.getMessage());
		}
	}
	
	public static int getAdminPermissionTier(String adminName) throws RepositoryErrorException{
		DatabaseManager dbm = DatabaseManager.getInstance();
		ResultSet rs;
		try {
			rs = dbm.queryPrepared("SELECT permissionTier"
				+" FROM "+DatabaseSchema.TABLE_NAME_ADMINS
				+" WHERE username = ?", adminName);
		} catch (SQLException e) {
			 throw new RepositoryErrorException(e.getMessage());
		} 
		
		int permissionTier = 0;
		
		try {
			if (rs.next()){
				permissionTier = (rs.getInt("permissionTier"));
			} 
		} catch (SQLException e) {
			throw new RepositoryErrorException("Error reading admins data: "+e);
		}
		return permissionTier;
	}
}
