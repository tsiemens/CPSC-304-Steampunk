package org.steampunk.mist.model;

import java.util.Calendar;

public class Admin extends User {
	
	private int mPermissionTier;
	
	// constructors
	public Admin() {
		super();
		mPermissionTier = 0;
	}
	
	public Admin(int permissionTier, String username, byte[] passwordHash, byte[] salt, String email, Calendar dateJoined) {
		super(username, passwordHash, salt, email, dateJoined);
		mPermissionTier = permissionTier;
	}
	
	// accessors
	public int getPermissionTier() {
		return mPermissionTier;
	}
	
	// mutators
	public void setPermissionTier(int permissionTier) {
		mPermissionTier = permissionTier;
	}
}
