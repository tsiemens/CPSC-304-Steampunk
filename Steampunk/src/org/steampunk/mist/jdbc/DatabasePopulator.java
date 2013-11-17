package org.steampunk.mist.jdbc;

import java.util.Calendar;

import org.steampunk.mist.model.Admin;
import org.steampunk.mist.model.Comment;
import org.steampunk.mist.model.Game;
import org.steampunk.mist.model.GameCopy;
import org.steampunk.mist.model.Player;
import org.steampunk.mist.model.User;
import org.steampunk.mist.repository.AdminRepository;
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
		Admin admin = new Admin(Admin.ADMIN_TIER_ROOT, "root", passhash, salt, "root@root", Calendar.getInstance());
		try {
			AdminRepository.addAdmin(admin);
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
		// Sample Players
		byte[] salt = User.generateSalt();
		byte[] passhash = User.getHash("123", salt);
		Player player = new Player("SWAG", passhash, salt, "xSWAGYOLO420x@pleaseignore.com", Calendar.getInstance());
		try {
			PlayerRepository.addPlayer(player);
		} catch (RepositoryErrorException e) {
			System.err.println("Failed to add demo players "+e);
		}
		
		// Sample Games
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
		
		// Sample Game copies
		try {
			Calendar now = Calendar.getInstance();
			GameCopy demoCopy = new GameCopy("123-456-789", 0, "SWAG", now);
			GameCopyRepository.addGameCopy(demoCopy);
			demoCopy = new GameCopy("111-111-111", 1, "SWAG", now);
			GameCopyRepository.addGameCopy(demoCopy);
			demoCopy = new GameCopy("222-222-222", 2, "SWAG", now);
			GameCopyRepository.addGameCopy(demoCopy);
			demoCopy = new GameCopy("987-654-321", 3, "SWAG", now);
			GameCopyRepository.addGameCopy(demoCopy);
			demoCopy = new GameCopy("333-333-333", 5, "SWAG", now);
			GameCopyRepository.addGameCopy(demoCopy);
		} catch (RepositoryErrorException e) {
			System.err.println("Failed to add demo game copies "+e);
		}
		
		// Sample Comments
		try {
			Calendar now = Calendar.getInstance();
			Comment demoComment = new Comment("best game evarrrrr!", "SWAG", 5, now);
			CommentRepository.addComment(demoComment);
			now.add(Calendar.MILLISECOND, 10);
			demoComment = new Comment("TROLOLO", "SWAG", 5, now);
			CommentRepository.addComment(demoComment);
			now.add(Calendar.MILLISECOND, 10);
			demoComment = new Comment("meh", "SWAG", 3, now);
			CommentRepository.addComment(demoComment);
			now.add(Calendar.MILLISECOND, 10);
			demoComment = new Comment("<3 bethesda", "SWAG", 1, now);
			CommentRepository.addComment(demoComment);
			now.add(Calendar.MILLISECOND, 10);
			demoComment = new Comment("Please stop or I will have to ban you.", "root", 5, now);
			CommentRepository.addComment(demoComment);
			now.add(Calendar.MILLISECOND, 10);
		} catch (RepositoryErrorException e) {
			System.err.println("Failed to add demo comments "+e);
		}
		
		System.out.println("done.");
	}
}
