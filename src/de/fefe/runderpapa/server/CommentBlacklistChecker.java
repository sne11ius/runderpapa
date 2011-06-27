package de.fefe.runderpapa.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.fefe.runderpapa.shared.BlogPostComment;

public class CommentBlacklistChecker {
	
	private static final Log LOG = LogFactory.getLog(CommentBlacklistChecker.class);
	
	private static final String BLACKLIST_FILENAME = "blacklist";
	private static final int MIN_STRING_DISTANCE = 2;
	private static final String PREFORMAT_REGEX = "[^\\w \\xC0-\\xFF]";
	
	public static boolean isCommentAllowed(final BlogPostComment comment) throws IOException {
		if (null == comment.getUsername() || null == comment.getComment()) {
			return false;
		}		
		final String username = comment.getUsername().trim();
		final String commentString = preformatComment(comment.getComment());

		for (String entry : getBlacklist()) {
			if (StringUtils.getLevenshteinDistance(username, entry) < MIN_STRING_DISTANCE) {
				LOG.info("Username `" + username + "' is blacklisted because of `" + entry + "'.");
				return false;
			}
			for (String word : Arrays.asList(commentString.split("\\s"))) {
				if (StringUtils.getLevenshteinDistance(word.trim(), entry) < MIN_STRING_DISTANCE) {
					LOG.info("Word `" + word.trim() + "' in comment is blacklisted because of `" + entry + "'.");
					return false;
				}
			}
		}
		
		return true;
	}

	private static String preformatComment(String comment) {
		return comment.replaceAll(PREFORMAT_REGEX, " ");
	}
	
	private static List<String> getBlacklist() throws IOException {
		LOG.info("Loading blacklist...");
		InputStream blacklistStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(BLACKLIST_FILENAME);
		StringWriter writer = new StringWriter();
		IOUtils.copy(blacklistStream, writer);
		return Arrays.asList(writer.toString().split("\\s"));
	}
}
