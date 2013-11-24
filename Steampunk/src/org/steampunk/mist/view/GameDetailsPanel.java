package org.steampunk.mist.view;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import java.awt.FlowLayout;

import javax.swing.BoxLayout;

import java.awt.Component;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.UIManager;

import org.steampunk.mist.AccountManager;
import org.steampunk.mist.model.Admin;
import org.steampunk.mist.model.Comment;
import org.steampunk.mist.model.Game;
import org.steampunk.mist.model.Player;
import org.steampunk.mist.model.User;
import org.steampunk.mist.repository.CommentRepository;
import org.steampunk.mist.repository.GameCopyRepository;
import org.steampunk.mist.repository.GameRepository;
import org.steampunk.mist.repository.PlayerRepository;
import org.steampunk.mist.repository.RepositoryErrorException;
import org.steampunk.mist.view.list.AchievementListPanel;
import org.steampunk.mist.view.list.CommentListPanel;

public class GameDetailsPanel extends JPanel {

	private static final long serialVersionUID = -6594833877793225722L;
	
	private JLabel mGameTitleLabel;
	private JLabel mRatingLabel;
	
	private JButton mEditDescButton;
	private JButton mEditTitleButton;
	private JButton mCreateClanButton;
	private JButton mEditRatingButton;
	private JButton mEditPubButton;
	private JButton mBuyButton;
	
	private JTextArea mDescriptionText;
	private JLabel mPublisherLabel;
	
	CommentListPanel mCommentListPanel;
	AchievementListPanel mAchievementsPanel;
	
	private Game mGame;
	
	/**
	 * Create the panel.
	 */
	public GameDetailsPanel() {
		createView();
	}
	
	private void createView() {
		setLayout(new BorderLayout(0, 0));
		
		JPanel titlepanel = new JPanel();
		add(titlepanel, BorderLayout.NORTH);
		
		mGameTitleLabel = new JLabel("<Title>");
		titlepanel.add(mGameTitleLabel);
		mGameTitleLabel.setHorizontalAlignment(SwingConstants.LEFT);
		mGameTitleLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		
		mEditTitleButton = new JButton("Edit Title");
		titlepanel.add(mEditTitleButton);
		mEditTitleButton.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("implement (edit title)");	
			}
		});
		
		mCreateClanButton = new JButton("Create New Clan");
		titlepanel.add(mCreateClanButton);
		mCreateClanButton.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO
				System.out.println("implement (create clan)");	
			}
		});
		
		mBuyButton = new JButton("Buy: Free");
		titlepanel.add(mBuyButton);
		mBuyButton.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					if (GameCopyRepository.purchaseCopyOfGame(mGame.getGameID(),
							AccountManager.getInstance().getCurrentUser().getUsername())) {
						GameDetailsPanel.this.updateGameInfo();
					} else {
						JOptionPane.showMessageDialog(GameDetailsPanel.this, "No copies left for this game.", "Error: Could not buy game",
								JOptionPane.ERROR_MESSAGE);
					}
				} catch (RepositoryErrorException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					JOptionPane.showMessageDialog(GameDetailsPanel.this, "Unknown database error occurred", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.WEST);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		JPanel panel_1 = new JPanel();
		panel_1.setAlignmentY(Component.TOP_ALIGNMENT);
		panel_1.setAlignmentX(Component.RIGHT_ALIGNMENT);
		FlowLayout flowLayout = (FlowLayout) panel_1.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		panel.add(panel_1);
		
		mDescriptionText = new JTextArea();
		mDescriptionText.setBackground(UIManager.getColor("Button.background"));
		mDescriptionText.setLineWrap(true);
		panel_1.add(mDescriptionText);
		mDescriptionText.setSize(300, 100);
		mDescriptionText.setEditable(false);
		mDescriptionText.setWrapStyleWord(true);
		mDescriptionText.setText("No description");
		
		mEditDescButton = new JButton("Edit Description");
		panel_1.add(mEditDescButton);
		mEditDescButton.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO
				System.out.println("implement (edit desc)");	
			}
		});
		
		Box verticalBox = Box.createVerticalBox();
		panel.add(verticalBox);
		
		JPanel panel_2 = new JPanel();
		verticalBox.add(panel_2);
		FlowLayout flowLayout_1 = (FlowLayout) panel_2.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		
		JLabel lblRatingTitle = new JLabel("Rating:");
		panel_2.add(lblRatingTitle);
		
		mRatingLabel = new JLabel("New label");
		panel_2.add(mRatingLabel);
		
		mEditRatingButton = new JButton("Edit Rating");
		panel_2.add(mEditRatingButton);
		mEditRatingButton.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO
				System.out.println("implement (edit rating)");	
			}
		});
		
		JPanel panel_3 = new JPanel();
		verticalBox.add(panel_3);
		FlowLayout flowLayout_2 = (FlowLayout) panel_3.getLayout();
		flowLayout_2.setAlignment(FlowLayout.LEFT);
		
		JLabel lblPublisherTitle = new JLabel("Publisher:");
		panel_3.add(lblPublisherTitle);
		
		mPublisherLabel = new JLabel("New label");
		panel_3.add(mPublisherLabel);
		
		mEditPubButton = new JButton("Edit Publisher");
		panel_3.add(mEditPubButton);
		mEditPubButton.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO
				System.out.println("implement (edit pub)");	
			}
		});
		
		JPanel panel_4 = new JPanel();
		panel.add(panel_4);
		panel_4.setLayout(new BorderLayout(0, 0));
		
		JLabel lblComments = new JLabel("comments");
		panel_4.add(lblComments);
		panel_4.add(lblComments, BorderLayout.NORTH);
		
		mCommentListPanel = new CommentListPanel();
		panel_4.add(mCommentListPanel, BorderLayout.CENTER);
		
		// This panel will hold the achievements and keys views
		JPanel panel_5 = new JPanel();
		panel_5.setLayout(new BoxLayout(panel_5, BoxLayout.Y_AXIS));
		add(panel_5, BorderLayout.EAST);
		
		mAchievementsPanel = new AchievementListPanel();
		panel_5.add(mAchievementsPanel);

		onCreateView();
	}
	
	public void onCreateView()
	{
		
	}
	
	public void setGame(Game game){
		mGame = game;
		updateGameInfo();
	}
	
	private void updateGameInfo(){
		if (mGame != null) {
			mGameTitleLabel.setText(mGame.getGameName());
			mDescriptionText.setText(mGame.getDescription());
			mRatingLabel.setText(mGame.getRating());
			mPublisherLabel.setText(mGame.getPublisher());
			
			boolean userAdminsGame = false;
			boolean userOwnsGame = false;
			User cUser = AccountManager.getInstance().getCurrentUser();
			if (cUser instanceof Admin) {
				// TODO check if admin owns this game
				userAdminsGame = true;
			} else {
				try {
					userOwnsGame = GameCopyRepository.userOwnsGame(cUser.getUsername(), mGame.getGameID());
				} catch (RepositoryErrorException e) {
					e.printStackTrace();
					userOwnsGame = false;
				}
			}
			mEditDescButton.setVisible(userAdminsGame);
			mEditPubButton.setVisible(userAdminsGame);
			mEditTitleButton.setVisible(userAdminsGame);
			mEditRatingButton.setVisible(userAdminsGame);
			
			mCreateClanButton.setVisible(!userAdminsGame && userOwnsGame);
			mBuyButton.setVisible(!userOwnsGame);
			
			Vector<Comment> cv;
			try {
				cv = CommentRepository.getCommentsForGame(mGame.getGameID());
				mCommentListPanel.setComments(cv);
			} catch (RepositoryErrorException e) {
				System.err.println(e.toString());
			}
			
			mAchievementsPanel.setGame(mGame.getGameID());
		}
	}
}
