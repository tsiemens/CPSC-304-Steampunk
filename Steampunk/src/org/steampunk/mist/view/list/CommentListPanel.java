package org.steampunk.mist.view.list;

import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JList;

import org.steampunk.mist.model.Comment;

import javax.swing.JButton;
import javax.swing.SwingConstants;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CommentListPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = -701615386909451899L;

	private Vector<Comment> mComments;
	
	private JList<Comment> mCommentList;
	private JButton mNewCommentButton;

	private ActionListener mAddCommentListener;
	
	/**
	 * Create the panel.
	 */
	public CommentListPanel() {
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		add(panel, BorderLayout.NORTH);
		
		mNewCommentButton = new JButton("Add comment");
		panel.add(mNewCommentButton);
		mNewCommentButton.addActionListener(this);
		
		mCommentList = new JList<Comment>();
		mCommentList.setCellRenderer(new CommentListItem.CommentCellRenderer());
		
		JScrollPane scrollPane = new JScrollPane(mCommentList);
		add(scrollPane, BorderLayout.CENTER);
	}
	
	public void setComments(Vector<Comment> comments) {
		mComments = comments;
		mCommentList.setListData(mComments);
	}
	
	public void addComment(Comment comment) {
		mComments.add(0, comment);
		mCommentList.setListData(mComments);
	}
	
	public void setAddCommentButtonListener(ActionListener listener) {
		mAddCommentListener = listener;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (mAddCommentListener != null)
			mAddCommentListener.actionPerformed(event);
	}

}
