package org.steampunk.mist;

import java.awt.EventQueue;

import org.steampunk.mist.jdbc.DatabaseManager;
import org.steampunk.mist.view.Mist;

public class MistMain {
	
	public static void main(String[] args)
	{
		DatabaseManager.getInstance();
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Mist window = new Mist();
					//...
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
