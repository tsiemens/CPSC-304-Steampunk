package org.steampunk.mist.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;

import java.awt.Component;

import javax.swing.JPasswordField;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Calendar;

import javax.swing.SwingConstants;

import org.steampunk.mist.AccountManager;
import org.steampunk.mist.model.Player;
import org.steampunk.mist.model.User;
import org.steampunk.mist.repository.AdminRepository;
import org.steampunk.mist.repository.PlayerRepository;
import org.steampunk.mist.repository.RepositoryErrorException;
import org.steampunk.mist.repository.UserRepository;
import org.steampunk.mist.repository.UserRepository.UserNotFoundException;

public class LoginDialog extends JDialog {

	private static final long serialVersionUID = 850240242698660511L;
	
	private final JPanel contentPanel = new JPanel();
	private JPanel newUserPanel;
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JPanel usernamePanel;
	private JPanel passwordPanel;
	private JLabel lblUsername;
	private JPasswordField confPasswordField;
	private JTextField emailField;
	
	private JButton cancelSignUpButton;
	private JButton signUpButton;
	private JButton loginButton;
	
	boolean isSignInMode;
	OnLoginListener mLoginListener;

	/**
	 * Create the dialog.
	 */
	public LoginDialog() {
		setTitle("Login");
		isSignInMode = false;
		
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		{
			JPanel loginPanel = new JPanel();
			loginPanel.setBorder(null);
			contentPanel.add(loginPanel);
			{
				usernamePanel = new JPanel();
				usernamePanel.setBorder(null);
				usernamePanel.setAlignmentY(Component.TOP_ALIGNMENT);
				{
					lblUsername = new JLabel("Username:");
					lblUsername.setHorizontalAlignment(SwingConstants.TRAILING);
					lblUsername.setBounds(10, 12, 95, 14);
				}
				{
					usernameField = new JTextField();
					usernameField.setBounds(115, 6, 299, 27);
					usernameField.setColumns(10);
				}
			}
			{
				passwordPanel = new JPanel();
				passwordPanel.setLayout(null);
				{
					JLabel lblPassword = new JLabel("Password:");
					lblPassword.setHorizontalAlignment(SwingConstants.TRAILING);
					lblPassword.setBounds(10, 13, 95, 14);
					passwordPanel.add(lblPassword);
				}
				{
					passwordField = new JPasswordField();
					passwordField.setBounds(115, 6, 299, 29);
					passwordPanel.add(passwordField);
				}
			}
			loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
			usernamePanel.setLayout(null);
			usernamePanel.add(lblUsername);
			usernamePanel.add(usernameField);
			loginPanel.add(usernamePanel);
			loginPanel.add(passwordPanel);
		}
		{
			newUserPanel = new JPanel();
			contentPanel.add(newUserPanel);
			newUserPanel.setLayout(new BoxLayout(newUserPanel, BoxLayout.Y_AXIS));
			{
				JPanel panel = new JPanel();
				panel.setLayout(null);
				newUserPanel.add(panel);
				{
					JLabel lblConfirmPass = new JLabel("Confirm Password:");
					lblConfirmPass.setHorizontalAlignment(SwingConstants.TRAILING);
					lblConfirmPass.setBounds(10, 13, 95, 14);
					panel.add(lblConfirmPass);
				}
				{
					confPasswordField = new JPasswordField();
					confPasswordField.setBounds(115, 6, 299, 29);
					panel.add(confPasswordField);
				}
			}
			{
				JPanel panel = new JPanel();
				panel.setLayout(null);
				newUserPanel.add(panel);
				{
					JLabel lblEmail = new JLabel("Email:");
					lblEmail.setHorizontalAlignment(SwingConstants.TRAILING);
					lblEmail.setBounds(10, 13, 97, 14);
					panel.add(lblEmail);
				}
				{
					emailField = new JTextField();
					emailField.setBounds(117, 10, 297, 29);
					panel.add(emailField);
					emailField.setColumns(10);
				}
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				loginButton = new JButton("Login");
				loginButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						login();
					}
				});
				loginButton.setActionCommand("Login");
				buttonPane.add(loginButton);
				getRootPane().setDefaultButton(loginButton);
			}
			{
				signUpButton = new JButton("Sign Up!");
				signUpButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						if (!isSignInMode)
							setSignInMode(true);
						else {
							createPlayerAccount();
						}
					}
				});
				signUpButton.setActionCommand("SignUp");
				buttonPane.add(signUpButton);
			}
			
			cancelSignUpButton = new JButton("Cancel");
			cancelSignUpButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					setSignInMode(false);
				}
			});
			cancelSignUpButton.setActionCommand("Cancel");
			buttonPane.add(cancelSignUpButton);
		}
		
		setSignInMode(isSignInMode);
	}
	
	public void setSignInMode(boolean isSignIn) {
		isSignInMode = isSignIn;
		confPasswordField.setEnabled(isSignIn);
		emailField.setEditable(isSignIn);
		
		cancelSignUpButton.setEnabled(isSignIn);
		cancelSignUpButton.setVisible(isSignIn);
		
		loginButton.setEnabled(!isSignIn);
		loginButton.setVisible(!isSignIn);
	}
	
	private void login() {
		String username = usernameField.getText();
		String password = new String(passwordField.getPassword());
		
		try {
			if (UserRepository.authorizeUser(username, password)) {
				User user;
				if (PlayerRepository.playerExists(username)) {
					user = PlayerRepository.getPlayer(username);
				} else if (AdminRepository.adminExists(username)) {
					user = AdminRepository.getAdmin(username);
				} else {
					throw new RepositoryErrorException("User was not an admin or player");
				}
				
				AccountManager.getInstance().setCurrentUser(user);
				if (mLoginListener != null) {
					mLoginListener.onLogin();
				}
				this.dispose();
			} else {
				JOptionPane.showMessageDialog(this, "Username or password incorrect!");
			}
		} catch (UserNotFoundException e) {
			JOptionPane.showMessageDialog(this, "Username or password incorrect!");
		} catch (RepositoryErrorException e) {
			System.out.println(e);
			JOptionPane.showMessageDialog(this, "Unknown Database Error Occurred!");
		}
	}
	
	private void createPlayerAccount() {
		String username = usernameField.getText();
		String password = new String(passwordField.getPassword());
		String confString = new String(confPasswordField.getPassword());
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
				login();
			}
		} catch (RepositoryErrorException e) {
			JOptionPane.showMessageDialog(this, "Error occured. Check inputs.");
		}
	}
	
	public void setOnLoginListener(OnLoginListener listener)
	{
		mLoginListener = listener;
	}
	
	public static interface OnLoginListener
	{
		
		public void onLogin();
	}
}
