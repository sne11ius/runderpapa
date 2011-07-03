package de.fefe.runderpapa.server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
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

public class CommentBlacklistManager {
	
	private static final Log LOG = LogFactory.getLog(CommentBlacklistManager.class);

	private static final int MIN_STRING_DISTANCE = 2;
	private static final String PREFORMAT_REGEX = "[^\\w \\xC0-\\xFF]";
	
	public static boolean isCommentAllowed(String blacklistFilename, final BlogPostComment comment) throws IOException {
		if (null == comment.getUsername() || null == comment.getComment()) {
			return false;
		}		
		final String username = comment.getUsername().trim().toLowerCase();
		final String commentString = preformatComment(comment.getComment()).toLowerCase();

		for (String entry : getBlacklist(blacklistFilename)) {
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
	
	public static List<String> getBlacklist(String blacklistFilename) throws IOException {
		StringWriter writer = new StringWriter();
		InputStream blacklistStream = openBlacklist(blacklistFilename);
		IOUtils.copy(blacklistStream, writer);
		blacklistStream.close();
		return Arrays.asList(writer.toString().split("\\s"));
	}
	
	public static void writeBlacklist(List<String> blacklist, String blacklistFilename) throws IOException {
		BufferedWriter blacklistWriter = new BufferedWriter(new FileWriter(new File(blacklistFilename)));
		for (String entry : blacklist) {
			blacklistWriter.write(entry.trim() + System.getProperty("line.separator"));
		}
		blacklistWriter.close();
	}

	public static InputStream openBlacklist(String blacklistFilename) throws FileNotFoundException {
		File blacklistFile = new File(blacklistFilename);
		return new FileInputStream(blacklistFile);
	}

}
