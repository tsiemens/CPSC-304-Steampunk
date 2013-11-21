package org.steampunk.mist.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.steampunk.mist.AccountManager;
import org.steampunk.mist.model.Player;

public class Mist extends JFrame{

	private static final long serialVersionUID = 234794033610547082L;
	
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
		setBounds(100, 100, 600, 500);
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
		if (AccountManager.getInstance().getCurrentUser() instanceof Player) {
			// Player tabs
			addTab("Store", null, new StoreTab(), null);
			addTab("Game Library", null, new GameLibraryTab(), null);
			addTab(AccountManager.getInstance().getCurrentUser().getUsername(), null,
				new UserDetailsTab(), null);
			addTab("Clans", null, new ClansTab(), null);
			addTab("Friends", null, new FriendsTab(), null);
			tabbedPane.setSelectedIndex(1);
		} else {
			// Admin tabs
			addTab(AccountManager.getInstance().getCurrentUser().getUsername(), null,
					new UserDetailsTab(), null);
			addTab("Users", null, new SystemAdminUsersTab(), null);
		}
	}
}

