package org.steampunk.mist;

import java.awt.EventQueue;

import javax.swing.JDialog;

import org.steampunk.mist.jdbc.DatabaseManager;
import org.steampunk.mist.view.LoginDialog;
import org.steampunk.mist.view.Mist;

public class MistMain {
	
	private static Mist window;
	
	public static void main(String[] args)
	{	
		DatabaseManager.getInstance();
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
					window = new Mist();
					window.showLoginDialog();
			}
		});
	}
}
