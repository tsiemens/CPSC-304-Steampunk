package org.steampunk.mist.jdbc;

import java.util.ArrayList;
import java.util.List;

public class DatabaseSchema {

	public static final String TABLE_NAME_GAMES = "Games";
	public static final String TABLE_NAME_ACHIEVEMENTS = "Achievements";
	public static final String TABLE_NAME_USERS = "Users";
	public static final String TABLE_NAME_PLAYERS = "Players";
	public static final String TABLE_NAME_GAME_COPIES = "GameCopies";
	public static final String TABLE_NAME_COMMENTS = "Comments";
	public static final String TABLE_NAME_CLANS = "Clans";
	public static final String TABLE_NAME_ARE_FRIENDS = "areFriends";
	public static final String TABLE_NAME_ADMINS = "Admins";
	public static final String TABLE_NAME_ADMINISTRATES = "administrates";
	public static final String TABLE_NAME_IS_MEMBER = "isMember";
	public static final String TABLE_NAME_HAS_EARNED = "hasEarned";
	
	public static final String CREATE_TABLE_GAMES = 
		"CREATE TABLE " + TABLE_NAME_GAMES
		+"(gameID INTEGER PRIMARY KEY,"
		+"gameName VARCHAR(50),"
		+"publisher VARCHAR(50),"
		+"rating VARCHAR(8),"
		+"description VARCHAR(500))";
	
	public static final String CREATE_TABLE_ACHIEVEMENTS =	
		"CREATE TABLE " + TABLE_NAME_ACHIEVEMENTS
		+" (achievementName VARCHAR(50),"
		+" achievementDesc VARCHAR(140),"
		+" gameID INTEGER NOT NULL,"
		+" points INTEGER,"
		+" PRIMARY KEY (achievementName, gameID),"
		+" FOREIGN KEY (gameID) REFERENCES Games"
			+" ON DELETE CASCADE)";

	public static final String CREATE_TABLE_USERS =
		"CREATE TABLE " + TABLE_NAME_USERS
		+"(username VARCHAR(20),"
		+"password RAW(32) NOT NULL,"
		+"passSalt RAW(8) NOT NULL,"
		+"email VARCHAR(50) UNIQUE NOT NULL,"
		+"dateJoined DATE,"
		+"PRIMARY KEY (username))";
	
	public static final String CREATE_TABLE_PLAYERS =
		"CREATE TABLE " + TABLE_NAME_PLAYERS
		+"(username VARCHAR(20) PRIMARY KEY,"
		+"FOREIGN KEY (username) REFERENCES Users "
			+"ON DELETE CASCADE)";
	
	public static final String CREATE_TABLE_GAME_COPIES =	 
		"CREATE TABLE " + TABLE_NAME_GAME_COPIES
		+"(gameKey VARCHAR(32),"
		+"gameID INTEGER NOT NULL,"
		+"ownerUsername VARCHAR(20),"
		+"purchaseDate DATE,"
		+"PRIMARY KEY (gameKey),"
		+"FOREIGN KEY (gameID) REFERENCES Games " 
			+"ON DELETE CASCADE,"
		+"FOREIGN KEY (ownerUsername) REFERENCES Players(username) "
			+"ON DELETE SET NULL)";
	
	public static final String CREATE_TABLE_COMMENTS =
		"CREATE TABLE " + TABLE_NAME_COMMENTS
		+"(timestamp TIMESTAMP,"
		+"gameID INTEGER NOT NULL,"
		+"username VARCHAR(20),"
		+"text VARCHAR(500),"
		+"PRIMARY KEY (timestamp, gameID),"
		+"FOREIGN KEY (gameID) REFERENCES Games "
			+"ON DELETE CASCADE,"
		+"FOREIGN KEY (username) REFERENCES Users "
			+"ON DELETE SET NULL)";
	
	public static final String CREATE_TABLE_CLANS =
		"CREATE TABLE " + TABLE_NAME_CLANS
		+"(clanName VARCHAR(50),"
		+"MoD VARCHAR(500),"
		+"clanOwner VARCHAR(20) NOT NULL,"
		+"gameID INTEGER,"
		+"PRIMARY KEY (clanName, gameID),"
		+"FOREIGN KEY (gameID) REFERENCES Games "
			+"ON DELETE CASCADE,"
		+"FOREIGN KEY (clanOwner) REFERENCES Players(username) "
			+"ON DELETE CASCADE)";
	
	public static final String CREATE_TABLE_ARE_FRIENDS =
		"CREATE TABLE " + TABLE_NAME_ARE_FRIENDS
		+"(inviterUsername VARCHAR(20),"
		+"inviteeUsername VARCHAR(20),"
		+"PRIMARY KEY (inviterUsername, inviteeUserName),"
		+"FOREIGN KEY (inviterUsername) REFERENCES Players(username) " 
			+"ON DELETE CASCADE,"
		+"FOREIGN KEY (inviteeUsername) REFERENCES Players(username) "
			+"ON DELETE CASCADE)";
	
	public static final String CREATE_TABLE_ADMINS =
		"CREATE TABLE " + TABLE_NAME_ADMINS
		+"(username VARCHAR(20) PRIMARY KEY,"
		+"permissionTier INTEGER,"
		+"FOREIGN KEY (username) REFERENCES Users "
			+"ON DELETE CASCADE)";
	
	public static final String CREATE_TABLE_ADMINISTRATES =
		"CREATE TABLE " + TABLE_NAME_ADMINISTRATES
		+"(username VARCHAR(20),"
		+"gameID INTEGER,"
		+"PRIMARY KEY (username, gameID),"
		+"FOREIGN KEY (username) REFERENCES Admins "
			+"ON DELETE CASCADE,"
		+"FOREIGN KEY (gameID) REFERENCES Games "
			+"ON DELETE CASCADE)";
	
	public static final String CREATE_TABLE_IS_MEMBER =
		"CREATE TABLE " + TABLE_NAME_IS_MEMBER
		+"(username VARCHAR(20),"
		+"clanName VARCHAR(50),"
		+"gameID INTEGER,"
		+"memberSince DATE,"
		+"PRIMARY KEY (username, clanName, gameID),"
		+"FOREIGN KEY (username) REFERENCES Players "
			+"ON DELETE CASCADE,"
		+"FOREIGN KEY (clanName, gameID) REFERENCES Clans "
			+"ON DELETE CASCADE,"
		+"FOREIGN KEY (gameID) REFERENCES Games "
			+"ON DELETE CASCADE)";
	
	public static final String CREATE_TABLE_HAS_EARNED =
		"CREATE TABLE " + TABLE_NAME_HAS_EARNED
		+"(username VARCHAR(20),"
		+"gameID INTEGER,"
		+"achievementName VARCHAR(50),"
		+"PRIMARY KEY (username, gameID, achievementName),"
		+"FOREIGN KEY (username) REFERENCES Players "
			+"ON DELETE CASCADE,"
		+"FOREIGN KEY (gameID) REFERENCES Games "
			+"ON DELETE CASCADE,"
		+"FOREIGN KEY (achievementName, gameID) REFERENCES Achievements "
			+"ON DELETE CASCADE)";

	/**
	 * @return A List of table names, ordered the same as would be run to create them.
	 */
	public static List<String> getTableNames(){
		ArrayList<String> tables = new ArrayList<String>(12);
		tables.add(TABLE_NAME_GAMES);
		tables.add(TABLE_NAME_ACHIEVEMENTS); 
		tables.add(TABLE_NAME_USERS);
		tables.add(TABLE_NAME_PLAYERS); 
		tables.add(TABLE_NAME_GAME_COPIES);
		tables.add(TABLE_NAME_COMMENTS); 
		tables.add(TABLE_NAME_CLANS);
		tables.add(TABLE_NAME_ARE_FRIENDS); 
		tables.add(TABLE_NAME_ADMINS);
		tables.add(TABLE_NAME_ADMINISTRATES);
		tables.add(TABLE_NAME_IS_MEMBER);
		tables.add(TABLE_NAME_HAS_EARNED);
		return tables;
	}
	
	/**
	 * @return a List of create table statements, in the order they must be
	 * run, to preserve reference integrity
	 */
	public static List<String> getCreateTableStatements(){
		ArrayList<String> statements = new ArrayList<String>(12);
		statements.add(CREATE_TABLE_GAMES); 
		statements.add(CREATE_TABLE_ACHIEVEMENTS); 
		statements.add(CREATE_TABLE_USERS);
		statements.add(CREATE_TABLE_PLAYERS);
		statements.add(CREATE_TABLE_GAME_COPIES);
		statements.add(CREATE_TABLE_COMMENTS); 
		statements.add(CREATE_TABLE_CLANS);
		statements.add(CREATE_TABLE_ARE_FRIENDS);
		statements.add(CREATE_TABLE_ADMINS);
		statements.add(CREATE_TABLE_ADMINISTRATES);
		statements.add(CREATE_TABLE_IS_MEMBER);
		statements.add(CREATE_TABLE_HAS_EARNED);
		return statements;
	}
}
