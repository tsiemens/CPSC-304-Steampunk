package org.steampunk.mist.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Vector;

import org.steampunk.mist.jdbc.DatabaseManager;
import org.steampunk.mist.jdbc.DatabaseSchema;
import org.steampunk.mist.model.Comment;

public class CommentRepository {
	public static final String FIELD_TIMESTAMP = "timestamp";
	public static final String FIELD_GAMEID = "gameID";
	public static final String FIELD_USERNAME = "username";
	public static final String FIELD_TEXT = "text";
	
	/**
	 * Retrieves comments from database, for game key
	 * @param username
	 * @return the user, or null if an error occurred
	 * @throws RepositoryErrorException
	 * @throws UserNotFoundException
	 */
	public static Vector<Comment> getCommentsForGame(int gameID)  throws RepositoryErrorException{
		DatabaseManager dbm = DatabaseManager.getInstance();
		ResultSet rs;
		try {
			rs = dbm.queryPrepared("SELECT "+FIELD_GAMEID+", "+FIELD_USERNAME+", "+FIELD_TEXT+", "+FIELD_TIMESTAMP
				+" FROM "+DatabaseSchema.TABLE_NAME_COMMENTS
				+" WHERE "+FIELD_GAMEID+" = ?"
				+" ORDER BY "+FIELD_TIMESTAMP+" DESC", gameID);
		} catch (SQLException e) {
			 throw new RepositoryErrorException(e.getMessage());
		}
		
		Vector<Comment> comments = new Vector<Comment>();
		Comment tempComment;
		Calendar calTemp;
		
		try{
			while (rs.next()){
				calTemp = Calendar.getInstance();
				calTemp.setTimeInMillis(rs.getTimestamp(FIELD_TIMESTAMP).getTime());
				tempComment = new Comment(rs.getString(FIELD_TEXT), rs.getString(FIELD_USERNAME),
						rs.getInt(FIELD_GAMEID), calTemp);
				comments.add(tempComment);
			}
		} catch (SQLException e) {
			throw new RepositoryErrorException("Error reading comment data: "+e);
		}
		return comments;
	}
	
	
	
	public static void addComment(Comment comment) throws RepositoryErrorException {
		DatabaseManager dbm = DatabaseManager.getInstance();
		try {
			System.out.println("Adding comment with date "+comment.getTimestamp().getTimeInMillis());
			dbm.updatePrepared("INSERT INTO " + DatabaseSchema.TABLE_NAME_COMMENTS 
					+"("+FIELD_GAMEID+", "+FIELD_USERNAME+", "+FIELD_TEXT+", "+FIELD_TIMESTAMP+")"
					+" VALUES(?, ?, ?, ?)", comment.getGameID(), comment.getUsername(),
					comment.getText(), new Timestamp(comment.getTimestamp().getTimeInMillis()));
		} catch (SQLException e) {
			throw new RepositoryErrorException(e.getMessage());
		}
	}
	
	public static void deleteComment(Comment comment) throws RepositoryErrorException {
		DatabaseManager dbm = DatabaseManager.getInstance();
		try {
			dbm.updatePrepared("DELETE FROM " + DatabaseSchema.TABLE_NAME_COMMENTS 
					+" WHERE "+FIELD_GAMEID+" = ? AND "+FIELD_TIMESTAMP+" = ?",
					comment.getGameID(), new Timestamp(comment.getTimestamp().getTimeInMillis()));
		} catch (SQLException e) {
			throw new RepositoryErrorException(e.getMessage());
		}
	}

	public static String getAverageLength(String adminName) throws RepositoryErrorException {
		DatabaseManager dbm = DatabaseManager.getInstance();
		ResultSet rs;
		try {
			rs = dbm.queryPrepared("SELECT AVG(" + DatabaseSchema.VIEW_NAME_AVERAGE_COMMENT_LENGTH + ".length)"
				+ " FROM "+ DatabaseSchema.VIEW_NAME_AVERAGE_COMMENT_LENGTH + ", " + DatabaseSchema.TABLE_NAME_ADMINISTRATES + " adm"
				+ " WHERE adm.gameID = " + DatabaseSchema.VIEW_NAME_AVERAGE_COMMENT_LENGTH + ".gameID AND adm.username = ?", adminName);
		} catch (SQLException e) {
			 throw new RepositoryErrorException(e.getMessage());
		}
		
		String result = null;
		try {
			if (rs.next()){
				result = Float.toString(rs.getFloat(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public static String getMinLength(String adminName) throws RepositoryErrorException {
		DatabaseManager dbm = DatabaseManager.getInstance();
		ResultSet rs;
		try {
			rs = dbm.queryPrepared("SELECT MIN(" + DatabaseSchema.VIEW_NAME_AVERAGE_COMMENT_LENGTH + ".length)"
				+ " FROM "+ DatabaseSchema.VIEW_NAME_AVERAGE_COMMENT_LENGTH + ", " + DatabaseSchema.TABLE_NAME_ADMINISTRATES + " adm"
				+ " WHERE adm.gameID = " + DatabaseSchema.VIEW_NAME_AVERAGE_COMMENT_LENGTH + ".gameID AND adm.username = ?", adminName);
		} catch (SQLException e) {
			 throw new RepositoryErrorException(e.getMessage());
		}
		
		String result = null;
		try {
			if (rs.next()){
				result = Float.toString(rs.getFloat(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public static String getMaxLength(String adminName) throws RepositoryErrorException {
		DatabaseManager dbm = DatabaseManager.getInstance();
		ResultSet rs;
		try {
			rs = dbm.queryPrepared("SELECT MAX(" + DatabaseSchema.VIEW_NAME_AVERAGE_COMMENT_LENGTH + ".length)"
				+ " FROM "+ DatabaseSchema.VIEW_NAME_AVERAGE_COMMENT_LENGTH + ", " + DatabaseSchema.TABLE_NAME_ADMINISTRATES + " adm"
				+ " WHERE adm.gameID = " + DatabaseSchema.VIEW_NAME_AVERAGE_COMMENT_LENGTH + ".gameID AND adm.username = ?", adminName);
		} catch (SQLException e) {
			 throw new RepositoryErrorException(e.getMessage());
		}
		
		String result = null;
		try {
			if (rs.next()){
				result = Float.toString(rs.getFloat(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

}