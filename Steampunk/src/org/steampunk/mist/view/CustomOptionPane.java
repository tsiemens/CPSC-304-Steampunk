package org.steampunk.mist.view;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

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
}
