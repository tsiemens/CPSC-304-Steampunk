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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.steampunk.mist.AccountManager;
import org.steampunk.mist.model.Player;
import org.steampunk.mist.model.User;
import org.steampunk.mist.repository.AdminRepository;
import org.steampunk.mist.repository.RepositoryErrorException;
import org.steampunk.mist.repository.UserRepository;

public class Mist extends JFrame implements ChangeListener{

	private static final long serialVersionUID = 234794033610547082L;
	
	private JTabbedPane tabbedPane;
	private String username;
	private int permissionTier;
	
	private GameLibraryTab mGameLibTab;
	private ClansTab mClansTab;

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
		tabbedPane.addChangeListener(this);
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
		
		mnMist.addSeparator();
		JMenuItem mntmDelete = new JMenuItem("Delete Account");
		mntmDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {				
				showDeleteAccountDialog();			
			}
		});
		mnMist.add(mntmDelete);
		mnMist.addSeparator();
		
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
			mGameLibTab = new GameLibraryTab();
			addTab("Game Library", null, mGameLibTab, null);
			addTab(AccountManager.getInstance().getCurrentUser().getUsername(), null,
				new UserDetailsTab(), null);
			mClansTab = new ClansTab();
			addTab("Clans", null, mClansTab, null);
			addTab("Friends", null, new FriendsTab(), null);
			tabbedPane.setSelectedIndex(1);
		} else {
			// Admin tabs
			username= AccountManager.getInstance().getCurrentUser().getUsername();
			try {
				permissionTier = AdminRepository.getAdminPermissionTier(username);
			} catch (RepositoryErrorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (permissionTier == 0) {
				addTab(username, null, new UserDetailsTab(), null);
				addTab("Users", null, new SystemAdminUsersTab(), null);
				addTab("Games", null, new GameAdminTab(), null);
			}
			if (permissionTier == 2) {
				addTab(username, null, new UserDetailsTab(), null);

				addTab("Games", null, new GameAdminTab(), null);
			}

		}
	}
	
	@Override
	public void stateChanged(ChangeEvent arg0) {
		if (mGameLibTab != null)
				mGameLibTab.refreshGameList();
		
		if (mClansTab != null) {
			mClansTab.refreshJoinedClansList(AccountManager.getInstance().getCurrentUser().getUsername());
		}
		
	}
	
	private void showDeleteAccountDialog() {
		User cUser = AccountManager.getInstance().getCurrentUser();
		int delete = JOptionPane.showConfirmDialog(this, "Really delete your account?");
		if (delete == JOptionPane.OK_OPTION) {
			try {
				UserRepository.deleteUser(cUser);
				AccountManager.getInstance().setCurrentUser(null);
				tabbedPane.removeAll();
				showLoginDialog();
			} catch (RepositoryErrorException e) {
				JOptionPane.showMessageDialog(this, "Could not delete account. If you own any clans, please trasnfer ownership first.");
			}
		}
	}
}
