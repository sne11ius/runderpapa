package de.fefe.runderpapa.shared;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class BlogPost implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int postId;
	private String text = null;
	private List<BlogPostComment> comments = new LinkedList<BlogPostComment>();
	
	public BlogPost() {
	}
	
	public BlogPost(int postId, String text, List<BlogPostComment> comments) {
		setPostId(postId);
		setText(text);
		setComments(comments);
	}
	
	public int getPostId() {
		return postId;
	}
	
	public void setPostId(int postId) {
		this.postId = postId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public List<BlogPostComment> getComments() {
		return comments;
	}

	private void setComments(List<BlogPostComment> comments) {
		this.comments = comments;	
	}
	
}
