package de.fefe.runderpapa.client.components;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Popup;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ToggleButton;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

import de.fefe.runderpapa.client.BlacklistService;
import de.fefe.runderpapa.client.BlacklistServiceAsync;
import de.fefe.runderpapa.shared.exceptions.AuthenticationException;

public class BlacklistEditor extends Popup {

	private final BlacklistServiceAsync blacklistService = GWT.create(BlacklistService.class);

	private static final int WIDTH = 500;
	private static final int HEIGHT = 400;
	private ToggleButton toggleButton;
	private final TextArea blacklistArea = new TextArea();
	final Button saveButton = new Button("Speichern");
	
	private String password = null;

	public BlacklistEditor() {

		saveButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				saveButton.disable();
				List<String> blacklist = null == blacklistArea.getValue() ? new LinkedList<String>() : Arrays.asList(blacklistArea.getValue().split("\\s"));
				blacklistService.setBlacklist(blacklist, password, new AsyncCallback<Void>() {
					@Override
					public void onSuccess(Void result) {
						saveButton.enable();
					}
					@Override
					public void onFailure(Throwable caught) {
						MessageBox.info("Fail", "I failed @ storing the blacklist. Sorry :(", null);
						saveButton.enable();
					}
				});
			}
		});

		setStyleAttribute("background-color", "#DFE8F6");
		add(createHeader());
		blacklistArea.setHeight(366);
		blacklistArea.setWidth(496);
		add(blacklistArea, new BorderLayoutData(LayoutRegion.CENTER));

		setSize(WIDTH, HEIGHT);
		setBorders(true);
		setShadow(true);
		setAutoHide(false);
	}
	
	public void activate() {
		MessageBox passwordBox = MessageBox.prompt("Kennwort", "Bitte Kennwort eingeben:");
		passwordBox.addCallback(new Listener<MessageBoxEvent>() {
			@Override
			public void handleEvent(MessageBoxEvent be) {
				if (be.getButtonClicked().getItemId().equals(Dialog.OK)) {
					password = be.getValue();
					if (null == password || "".equals(password.trim())) {
						if (toggleButton.isPressed()) {
							toggleButton.toggle();
						}
						showFail();
						return;
					}
					password = password.trim();
					blacklistService.getBlacklist(password, new AsyncCallback<List<String>>() {
						@Override
						public void onSuccess(List<String> blacklist) {
							String blacklistAreaText = "";
							for (String entry : blacklist) {
								blacklistAreaText += entry + "\n";
							}
							blacklistArea.setValue(blacklistAreaText);
							show(toggleButton.getElement(), "tl-bl");
						}
						
						@Override
						public void onFailure(Throwable caught) {
							if (caught instanceof AuthenticationException) {
								showFail();
							} else {
								GWT.log("error: ", caught);
							}
							if (toggleButton.isPressed()) {
								toggleButton.toggle();
							}
						}
					});
				} else {
					if (toggleButton.isPressed()) {
						toggleButton.toggle();
					}
				}
			}
		});
		passwordBox.show();
	}

	public void setToggleButton(ToggleButton toggleButton) {
		this.toggleButton = toggleButton;
	}
	
	private final void showFail() {
		MessageBox.alert("Fail", "You failed @ providing pwd :D", null);
	}
	
	private LayoutContainer createHeader() {
		LayoutContainer result = new LayoutContainer();
		result.setHeight(30);
		result.setLayout(new BorderLayout());
		
		final BorderLayoutData northData = new BorderLayoutData(LayoutRegion.WEST, 300);
		northData.setMargins(new Margins(2));
		result.add(new Text("Words to be blacklisted"), northData);

		final BorderLayoutData eastData = new BorderLayoutData(LayoutRegion.EAST, 60);
		eastData.setMargins(new Margins(2));
		result.add(saveButton, eastData);
		
		return result;
	}
}
