package org.steampunk.mist.model;

import java.util.Calendar;

public class Comment {

	private String mText;
	private Calendar mTimestamp;
	
	public Comment() {
		mText = null;
		mTimestamp = null;
	}
	
	public Comment(String text, Calendar timestamp) {
		mText = text;
		mTimestamp = timestamp;
	}
	
	public String getText() {
		return mText;
	}
	
	public Calendar getTimestamp() {
		return mTimestamp;
	}
	
	public void setText(String text) {
		mText = text;
	}
	
	public void setTimestamp(Calendar timestamp) {
		mTimestamp = timestamp;
	}
}
