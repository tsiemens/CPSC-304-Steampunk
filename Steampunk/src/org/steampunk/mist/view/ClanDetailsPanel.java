package org.steampunk.mist.view;

import java.awt.Font;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.steampunk.mist.model.Clan;
import org.steampunk.mist.repository.ClanRepository;
import org.steampunk.mist.repository.ClanRepository.ClanNotFoundException;
import org.steampunk.mist.repository.RepositoryErrorException;

public class ClanDetailsPanel extends JPanel{

	private static final long serialVersionUID = 2684693407186761173L;
	
	private JTextArea mClanMoDTextArea;
	private JLabel mClanMemberCountLabel;
	private JLabel mClanNameLabel;
	
	private Clan mClan;
	
	Vector<String> mSharedGames;
	JList<String> mSharedGamesList;
	
	public ClanDetailsPanel() {
		createView();
	}
	
	private void createView() {
		setLayout(null);
		
		mClanNameLabel = new JLabel("<ClanName>");
		mClanNameLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
		mClanNameLabel.setBounds(10, 11, 184, 14);
		add(mClanNameLabel);
		
		JLabel lblMessageOfThe = new JLabel("Message of the Day:");
		lblMessageOfThe.setBounds(10, 36, 122, 14);
		add(lblMessageOfThe);
		
		mClanMoDTextArea = new JTextArea();
		mClanMoDTextArea.setText("<MessageOfTheDay>");
		mClanMoDTextArea.setBounds(10, 54, 248, 87);
		add(mClanMoDTextArea);
		
		JLabel lblTotalNumberOf = new JLabel("Total Number of Members: ");
		lblTotalNumberOf.setBounds(10, 152, 184, 14);
		add(lblTotalNumberOf);
		
		mClanMemberCountLabel = new JLabel("<MemberCount>");
		mClanMemberCountLabel.setBounds(175, 152, 83, 14);
		add(mClanMemberCountLabel);
		
		// SHARED GAMES
		
		JLabel lblSharedGames = new JLabel("Shared Games: ");
		lblSharedGames.setBounds(10, 177, 122, 14);
		add(lblSharedGames);
		
		mSharedGamesList = new JList<String>();
		mSharedGames = new Vector<String>();
		mSharedGamesList.setListData(mSharedGames);
		mSharedGamesList.setVisible(false);
		
		JScrollPane sharedGames_scrollPane = new JScrollPane();
		sharedGames_scrollPane.setBounds(10, 202, 248, 77);
		add(sharedGames_scrollPane);
		
		sharedGames_scrollPane.setViewportView(mSharedGamesList);
		
	}
	
	public void setClan(Clan clan) {
		mClan = clan;
		updateClanInfo();
	}
	
	private void updateClanInfo() {
		int memberCount = 0;
		try {
			memberCount = ClanRepository.getClanMemberCount(mClan.getClanName(), mClan.getClanGameID());
			mSharedGames = ClanRepository.getSharedGames(mClan.getClanName(), mClan.getClanGameID());
		} catch (RepositoryErrorException e1) {
			e1.printStackTrace();
		} catch (ClanNotFoundException e1) {
			e1.printStackTrace();
		}
		
		if (mClan != null) {
			mClanNameLabel.setText(mClan.getClanName());
			mClanMoDTextArea.setText(mClan.getMoD());
			mClanMemberCountLabel.setText( String.valueOf(memberCount) );
			mSharedGamesList.setListData(mSharedGames);
			mSharedGamesList.setVisible(true);
		}
	}
}
