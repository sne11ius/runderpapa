package de.fefe.runderpapa.client.components;

import java.util.List;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.HTML;

import de.fefe.runderpapa.shared.BlogPostComment;

public class CommentDisplay extends FieldSet {
	
	private int postId;
	private List<BlogPostComment> comments;

	public CommentDisplay(int postId, List<BlogPostComment> comments) {
		this.postId = postId;
		this.comments = comments;
		setCollapsible(true);
	}
	
	@Override
	protected void onRender(Element parent, int index) {
		super.onRender(parent, index);

		String commentCount = comments.isEmpty() ? "None" : "" + comments.size();
		setHeading("Comments for post #" + postId + ": " + commentCount);

	    if (comments.isEmpty()) {
	    	add(new HTML("[no comments so far]"));
	    	collapse();
	    	return;
	    }
	    
	    for (BlogPostComment comment : comments) {
	    	ContentPanel commentPanel = new ContentPanel();
	    	commentPanel.setHeading(comment.getUsername() + " says:");
	    	commentPanel.add(new Label(comment.getComment()));
	    	add(commentPanel);
	    }
	}

}
