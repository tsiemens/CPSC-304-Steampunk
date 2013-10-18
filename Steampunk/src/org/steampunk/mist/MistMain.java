package org.steampunk.mist;

import org.steampunk.mist.jdbc.DatabaseManager;

public class MistMain {

	public static void main(String[] args)
	{
		DatabaseManager.getInstance();
	}

}
