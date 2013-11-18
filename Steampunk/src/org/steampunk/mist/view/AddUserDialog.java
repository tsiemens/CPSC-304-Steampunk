package org.steampunk.mist.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.steampunk.mist.model.Player;
import org.steampunk.mist.model.User;
import org.steampunk.mist.repository.PlayerRepository;
import org.steampunk.mist.repository.RepositoryErrorException;
import org.steampunk.mist.repository.UserRepository;

public class AddUserDialog extends JDialog {
	
	private static final long serialVersionUID = 7243275242105244508L;
	
	private JTextField usernameField;
	private JTextField emailField;
	private JPasswordField passwordField;
	private JPasswordField passConfField;
	
	public AddUserDialog() {
		
		JPanel inputPanel = new JPanel();
		getContentPane().add(inputPanel, BorderLayout.CENTER);
		GridBagLayout gbl_inputPanel = new GridBagLayout();
		gbl_inputPanel.columnWidths = new int[]{442, 0};
		gbl_inputPanel.rowHeights = new int[]{29, 29, 29, 29, 0};
		gbl_inputPanel.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_inputPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		inputPanel.setLayout(gbl_inputPanel);
		
		JPanel usernamePanel = new JPanel();
		GridBagConstraints gbc_usernamePanel = new GridBagConstraints();
		gbc_usernamePanel.fill = GridBagConstraints.BOTH;
		gbc_usernamePanel.insets = new Insets(0, 0, 5, 0);
		gbc_usernamePanel.gridx = 0;
		gbc_usernamePanel.gridy = 0;
		inputPanel.add(usernamePanel, gbc_usernamePanel);
		usernamePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel mUsername = new JLabel("Username:");
		usernamePanel.add(mUsername);
		
		usernameField = new JTextField();
		usernameField.setColumns(20);
		usernamePanel.add(usernameField);
		
		JPanel passwordPanel = new JPanel();
		GridBagConstraints gbc_passwordPanel = new GridBagConstraints();
		gbc_passwordPanel.fill = GridBagConstraints.BOTH;
		gbc_passwordPanel.insets = new Insets(0, 0, 5, 0);
		gbc_passwordPanel.gridx = 0;
		gbc_passwordPanel.gridy = 1;
		inputPanel.add(passwordPanel, gbc_passwordPanel);
		
		JLabel lblNewLabel = new JLabel("Password:");
		passwordPanel.add(lblNewLabel);
		
		passwordField = new JPasswordField();
		passwordField.setColumns(20);
		passwordPanel.add(passwordField);
		
		JPanel passwordConfPanel = new JPanel();
		GridBagConstraints gbc_passwordConfPanel = new GridBagConstraints();
		gbc_passwordConfPanel.fill = GridBagConstraints.BOTH;
		gbc_passwordConfPanel.insets = new Insets(0, 0, 5, 0);
		gbc_passwordConfPanel.gridx = 0;
		gbc_passwordConfPanel.gridy = 2;
		inputPanel.add(passwordConfPanel, gbc_passwordConfPanel);
		
		JLabel lblConfirmPassword = new JLabel("Confirm password:");
		passwordConfPanel.add(lblConfirmPassword);
		
		passConfField = new JPasswordField();
		passConfField.setColumns(20);
		passwordConfPanel.add(passConfField);
		
		JPanel emailPanel = new JPanel();
		GridBagConstraints gbc_emailPanel = new GridBagConstraints();
		gbc_emailPanel.fill = GridBagConstraints.BOTH;
		gbc_emailPanel.gridx = 0;
		gbc_emailPanel.gridy = 3;
		inputPanel.add(emailPanel, gbc_emailPanel);
		
		JLabel lblEmail = new JLabel("Email:");
		emailPanel.add(lblEmail);
		
		emailField = new JTextField();
		emailPanel.add(emailField);
		emailField.setColumns(20);
		
		JPanel buttonPanel = new JPanel();
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		
		JButton addUserButton = new JButton("Add User");
		addUserButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addUser();
			}
		});
		buttonPanel.add(addUserButton);
	}	

	
	private void addUser() {		
		//Taken from LoginDialog.createPlayerAccount() since it's a private function => need to fix this!
		String username = usernameField.getText();		
		String password = new String(passwordField.getPassword());
		String confString = new String(passConfField.getPassword());
		String email = emailField.getText();

		try {
			if (username.isEmpty() || password.isEmpty() || confString.isEmpty() || email.isEmpty()) {
				JOptionPane.showMessageDialog(this, "All fields must be filled.");
				return;
			} else if (password.length() > 30) {
				JOptionPane.showMessageDialog(this, "Password can be maximum 30 characters.");
				return;
			} else if (username.length() > 20) {
				JOptionPane.showMessageDialog(this, "Username can be maximum 20 characters.");
				return;
			} else if (email.length() > 50) {
				JOptionPane.showMessageDialog(this, "Email can be maximum 50 characters.");
				return;
			}
			else if (!password.contentEquals(confString)) {
				JOptionPane.showMessageDialog(this, "Password confirmation does not match.");
				return;
			} else if (UserRepository.userExists(username)) {
				JOptionPane.showMessageDialog(this, "User already exists!");
				return;
			} else {
				byte[] salt = User.generateSalt();
				Player newPlayer = new Player(username, User.getHash(password, salt), salt,
						email, Calendar.getInstance());
				PlayerRepository.addPlayer(newPlayer);
				addUser();
			}
		} catch (RepositoryErrorException e) {
			JOptionPane.showMessageDialog(this, "Unknown error occured while communicating with the server.");
		}

	}
}
