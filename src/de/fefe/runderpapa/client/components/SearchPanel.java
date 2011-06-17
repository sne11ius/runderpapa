package de.fefe.runderpapa.client.components;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.google.gwt.user.client.Element;

public class SearchPanel extends LayoutContainer {

	private static final int MIN_SEARCH_CHARS = 4;	
	private final TextField<String> searchTextField = new TextField<String>();
	private final Text explanationText = new Text("Suchtext eingeben");

	public SearchPanel(final SearchPopup searchPopup) {
		
		final BorderLayout layout = new BorderLayout();
		setLayout(layout);
		
		final Button searchButton = new Button("suchen");
		searchButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent ce) {
				if (null == searchTextField.getValue() || searchTextField.getValue().length() < MIN_SEARCH_CHARS) {
					MessageBox.info("Suchtext zu kurz", "Du sollst nicht nach weniger als " + MIN_SEARCH_CHARS + " Zeichen suchen!", null);
				} else {
					searchPopup.doSearch(searchTextField.getValue());
				}
			}
		});
		
		final BorderLayoutData northData = new BorderLayoutData(LayoutRegion.NORTH, 20);
		northData.setMargins(new Margins(2));
		add(explanationText, northData);
		
		final BorderLayoutData centerData = new BorderLayoutData(LayoutRegion.CENTER);
		centerData.setMargins(new Margins(2));
		add(searchTextField, centerData);
		
		final BorderLayoutData eastData = new BorderLayoutData(LayoutRegion.EAST, 50);
		eastData.setMargins(new Margins(2));
		add(searchButton, eastData);
	}
	
	@Override
	protected void onRender(Element parent, int index) {
		super.onRender(parent, index);
		setSize(498, 55);
	}
	
}
