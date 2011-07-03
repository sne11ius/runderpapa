package de.fefe.runderpapa.client.components;

import java.util.List;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.fx.Resizable;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Popup;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;

import de.fefe.runderpapa.client.BlogPostServiceAsync;

public class SearchPopup extends Popup {
	
	private BlogPostServiceAsync blogPostService;

	private static final int WIDTH = 500;
	
	private static final int HEIGHT_SMALL = 58;
	private static final int HEIGHT_SEARCHING = 73;
	private static final int HEIGHT_EXTENDED = 600;
	
	private static final VerticalPanel content = new VerticalPanel();

	public SearchPopup(BlogPostServiceAsync blogPostService) {
		this.blogPostService = blogPostService;

		setStyleAttribute("background-color", "#DFE8F6");
		add(new SearchPanel(this));
		content.setScrollMode(Scroll.AUTO);
		add(content, new BorderLayoutData(LayoutRegion.CENTER));
		
		setSize(WIDTH, HEIGHT_SMALL);
		setBorders(true);
		setShadow(true);
		setAutoHide(false);

		@SuppressWarnings("unused")
		Resizable resizable = new Resizable(this);
	}
	
	@Override
	protected void onRender(Element target, int index) {
		setConstrainViewport(true);
		super.onRender(target, index);
	}

	public void doSearch(String text) {
		content.removeAll();
		final HTML waitHTML = new HTML("<img src='gxt/images/default/shared/loading-balls.gif' /> suche...");
		content.add(waitHTML);
		content.setHeight(HEIGHT_SEARCHING - 55);
		setSize(WIDTH, HEIGHT_SEARCHING);
		layout();
		blogPostService.searchPosts(text, new AsyncCallback<List<Integer>>() {
			
			@Override
			public void onSuccess(List<Integer> postsIds) {
				content.remove(waitHTML);
				setSize(WIDTH, HEIGHT_EXTENDED);
				content.setHeight(HEIGHT_EXTENDED - 55);
				for (Integer postId : postsIds) {
					content.add(new BlogPostPanel(postId, blogPostService));
				}
				layout();
			}
			
			@Override
			public void onFailure(Throwable caught) {
				content.remove(waitHTML);
				setHeight(HEIGHT_SMALL);
				content.setHeight(HEIGHT_SMALL - 55);
				layout();
				MessageBox.info("Achtung, Fehlerzeit!", "Grund: " + caught.getMessage(), null);
			}
		});
	}

}
