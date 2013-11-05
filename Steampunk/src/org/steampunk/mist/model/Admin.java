package org.steampunk.mist.model;

import java.util.Calendar;

public class Admin extends User {
	
	public static int ADMIN_TIER_ROOT = 0;
	public static int ADMIN_TIER_SYS_MOD = 1;
	public static int ADMIN_TIER_GAME_MOD = 2;
	
	private int mPermissionTier;
	
	// constructors
	public Admin() {
		super();
		mPermissionTier = ADMIN_TIER_GAME_MOD;
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
