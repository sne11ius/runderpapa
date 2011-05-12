package de.fefe.runderpapa.client.components;

import java.util.LinkedList;
import java.util.List;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.widget.VerticalPanel;

import de.fefe.runderpapa.client.BlogPostServiceAsync;

public class MainPanel extends VerticalPanel {
	
	private List<BlogPostPanel> blogPostPanels = new LinkedList<BlogPostPanel>();
	
	private int maxPostId = 0;
	private int postsToShow = 0;
	
	private BlogPostServiceAsync blogPostService = null;
	
	public MainPanel(int maxPostId, int postsToShow, BlogPostServiceAsync blogPostService) {
		this.maxPostId = maxPostId;
		this.postsToShow = postsToShow;
		this.blogPostService = blogPostService; 
		setScrollMode(Scroll.AUTO);
		setSpacing(10);
		
		add(new IntroPanel());
		
		showPosts(postsToShow);
	}
	
	private void clear() {
		for (BlogPostPanel blogPostPanel : blogPostPanels) {
			remove(blogPostPanel);
		}
		blogPostPanels.clear();
	}

	public void showPosts(int count) {
		clear();
		for (int i = maxPostId; i >= maxPostId - postsToShow; --i) {
			blogPostPanels.add(new BlogPostPanel(i, blogPostService));
		}
		
		for (BlogPostPanel blogPostPanel : blogPostPanels) {
			add(blogPostPanel);
		}
	}	
}
