package de.fefe.runderpapa.client.components;

import java.util.LinkedList;
import java.util.List;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

import de.fefe.runderpapa.client.BlogPostService;
import de.fefe.runderpapa.client.BlogPostServiceAsync;
import de.fefe.runderpapa.shared.BlogPost;
import de.fefe.runderpapa.shared.BlogPostComment;

public class MainPanel extends VerticalPanel {

	private final BlogPostServiceAsync blogPostService = GWT.create(BlogPostService.class);
	
	public MainPanel() {
		setScrollMode(Scroll.AUTO);
		setSpacing(10);
		
		add(new IntroPanel());
		
		blogPostService.getPosts(new AsyncCallback<List<BlogPost>>() {
			
			@Override
			public void onSuccess(List<BlogPost> posts) {				
				for (BlogPost post : posts) {
					add(new BlogPostPanel(post, blogPostService));
				}
				GWT.log("all posts loaded - laying out");
				layout();
				GWT.log("layout done...");
			}
			
			@Override
			public void onFailure(Throwable caught) {
				add(new BlogPostPanel(new BlogPost(0, caught.getLocalizedMessage(), new LinkedList<BlogPostComment>()), blogPostService));
			}
		});
	}	
}
