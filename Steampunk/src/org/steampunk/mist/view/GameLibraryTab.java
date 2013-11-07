package org.steampunk.mist.view;

import javax.swing.JPanel;
import javax.swing.JList;

import java.awt.BorderLayout;
import java.util.Vector;

import javax.swing.ListSelectionModel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class GameLibraryTab extends JPanel implements ListSelectionListener {

	private static final long serialVersionUID = 5169865659990546731L;
	
	Vector<GameListItem> mGames;
	JList<GameListItem> mGameList;
	
	private class GameListItem {
		public String title;
		public int id;
		
		public GameListItem(int id, String title) {
			this.title = title;
			this.id = id;
		}
		
		@Override
		public String toString() {
			return title;
		}
	}
	
	/**
	 * Create the panel.
	 */
	public GameLibraryTab() {
		setLayout(new BorderLayout(0, 0));
		
		mGameList = new JList<GameListItem>();
		mGameList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		mGameList.addListSelectionListener(this);
		JScrollPane scrollPane = new JScrollPane(mGameList);
		add(scrollPane, BorderLayout.WEST);
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.CENTER);

		mGames = new Vector<GameListItem>();
		mGameList.setListData(mGames);
		mGames.add(new GameListItem(0, "HL3"));
		mGames.add(new GameListItem(1, "HL4"));
		mGames.add(new GameListItem(2, "Battlefield"));
		mGames.add(new GameListItem(3, "Warframe"));
		mGames.add(new GameListItem(4, "Killzone"));
	}
	
	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		
		System.out.println("First: "+mGameList.getSelectedIndex());
		
	}

}
