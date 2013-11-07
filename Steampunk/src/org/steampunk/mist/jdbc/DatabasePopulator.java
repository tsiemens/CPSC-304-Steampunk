package org.steampunk.mist.jdbc;

import java.util.Calendar;

import org.steampunk.mist.model.Admin;
import org.steampunk.mist.model.Player;
import org.steampunk.mist.model.User;
import org.steampunk.mist.repository.AdminRepository;
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
		System.out.print("Populating database with demo entities and relationships... ");
		byte[] salt = User.generateSalt();
		byte[] passhash = User.getHash("lksjdfkljdsfkl", salt);
		Player player = new Player("testuser", passhash, salt, "testemail@pleaseignore.com", Calendar.getInstance());
		try {
			PlayerRepository.addPlayer(player);
		} catch (RepositoryErrorException e) {
			System.err.println("\nFailed to add demo entities "+e);
		}
		System.out.println("done.");
	}
}
