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
		+"gameName CHAR(50),"
		+"publisher CHAR(50),"
		+"rating CHAR(8),"
		+"description CHAR(500))";
	
	public static final String CREATE_TABLE_ACHIEVEMENTS =	
		"CREATE TABLE " + TABLE_NAME_ACHIEVEMENTS
		+" (achievementName CHAR(50),"
		+" achievementDescription CHAR(140),"
		+" gameID INTEGER NOT NULL,"
		+" points INTEGER,"
		+" PRIMARY KEY (achievementName, gameID),"
		+" FOREIGN KEY (gameID) REFERENCES Games"
			+" ON DELETE CASCADE)";

	public static final String CREATE_TABLE_USERS =
		"CREATE TABLE " + TABLE_NAME_USERS
		+"(username CHAR(20),"
		+"password CHAR(20) NOT NULL,"
		+"email CHAR(50) UNIQUE NOT NULL,"
		+"dateJoined DATE,"
		+"PRIMARY KEY (username))";
	
	public static final String CREATE_TABLE_PLAYERS =
		"CREATE TABLE " + TABLE_NAME_PLAYERS
		+"(username CHAR(20) PRIMARY KEY,"
		+"FOREIGN KEY (username) REFERENCES Users "
			+"ON DELETE CASCADE)";
	
	public static final String CREATE_TABLE_GAME_COPIES =	 
		"CREATE TABLE " + TABLE_NAME_GAME_COPIES
		+"(gameKey CHAR(32),"
		+"gameID INTEGER NOT NULL,"
		+"ownerUsername CHAR(20),"
		+"purchaseDate Date,"
		+"PRIMARY KEY (gameKey),"
		+"FOREIGN KEY (gameID) REFERENCES Games " 
			+"ON DELETE CASCADE,"
		+"FOREIGN KEY (ownerUsername) REFERENCES Players(username) "
			+"ON DELETE SET NULL)";
	
	public static final String CREATE_TABLE_COMMENTS =
		"CREATE TABLE " + TABLE_NAME_COMMENTS
		+"(timestamp DATE,"
		+"gameID INTEGER NOT NULL,"
		+"username CHAR(20),"
		+"text CHAR(500),"
		+"PRIMARY KEY (timestamp, gameID),"
		+"FOREIGN KEY (gameID) REFERENCES Games "
			+"ON DELETE CASCADE,"
		+"FOREIGN KEY (username) REFERENCES Users "
			+"ON DELETE SET NULL)";
	
	public static final String CREATE_TABLE_CLANS =
		"CREATE TABLE " + TABLE_NAME_CLANS
		+"(clanName CHAR(50),"
		+"MoD CHAR(500),"
		+"clanOwner CHAR(20) NOT NULL,"
		+"gameID INTEGER,"
		+"PRIMARY KEY (clanName, gameID),"
		+"FOREIGN KEY (gameID) REFERENCES Games "
			+"ON DELETE CASCADE,"
		+"FOREIGN KEY (clanOwner) REFERENCES Players(username) "
			+"ON DELETE CASCADE)";
	
	public static final String CREATE_TABLE_ARE_FRIENDS =
		"CREATE TABLE " + TABLE_NAME_ARE_FRIENDS
		+"(inviterUsername CHAR(20),"
		+"inviteeUsername CHAR(20),"
		+"PRIMARY KEY (inviterUsername, inviteeUserName),"
		+"FOREIGN KEY (inviterUsername) REFERENCES Players(username) " 
			+"ON DELETE CASCADE,"
		+"FOREIGN KEY (inviteeUsername) REFERENCES Players(username) "
			+"ON DELETE CASCADE)";
	
	public static final String CREATE_TABLE_ADMINS =
		"CREATE TABLE " + TABLE_NAME_ADMINS
		+"(username CHAR(20) PRIMARY KEY,"
		+"permissionTier INTEGER,"
		+"FOREIGN KEY (username) REFERENCES Users "
			+"ON DELETE CASCADE)";
	
	public static final String CREATE_TABLE_ADMINISTRATES =
		"CREATE TABLE " + TABLE_NAME_ADMINISTRATES
		+"(username CHAR(20),"
		+"gameID INTEGER,"
		+"PRIMARY KEY (username, gameID),"
		+"FOREIGN KEY (username) REFERENCES Admins "
			+"ON DELETE CASCADE,"
		+"FOREIGN KEY (gameID) REFERENCES Games "
			+"ON DELETE CASCADE)";
	
	public static final String CREATE_TABLE_IS_MEMBER =
		"CREATE TABLE " + TABLE_NAME_IS_MEMBER
		+"(username CHAR(20),"
		+"clanName CHAR(50),"
		+"gameID INTEGER,"
		+"rank CHAR(20),"
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
		+"(username CHAR(20),"
		+"gameID INTEGER,"
		+"achievementName CHAR(50),"
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
