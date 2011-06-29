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
	
	//private static final String BLACKLIST_FILENAME = "blacklist";
	private static final int MIN_STRING_DISTANCE = 2;
	private static final String PREFORMAT_REGEX = "[^\\w \\xC0-\\xFF]";
	
	public static boolean isCommentAllowed(InputStream blacklistFile, final BlogPostComment comment) throws IOException {
		if (null == comment.getUsername() || null == comment.getComment()) {
			return false;
		}		
		final String username = comment.getUsername().trim().toLowerCase();
		final String commentString = preformatComment(comment.getComment()).toLowerCase();

		for (String entry : getBlacklist(blacklistFile)) {
			if (StringUtils.getLevenshteinDistance(username, entry.toLowerCase()) < MIN_STRING_DISTANCE) {
				LOG.info("Username `" + username + "' is blacklisted because of `" + entry.toLowerCase() + "'.");
				return false;
			}
			for (String word : Arrays.asList(commentString.split("\\s"))) {
				if (StringUtils.getLevenshteinDistance(word.trim(), entry.toLowerCase()) < MIN_STRING_DISTANCE) {
					LOG.info("Word `" + word.trim() + "' in comment is blacklisted because of `" + entry.toLowerCase() + "'.");
					return false;
				}
			}
		}
		
		return true;
	}

	private static String preformatComment(String comment) {
		return comment.replaceAll(PREFORMAT_REGEX, " ");
	}
	
	private static List<String> getBlacklist(InputStream blacklistStream) throws IOException {
		StringWriter writer = new StringWriter();
		IOUtils.copy(blacklistStream, writer);
		return Arrays.asList(writer.toString().split("\\s"));
	}

}
