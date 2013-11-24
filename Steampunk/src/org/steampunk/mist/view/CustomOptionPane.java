package org.steampunk.mist.view;

import java.awt.Component;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class CustomOptionPane extends JOptionPane{

	private static final long serialVersionUID = 2944154018272420940L;

	/**
	 * Opens a dialog with a password field
	 * @param parentComponent
	 * @param message
	 * @param title
	 * @param icon
	 * @return the password entered, or null if cancelled.
	 */
	public static String showPasswordDialog(Component parentComponent, String message, String title, Icon icon) {
		JPanel panel = new JPanel();
		JLabel label = new JLabel(message);
		JPasswordField pass = new JPasswordField(10);
		panel.add(label);
		panel.add(pass);
		String[] options = new String[]{"OK", "Cancel"};
		int option = JOptionPane.showOptionDialog(parentComponent, panel, title,
		                         JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE,
		                         icon, options, options[0]);
		if(option == 0 && pass != null) // pressing OK button, and window not closed
		{
		    char[] password = pass.getPassword();
		    return new String(password);
		} else {
			return null;
		}
	}
	
	/**
	 * Opens a dialog with a username, a password, and an email field
	 * @param parentComponent
	 * @param message
	 * @param title
	 * @param icon
	 * @return the username, password, and email entered, or null if cancelled.
	 */
	public static Vector<String> showAddUserDialog(Component parentComponent, String message, String title, Icon icon) {
		JPanel panel = new JPanel();
		Vector<String> newUserVector = new Vector<String>();

		//username field
		JLabel usernameLabel = new JLabel("Username: ");
		JTextField usernameField = new JTextField(10);
		panel.add(usernameLabel);
		panel.add(usernameField);
		//password field
		JLabel passwordLabel = new JLabel("Password: ");
		JPasswordField passwordField = new JPasswordField(10);
		panel.add(passwordLabel);
		panel.add(passwordField);
		//email field
		JLabel emailLabel = new JLabel("Email: ");
		JTextField emailField = new JTextField(10);
		panel.add(emailLabel);
		panel.add(emailField);
		//set up option buttons
		String[] options = new String[]{"Add Player", "Add System Admin", "Add Game Admin", "Cancel"};
		int option = JOptionPane.showOptionDialog(parentComponent, panel, title,
		                         JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE,
		                         icon, options, options[0]);
		if(option == 0 && usernameField != null && passwordField != null && emailField != null) // pressing Add Player button, and window not closed
		{
			//add username
		    String username = usernameField.getText();
		    newUserVector.add(username);
		    //add password
		    char[] password = passwordField.getPassword();
		    newUserVector.add(new String(password));
		    //add email
		    String email = emailField.getText();
		    newUserVector.add(email);
		    //add type of user selection
		    newUserVector.add("Player");
		    
		    return newUserVector;
		}
		if(option == 1 && usernameField != null && passwordField != null && emailField != null) // pressing Add System Admin button, and window not closed
		{
			//add username
		    String username = usernameField.getText();
		    newUserVector.add(username);
		    //add password
		    char[] password = passwordField.getPassword();
		    newUserVector.add(new String(password));
		    //add email
		    String email = emailField.getText();
		    newUserVector.add(email);
		    //add type of user selection
		    newUserVector.add("System Admin");
		    
		    return newUserVector;
		}
		if(option == 2 && usernameField != null && passwordField != null && emailField != null) // pressing Game Admin button, and window not closed
		{
			//add username
		    String username = usernameField.getText();
		    newUserVector.add(username);
		    //add password
		    char[] password = passwordField.getPassword();
		    newUserVector.add(new String(password));
		    //add email
		    String email = emailField.getText();
		    newUserVector.add(email);
		    //add type of user selection
		    newUserVector.add("Game Admin");
		    
		    return newUserVector;
		}
		if(option == 3){
			return null;
		}
		return null;
		
	}
}
