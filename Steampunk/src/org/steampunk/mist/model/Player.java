package org.steampunk.mist.model;

import java.util.Calendar;

public class Player extends User{

	// constructors
	public Player() {
		super();
	}
	
	public Player(String username, byte[] passwordHash, byte[] salt, String email, Calendar dateJoined) {
		super(username, passwordHash, salt, email, dateJoined);
	}
	
	// all accessors/mutators inherhited from parent User class
	
}
