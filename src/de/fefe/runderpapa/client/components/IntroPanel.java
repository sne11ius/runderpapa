package de.fefe.runderpapa.client.components;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.button.ToggleButton;
import com.google.gwt.user.client.ui.HTML;

import de.fefe.runderpapa.client.BlogPostServiceAsync;

public class IntroPanel extends ContentPanel {

	public IntroPanel(int numPosts, BlogPostServiceAsync blogPostService) {
		final SearchPopup searchPopup = new SearchPopup(blogPostService);
		final BlacklistEditor blacklistEditor = new BlacklistEditor();
		setHeaderVisible(false);
		setButtonAlign(HorizontalAlignment.LEFT);

		add(new HTML("<center><b><a href=\"http://fettemama.org\">fettemama.org</a></b> wrapped in cozy gxt. " +
				"Showing the <strong>" + numPosts + " newest</strong> posts. " +
				"src avail @ <a href='https://github.com/sne11ius/runderpapa'>github</a></center>"));
		
		final ToggleButton searchPopupButton = new ToggleButton("Post suchen");
		searchPopupButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				if (searchPopupButton.isPressed()) {
					searchPopup.show(searchPopupButton.getElement(), "tl-bl");
				} else {
					searchPopup.hide();
				}
			}
		});
		addButton(searchPopupButton);
		
		final ToggleButton editBlacklistPopupButton = new ToggleButton("Blacklist bearbeiten");
		editBlacklistPopupButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				if (editBlacklistPopupButton.isPressed()) {
					blacklistEditor.activate();
				} else {
					if (blacklistEditor.isVisible()) {
						blacklistEditor.hide();
					}
				}
			}
		});
		addButton(editBlacklistPopupButton);
		blacklistEditor.setToggleButton(editBlacklistPopupButton);
	}
}
