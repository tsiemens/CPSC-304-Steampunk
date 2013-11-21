package org.steampunk.mist.view;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import org.steampunk.mist.AccountManager;
import org.steampunk.mist.model.User;
import org.steampunk.mist.repository.AdminRepository;
import org.steampunk.mist.repository.PlayerRepository;
import org.steampunk.mist.repository.RepositoryErrorException;
import org.steampunk.mist.repository.UserRepository;
import org.steampunk.mist.repository.UserRepository.UserNotFoundException;

import java.awt.FlowLayout;

import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class FriendsTab extends JPanel implements ListSelectionListener {
	
	private static final long serialVersionUID = 8436157125343804361L;
	
	Vector<String> mFriends;
	JLabel mFriendsLabel;
	String inviter = AccountManager.getInstance().getCurrentUser().getUsername();
	JList<String> mFriendsList;
	private JTextField txtPlayerName;
	
	
	
	public FriendsTab() {
		setLayout(new BorderLayout(0, 0));
		
		mFriendsLabel = new JLabel("Friends");
		mFriendsLabel.setHorizontalAlignment(SwingConstants.CENTER);
		mFriendsLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		add(mFriendsLabel, BorderLayout.NORTH);
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.CENTER);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0, 5, 40, 0};
		gbl_panel.rowHeights = new int[]{50, 50, 30, 30, 50, 50, 0};
		gbl_panel.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, 0.0};
		gbl_panel.rowWeights = new double[]{1.0, 0.0, 0.0, 0.0, 0.0};
		panel.setLayout(gbl_panel);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridheight = 6;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 1;
		gbc_scrollPane.gridy = 0;
		panel.add(scrollPane, gbc_scrollPane);
		
		mFriendsList = new JList<String>();
		mFriendsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		mFriendsList.addListSelectionListener(this);
		
		scrollPane.setViewportView(mFriendsList);
		refreshFriendsList();
		

		
		txtPlayerName = new JTextField();
		//txtPlayersName.setText("Player's name");
		txtPlayerName.setToolTipText("Type a player's name");
		GridBagConstraints gbc_txtPlayersName = new GridBagConstraints();
		gbc_txtPlayersName.gridwidth = 3;
		gbc_txtPlayersName.insets = new Insets(0, 0, 5, 5);
		gbc_txtPlayersName.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtPlayersName.gridx = 2;
		gbc_txtPlayersName.gridy = 1;
		panel.add(txtPlayerName, gbc_txtPlayersName);
		txtPlayerName.setColumns(10);
		
		JButton btnAddFriend = new JButton("Add Friend");
		btnAddFriend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				addFriend();
				refreshFriendsList();
			}
		});	
		
		GridBagConstraints gbc_btnAddFriend = new GridBagConstraints();
		gbc_btnAddFriend.insets = new Insets(0, 0, 5, 5);
		gbc_btnAddFriend.gridx = 3;
		gbc_btnAddFriend.gridy = 2;
		panel.add(btnAddFriend, gbc_btnAddFriend);
		
		JButton btnDeleteFriend = new JButton("Delete Friend");
		btnDeleteFriend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				removeFriend();
				refreshFriendsList();
			}
		});
		GridBagConstraints gbc_btnDeleteFriend = new GridBagConstraints();
		gbc_btnDeleteFriend.insets = new Insets(0, 0, 5, 5);
		gbc_btnDeleteFriend.gridx = 3;
		gbc_btnDeleteFriend.gridy = 3;
		panel.add(btnDeleteFriend, gbc_btnDeleteFriend);
		


	}
	
	private void refreshFriendsList() {
		try {
			mFriends = PlayerRepository.getFriends(inviter);
			mFriendsList.setListData(mFriends);
		} catch (RepositoryErrorException e) {
			System.err.println("Failed to get friend names "+e);
		}
	}	

	private void addFriend(){		
		
		String invitee = txtPlayerName.getText();	

		try {
			// check if the invitee exist
			if (UserRepository.userExists(invitee)) {	
				// check if the invitee is the same as inviter
				if (!inviter.equals(invitee)){
					// check if invitee is already a friend
					if(!PlayerRepository.isFriend(inviter, invitee)){
						// add tuple into areFriends
						PlayerRepository.addFriend(inviter, invitee);
						JOptionPane.showMessageDialog(this, invitee + " is added to friend list");
					} else {
						JOptionPane.showMessageDialog(this, invitee + " is already in your friend list!");
					}
				} else {
					JOptionPane.showMessageDialog(this, "You can't add yourself to friend list!");					
				}
			} else {
				JOptionPane.showMessageDialog(this, "No such player!");
			}

		} catch (RepositoryErrorException e) {
			System.out.println(e);
			JOptionPane.showMessageDialog(this, "Unknown Database Error Occurred!");
		}
		
	}
	
	private void removeFriend(){		

		String invitee = txtPlayerName.getText();

		try {
			// check if the invitee exist
			if (UserRepository.userExists(invitee)) {	
				// check if the invitee is the same as inviter
				if (!inviter.equals(invitee)){
					// check if invitee is already a friend
					if(PlayerRepository.isFriend(inviter, invitee)){
						// add tuple into areFriends
						PlayerRepository.removeFriend(inviter, invitee);
						JOptionPane.showMessageDialog(this, invitee + " is removed from friend list");
					} else {
						JOptionPane.showMessageDialog(this, invitee + " is not your friend yet!");
					}
				} else {
					JOptionPane.showMessageDialog(this, "You are not in your friend list!");					
				}
			} else {
				JOptionPane.showMessageDialog(this, "No such player!");
			}

		} catch (RepositoryErrorException e) {
			System.out.println(e);
			JOptionPane.showMessageDialog(this, "Unknown Database Error Occurred!");
		}
		

		
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		if (!e.getValueIsAdjusting()){
			txtPlayerName.setText(mFriends.get(mFriendsList.getSelectedIndex()));
			
		}
		
	}

}
