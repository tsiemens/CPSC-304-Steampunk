package org.steampunk.mist.jdbc;

import java.util.Calendar;

import org.steampunk.mist.model.Admin;
import org.steampunk.mist.model.Game;
import org.steampunk.mist.model.GameCopy;
import org.steampunk.mist.model.Player;
import org.steampunk.mist.model.User;
import org.steampunk.mist.repository.AdminRepository;
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
			Game demoGame = new Game(0, "Full Death 2.99999", "Spiggot", "E", "IT'S ALMOST HERE!!");
			GameRepository.addGame(demoGame);
			demoGame = new Game(42, "Banana Cart 7", "Nontondooo", "E", "Racing fun for everyone!");
			GameRepository.addGame(demoGame);
			demoGame = new Game(64, "Super Key Adventures", "DBM Industries", "CS", "1000101010101");
			GameRepository.addGame(demoGame);
			demoGame = new Game(1, "Ummmm... 4?", "Blarg", "U", "I ran out of ideas, but this description is going to be really, really, really, really, really, really, really, really, really, really, really, really, really, really, really, really, really, really, really long");
			GameRepository.addGame(demoGame);
		} catch (RepositoryErrorException e) {
			System.err.println("Failed to add demo games "+e);
		}
		
		// Sample Game copies
		try {
			Calendar now = Calendar.getInstance();
			GameCopy demoCopy = new GameCopy("123-456-789", 0, "SWAG", now);
			GameCopyRepository.addGameCopy(demoCopy);
			demoCopy = new GameCopy("111-111-111", 42, "SWAG", now);
			GameCopyRepository.addGameCopy(demoCopy);
			demoCopy = new GameCopy("222-222-222", 64, "SWAG", now);
			GameCopyRepository.addGameCopy(demoCopy);
			demoCopy = new GameCopy("987-654-321", 1, "SWAG", now);
			GameCopyRepository.addGameCopy(demoCopy);
		} catch (RepositoryErrorException e) {
			System.err.println("Failed to add demo game copies "+e);
		}
		
		System.out.println("done.");
	}
}
