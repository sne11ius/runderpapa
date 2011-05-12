package de.fefe.runderpapa.client.components;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.google.gwt.user.client.ui.HTML;

public class IntroPanel extends ContentPanel {

	public IntroPanel() {
		setHeaderVisible(false);
		add(new HTML("<center><b><a href=\"http://fettemama.org\">fettemama.org</a></b> on google crack :D</center>"));
	}
}
