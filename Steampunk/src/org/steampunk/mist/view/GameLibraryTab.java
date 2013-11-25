package org.steampunk.mist.view;

import javax.swing.JPanel;
import javax.swing.JList;

import java.awt.BorderLayout;
import java.util.Vector;

import javax.swing.ListSelectionModel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.steampunk.mist.AccountManager;
import org.steampunk.mist.model.Game;
import org.steampunk.mist.repository.GameRepository;
import org.steampunk.mist.repository.GameRepository.GameNameIdPair;
import org.steampunk.mist.repository.GameRepository.GameNotFoundException;
import org.steampunk.mist.repository.RepositoryErrorException;
import javax.swing.JLabel;

public class GameLibraryTab extends JPanel implements ListSelectionListener {

	private static final long serialVersionUID = 5169865659990546731L;
	
	Vector<GameNameIdPair> mGames;
	JList<GameNameIdPair> mGameList;
	
	JPanel mContentPanel;
	JLabel mContentWarningLabel;
	GameDetailsPanel mGameDetailsPanel;
	
	/**
	 * Create the panel.
	 */
	public GameLibraryTab() {
		setLayout(new BorderLayout(0, 0));
		
		mGameList = new JList<GameNameIdPair>();
		mGameList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		mGameList.addListSelectionListener(this);
		JScrollPane scrollPane = new JScrollPane(mGameList);
		add(scrollPane, BorderLayout.WEST);
		
		mContentPanel = new JPanel();
		add(mContentPanel, BorderLayout.CENTER);
		
		mContentWarningLabel = new JLabel("Nothing Selected");
		mContentPanel.add(mContentWarningLabel);
		
		mGameDetailsPanel = new GameDetailsPanel();
		mGameDetailsPanel.setVisible(false);
		mContentPanel.add(mGameDetailsPanel);

		refreshGameList();
	}
	
	public void refreshGameList() {
		try {
			mGames = GameRepository.getGameNamesOwnedByUser(AccountManager.getInstance().getCurrentUser().getUsername());
			mGameList.setListData(mGames);
		} catch (RepositoryErrorException e) {
			System.err.println("Failed to get user games "+e);
		}
	}
	
	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		
		if (!e.getValueIsAdjusting()){
			System.out.println("Selected index: "+mGameList.getSelectedIndex());
			int gameid = mGames.get(mGameList.getSelectedIndex()).id;
			try {
				Game game = GameRepository.getGame(gameid);
				mContentWarningLabel.setVisible(false);
				mGameDetailsPanel.setGame(game);
				mGameDetailsPanel.setVisible(true);
			} catch (RepositoryErrorException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (GameNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		
	}

}
