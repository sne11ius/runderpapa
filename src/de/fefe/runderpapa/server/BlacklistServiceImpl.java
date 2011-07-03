package de.fefe.runderpapa.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.fefe.runderpapa.client.BlacklistService;
import de.fefe.runderpapa.shared.exceptions.AuthenticationException;

@SuppressWarnings("serial")
public class BlacklistServiceImpl extends RemoteServiceServlet implements BlacklistService {
	
	private static final String PASSWORDS_FILE_INIT_PARAM = "passwordsFilename";
	private static final String BLACKLIST_FILENAME_INIT_PARAM = "blacklistFilename";

	@Override
	public List<String> getBlacklist(String password) throws IOException, AuthenticationException {
		if (null == password) {
			throw new IllegalArgumentException("Param `password' cannot be NULL.");
		}
		if (!checkPassword(password)) {
			throw new AuthenticationException();
		}

		return CommentBlacklistManager.getBlacklist(getServletContext().getInitParameter(BLACKLIST_FILENAME_INIT_PARAM));
	}

	@Override
	public void setBlacklist(List<String> blacklist, String password) throws AuthenticationException, IOException {
		if (null == password) {
			throw new IllegalArgumentException("Param `password' cannot be NULL.");
		}
		if (!checkPassword(password)) {
			throw new AuthenticationException();
		}

		CommentBlacklistManager.writeBlacklist(blacklist, getServletContext().getInitParameter(BLACKLIST_FILENAME_INIT_PARAM));
	}

	private boolean checkPassword(String password) throws IOException {
		InputStream passwordsStream = new FileInputStream(new File(getServletContext().getInitParameter(PASSWORDS_FILE_INIT_PARAM)));
		StringWriter writer = new StringWriter();
		IOUtils.copy(passwordsStream, writer);
		return Arrays.asList(writer.toString().split("\\s")).contains(password);
	}

}
