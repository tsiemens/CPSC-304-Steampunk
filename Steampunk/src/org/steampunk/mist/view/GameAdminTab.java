package org.steampunk.mist.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
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
import org.steampunk.mist.repository.GameCopyRepository;
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



public class GameAdminTab extends JPanel implements ListSelectionListener {
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2155537548470194331L;
	
	Vector<String> mGamesAdministered;
	JLabel mGAmesAdminsteredLabel;
	String administrator = AccountManager.getInstance().getCurrentUser().getUsername();
	JList<String> mGamesList;
	
	public GameAdminTab() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{5, 30, 40, 30, 50, 50, 50, 50, 50, 50, 5};
		gridBagLayout.rowHeights = new int[]{20, 20, 30, 30, 30, 30, 30, 30, 30, 20};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0};
		setLayout(gridBagLayout);
				
						
						mGAmesAdminsteredLabel = new JLabel("Games Adminstered");
						mGAmesAdminsteredLabel.setHorizontalAlignment(SwingConstants.CENTER);
						mGAmesAdminsteredLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
						GridBagConstraints gbc_mGAmesAdminsteredLabel = new GridBagConstraints();
						gbc_mGAmesAdminsteredLabel.gridwidth = 4;
						gbc_mGAmesAdminsteredLabel.insets = new Insets(0, 0, 5, 5);
						gbc_mGAmesAdminsteredLabel.anchor = GridBagConstraints.NORTHWEST;
						gbc_mGAmesAdminsteredLabel.gridx = 2;
						gbc_mGAmesAdminsteredLabel.gridy = 0;
						add(mGAmesAdminsteredLabel, gbc_mGAmesAdminsteredLabel);
				
				JPanel panel = new JPanel();
				GridBagConstraints gbc_panel = new GridBagConstraints();
				gbc_panel.gridwidth = 4;
				gbc_panel.gridheight = 8;
				gbc_panel.insets = new Insets(0, 0, 5, 5);
				gbc_panel.fill = GridBagConstraints.BOTH;
				gbc_panel.gridx = 1;
				gbc_panel.gridy = 1;
				add(panel, gbc_panel);
				
				JScrollPane scrollPane = new JScrollPane();
				panel.add(scrollPane);
				scrollPane.setPreferredSize(new Dimension(250, 230));
				
				mGamesList = new JList<String>();
				mGamesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				mGamesList.addListSelectionListener(this);
				
				scrollPane.setViewportView(mGamesList);
				refreshGamesList();
				
				
				
				
				
				
				
				
				
				JPanel panel_1 = new JPanel();
				GridBagConstraints gbc_panel_1 = new GridBagConstraints();
				gbc_panel_1.gridheight = 4;
				gbc_panel_1.gridwidth = 5;
				gbc_panel_1.insets = new Insets(0, 0, 5, 5);
				gbc_panel_1.fill = GridBagConstraints.BOTH;
				gbc_panel_1.gridx = 5;
				gbc_panel_1.gridy = 5;
				add(panel_1, gbc_panel_1);				
				
				JPanel panel_2 = new JPanel();
				GridBagConstraints gbc_panel_2 = new GridBagConstraints();
				gbc_panel_2.gridheight = 4;
				gbc_panel_2.gridwidth = 5;
				gbc_panel_2.insets = new Insets(0, 0, 5, 5);
				gbc_panel_2.fill = GridBagConstraints.BOTH;
				gbc_panel_2.gridx = 5;
				gbc_panel_2.gridy = 1;
				add(panel_2, gbc_panel_2);
				

	}


	private void refreshGamesList() {
		try {
			mGamesAdministered = GameCopyRepository.getGameAdministered(administrator);
			mGamesList.setListData(mGamesAdministered);
		} catch (RepositoryErrorException e) {
			System.err.println("Failed to get games "+e);
		}
	}


	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	

}
