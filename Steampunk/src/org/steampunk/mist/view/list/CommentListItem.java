package org.steampunk.mist.view.list;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.ListCellRenderer;

import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.SwingConstants;

import org.steampunk.mist.model.Comment;
import org.steampunk.mist.util.MistTextFormatter;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JSeparator;

public class CommentListItem extends JPanel{

	private static final long serialVersionUID = 4228285369406356281L;
	
	private JLabel mUsernameLabel;
	private JLabel mDateLabel;
	private JTextArea mCommentTextArea;
	private JSeparator mseparator;
	
	/**
	 * Create the panel.
	 */
	public CommentListItem(String username, String date, String text) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		panel.setAlignmentX(Component.RIGHT_ALIGNMENT);
		add(panel);
		
		mUsernameLabel = new JLabel( (username != null) ? username : "<deleted>");
		mUsernameLabel.setHorizontalAlignment(SwingConstants.LEFT);
		panel.add(mUsernameLabel);
		mUsernameLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		
		mDateLabel = new JLabel(date);
		mDateLabel.setHorizontalAlignment(SwingConstants.LEFT);
		panel.add(mDateLabel);
		
		mCommentTextArea = new JTextArea();
		mCommentTextArea.setAlignmentY(Component.TOP_ALIGNMENT);
		mCommentTextArea.setText(text);
		mCommentTextArea.setLineWrap(true);
		mCommentTextArea.setEditable(false);
		mCommentTextArea.setWrapStyleWord(true);
		add(mCommentTextArea);
		
		mseparator = new JSeparator();
		add(mseparator);

	}

	public static class CommentCellRenderer  implements ListCellRenderer<Comment> {
		@SuppressWarnings("rawtypes")
		@Override
		public Component getListCellRendererComponent(JList list, Comment comment,
				int index, boolean isSelected, boolean cellHasFocus) {
			// TODO Auto-generated method stub
			CommentListItem item = new CommentListItem(comment.getUsername(),
					MistTextFormatter.formatDateTimeString(comment.getTimestamp()),
					comment.getText());
			item.setPreferredSize(new Dimension(300, ( (((int)(comment.getText().length()/80))+1) * 18) + 24) );

			return item;
		}
	}

}
