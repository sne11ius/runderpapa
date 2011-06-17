package de.fefe.runderpapa.client;

import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;

import de.fefe.runderpapa.client.components.MainPanel;

public class RunderPapa implements EntryPoint {
	
	private static final int POSTS_TO_SHOW = 20;
	
	private final BlogPostServiceAsync blogPostService = GWT.create(BlogPostService.class);

	public void onModuleLoad() {
		final Viewport viewport = new Viewport();
		viewport.setLayout(new FitLayout());
		
		blogPostService.getMaxPostId(new AsyncCallback<Integer>() {
			
			@Override
			public void onSuccess(Integer maxPostId) {				
				MainPanel mainPanel = new MainPanel(maxPostId, POSTS_TO_SHOW, blogPostService);
				viewport.add(mainPanel, new FitData(0, 25, 25, 00));
				
				RootPanel.get().add(viewport);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Could not get maxPostId, dying :D", caught);
			}
		});
	}
}
