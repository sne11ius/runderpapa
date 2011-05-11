package de.fefe.runderpapa.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.SocketException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.telnet.TelnetClient;

import de.fefe.runderpapa.shared.BlogPostComment;

public class FetteMamaTelnetClient {
	
	private static final Log LOG = LogFactory.getLog(FetteMamaTelnetClient.class);

	private static final String HOSTNAME = "fettemama.org";
	private static final int PORT = 1337;
	
	private static final String PROMPT = "#: ";
	private static final String GREETING_END = "Go";
	private static final String KNOWN_GREETING = "fettemama.org blog system version v0.2\n\t(c) don vito 2011\n\twritten in Go";
	
	private PrintStream out = null;
	private InputStream in = null;

	private TelnetClient client = new TelnetClient();

	public FetteMamaTelnetClient() {
	}
	
	public void addComment(int postId, BlogPostComment comment)	throws SocketException, IOException {
		login();
		String feedback = sendCommand(getPostCommentCommand(postId, comment));
		String expectedFeedback = getExpectedCommentFeedback(postId);
		if (!feedback.trim().startsWith(expectedFeedback)) {
			LOG.error("Unexpected feedback for comment.\n\tExpected: `" + expectedFeedback + "'\n\tGot: `" + feedback + "'");
		}
		logout();
	}
	
	private void login() throws SocketException, IOException {
		client.connect(HOSTNAME, PORT);
		out = new PrintStream(client.getOutputStream());
		in = client.getInputStream();
		
		String greeting = readUntil(GREETING_END);
		if (!KNOWN_GREETING.equals(greeting)) {
			LOG.error("Unkown telnet greeting - new version srsly?\n\tContinuing anyways . . .");
		}
		readUntil(PROMPT);
	}
	
	private void logout() throws IOException {
		client.disconnect();
	}
	
	String getExpectedCommentFeedback(int postId) {
		return "commented post " + postId + ".";
	}

	private String getPostCommentCommand(int postId, BlogPostComment comment) {
		String commentCommand = "comment " + postId + " " + comment.getUsername() + " " + comment.getComment();
		return commentCommand;
	}
	
	private String sendCommand(String command) {
		write(command);
		return readUntil(PROMPT).trim();
	}
	
	private void write(String value) {
		out.println(value);
		out.flush();
	}

	private String readUntil(String pattern) {
		try {
			char lastChar = pattern.charAt(pattern.length() - 1);
			StringBuffer sb = new StringBuffer();
			char ch = (char) in.read();
			while (true) {
				sb.append(ch);
				if (ch == lastChar) {
					if (sb.toString().endsWith(pattern)) {
						return sb.toString();
					}
				}
				ch = (char) in.read();
			}
		} catch (Exception e) {
			LOG.error(e);
		}
		return null;
	}

}
