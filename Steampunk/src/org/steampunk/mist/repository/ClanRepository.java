package org.steampunk.mist.repository;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Vector;

import org.steampunk.mist.jdbc.DatabaseManager;
import org.steampunk.mist.jdbc.DatabaseSchema;
import org.steampunk.mist.model.Clan;
import org.steampunk.mist.repository.GameRepository.GameNotFoundException;

public class ClanRepository {

	/**
	 * Finds the entry of a clan in the Clans table and stores it in the Clan model
	 * @param clanName, gameID
	 * @return a Clan object
	 * @throws RepositoryErrorException
	 */
	public static Clan getClan(String clanName, int gameID) throws RepositoryErrorException, ClanNotFoundException {
		Clan clan = new Clan();
		setClanData(clanName, gameID, clan);
		return clan;
	}
	
	public static void setClanData(String clanName, int gameID, Clan clan) throws RepositoryErrorException, ClanNotFoundException {
		DatabaseManager dbm = DatabaseManager.getInstance();
		ResultSet rs;
		try {
			rs = dbm.queryPrepared("SELECT clanName, MoD, clanOwner, gameID"
					+" FROM "+DatabaseSchema.TABLE_NAME_CLANS
					+" WHERE clanName = ?"
					+" AND gameID = ?", clanName, gameID);
		} catch (SQLException e) {
			 throw new RepositoryErrorException(e.getMessage());
		}
		
		try{
			if (rs.next()){
				clan.setClanName(rs.getString("clanName"));
				clan.setMoD(rs.getString("MoD"));
				clan.setClanOwner(rs.getString("clanOwner"));
				clan.setGameID(rs.getInt("gameID"));
				System.out.println(clan.getClanName() + clan.getMoD() + clan.getClanOwner() + clan.getClanGameID());
			} else {
				throw new ClanNotFoundException("Clan does not exist");
			}
		} catch (SQLException e) {
			throw new RepositoryErrorException("Error reading game data: "+e);
		}
			
	}
	
	/**
	 * Determines if a user is an owner of the particular clan
	 * @param clanName, gameID, username
	 * @return true if owner for that clan, false otherwise
	 * @throws RepositoryErrorException
	 */
	public static boolean isClanOwner(String clanName, int gameID, String username) throws RepositoryErrorException, 
	ClanNotFoundException {
		/* 
		 * Query:
		 * 	SELECT clanOwner
		 * 	FROM Clans
		 * 	WHERE clanName = clanName AND gameID = gameID;
		 * 
		 * Query returns the owner of a clan
		 * Convert clan owner to a string
		 * Check if this string matches username
		 * Return true if matches / return false if strings do not match
		 * 
		 */
		String clanOwner = "";
		
		DatabaseManager dbm = DatabaseManager.getInstance();
		ResultSet rs;
		
		try {
			rs = dbm.queryPrepared("SELECT clanOwner"
					+" FROM "+DatabaseSchema.TABLE_NAME_CLANS
					+" WHERE clanName = ?"
					+" AND gameID = ?", clanName, gameID);
		} catch (SQLException e) {
			throw new RepositoryErrorException(e.getMessage());
		}
		
		try{
			if (rs.next()){
				clanOwner = rs.getString("clanOwner");
				System.out.println(clanOwner);
			} else {
				throw new ClanNotFoundException("Clan does not exist");
			}
		} catch (SQLException e) {
			throw new RepositoryErrorException("Error reading clan data: "+e);
		}

		if (clanOwner.equals(username)) {
			return true;
		} else {
			return false;
		}
	}

	
	/**
	 * Gets the message of the day for a particular clan
	 * @param clanName, gameID
	 * @return message of the day of the clan
	 * @throws RepositoryErrorException
	 */
	public static String getClanMoD(String clanName, int gameID) throws RepositoryErrorException, ClanNotFoundException {
		/*
		 * Query:
		 * 	SELECT MoD
		 * 	FROM Clans
		 * 	WHERE clanName = clanName AND gameID = gameID;
		 * 
		 * Query returns the MoD of a clan
		 * Convert clan MoD to string
		 * Return MoD string
		 */
		
		DatabaseManager dbm = DatabaseManager.getInstance();
		ResultSet rs;
		
		try {
			rs = dbm.queryPrepared("SELECT MoD"
					+" FROM "+DatabaseSchema.TABLE_NAME_CLANS
					+" WHERE clanName = ?", clanName
					+" AND gameID = ?", gameID);
		} catch (SQLException e) {
			throw new RepositoryErrorException(e.getMessage());
		}

		try{
			if (rs.next()){
				return rs.getString("MoD");
			} else {
				throw new ClanNotFoundException("Clan does not exist");
			}
		} catch (SQLException e) {
			throw new RepositoryErrorException("Error reading clan data: "+e);
		}
		
	}
	
	/**
	 * Gets the message of the day for a particular clan
	 * @param clanName, gameID
	 * @return the total number of players in a clan
	 * @throws RepositoryErrorException
	 */	
	public static int getClanMemberCount(String clanName, int gameID) throws RepositoryErrorException, ClanNotFoundException {
		/*
		 * Query:
		 * 	SELECT COUNT(DISTINCT username) as countMembers
		 * 	FROM isMember
		 * 	WHERE clanName = clanName AND gameID = gameID;
		 *  GROUP BY clanName and gameID;
		 * 	
		 * Query returns the number of members of a clan
		 * Convert number to integer
		 * Return integer
		 * 
		 */
		
		DatabaseManager dbm = DatabaseManager.getInstance();
		ResultSet rs;
		
		try {
			rs = dbm.queryPrepared("SELECT COUNT(DISTINCT username) as countMembers"
					+" FROM "+DatabaseSchema.TABLE_NAME_IS_MEMBER
					+" WHERE clanName = ?"
					+" AND gameID = ?"
					+" GROUP BY clanName, gameID", clanName, gameID);
		} catch (SQLException e) {
			throw new RepositoryErrorException(e.getMessage());
		}

		try{
			if (rs.next()){
				return rs.getInt("countMembers");
			} else {
				throw new ClanNotFoundException("Clan does not exist");
			}
		} catch (SQLException e) {
			throw new RepositoryErrorException("Error reading clan data: "+e);
		}		
		
	}

	/**
	 * Gets the list of mutual games shared between all clan members (as a concatenated string)
	 * @param clanName, gameID
	 * @return a string including all of the games that are shared by all members, separated by commas
	 * @throws RepositoryErrorException
	 */	
	public static Vector<String> getSharedGames(String clanName, int gameID) throws RepositoryErrorException {
		/*
		 * Query:
		 *  CREATE OR REPLACE VIEW r AS
		 *  SELECT gameID, ownerUsername
		 *  FROM gameCopies;
		 *  
		 *  CREATE OR REPLACE VIEW s AS
		 *  SELECT username
		 *  FROM isMember
		 *  WHERE clanName = clanName
		 *  AND gameID = gameID;
		 *  
		 *  CREATE OR REPLACE VIEW xProjection AS
		 *  SELECT gameID
		 *  FROM r;
		 *  
		 *  CREATE OR REPLACE VIEW xProjOfCrossSMinusR AS
		 *  SELECT *
		 *  FROM xProjection, s
		 *  MINUS
		 *  SELECT *
		 *  FROM r;
		 *  
		 *  CREATE OR REPLACE VIEW dqXvalues AS
		 *  SELECT gameID
		 *  FROM xProjOfCrossSMinusR;
		 *  
		 *  SELECT gameID
		 *  FROM r
		 *  MINUS
		 *  SELECT gameID
		 *  FROM dqXvalues;
		 *  
		 * 
		 * 
		 *
		 */
		
		Vector<String> sharedGameNames = new Vector<String>();
		
		DatabaseManager dbm = DatabaseManager.getInstance();
		ResultSet rs;
		
		try {
			/*
			dbm.queryPrepared("CREATE OR REPLACE VIEW r AS"
					+" SELECT gameID, ownerUsername "
					+" FROM gameCopies");
			
			dbm.queryPrepared("CREATE OR REPLACE VIEW s AS"
					+" SELECT username"
					+" FROM isMember"
					+" WHERE clanName = ?"
					+" AND gameID = ?", clanName, gameID);
			
			dbm.queryPrepared("CREATE OR REPLACE VIEW xProjection AS"
					+" SELECT gameID"
					+" FROM r");
			
			dbm.queryPrepared("CREATE OR REPLACE VIEW XProjOfCrossSMinusR AS"
					+" SELECT *"
					+" FROM xProjection, s"
					+" MINUS"
					+" SELECT *"
					+" FROM r");
			
			dbm.queryPrepared("CREATE OR REPLACE VIEW dqXvalues AS"
					+" SELECT gameID"
					+" FROM xProjOfCrossSMinusR");
			
			rs = dbm.queryPrepared("SELECT gameID"
					+" FROM r"
					+" MINUS"
					+" SELECT gameID"
					+" FROM dqXvalues");
			*/
			
			rs = dbm.queryPrepared("SELECT gameID"
								 +" FROM (SELECT gameID, ownerUsername"
								 	  + " FROM gameCopies)"
								 +" MINUS"
								 +" SELECT gameID"
								 +" FROM (SELECT gameID"
								 	   +" FROM (SELECT *"
								 	   +" FROM"
								 	  		+" (SELECT gameID"
								 	  		 +" FROM (SELECT gameID, ownerUsername"
								 	  			   +" FROM gameCopies)),"
								 	  		+" (SELECT username"
								 	  		 +" FROM isMember"
								 	  		 +" WHERE clanName = ? AND gameID = ?)"
								 +" MINUS"
								 +" SELECT *"
								 +" FROM (SELECT gameID, ownerUsername"
								  	   +" FROM gameCopies)))", 
								 clanName, gameID);
			
		} catch (SQLException e) {
			throw new RepositoryErrorException(e.getMessage());
		}
		
		System.out.println("Result of query: ");
		try{
			while (rs.next()){
				sharedGameNames.add(GameRepository.getGameName(rs.getInt("gameID")));
			} 
		} catch (SQLException e) {
			throw new RepositoryErrorException("Error reading clan data: "+e);
		} catch (GameNotFoundException e) {
			e.printStackTrace();
		}
		
		return sharedGameNames;
	}
	
	/**
	 * Gets a list of a clan's members (as a vector)
	 * @param clanName, gameID
	 * @return an array of strings including all the names of all the clan members
	 * @throws RepositoryErrorException
	 */	
	public static Vector<String> getClanMembersListing(String clanName, int gameID) throws RepositoryErrorException {
		/* 
		 * Query:
		 * 	SELECT DISTINCT username
		 * 	FROM isMember
		 * 	WHERE clanName = clanName AND gameID = gameID;
		 * 
		 * Query returns names of all members in the clan
		 * Convert data into a new vector of strings
		 * Return this vector
		 * 
		 */
		Vector<String> clanMembers = new Vector<String>();
		
		DatabaseManager dbm = DatabaseManager.getInstance();
		ResultSet rs;
		
		try {
			rs = dbm.queryPrepared("SELECT DISTINCT username"
					+" FROM "+DatabaseSchema.TABLE_NAME_IS_MEMBER
					+" WHERE clanName = ?"
					+" AND gameID = ?", clanName, gameID);
		} catch (SQLException e) {
			throw new RepositoryErrorException(e.getMessage());
		}
		
		try{
			while (rs.next()){
				clanMembers.add(rs.getString("username"));
			} 
		} catch (SQLException e) {
			throw new RepositoryErrorException("Error reading clan data: "+e);
		}

		return clanMembers;
	}	

	/**
	 * Removes a player from the clan
	 * @param clanName, gameID, username
	 * @return nothing
	 * @throws RepositoryErrorException
	 */	
	public static void removeClanMember(String clanName, int gameID, String username) throws RepositoryErrorException {
		/*
		 * Query:
		 * 	REMOVE FROM isMember
		 * 	WHERE clanName = clanName AND gameID = gameID AND username = username;
		 * 
		 */
		
		DatabaseManager dbm = DatabaseManager.getInstance();
		
		try {
			dbm.updatePrepared("DELETE FROM "+ DatabaseSchema.TABLE_NAME_IS_MEMBER
					+" WHERE clanName = ?"
					+" AND gameID = ?"
					+" AND username = ?", clanName, gameID, username);
		} catch (SQLException e) {
			throw new RepositoryErrorException(e.getMessage());
		}		
		
		return;
	}

	/**
	 * Add a member to the clan
	 * @param clanName, gameID, username
	 * @return nothing
	 * @throws RepositoryErrorException
	 */	
	public static void addClanMember(String clanName, int gameID, String username, Calendar memberSince) throws RepositoryErrorException {
		/*
		 * Set memberSince as the current date/time 
		 * Query:
		 * 	INSERT INTO isMember
		 * 	VALUES (username, clanName, gameID, memberSince);
		 * 
		 */
		Calendar now = Calendar.getInstance();
		
		DatabaseManager dbm = DatabaseManager.getInstance();
		try {
			dbm.updatePrepared("INSERT INTO "+ DatabaseSchema.TABLE_NAME_IS_MEMBER +"(username, clanName, gameID, memberSince)"
					+" VALUES(?, ?, ?, ?)", username, clanName, gameID, new Date(now.getTimeInMillis()));
		} catch (SQLException e) {
			throw new RepositoryErrorException(e.getMessage());
		}		
		
		return;
	}
	
	/**
	 * Checks if a user is a member of a particular clan
	 * @param clanName, gameID, username
	 * @return true if a member, false if not a member
	 * @throws RepositoryErrorException
	 */		
	public static boolean checkMembership(String clanName, int gameID, String username) throws RepositoryErrorException, ClanNotFoundException {
		/*
		 * Query:
		 * 	SELECT COUNT(*)
		 * 	FROM isMember
		 * 	WHERE clanName = clanName AND gameID = gameID AND username = username;
		 * 
		 * If 0 returned, return false
		 * Else if 1 returned, return true
		 * 
		 */
		DatabaseManager dbm = DatabaseManager.getInstance();
		ResultSet rs;
		
		int membershipCount = 0;
		
		try {
			rs = dbm.queryPrepared("SELECT COUNT(*) as checkMembership"
					+" FROM "+DatabaseSchema.TABLE_NAME_IS_MEMBER
					+" WHERE clanName = ?"
					+" AND gameID = ?"
					+" AND username = ?", clanName, gameID, username);
		} catch (SQLException e) {
			throw new RepositoryErrorException(e.getMessage());
		}

		try{
			if (rs.next()){
				membershipCount = rs.getInt("checkMembership");
			} else {
				throw new ClanNotFoundException("Clan does not exist");
			}
		} catch (SQLException e) {
			throw new RepositoryErrorException("Error reading clan data: "+e);
		}
		
		if (membershipCount == 1) {
			return true;
		} else {
			return false;
		}
		
	}
	
	/**
	 * Creates a clan
	 * @param clanName, gameID, MoD, ownerName
	 * @return nothing
	 * @throws RepositoryErrorException
	 */		
	public static void addClan(Clan clan) throws RepositoryErrorException {
		/*
		 * Query:
		 * 	INSERT INTO Clans
		 * 	VALUES (clanName, MoD, ClanOwner, gameID);
		 * 
		 */
		
		DatabaseManager dbm = DatabaseManager.getInstance();
		try {
			dbm.updatePrepared("INSERT INTO "+ DatabaseSchema.TABLE_NAME_CLANS +"(clanName, MoD, clanOwner, gameID)"
					+" VALUES(?, ?, ?, ?)", clan.getClanName(), clan.getMoD(), clan.getClanOwner(), clan.getClanGameID());
		} catch (SQLException e) {
			throw new RepositoryErrorException(e.getMessage());
		}		
		
		return;
	}
	
	/**
	 * Transfer ownership of a clan from one user to another member
	 * @param clanName, gameID, newOwner
	 * @return nothing
	 * @throws RepositoryErrorException
	 */			
	public static void transferOwnership(String clanName, int gameID, String newOwner) throws RepositoryErrorException {
		/*
		 * Query:
		 * 	UPDATE Clans
		 * 	SET clanOwner = newOwner
		 * 	WHERE clanName = clanName AND gameID = gameID;
		 * 
		 */
		
		DatabaseManager dbm = DatabaseManager.getInstance();
		try {
			dbm.updatePrepared("UPDATE "+ DatabaseSchema.TABLE_NAME_CLANS
					+" SET clanOwner = ?"
					+" WHERE clanName = ?"
					+" AND gameID = ?", newOwner, clanName, gameID);
		} catch (SQLException e) {
			throw new RepositoryErrorException(e.getMessage());
		}		
		
		return;
	}
	
	/**
	 * Set the MoD of a clan
	 * @param clanName, gameID, MoD
	 * @return nothing
	 * @throws RepositoryErrorException
	 */			
	public static void updateClanMoD(String clanName, int gameID, String MoD) throws RepositoryErrorException {
		/*
		 * Query:
		 * 	UPDATE Clans
		 * 	SET MoD = MoD
		 * 	WHERE clanName = clanName AND gameID = gameID;
		 * 
		 */
		
		DatabaseManager dbm = DatabaseManager.getInstance();
		try {
			dbm.updatePrepared("UPDATE "+ DatabaseSchema.TABLE_NAME_CLANS
					+" SET MoD = ?"
					+" WHERE clanName = ?"
					+" AND gameID = ?", MoD, clanName, gameID);
		} catch (SQLException e) {
			throw new RepositoryErrorException(e.getMessage());
		}		
		
		return;
	}

	/**
	 * Get a complete listing of the clans that the current player is a member of
	 * @param username
	 * @return A vector of the clan names and their gameID's
	 * @throws RepositoryErrorException
	 */	
	public static Vector<ClanNameGameIdPair> getClansJoinedListing(String username) throws RepositoryErrorException {
		/*
		 * TODO: How to return the gameID and the clanName?
		 * Need to cover the case when clans in two different games have the same name
		 * 
		 * Query:
		 * 	SELECT clanName, gameID
		 * 	FROM Clans
		 * 	WHERE username = username;
		 */
		
		Vector<ClanNameGameIdPair> clans = new Vector<ClanNameGameIdPair>();
		
		DatabaseManager dbm = DatabaseManager.getInstance();
		ResultSet rs;
		
		try {
			rs = dbm.queryPrepared("SELECT clanName, gameID"
					+" FROM "+DatabaseSchema.TABLE_NAME_IS_MEMBER
					+" WHERE username = ?", username);
		} catch (SQLException e) {
			throw new RepositoryErrorException(e.getMessage());
		}
		
		try{
			while (rs.next()){
				clans.add(new ClanNameGameIdPair(rs.getString("clanName"), rs.getInt("gameID")));
			} 
		} catch (SQLException e) {
			throw new RepositoryErrorException("Error reading clan data: "+e);
		}

		return clans;
	}
	
	public static class ClanNotFoundException extends Exception{	
		
		private static final long serialVersionUID = -4327940496520103396L;

		ClanNotFoundException(String reason) {
			super(reason);
		}
	}
	
	/*
	 * A helper class to store the clan name and game id pairs
	 */
	public static class ClanNameGameIdPair {
		public String clanName;
		public int gameID;
		
		public ClanNameGameIdPair(String title, int id) {
			this.clanName = title;
			this.gameID = id;
		}
		
		@Override
		public String toString() {
			return clanName + " (" + gameID + " )";
		}
	}
	
	
}
