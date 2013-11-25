package org.steampunk.mist.jdbc;

import java.util.Calendar;

import org.steampunk.mist.model.Admin;
import org.steampunk.mist.model.Clan;
import org.steampunk.mist.model.Comment;
import org.steampunk.mist.model.Game;
import org.steampunk.mist.model.GameCopy;
import org.steampunk.mist.model.Player;
import org.steampunk.mist.model.User;
import org.steampunk.mist.repository.AdminRepository;
import org.steampunk.mist.repository.ClanRepository;
import org.steampunk.mist.repository.CommentRepository;
import org.steampunk.mist.repository.GameCopyRepository;
import org.steampunk.mist.repository.GameRepository;
import org.steampunk.mist.repository.PlayerRepository;
import org.steampunk.mist.repository.RepositoryErrorException;

public class DatabasePopulator {

	/**
	 * This must be run on a new database to populate it with the minimum entities:
	 * Root admin -- username: 'root', password: 'worstpassever' (change this immediately!) , email: "root@root"
	 */
	public static boolean addRequiredEntities(){
		byte[] salt = User.generateSalt();
		byte[] passhash = User.getHash("root", salt);
		byte[] passhash2 = User.getHash("admin", salt);
		Admin admin = new Admin(Admin.ADMIN_TIER_SYS_MOD, "root", passhash, salt, "root@root.root", Calendar.getInstance());
		Admin adminGame = new Admin(Admin.ADMIN_TIER_GAME_MOD, "tier1admin", passhash2, salt, "tier1@admin.admin", Calendar.getInstance());
		try {
			AdminRepository.addAdmin(admin);
			AdminRepository.addAdmin(adminGame);
			return true;
		} catch (RepositoryErrorException e) {
			System.err.println("Failed to add required entities "+e);
		}
		return false;
	}
	
	/**
	 * Adds a bunch of demo entities to the database
	 */
	public static void demoPopulate() {
		System.out.println("Populating database with demo entities and relationships... ");
		
		// ----- PLAYERS -----
		// TODO: Change passwords
		
		byte[] salt = User.generateSalt();
		byte[] passhash = User.getHash("123", salt);
		Player player = new Player("SWAG", passhash, salt, "xSWAGYOLO420x@pleaseignore.com", Calendar.getInstance());
		try {
			PlayerRepository.addPlayer(player);
			
			Calendar testDate = Calendar.getInstance();
			
			testDate.set(2009, 5, 16);
			salt = User.generateSalt();
			passhash = User.getHash("password", salt);
			player = new Player("John Doe", passhash, salt, "johndoe123@gmail.com", testDate);
			PlayerRepository.addPlayer(player);
			
			testDate.set(2012, 2, 5);
			salt = User.generateSalt();
			passhash = User.getHash("password", salt);
			player = new Player("Anthony", passhash, salt, "anthony223@gmail.com", testDate);
			PlayerRepository.addPlayer(player);
			
			testDate.set(2010, 8, 8);
			salt = User.generateSalt();
			passhash = User.getHash("password", salt);
			player = new Player("Kenny", passhash, salt, "ken019287@gmail.com", testDate);
			PlayerRepository.addPlayer(player);
			
			testDate.set(2011, 1, 1);
			salt = User.generateSalt();
			passhash = User.getHash("password", salt);
			player = new Player("Beanny", passhash, salt, "beancurd128@gmail.com", testDate);
			PlayerRepository.addPlayer(player);
			
			testDate.set(2013, 4, 9);
			salt = User.generateSalt();
			passhash = User.getHash("password", salt);
			player = new Player("Laura P", passhash, salt, "laura118@hotmail.com", testDate);
			PlayerRepository.addPlayer(player);
			
			testDate.set(2012, 11, 22);
			salt = User.generateSalt();
			passhash = User.getHash("password", salt);
			player = new Player("Oscar", passhash, salt, "oscar2345@yahoo.com", testDate);
			PlayerRepository.addPlayer(player);
			
			testDate.set(2011, 9, 16);
			salt = User.generateSalt();
			passhash = User.getHash("password", salt);
			player = new Player("Horatio", passhash, salt, "horatio222@gmail.com", testDate);
			PlayerRepository.addPlayer(player);
			
			testDate.set(2012, 9, 31);
			salt = User.generateSalt();
			passhash = User.getHash("password", salt);
			player = new Player("Battler", passhash, salt, "battle8@hotmail.com", testDate);
			PlayerRepository.addPlayer(player);
			
			
		} catch (RepositoryErrorException e) {
			System.err.println("Failed to add demo players "+e);
		}
		
		// ----- GAMES -----
		try {
			
			Game demoGame = new Game(0, "Beyond: Two Souls","Sony","M", "A psychological thriller about a girl.");
			GameRepository.addGame(demoGame);
			demoGame = new Game(1,"Dishonored","Bethesda","T","A sandbox assassin game.");
			GameRepository.addGame(demoGame);
			demoGame = new Game(2, "The Last Of Us","Sony","M","A post-apocalyptic action adventure about a guy.");
			GameRepository.addGame(demoGame);
			demoGame = new Game(3, "Need For Speed: Most Wanted","EA","T","An open-world racing game.");
			GameRepository.addGame(demoGame);
			demoGame = new Game(4, "Project P-100","Sony","E","Japanese style mecha game.");
			GameRepository.addGame(demoGame);
			demoGame = new Game(5, "Warframe","Digital Extremes","T","SPACE NINJAS EVERYWHERE! WAM BLAM KAPOW!");
			GameRepository.addGame(demoGame);
			
		} catch (RepositoryErrorException e) {
			System.err.println("Failed to add demo games "+e);
		}
		
		// ----- GAME COPIES -----
		try {
			
			Calendar testDate = Calendar.getInstance();
			
			testDate.set(2013, 4, 5);
			GameCopy demoCopy = new GameCopy("aer2n-le3d2-9438d-divr3", 0, "Laura P", testDate);
			GameCopyRepository.addGameCopy(demoCopy);
			
			testDate.set(2013, 2, 12);
			demoCopy = new GameCopy("dke9v-204k8-dine3-dien8", 3, "Beanny", testDate);
			GameCopyRepository.addGameCopy(demoCopy);
			
			testDate.set(2013, 5, 28);
			demoCopy = new GameCopy("lyb80-09dj7-dkeaq-de9cc", 2, "John Doe", testDate);
			GameCopyRepository.addGameCopy(demoCopy);
			
			testDate.set(2013, 6, 11);
			demoCopy = new GameCopy("m2sw0-0d9d5-pelc9-ki86d", 1, "Kenny", testDate);
			GameCopyRepository.addGameCopy(demoCopy);
			
			testDate.set(2013, 0, 1);
			demoCopy = new GameCopy("qx09m-8bg3k-sloee-dneh8", 1, "Anthony", testDate);
			GameCopyRepository.addGameCopy(demoCopy);
			
			// extra: For testing shared games
			testDate.set(2013, 5, 28);
			demoCopy = new GameCopy("xyb80-09dj7-dkeaq-de9cc", 1, "John Doe", testDate);
			GameCopyRepository.addGameCopy(demoCopy);
			
			testDate.set(2013, 5, 28);
			demoCopy = new GameCopy("ayb80-09dj7-dkeaq-de9cc", 2, "Kenny", testDate);
			GameCopyRepository.addGameCopy(demoCopy);
			
			testDate.set(2013, 5, 28);
			demoCopy = new GameCopy("byb80-09dj7-dkeaq-de9cc", 2, "Anthony", testDate);
			GameCopyRepository.addGameCopy(demoCopy);
			
			testDate.set(2013, 5, 28);
			demoCopy = new GameCopy("cyb80-09dj7-dkeaq-de9cc", 3, "John Doe", testDate);
			GameCopyRepository.addGameCopy(demoCopy);
			
			testDate.set(2013, 5, 28);
			demoCopy = new GameCopy("dyb80-09dj7-dkeaq-de9cc", 4, "Kenny", testDate);
			GameCopyRepository.addGameCopy(demoCopy);
			
			testDate.set(2013, 5, 28);
			demoCopy = new GameCopy("eyb80-09dj7-dkeaq-de9cc", 5, "Anthony", testDate);
			GameCopyRepository.addGameCopy(demoCopy);
			
			
		} catch (RepositoryErrorException e) {
			System.err.println("Failed to add demo game copies "+e);
		}
		
		// ----- COMMENTS -----
		// TODO: Change calendar dates
		try {
			
			Calendar now = Calendar.getInstance();
			
			Comment demoComment = new Comment("Best game!", "Kenny", 1, now);
			CommentRepository.addComment(demoComment);
			now.add(Calendar.MILLISECOND, 10);
			
			demoComment = new Comment("Talk about a total downer.", "Laura P", 0, now);
			CommentRepository.addComment(demoComment);
			now.add(Calendar.MILLISECOND, 10);
			
			demoComment = new Comment("Okay, this is the best game of the year.  Hands down.", "John Doe", 3, now);
			CommentRepository.addComment(demoComment);
			now.add(Calendar.MILLISECOND, 10);
			
			demoComment = new Comment("How could this game get a 5 rating?  It's not even good.", "Beanny", 2, now);
			CommentRepository.addComment(demoComment);
			now.add(Calendar.MILLISECOND, 10);
			
			demoComment = new Comment("Just because you don't like a game doesn't mean it's bad.", "John Doe", 2, now);
			CommentRepository.addComment(demoComment);
			now.add(Calendar.MILLISECOND, 10);
			
		} catch (RepositoryErrorException e) {
			System.err.println("Failed to add demo comments "+e);
		}
		
		// ----- CLANS -----
		try {
			
			Clan demoClan = new Clan("FearNot", "Gang up and destroy the NoFear clan", "Kenny", 1);
			ClanRepository.addClan(demoClan);
			
			demoClan = new Clan("NoFear", "FearNot is a pain in you know where", "Anthony", 1);
			ClanRepository.addClan(demoClan);
			
			demoClan = new Clan("SpeedTicketCollector", "Power boost option available for $250,000. Interested?", "Beanny", 3);
			ClanRepository.addClan(demoClan);
			
			demoClan = new Clan("Survivor", "Hungry, thirsty, no food, no weapon, no hope", "John Doe", 2);
			ClanRepository.addClan(demoClan);
			
			demoClan = new Clan("MyWayOrTheHighWay", "Eat our dust", "Beanny", 3);
			ClanRepository.addClan(demoClan);
			
		} catch (RepositoryErrorException e) {
			System.err.println("Failed to add demo comments "+e);
		}
		
		
		// ----- CLAN MEMBERSHIPS (IS MEMBER) -----
		
		try {
			
			Calendar testDate = Calendar.getInstance();
			
			testDate.set(2013, 5, 28);
			ClanRepository.addClanMember("FearNot", 1, "Kenny", testDate);
			
			testDate.set(2013, 5, 28);
			ClanRepository.addClanMember("NoFear", 1, "Anthony", testDate);
			
			testDate.set(2013, 5, 28);
			ClanRepository.addClanMember("SpeedTicketCollector", 3, "Beanny", testDate);
			
			testDate.set(2013, 5, 28);
			ClanRepository.addClanMember("Survivor", 2, "John Doe", testDate);
			
			testDate.set(2013, 5, 28);
			ClanRepository.addClanMember("MyWayOrTheHighWay", 3, "Beanny", testDate);
			
			// extra: For testing shared games
			testDate.set(2013, 5, 28);
			ClanRepository.addClanMember("FearNot", 1, "John Doe", testDate);
			
			testDate.set(2013, 5, 28);
			ClanRepository.addClanMember("FearNot", 1, "Anthony", testDate);
			
		} catch (RepositoryErrorException e) {
			System.err.println("Failed to add administrates "+e);
		}
		
		// ----- ACHIEVEMENTS -----
		
		// ----- ADMINS -----
		
		// ----- ARE FRIENDS -----
		
		// ----- ADMINISTRATES -----
		
		try {
			
			GameCopyRepository.addGameAdministered("tier1admin", 1);
			
			GameCopyRepository.addGameAdministered("tier1admin", 2);
			
			GameCopyRepository.addGameAdministered("tier1admin", 3);
			
		} catch (RepositoryErrorException e) {
			System.err.println("Failed to add demo comments "+e);
		}
		
		// ----- HAS EARNED -----
		
		System.out.println("done.");
	}
}
