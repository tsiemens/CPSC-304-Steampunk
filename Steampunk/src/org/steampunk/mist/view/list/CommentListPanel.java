package org.steampunk.mist.view.list;

import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.util.Vector;

import javax.swing.JScrollPane;
import javax.swing.JList;

import org.steampunk.mist.model.Comment;

public class CommentListPanel extends JPanel {

	private static final long serialVersionUID = -701615386909451899L;

	Vector<Comment> mComments;
	
	private JList<Comment> mCommentList;
	
	/**
	 * Create the panel.
	 */
	public CommentListPanel() {
		setLayout(new BorderLayout(0, 0));
		
		mCommentList = new JList<Comment>();
		mCommentList.setCellRenderer(new CommentListItem.CommentCellRenderer());
		
		JScrollPane scrollPane = new JScrollPane(mCommentList);
		add(scrollPane, BorderLayout.CENTER);
	}
	
	public void setComments(Vector<Comment> comments) {
		mComments = comments;
		mCommentList.setListData(mComments);
	}

}
