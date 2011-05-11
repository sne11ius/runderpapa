package de.fefe.runderpapa.client.components;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.MarginData;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;

import de.fefe.runderpapa.client.BlogPostServiceAsync;
import de.fefe.runderpapa.shared.BlogPostComment;

public class PostCommentPanel extends FieldSet {
	
	private int postId;
	private BlogPostServiceAsync blogPostServiceAsync = null;
	
	private final TextField<String> nickname = new TextField<String>();
	private final TextField<String> comment = new TextField<String>();
	private final Button sendButton = new Button("post your comment");
	private BlogPostPanel blogPostPanel = null;;
	
	public PostCommentPanel(BlogPostPanel blogPostPanel, int postId, BlogPostServiceAsync blogPostServiceAsync) {
		this.blogPostPanel = blogPostPanel;
		this.postId = postId;
		this.blogPostServiceAsync = blogPostServiceAsync;
		
		initComponents();
	}
	
	private void initComponents() {
		setCollapsible(true);
		setHeading("Write a comment for post #" + postId);
		FormLayout layout = new FormLayout();  
	    layout.setLabelWidth(75);  
	    this.setLayout(layout);
	    nickname.setFieldLabel("Nickname");  
	    nickname.setAllowBlank(false);
	    add(nickname, new FormData("-20"));
 
	    comment.setFieldLabel("Comment");
	    comment.setAllowBlank(false);
	    add(comment, new FormData("-20"));

	    sendButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent ce) {
				sendButton.disable();
				BlogPostComment blogPostComment = new BlogPostComment(nickname.getValue(), comment.getValue());
				nickname.setRawValue("");
				comment.setRawValue("");
				blogPostServiceAsync.addComment(postId, blogPostComment, new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
						MessageBox.info("Post comment", "I wasn't in ur fettemama.org, posting no comment: " + caught.getMessage(), new Listener<MessageBoxEvent>() {
							@Override
							public void handleEvent(MessageBoxEvent be) {
								sendButton.enable();
							}
						});
					}

					@Override
					public void onSuccess(Void result) {
						MessageBox.info("Post comment", "I was in ur fettemama.org, posting your comment.", new Listener<MessageBoxEvent>() {
							@Override
							public void handleEvent(MessageBoxEvent be) {
								sendButton.enable();
								blogPostPanel.reload();
							}
						});
					}
					
				});
			}
		});
	    add(sendButton, new MarginData(0, 0, 0, 80));
	}

	@Override
	protected void onRender(Element parent, int index) {
		super.onRender(parent, index);
	}
	
}
