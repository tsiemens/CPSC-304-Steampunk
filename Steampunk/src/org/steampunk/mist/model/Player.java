package org.steampunk.mist.model;

import java.util.Calendar;

public class Player extends User{

	// Further player specific attributes may be desired later
	
	// constructors
	public Player() {
		super();
	}
	
	public Player(String username, byte[] passwordHash, byte[] salt, String email, Calendar dateJoined) {
		super(username, passwordHash, salt, email, dateJoined);
	}
	
	/**
	 * Creates copy of player
	 * @param player
	 */
	public Player(Player player) {
		super(player);
	}
	
	// all accessors/mutators inherhited from parent User class
	
}
