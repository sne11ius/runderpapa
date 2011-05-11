package de.fefe.runderpapa.shared;

import java.io.Serializable;

public class BlogPostComment implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String username = null;
	private String comment = null;
	
	public BlogPostComment() {
	}
	
	public BlogPostComment(String username, String comment) {
		setUsername(username);
		setComment(comment);
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getComment() {
		return comment;
	}
	
	public void setComment(String comment) {
		this.comment = comment;
	}
	
}
