package org.steampunk.mist.view;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.steampunk.mist.AccountManager;
import org.steampunk.mist.model.User;
import org.steampunk.mist.repository.RepositoryErrorException;
import org.steampunk.mist.repository.UserRepository;
import java.awt.FlowLayout;
import javax.swing.JList;
import javax.swing.JScrollPane;

public class FriendsTab extends JPanel {
	
	private static final long serialVersionUID = 8436157125343804361L;
	
	JLabel mFriendsLabel;
	JLabel mEmailLabel;
	JLabel mJoinedLabel;
	private JButton mChangeEmailButton;
	private JButton mChangePassButton;
	
	
	
	public FriendsTab() {
		setLayout(new BorderLayout(0, 0));
		
		mFriendsLabel = new JLabel("Friends");
		mFriendsLabel.setHorizontalAlignment(SwingConstants.CENTER);
		mFriendsLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		add(mFriendsLabel, BorderLayout.NORTH);
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.CENTER);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0, 0, 0, 0};
		gbl_panel.rowHeights = new int[]{50, 50, 30, 30, 50, 50};
		gbl_panel.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, 0.0};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0};
		panel.setLayout(gbl_panel);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridheight = 6;
		gbc_scrollPane.insets = new Insets(0, 0, 0, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 1;
		gbc_scrollPane.gridy = 0;
		panel.add(scrollPane, gbc_scrollPane);
		
		JButton btnNewButton = new JButton("Add Friend");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton.gridx = 3;
		gbc_btnNewButton.gridy = 2;
		panel.add(btnNewButton, gbc_btnNewButton);
		
		JButton btnDeleteFriend = new JButton("Delete Friend");
		btnDeleteFriend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//...
			}
		});
		GridBagConstraints gbc_btnDeleteFriend = new GridBagConstraints();
		gbc_btnDeleteFriend.insets = new Insets(0, 0, 5, 5);
		gbc_btnDeleteFriend.gridx = 3;
		gbc_btnDeleteFriend.gridy = 3;
		panel.add(btnDeleteFriend, gbc_btnDeleteFriend);
		
		
		String d = "Denise";
	}
	


}
