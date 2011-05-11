package de.fefe.runderpapa.client;

import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

import de.fefe.runderpapa.client.components.MainPanel;

public class RunderPapa implements EntryPoint {
	public void onModuleLoad() {
		Viewport viewport = new Viewport();
		viewport.setLayout(new FitLayout());
		MainPanel mainPanel = new MainPanel();
		viewport.add(mainPanel, new FitData(0, 25, 25, 00));
		
		RootPanel.get().add(viewport);
	}
}
