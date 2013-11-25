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
import org.steampunk.mist.repository.CommentRepository;
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
	String minOrMax;
	String buttonChoosen;
	
	public GameAdminTab() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{5, 30, 50, 20};
		gridBagLayout.rowHeights = new int[]{20, 20, 30, 20};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0};
		setLayout(gridBagLayout);
				
						
						mGAmesAdminsteredLabel = new JLabel("Games Adminstered");
						mGAmesAdminsteredLabel.setHorizontalAlignment(SwingConstants.CENTER);
						mGAmesAdminsteredLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
						GridBagConstraints gbc_mGAmesAdminsteredLabel = new GridBagConstraints();
						gbc_mGAmesAdminsteredLabel.insets = new Insets(0, 0, 5, 5);
						gbc_mGAmesAdminsteredLabel.anchor = GridBagConstraints.NORTHWEST;
						gbc_mGAmesAdminsteredLabel.gridx = 1;
						gbc_mGAmesAdminsteredLabel.gridy = 0;
						add(mGAmesAdminsteredLabel, gbc_mGAmesAdminsteredLabel);
				
				JPanel panel = new JPanel();
				GridBagConstraints gbc_panel = new GridBagConstraints();
				gbc_panel.gridheight = 2;
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
				gbc_panel_1.insets = new Insets(0, 0, 5, 5);
				gbc_panel_1.fill = GridBagConstraints.BOTH;
				gbc_panel_1.gridx = 2;
				gbc_panel_1.gridy = 2;
				add(panel_1, gbc_panel_1);				
				
				JPanel panel_2 = new JPanel();
				GridBagConstraints gbc_panel_2 = new GridBagConstraints();
				gbc_panel_2.insets = new Insets(0, 0, 5, 5);
				gbc_panel_2.fill = GridBagConstraints.BOTH;
				gbc_panel_2.gridx = 2;
				gbc_panel_2.gridy = 1;
				add(panel_2, gbc_panel_2);
				GridBagLayout gbl_panel_2 = new GridBagLayout();
				gbl_panel_2.columnWidths = new int[]{74, 5, 46, 0, 0};
				gbl_panel_2.rowHeights = new int[]{14, 0, 0, 0, 0};
				gbl_panel_2.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
				gbl_panel_2.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
				panel_2.setLayout(gbl_panel_2);
				
				JLabel lblNewLabel;
				try {
					lblNewLabel = new JLabel(CommentRepository.getAverageLength(administrator));
					lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
					GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
					gbc_lblNewLabel.gridwidth = 2;
					gbc_lblNewLabel.anchor = GridBagConstraints.NORTH;
					gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
					gbc_lblNewLabel.gridx = 2;
					gbc_lblNewLabel.gridy = 1;
					panel_2.add(lblNewLabel, gbc_lblNewLabel);
				} catch (RepositoryErrorException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				
				JLabel lblNewLabel_2 = new JLabel("average");
				GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
				gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
				gbc_lblNewLabel_2.gridx = 0;
				gbc_lblNewLabel_2.gridy = 1;
				panel_2.add(lblNewLabel_2, gbc_lblNewLabel_2);
				
				final JLabel lblNewLabel_1 = new JLabel("Choose a button:");
				GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
				gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
				gbc_lblNewLabel_1.anchor = GridBagConstraints.NORTHWEST;
				gbc_lblNewLabel_1.gridx = 0;
				gbc_lblNewLabel_1.gridy = 2;
				panel_2.add(lblNewLabel_1, gbc_lblNewLabel_1);
				
				final JLabel lblMinormax = new JLabel(minOrMax);
				GridBagConstraints gbc_lblMinormax = new GridBagConstraints();
				gbc_lblMinormax.gridwidth = 2;
				gbc_lblMinormax.insets = new Insets(0, 0, 5, 5);
				gbc_lblMinormax.gridx = 2;
				gbc_lblMinormax.gridy = 2;
				panel_2.add(lblMinormax, gbc_lblMinormax);
				
				JButton btnNewButton = new JButton("Minimum");
				btnNewButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						try {
							minOrMax = CommentRepository.getMinLength(administrator);
							buttonChoosen = "Min Length:";
							lblMinormax.setText(minOrMax);
							lblNewLabel_1.setText(buttonChoosen);
						} catch (RepositoryErrorException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
				GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
				gbc_btnNewButton.insets = new Insets(0, 0, 0, 5);
				gbc_btnNewButton.gridx = 2;
				gbc_btnNewButton.gridy = 3;
				panel_2.add(btnNewButton, gbc_btnNewButton);
				
				JButton btnNewButton_1 = new JButton("Maximum");
				btnNewButton_1.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							minOrMax = CommentRepository.getMaxLength(administrator);
							buttonChoosen = "Max Length:";
							lblMinormax.setText(minOrMax);
							lblNewLabel_1.setText(buttonChoosen);
						} catch (RepositoryErrorException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				});
				GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
				gbc_btnNewButton_1.gridx = 3;
				gbc_btnNewButton_1.gridy = 3;
				panel_2.add(btnNewButton_1, gbc_btnNewButton_1);
				

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
