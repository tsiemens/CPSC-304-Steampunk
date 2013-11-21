package org.steampunk.mist.view;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import org.steampunk.mist.model.Admin;
import org.steampunk.mist.model.Player;
import org.steampunk.mist.repository.AdminRepository;
import org.steampunk.mist.repository.GameRepository;
import org.steampunk.mist.repository.PlayerRepository;
import org.steampunk.mist.repository.RepositoryErrorException;
import org.steampunk.mist.repository.UserRepository;
import org.steampunk.mist.repository.GameRepository.GameNameIdPair;
import org.steampunk.mist.repository.UserRepository.UserNotFoundException;

import javax.swing.JList;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

public class SystemAdminUsersTab extends JPanel {

	private static final long serialVersionUID = 6307043883115812582L;

	private static JLabel mUsername;
	private static JLabel lblUserList;
	private static JList<String> userList;
	private static Vector<String> users;
	private static JButton btnAddUser;
	private static JLabel lblGamesOfUser;
	private static JLabel lblUserEmail;
	private static JLabel lblUsernameLabel;
	private static JLabel lblUserEmailLabel;
	private static JList<GameNameIdPair> gameList;
	private static Vector<GameNameIdPair> games;
	private static JScrollPane gameScrollPane;
	private static JScrollPane userScrollPane;
//	private static JButton btnSearchUsers;
//	private static JTextField searchUsersTextField;
		
	public SystemAdminUsersTab() {

		setLayout(new BorderLayout(0, 0));
	
		//JPanel setup
		JPanel panel = new JPanel();
		add(panel, BorderLayout.CENTER);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] {0, 150, 0, 150, 30, 0};
		gbl_panel.rowHeights = new int[] {0, 14, 25, 25, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, 0.0};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0};
		panel.setLayout(gbl_panel);
		
		//Add User Button setup
		btnAddUser = new JButton("Add User");
		btnAddUser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				showAddUserDialog();
				setUserList();
			}
		});
		GridBagConstraints gbc_btnAddUser = new GridBagConstraints();
		gbc_btnAddUser.insets = new Insets(0, 0, 5, 5);
		gbc_btnAddUser.gridx = 1;
		gbc_btnAddUser.gridy = 1;
		panel.add(btnAddUser, gbc_btnAddUser);

		//Username Label setup
		lblUsernameLabel = new JLabel("Username:");
		GridBagConstraints gbc_lblUsernameLabel = new GridBagConstraints();
		gbc_lblUsernameLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblUsernameLabel.gridx = 3;
		gbc_lblUsernameLabel.gridy = 1;
		panel.add(lblUsernameLabel, gbc_lblUsernameLabel);
		
		//Username field setup
		mUsername = new JLabel("Username");
		GridBagConstraints gbc_mUsername = new GridBagConstraints();
		gbc_mUsername.insets = new Insets(0, 0, 5, 5);
		gbc_mUsername.gridx = 4;
		gbc_mUsername.gridy = 1;
		panel.add(mUsername, gbc_mUsername);
/*		
		//Search Users Button setup
		btnSearchUsers = new JButton("Search Users");
		btnSearchUsers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setUserList(searchUsersTextField.getText());
			}
		});
		GridBagConstraints gbc_btnSearchUsers = new GridBagConstraints();
		gbc_btnSearchUsers.insets = new Insets(0, 0, 5, 5);
		gbc_btnSearchUsers.gridx = 0;
		gbc_btnSearchUsers.gridy = 1;
		panel.add(btnSearchUsers, gbc_btnSearchUsers);
		
		//Search Users text field setup
		searchUsersTextField = new JTextField();
		GridBagConstraints gbc_searchUsersTextField = new GridBagConstraints();
		gbc_searchUsersTextField.insets = new Insets(0, 0, 5, 5);
		gbc_searchUsersTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_searchUsersTextField.gridx = 1;
		gbc_searchUsersTextField.gridy = 1;
		panel.add(searchUsersTextField, gbc_searchUsersTextField);
		searchUsersTextField.setColumns(20);
*/		
		//User Email Label setup
		lblUserEmailLabel = new JLabel("User email:");
		GridBagConstraints gbc_lblUserEmailLabel = new GridBagConstraints();
		gbc_lblUserEmailLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblUserEmailLabel.gridx = 3;
		gbc_lblUserEmailLabel.gridy = 2;
		panel.add(lblUserEmailLabel, gbc_lblUserEmailLabel);
		
		//User Email field setup
		lblUserEmail = new JLabel("User Email");
		GridBagConstraints gbc_lblUserEmail = new GridBagConstraints();
		gbc_lblUserEmail.insets = new Insets(0, 0, 5, 5);
		gbc_lblUserEmail.gridx = 4;
		gbc_lblUserEmail.gridy = 2;
		panel.add(lblUserEmail, gbc_lblUserEmail);
		
		//List of Users Label setup
		lblUserList = new JLabel("User List");
		GridBagConstraints gbc_lblUserList = new GridBagConstraints();
		gbc_lblUserList.anchor = GridBagConstraints.WEST;
		gbc_lblUserList.insets = new Insets(0, 0, 5, 5);
		gbc_lblUserList.gridx = 1;
		gbc_lblUserList.gridy = 3;
		panel.add(lblUserList, gbc_lblUserList);
		
		//List of Users ScrollPane setup
		userScrollPane = new JScrollPane();
		GridBagConstraints gbc_userScrollPane = new GridBagConstraints();
		gbc_userScrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_userScrollPane.fill = GridBagConstraints.BOTH;
		gbc_userScrollPane.gridx = 1;
		gbc_userScrollPane.gridy = 4;
		panel.add(userScrollPane, gbc_userScrollPane);
		
		//List of Users setup
		userList = new JList<String>();
		userScrollPane.setViewportView(userList);
		userList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				SystemAdminUsersTab.userListHandler(e);
			}
		});
		//
		userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		userList.setLayoutOrientation(JList.VERTICAL);
		userList.setVisibleRowCount(-1);
		
		//Games of User List Label setup
		lblGamesOfUser = new JLabel("Games of User");
		GridBagConstraints gbc_lblGamesOfUser = new GridBagConstraints();
		gbc_lblGamesOfUser.anchor = GridBagConstraints.NORTH;
		gbc_lblGamesOfUser.insets = new Insets(0, 0, 5, 5);
		gbc_lblGamesOfUser.gridx = 3;
		gbc_lblGamesOfUser.gridy = 3;
		panel.add(lblGamesOfUser, gbc_lblGamesOfUser);
		
		//Games of User List ScrollPane setup
		gameScrollPane = new JScrollPane();
		GridBagConstraints gbc_gameScrollPane = new GridBagConstraints();
		gbc_gameScrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_gameScrollPane.gridwidth = 2;
		gbc_gameScrollPane.fill = GridBagConstraints.BOTH;
		gbc_gameScrollPane.gridx = 3;
		gbc_gameScrollPane.gridy = 4;
		panel.add(gameScrollPane, gbc_gameScrollPane);
		
		//Games of User setup
		gameList = new JList<GameNameIdPair>();
		gameScrollPane.setViewportView(gameList);
		//
		gameList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		gameList.setLayoutOrientation(JList.VERTICAL);
		gameList.setVisibleRowCount(-1);
		
		onCreateView();
	}
	
	public void onCreateView()
	{		
		lblGamesOfUser.setVisible(false);
		lblUsernameLabel.setVisible(false);
		lblUserEmailLabel.setVisible(false);
		
		mUsername.setText("");
		lblUserEmail.setText("");

		setUserList();
		refreshGameList("");
	}

	private static void setUpGameList(String username){
		//Set up a vector of gameNameIdPairs of user from GameCopyRepository
		 try {
			games = GameRepository.getGameNamesOwnedByUser(username);
			gameList.setListData(games);
		} catch (RepositoryErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
	}

	private void showAddUserDialog(){
		/*
		 * //add username
		 * //add password
		 * //add email
		 * //add type of user selection
		 */
		Vector<String> userVector = CustomOptionPane.showAddUserDialog(this, "Fill in all fields below", "Add User", null);
		if(userVector == null){
			return;
		}
		if(userVector.elementAt(3) == "Player"){
			Player player = new Player();
			
			player.setUsername(userVector.elementAt(0));
			player.setPasswordSalt(Player.generateSalt());
			player.setPassword(userVector.elementAt(1));
			player.setEmail(userVector.elementAt(2));
			player.setDateJoined(Calendar.getInstance());
			
			try {
				PlayerRepository.addPlayer(player);
			} catch (RepositoryErrorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
		if(userVector.elementAt(3) == "System Admin"){
			Admin admin = new Admin();
			
			admin.setUsername(userVector.elementAt(0));
			admin.setPasswordSalt(Admin.generateSalt());
			admin.setPassword(userVector.elementAt(1));
			admin.setEmail(userVector.elementAt(2));
			admin.setDateJoined(Calendar.getInstance());
			admin.setPermissionTier(1);
			
			try {
				AdminRepository.addAdmin(admin);
			} catch (RepositoryErrorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
		if(userVector.elementAt(3) == "Game Admin"){
			Admin admin = new Admin();
			
			admin.setUsername(userVector.elementAt(0));
			admin.setPasswordSalt(Admin.generateSalt());
			admin.setPassword(userVector.elementAt(1));
			admin.setEmail(userVector.elementAt(2));
			admin.setDateJoined(Calendar.getInstance());
			admin.setPermissionTier(2);
			
			try {
				AdminRepository.addAdmin(admin);
			} catch (RepositoryErrorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
	}

	private static void userListHandler(ListSelectionEvent event) {
		if (event.getValueIsAdjusting() == false) {
	        if (userList.getSelectedIndex() == -1) {
	        	//No selection, do nothing.
	        } else {
	        	//Selection, reset user details.
//	        	System.out.println("Selected index: "+userList.getSelectedIndex());
	        	
	        	//Test if user is a player
	        	Boolean isPlayer = false;
				try {
					isPlayer = PlayerRepository.playerExists(users.elementAt(userList.getSelectedIndex()));
				} catch (RepositoryErrorException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	        	
				if(isPlayer){
	        		//reset username
	        		lblUsernameLabel.setVisible(true);
		        	String username = users.elementAt(userList.getSelectedIndex());
		        	mUsername.setText(username);
		        	//reset user email
		        	lblUserEmailLabel.setVisible(true);
		        	String email = "";
					try {
						email = UserRepository.getUser(username).getEmail();
					} catch (RepositoryErrorException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (UserNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    		lblUserEmail.setText(email);
		    		//reset user game list
	        		lblGamesOfUser.setVisible(true);
		    		refreshGameList(username);
	        	}
			
				//Test if user is an admin
	        	Boolean isAdmin = false;
				try {
					isAdmin = AdminRepository.adminExists(users.elementAt(userList.getSelectedIndex()));
				} catch (RepositoryErrorException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	        	
				if(isAdmin){
	        		//reset username
	        		lblUsernameLabel.setVisible(true);
		        	String username = users.elementAt(userList.getSelectedIndex());
		        	mUsername.setText(username);
		        	//reset user email
		        	lblUserEmailLabel.setVisible(true);
		        	String email = "";
					try {
						email = UserRepository.getUser(username).getEmail();
					} catch (RepositoryErrorException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (UserNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    		lblUserEmail.setText(email);
		    		//reset user game list (hide it)
	        		lblGamesOfUser.setVisible(false);
		    		refreshGameList("");
	        	}
	        }
	    }
		
	}

	private static void setUserList() {
		try {
//			users = UserRepository.searchUsers(username);
			users = UserRepository.getAllUsers();
			userList.setListData(users);
		} catch (RepositoryErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private static void refreshGameList(String username) {
		//If User is not Player, make sure the game list and label aren't put on the screen.
		try {
			if (!PlayerRepository.playerExists(username)){
				gameScrollPane.setVisible(false);
				gameList.setVisible(false);
				lblGamesOfUser.setVisible(false);				
			}else{
				setUpGameList(username);
				gameScrollPane.setVisible(true);
				gameList.setVisible(true);
				lblGamesOfUser.setVisible(true);
			}
		} catch (RepositoryErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}