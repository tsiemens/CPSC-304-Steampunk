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
import javax.swing.JTextField;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class StoreTab extends JPanel implements ListSelectionListener, ActionListener {

	private static final long serialVersionUID = 5986963086742610538L;
	
	Vector<GameNameIdPair> mGameResults;
	JList<GameNameIdPair> mGameList;
	
	JPanel mContentPanel;
	JLabel mContentWarningLabel;
	GameDetailsPanel mGameDetailsPanel;
	private JTextField mSearchField;
	private JButton mSearchButton;
	
	/**
	 * Create the panel.
	 */
	public StoreTab() {
		setLayout(new BorderLayout(0, 0));
		
		mGameList = new JList<GameNameIdPair>();
		mGameList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		mGameList.addListSelectionListener(this);
		
		JPanel searchpanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) searchpanel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		add(searchpanel, BorderLayout.NORTH);
		
		mSearchField = new JTextField();
		mSearchField.setToolTipText("search");
		searchpanel.add(mSearchField);
		mSearchField.setColumns(10);
		
		mSearchButton = new JButton("Search");
		mSearchButton.addActionListener(this);
		searchpanel.add(mSearchButton);
		JScrollPane scrollPane = new JScrollPane(mGameList);
		add(scrollPane, BorderLayout.WEST);
		
		mContentPanel = new JPanel();
		add(mContentPanel, BorderLayout.CENTER);
		
		mContentWarningLabel = new JLabel("Nothing Selected");
		mContentPanel.add(mContentWarningLabel);
		
		mGameDetailsPanel = new GameDetailsPanel();
		mGameDetailsPanel.setVisible(false);
		mContentPanel.add(mGameDetailsPanel);
	}
	
	public void searchGames() {
		try {
			mGameResults = GameRepository.searchGames(mSearchField.getText());
			mGameList.setListData(mGameResults);
		} catch (RepositoryErrorException e) {
			System.err.println("Failed to search games "+e);
		}
	}
	
	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		
		if (!e.getValueIsAdjusting()){
			System.out.println("Selected index: "+mGameList.getSelectedIndex());
			int gameid = mGameResults.get(mGameList.getSelectedIndex()).id;
			try {
				Game game = GameRepository.getGame(gameid);
				mContentWarningLabel.setVisible(false);
				mGameDetailsPanel.setGame(game);
				mGameDetailsPanel.setVisible(true);
			} catch (RepositoryErrorException e1) {
				e1.printStackTrace();
			} catch (GameNotFoundException e1) {
				e1.printStackTrace();
			}
		}
		
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		searchGames();
	}

}
