package org.steampunk.mist.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.steampunk.mist.AccountManager;
import org.steampunk.mist.model.Clan;
import org.steampunk.mist.repository.ClanRepository;
import org.steampunk.mist.repository.UserRepository;
import org.steampunk.mist.repository.ClanRepository.ClanNameGameIdPair;
import org.steampunk.mist.repository.ClanRepository.ClanNotFoundException;
import org.steampunk.mist.repository.RepositoryErrorException;

public class ClansTab extends JPanel implements ListSelectionListener {
	
	// member variables
	Vector<ClanNameGameIdPair> mJoinedClans;
	JList<ClanNameGameIdPair> mClanList;
	
	Vector<String> mMembers;
	JList<String> mMembersList;
	
	JPanel mContentPanel;
	JLabel mContentWarningLabel;
	ClanDetailsPanel mClanDetailsPanel;
	
	JButton mBtnLeave;
	JButton mBtnTransferOwnership;
	JButton mBtnAddMember;
	JButton mBtnRemoveMember;
	JButton mBtnChangeMoD;
	
	public ClansTab() {
		setLayout(null);
		
		// ----- CLAN LISTING -----
		try {
			mClanList = new JList<ClanNameGameIdPair>();
			mJoinedClans = ClanRepository.getClansJoinedListing(AccountManager.getInstance().getCurrentUser().getUsername());
			mClanList.setListData(mJoinedClans);
		} catch (RepositoryErrorException e) {
			System.err.println("Failed to get user clans "+e);
		}
		
		JScrollPane clansJoined_scrollPane = new JScrollPane(mClanList);
		clansJoined_scrollPane.setBounds(10, 26, 139, 272);
		add(clansJoined_scrollPane);

		mClanList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		mClanList.addListSelectionListener(this);

		clansJoined_scrollPane.setViewportView(mClanList);
		
		
		// ----- CLAN DETAILS PANEL -----
		mContentPanel = new JPanel();
		add(mContentPanel);
		
		mContentWarningLabel = new JLabel("Nothing Selected");
		mContentWarningLabel.setSize(100, 20);
		mContentWarningLabel.setLocation(0, 0);
		mContentPanel.add(mContentWarningLabel);
		
		mClanDetailsPanel = new ClanDetailsPanel();
		mClanDetailsPanel.setVisible(false);
		mContentPanel.setLayout(null);
		mContentPanel.add(mClanDetailsPanel);
		mContentPanel.setBounds(159, 26, 243, 272);
		mClanDetailsPanel.setBounds(0, 0, 243, 272);
		
		// ----- STATIC LABELS -----
		
		JLabel lblClansJoined = new JLabel("Clans Joined");
		lblClansJoined.setBounds(10, 11, 112, 14);
		add(lblClansJoined);
		
		JLabel lblClanMembers = new JLabel("Clan Members");
		lblClanMembers.setBounds(412, 11, 100, 14);
		add(lblClanMembers);
		
		JLabel lblClanDetails = new JLabel("Clan Details");
		lblClanDetails.setBounds(167, 11, 107, 14);
		add(lblClanDetails);
		
		// ----- BUTTONS -----
		
		mBtnLeave = new JButton("Leave");
		mBtnLeave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onLeave();
			}
		});
		mBtnLeave.setVisible(false);
		mBtnLeave.setBounds(448, 332, 112, 23);
		add(mBtnLeave);
		
		mBtnAddMember = new JButton("Add Member");
		mBtnAddMember.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showAddMemberDialog();
			}
		});
		mBtnAddMember.setVisible(false);
		mBtnAddMember.setBounds(241, 332, 161, 23);
		add(mBtnAddMember);
		
		mBtnRemoveMember = new JButton("Remove Member");
		mBtnRemoveMember.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showRemoveMemberDialog();
			}
		});
		mBtnRemoveMember.setVisible(false);
		mBtnRemoveMember.setBounds(241, 366, 161, 23);
		add(mBtnRemoveMember);
		
		mBtnTransferOwnership = new JButton("Transfer Ownership");
		mBtnTransferOwnership.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showTransferOwnershipDialog();
			}
		});
		mBtnTransferOwnership.setVisible(false);
		mBtnTransferOwnership.setBounds(10, 366, 221, 23);
		add(mBtnTransferOwnership);
		
		mBtnChangeMoD = new JButton("Change Message of the Day");
		mBtnChangeMoD.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				showChangeMessageDialog();
			}
		});
		mBtnChangeMoD.setVisible(false);
		mBtnChangeMoD.setBounds(10, 332, 221, 23);
		add(mBtnChangeMoD);
		

		// ----- CLAN MEMBERS LISTING -----

		mMembersList = new JList<String>();
		mMembers = new Vector<String>();
		mMembersList.setListData(mMembers);
		mMembersList.setVisible(false);
		
		JScrollPane clanMembers_scrollPane = new JScrollPane(mMembersList);
		clanMembers_scrollPane.setBounds(412, 26, 148, 272);
		add(clanMembers_scrollPane);

		clanMembers_scrollPane.setViewportView(mMembersList);
		

		
	}
	
	// ----- WHEN A CLAN IS SELECTED
	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting()){
			System.out.println("Selected index: "+mClanList.getSelectedIndex());
			
			String clanName = mJoinedClans.get(mClanList.getSelectedIndex()).clanName;
			int gameID = mJoinedClans.get(mClanList.getSelectedIndex()).gameID;
			System.out.println("Clan name: " + clanName);
			System.out.println("GameID: " + gameID);
			
			try {
				// check if owner and enable corresponding buttons
				if (ClanRepository.isClanOwner(clanName, gameID, AccountManager.getInstance().getCurrentUser().getUsername()) == true) {
					mBtnAddMember.setVisible(true);
					mBtnRemoveMember.setVisible(true);
					mBtnTransferOwnership.setVisible(true);
					mBtnChangeMoD.setVisible(true);
					mBtnLeave.setVisible(false);
				} else {
					mBtnAddMember.setVisible(false);
					mBtnRemoveMember.setVisible(false);
					mBtnTransferOwnership.setVisible(false);
					mBtnChangeMoD.setVisible(false);
					mBtnLeave.setVisible(true);					
				}
				
				Clan clan = ClanRepository.getClan(clanName, gameID);
				mContentWarningLabel.setVisible(false);
				mClanDetailsPanel.setClan(clan);
				mClanDetailsPanel.setVisible(true);
				mMembers = ClanRepository.getClanMembersListing(clanName,  gameID);
				mMembersList.setListData(mMembers);
				mMembersList.setVisible(true);
				
			} catch (RepositoryErrorException e1) {
				e1.printStackTrace();
			} catch (ClanNotFoundException e1) {
				e1.printStackTrace();
			}
			
			
		}
	}
	
	public void showAddMemberDialog() {
		System.out.println("Adding member...");
		
		String clanName = mJoinedClans.get(mClanList.getSelectedIndex()).clanName;
		int gameID = mJoinedClans.get(mClanList.getSelectedIndex()).gameID;
		
		String newMember = (String)JOptionPane.showInputDialog(this, "Enter username", "Add Member", 
				JOptionPane.PLAIN_MESSAGE, null, null, "Type new username here");
		
		try {
			if (newMember == null) {
				return;
			} else if(UserRepository.userExists(newMember) == false) {
				JOptionPane.showMessageDialog(this, "That user does not exist.");
			} else if (ClanRepository.checkMembership(clanName, gameID, newMember) == true){
				JOptionPane.showMessageDialog(this, "That user is already a member of this clan!");
			} else {
				ClanRepository.addClanMember(clanName, gameID, newMember);
			}
		} catch (RepositoryErrorException e) {
			System.err.println(e);
		} catch (ClanNotFoundException e) {
			System.err.println(e);
		}
		
		refreshClanDetailsPanel(clanName, gameID);
		refreshClanMembersList(clanName, gameID);
		
	}
	
	public void showRemoveMemberDialog() {
		System.out.println("Removing member...");
		
		String clanName = mJoinedClans.get(mClanList.getSelectedIndex()).clanName;
		int gameID = mJoinedClans.get(mClanList.getSelectedIndex()).gameID;
		
		String memberToRemove = (String)JOptionPane.showInputDialog(this, "Enter username", "Remove Member", 
				JOptionPane.PLAIN_MESSAGE, null, null, "Type username of player to remove here");
		
		try {
			if (memberToRemove == null) {
				return;
			} else if(UserRepository.userExists(memberToRemove) == false) {
				JOptionPane.showMessageDialog(this, "That user does not exist.");
			} else if (ClanRepository.checkMembership(clanName, gameID, memberToRemove) == false){
				JOptionPane.showMessageDialog(this, "That user is not a member of this clan!");
			} else if (ClanRepository.isClanOwner(clanName, gameID, memberToRemove)){ 
				JOptionPane.showMessageDialog(this, "You cannot delete the owner of this clan. Transfer ownership first.");
			} else {
				ClanRepository.removeClanMember(clanName, gameID, memberToRemove);
			}
		} catch (RepositoryErrorException e) {
			System.err.println(e);
		} catch (ClanNotFoundException e) {
			System.err.println(e);
		}
		
		refreshClanDetailsPanel(clanName, gameID);
		refreshClanMembersList(clanName, gameID);
	}
	
	public void showTransferOwnershipDialog() {
		System.out.println("Transferring ownership...");
		
		String clanName = mJoinedClans.get(mClanList.getSelectedIndex()).clanName;
		int gameID = mJoinedClans.get(mClanList.getSelectedIndex()).gameID;
		
		String newOwner = (String)JOptionPane.showInputDialog(this, "Enter username of new owner", "New Clan Owner", 
				JOptionPane.PLAIN_MESSAGE, null, null, "Type username of player to remove here");
		
		try {
			if (newOwner == null) {
				return;
			} else if(UserRepository.userExists(newOwner) == false) {
				JOptionPane.showMessageDialog(this, "That user does not exist.");
			} else if (ClanRepository.checkMembership(clanName, gameID, newOwner) == false){
				JOptionPane.showMessageDialog(this, "That user is not a member of this clan!");
			} else if (ClanRepository.isClanOwner(clanName, gameID, newOwner)){ 
				JOptionPane.showMessageDialog(this, "That user is already the owner of this clan");
			} else {
				ClanRepository.transferOwnership(clanName, gameID, newOwner);
				refreshButtons(clanName, gameID);
			}
		} catch (RepositoryErrorException e) {
			System.err.println(e);
		} catch (ClanNotFoundException e) {
			System.err.println(e);
		}	
	}
	
	public void showChangeMessageDialog() {
		System.out.println("Changing clan message...");
		
		String clanName = mJoinedClans.get(mClanList.getSelectedIndex()).clanName;
		int gameID = mJoinedClans.get(mClanList.getSelectedIndex()).gameID;
		
		String newMessage = (String)JOptionPane.showInputDialog(this, "Enter new message", "Message", 
				JOptionPane.PLAIN_MESSAGE, null, null, "Type new message here");
		if (newMessage == null) {
			return;
		} else if (newMessage.length() > 500) {
			JOptionPane.showMessageDialog(this, "Message can be maximum 500 characters");
		} else {
			try {
				ClanRepository.updateClanMoD(clanName, gameID, newMessage);
				refreshClanDetailsPanel(clanName, gameID);
			} catch (RepositoryErrorException e) {
				System.err.println(e);
				JOptionPane.showMessageDialog(this, "Unknown database error occured.");
			}
		}
	}
	
	public void onLeave() {
		System.out.println("Leaving this clan...");
		
		String clanName = mJoinedClans.get(mClanList.getSelectedIndex()).clanName;
		int gameID = mJoinedClans.get(mClanList.getSelectedIndex()).gameID;
		String thisUser = AccountManager.getInstance().getCurrentUser().getUsername();
		
		try {
				ClanRepository.removeClanMember(clanName, gameID, thisUser);
				refreshJoinedClansList(thisUser);
				
				mContentWarningLabel.setVisible(true);
				mClanDetailsPanel.setVisible(false);
				mMembersList.setVisible(false);
				mBtnLeave.setVisible(false);
				
		} catch (RepositoryErrorException e) {
			System.err.println(e);
		} 
		
	}
	
	public void refreshClanDetailsPanel(String clanName, int gameID) {
		try {
			Clan clan = ClanRepository.getClan(clanName, gameID);
			mContentWarningLabel.setVisible(false);
			mClanDetailsPanel.setClan(clan);
		} catch (RepositoryErrorException e) {
			System.err.println(e);
		} catch (ClanNotFoundException e) {
			System.err.println(e);
		}

	}
	
	public void refreshClanMembersList(String clanName, int gameID) {
		try {
			mMembers = ClanRepository.getClanMembersListing(clanName,  gameID);
			mMembersList.setListData(mMembers);
			mMembersList.setVisible(true);
		} catch (RepositoryErrorException e) {
			System.err.println(e);
		}
	}
	
	public void refreshButtons (String clanName, int gameID) {
		try {
			// check if owner and enable corresponding buttons
			if (ClanRepository.isClanOwner(clanName, gameID, AccountManager.getInstance().getCurrentUser().getUsername()) == true) {
				mBtnAddMember.setVisible(true);
				mBtnRemoveMember.setVisible(true);
				mBtnTransferOwnership.setVisible(true);
				mBtnChangeMoD.setVisible(true);
				mBtnLeave.setVisible(false);
			} else {
				mBtnAddMember.setVisible(false);
				mBtnRemoveMember.setVisible(false);
				mBtnTransferOwnership.setVisible(false);
				mBtnChangeMoD.setVisible(false);
				mBtnLeave.setVisible(true);					
			} 
			
		} catch (RepositoryErrorException e) {
			System.err.println(e);
		} catch (ClanNotFoundException e) {
			System.err.println(e);
		}
	
		}
	
	public void refreshJoinedClansList (String username) {
		try {
			mJoinedClans = ClanRepository.getClansJoinedListing(username);
			mClanList.setListData(mJoinedClans);
		} catch (RepositoryErrorException e) {
			System.err.println("Failed to get user clans "+e);
		}
	}
}
