package org.steampunk.mist.view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.BorderLayout;

import javax.swing.Icon;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Mist extends JFrame{

	private JTabbedPane tabbedPane;

	/**
	 * Create the application.
	 */
	public Mist() {
		initialize();
		setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		setBounds(100, 100, 450, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnMist = new JMenu("Mist");
		menuBar.add(mnMist);
		
		JMenuItem mntmLogout = new JMenuItem("Logout");
		mnMist.add(mntmLogout);
		
		JMenuItem mntmExit_1 = new JMenuItem("Exit");
		mntmExit_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Exiting...");
				System.exit(0);
			}
		});
		mnMist.add(mntmExit_1);
		
	}

	/**
	 * Create a tab.
	 */
	public void addTab(String title, Icon icon, JPanel panel, String tip){
		tabbedPane.addTab(title, icon, panel, tip);
	}
}

