package de.fefe.runderpapa.client.components;

import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;

import de.fefe.runderpapa.client.BlogPostServiceAsync;
import de.fefe.runderpapa.shared.BlogPost;

public class BlogPostPanel extends FormPanel {
	
	private BlogPost post = null;
	private BlogPostServiceAsync blogPostServiceAsync = null;
	
	private HTML postText = null;
	private CommentDisplay commentDisplay = null;
	private PostCommentPanel postCommentPanel = null;
		
	public BlogPostPanel(BlogPost post, BlogPostServiceAsync blogPostServiceAsync) {
		this.post = post;
		this.blogPostServiceAsync = blogPostServiceAsync;
		initComponents();
	}
	
	/*
	@Override
	protected void onRender(Element parent, int index) {
		super.onRender(parent, index);
		initComponents();
	}
	*/
	
	private void initComponents() {
		remove(postText);
		remove(commentDisplay);
		remove(postCommentPanel);
		
		postText = new HTML(post.getText());
		commentDisplay = new CommentDisplay(post.getPostId(), post.getComments());
		postCommentPanel = new PostCommentPanel(this, post.getPostId(), blogPostServiceAsync);
		
		this.add(postText);
		this.add(commentDisplay);
		this.add(postCommentPanel);
	}

	public void reload() {
		blogPostServiceAsync.getPost(post.getPostId(), new AsyncCallback<BlogPost>() {
			
			@Override
			public void onSuccess(BlogPost result) {
				post = result;
				initComponents();
			}
			
			@Override
			public void onFailure(Throwable caught) {
				add(new HTML("Failed to realod Post :/.<br>Reason:<p>" + caught.getMessage() + "</p>"));
			}
		});
	}

}
