package org.steampunk.mist.model;

import java.util.Calendar;

public class Admin extends User {
	
	
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
	
	/**
	 * Creates copy of admin
	 * @param admin
	 */
	public Admin(Admin admin) {
		super(admin);
		mPermissionTier = admin.getPermissionTier();
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
