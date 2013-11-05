package org.steampunk.mist.view;

import javax.swing.JFrame;

import java.awt.BorderLayout;

import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.steampunk.mist.AccountManager;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Mist extends JFrame{

	private JTabbedPane tabbedPane;

	/**
	 * Create the application.
	 */
	public Mist() {
		initialize();
		setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		setBounds(100, 100, 450, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnMist = new JMenu("Mist");
		menuBar.add(mnMist);
		
		JMenuItem mntmLogout = new JMenuItem("Logout");
		mntmLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AccountManager.getInstance().setCurrentUser(null);
				
				tabbedPane.removeAll();			
				showLoginDialog();
			}
		});
		mnMist.add(mntmLogout);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Exiting...");
				System.exit(0);
			}
		});
		mnMist.add(mntmExit);
		
	}

	/**
	 * Create a tab.
	 */
	public void addTab(String title, Icon icon, JPanel panel, String tip){
		tabbedPane.addTab(title, icon, panel, tip);
	}
	
	public void showLoginDialog() {
		LoginDialog dialog = new LoginDialog();
		dialog.setModal(true);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setOnLoginListener(new LoginDialog.OnLoginListener() {
			
			@Override
			public void onLogin() {
				setupTabs();
			}
		});
		dialog.setVisible(true);
	}
	
	private void setupTabs() {
		addTab(AccountManager.getInstance().getCurrentUser().getUsername(), null,
				new UserDetailsTab(), null);
	}
}

