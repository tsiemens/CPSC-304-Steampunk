package org.steampunk.mist.view;

import javax.swing.JPanel;
import javax.swing.JLabel;

import org.steampunk.mist.AccountManager;
import org.steampunk.mist.model.User;

import java.awt.Font;

import javax.swing.BoxLayout;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.SwingConstants;
import javax.swing.JButton;

public class UserDetailsTab extends JPanel {

	JLabel mUsernameLabel;
	JLabel mEmailLabel;
	JLabel mJoinedLabel;
	private JButton mChangeEmailButton;
	private JButton mChangePassButton;
	
	/**
	 * Create the panel.
	 */
	public UserDetailsTab() {
		setLayout(new BorderLayout(0, 0));
		
		mUsernameLabel = new JLabel("<Username>");
		mUsernameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		mUsernameLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		add(mUsernameLabel, BorderLayout.NORTH);
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.CENTER);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] {100, 46, 0, 0, 0};
		gbl_panel.rowHeights = new int[]{14, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JLabel lblEmailTitle = new JLabel("Email:");
		GridBagConstraints gbc_lblEmailTitle = new GridBagConstraints();
		gbc_lblEmailTitle.insets = new Insets(0, 0, 5, 5);
		gbc_lblEmailTitle.anchor = GridBagConstraints.NORTHWEST;
		gbc_lblEmailTitle.gridx = 0;
		gbc_lblEmailTitle.gridy = 0;
		panel.add(lblEmailTitle, gbc_lblEmailTitle);
		
		mEmailLabel = new JLabel("New label");
		GridBagConstraints gbc_mEmailLabel = new GridBagConstraints();
		gbc_mEmailLabel.insets = new Insets(0, 0, 5, 5);
		gbc_mEmailLabel.gridx = 1;
		gbc_mEmailLabel.gridy = 0;
		panel.add(mEmailLabel, gbc_mEmailLabel);
		
		mChangeEmailButton = new JButton("Change");
		GridBagConstraints gbc_mChangeEmailButton = new GridBagConstraints();
		gbc_mChangeEmailButton.insets = new Insets(0, 0, 5, 0);
		gbc_mChangeEmailButton.gridx = 3;
		gbc_mChangeEmailButton.gridy = 0;
		panel.add(mChangeEmailButton, gbc_mChangeEmailButton);
		mChangeEmailButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				showChangeEmailDialog();	
			}
		});
		
		JLabel lblJoinedTitle = new JLabel("Joined:");
		GridBagConstraints gbc_lblJoinedTitle = new GridBagConstraints();
		gbc_lblJoinedTitle.anchor = GridBagConstraints.WEST;
		gbc_lblJoinedTitle.insets = new Insets(0, 0, 5, 5);
		gbc_lblJoinedTitle.gridx = 0;
		gbc_lblJoinedTitle.gridy = 1;
		panel.add(lblJoinedTitle, gbc_lblJoinedTitle);
		
		mJoinedLabel = new JLabel("New label");
		GridBagConstraints gbc_mJoinedLabel = new GridBagConstraints();
		gbc_mJoinedLabel.insets = new Insets(0, 0, 5, 5);
		gbc_mJoinedLabel.gridx = 1;
		gbc_mJoinedLabel.gridy = 1;
		panel.add(mJoinedLabel, gbc_mJoinedLabel);
		
		mChangePassButton = new JButton("Change password");
		GridBagConstraints gbc_mChangePassButton = new GridBagConstraints();
		gbc_mChangePassButton.gridx = 3;
		gbc_mChangePassButton.gridy = 7;
		panel.add(mChangePassButton, gbc_mChangePassButton);
		mChangePassButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				showChangePassDialog();	
			}
		});

		onCreateView();
	}
	
	public void onCreateView()
	{
		User currentUser = AccountManager.getInstance().getCurrentUser();
		mUsernameLabel.setText(currentUser.getUsername());
		mEmailLabel.setText(currentUser.getEmail());
		Calendar dj = currentUser.getDateJoined();
		mJoinedLabel.setText(""+(dj.get(Calendar.MONTH)+1)+"/"+dj.get(Calendar.DAY_OF_MONTH)+"/"+dj.get(Calendar.YEAR));
	}
	
	private void showChangeEmailDialog()
	{
		System.out.println("TODO: implement change email");
		// TODO
	}
	
	private void showChangePassDialog()
	{
		System.out.println("TODO: implement change password");
		// TODO
	}

}
