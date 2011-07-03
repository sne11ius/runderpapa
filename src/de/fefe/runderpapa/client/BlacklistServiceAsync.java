package de.fefe.runderpapa.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface BlacklistServiceAsync {
	void getBlacklist(String password, AsyncCallback<List<String>> callback);
	void setBlacklist(List<String> blacklist, String password, AsyncCallback<Void> callback);
}
