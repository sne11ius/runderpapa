package de.fefe.runderpapa.client;

import java.io.IOException;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.fefe.runderpapa.shared.exceptions.AuthenticationException;


@RemoteServiceRelativePath("BlacklistService")
public interface BlacklistService extends RemoteService {

	List<String> getBlacklist(String password) throws IOException, AuthenticationException;
	void setBlacklist(List<String> blacklist, String password) throws IOException, AuthenticationException;
	
}
