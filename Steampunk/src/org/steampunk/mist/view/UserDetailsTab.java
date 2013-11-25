package org.steampunk.mist.view;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;

import org.steampunk.mist.AccountManager;
import org.steampunk.mist.model.Player;
import org.steampunk.mist.model.User;
import org.steampunk.mist.repository.AchievementRepository;
import org.steampunk.mist.repository.AchievementRepository.AchievementNotFoundException;
import org.steampunk.mist.repository.RepositoryErrorException;
import org.steampunk.mist.repository.UserRepository;
import org.steampunk.mist.util.MistTextFormatter;

import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.SwingConstants;
import javax.swing.JButton;

public class UserDetailsTab extends JPanel {

	private static final long serialVersionUID = 8436157125343804361L;
	
	JLabel mUsernameLabel;
	JLabel mEmailLabel;
	JLabel mJoinedLabel;
	private JButton mChangeEmailButton;
	private JButton mChangePassButton;
	private JLabel mAchievementTotalTitle;
	private JLabel mAchievementTotal;
	
	/**
	 * Create the panel.
	 */
	public UserDetailsTab() {
		setLayout(new BorderLayout(0, 0));
		
		mUsernameLabel = new JLabel("<Username>");
		mUsernameLabel.setHorizontalAlignment(SwingConstants.LEFT);
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
		
		mChangeEmailButton = new JButton("Change Email");
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
		
		mAchievementTotalTitle = new JLabel("Total Achievement Points:");
		GridBagConstraints gbc_AchievementTotalTitle = new GridBagConstraints();
		gbc_AchievementTotalTitle.insets = new Insets(0, 0, 5, 5);
		gbc_AchievementTotalTitle.gridx = 0;
		gbc_AchievementTotalTitle.gridy = 2;
		panel.add(mAchievementTotalTitle, gbc_AchievementTotalTitle);
		
		mAchievementTotal = new JLabel("0");
		GridBagConstraints gbc_AchievementTotal = new GridBagConstraints();
		gbc_AchievementTotal.insets = new Insets(0, 0, 5, 5);
		gbc_AchievementTotal.gridx = 1;
		gbc_AchievementTotal.gridy = 2;
		panel.add(mAchievementTotal, gbc_AchievementTotal);
		
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
		mJoinedLabel.setText(MistTextFormatter.formatDateString(dj));
		
		if (currentUser instanceof Player) {
			mAchievementTotal.setVisible(true);
			mAchievementTotalTitle.setVisible(true);
			try {
				mAchievementTotal.setText(""+AchievementRepository.sumPlayerAchievementPoints(currentUser.getUsername()));
			} catch (Exception e) {
				mAchievementTotal.setText("--");
				e.printStackTrace();
			}
		} else {
			mAchievementTotal.setVisible(false);
			mAchievementTotalTitle.setVisible(false);
		}
	}
	
	private void showChangeEmailDialog()
	{
		User cUser = AccountManager.getInstance().getCurrentUser();
		String email = (String)JOptionPane.showInputDialog(this, "Enter new Email", "Email", 
				JOptionPane.PLAIN_MESSAGE, null, null, cUser.getEmail());
		
		if (email == null || email.contentEquals(cUser.getEmail())) {
			return;
		} else if (!email.matches(".+@.+")) {
			JOptionPane.showMessageDialog(this, "Must be valid email address.");
		} else if (email.length() > 50) {
			JOptionPane.showMessageDialog(this, "Email can be maximum 50 characters.");
		} else {
			try {
				UserRepository.updateEmail(cUser.getUsername(), email);
				// Ensure that email is not changed unless db update is successful
				cUser.setEmail(email);
				mEmailLabel.setText(email);
			} catch (RepositoryErrorException e) {
				System.err.println(e);
				JOptionPane.showMessageDialog(this, "Unknown database error occured.");
			}
		}
	}
	
	private void showChangePassDialog()
	{
		User cUser = AccountManager.getInstance().getCurrentUser();
		String password = CustomOptionPane.showPasswordDialog(this, "Enter new password", "Password", null);
		
		if (password == null) {
			return;
		} else if (password.length() == 0) {
			JOptionPane.showMessageDialog(this, "Password must be at least one character.");
			return;
		} else if (password.length() > 30) {
			JOptionPane.showMessageDialog(this, "Password can be maximum 30 characters.");
			return;
		} 
		
		String conf = CustomOptionPane.showPasswordDialog(this, "Confirm new password", "Password", null);
		if (!password.contentEquals(conf)) {
			JOptionPane.showMessageDialog(this, "Password does not match");
		} else {
			try {
				byte[] hash = User.getHash(password, cUser.getPasswordSalt());
				UserRepository.updatePassword(cUser.getUsername(), hash);
				// Ensure that hash is not changed unless db update is successful
				cUser.setPasswordHash(hash);
				JOptionPane.showMessageDialog(this, "Password change successful.");
			} catch (RepositoryErrorException e) {
				System.err.println(e);
				JOptionPane.showMessageDialog(this, "Unknown database error occured.");
			}
		}
	}

}
