package org.steampunk.mist.view.list;

import java.awt.Dimension;
import java.util.List;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.steampunk.mist.model.Achievement;
import org.steampunk.mist.repository.AchievementRepository;
import org.steampunk.mist.repository.AchievementRepository.AchievementNotFoundException;
import org.steampunk.mist.repository.RepositoryErrorException;

public class AchievementListPanel extends JPanel implements ListSelectionListener {

	private static final long serialVersionUID = -4535843071101096496L;
	
	private JList<Achievement> mAchievementList;
	private Vector<Achievement> mAchievements;
	
	private int mGameID;
	
	/**
	 * Create the panel.
	 */
	public AchievementListPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JLabel lblNewLabel = new JLabel("Achievements");
		add(lblNewLabel);
		
		JButton mNewButton = new JButton("Add new");
		add(mNewButton);
		
		mAchievementList = new JList<Achievement>();
		mAchievementList.addListSelectionListener(this);
		JScrollPane scrollPane = new JScrollPane(mAchievementList);
		scrollPane.setPreferredSize(new Dimension(200, 400));
		add(scrollPane);
	}
	
	public void setGame(int gameID) {
		mGameID = gameID;
		refreshList();
	}
	
	public void refreshList() {
		try {
			List<Achievement> achs = AchievementRepository.getGameAchievements(mGameID);
			mAchievements = new Vector<Achievement>(achs);
			mAchievementList.setListData(mAchievements);
		} catch (RepositoryErrorException e) {
			e.printStackTrace();
		} catch (AchievementNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (!e.getValueIsAdjusting()){
			int index = mAchievementList.getSelectedIndex();
			Achievement selectedAchmt = mAchievements.get(index);
			JOptionPane.showMessageDialog(this, "Points: "+selectedAchmt.getPoints()
					+"\n"+selectedAchmt.getAchievementDesc(), 
					selectedAchmt.getAchievementName(), JOptionPane.INFORMATION_MESSAGE);
		}
	}
}
