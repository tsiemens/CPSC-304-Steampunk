package org.steampunk.mist.jdbc;

import java.util.Calendar;

import org.steampunk.mist.model.User;
import org.steampunk.mist.repository.RepositoryErrorException;
import org.steampunk.mist.repository.UserRepository;

public class DatabasePopulator {

	/**
	 * This must be run on a new database to populate it with the minimum entities:
	 * Root admin -- username: 'root', password: 'worstpassever' (change this immediately!) , email: "root@root"
	 */
	public static boolean addRequiredEntities(){
		byte[] salt = User.generateSalt();
		byte[] passhash = User.getHash("worstpassever", salt);
		User user = new User("root", passhash, salt, "root@root", Calendar.getInstance());
		try {
			UserRepository.addUser(user);
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
		User user = new User("testuser", passhash, salt, "testemail@pleaseignore.com", Calendar.getInstance());
		try {
			UserRepository.addUser(user);
		} catch (RepositoryErrorException e) {
			System.err.println("\nFailed to add demo entities "+e);
		}
		System.out.println("done.");
	}
}
