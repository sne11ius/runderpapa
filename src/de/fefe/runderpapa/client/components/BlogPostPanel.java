package de.fefe.runderpapa.client.components;

import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;

import de.fefe.runderpapa.client.BlogPostServiceAsync;
import de.fefe.runderpapa.shared.BlogPost;

public class BlogPostPanel extends FormPanel {
	
	private int postId = 0;
	private BlogPostServiceAsync blogPostServiceAsync = null;
	
	private HTML postText = null;
	private CommentDisplay commentDisplay = null;
	private PostCommentPanel postCommentPanel = null;
		
	public BlogPostPanel(int postId, BlogPostServiceAsync blogPostServiceAsync) {
		this.postId = postId;
		this.blogPostServiceAsync = blogPostServiceAsync;
	}
	
	@Override
	protected void onRender(Element target, int index) {
		super.onRender(target, index);
		reload();
	}
	
	private void initComponents(BlogPost blogPost) {
		
		if (postText != null) remove(postText);
		if (commentDisplay != null) remove(commentDisplay);
		if (postCommentPanel != null) remove(postCommentPanel);
		
		postText = new HTML(blogPost.getText());
		commentDisplay = new CommentDisplay(blogPost.getPostId(), blogPost.getComments());
		postCommentPanel = new PostCommentPanel(this, blogPost.getPostId(), blogPostServiceAsync);
		
		this.add(postText);
		this.add(new HTML("<br><iframe src=\"http://www.facebook.com/plugins/like.php?href=http://fettemama.org/post?id=" + blogPost.getPostId() + "\" scrolling=\"no\" style=\"border:none; width: 500px; height:80px\"><iframe>"));
		this.add(commentDisplay);
		this.add(postCommentPanel);
		
		layout();
	}

	public void reload() {
		blogPostServiceAsync.getPost(postId, new AsyncCallback<BlogPost>() {
			
			@Override
			public void onSuccess(BlogPost blogPost) {
				initComponents(blogPost);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				add(new HTML("Failed to realod Post :/.<br>Reason:<p>" + caught.getMessage() + "</p>"));
			}
		});
	}

}
