package de.fefe.runderpapa.server;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.fefe.runderpapa.client.BlogPostService;
import de.fefe.runderpapa.shared.BlogPost;
import de.fefe.runderpapa.shared.BlogPostComment;

/**
 * Teh service.
 */
@SuppressWarnings("serial")
public class BlogPostServiceImpl extends RemoteServiceServlet implements BlogPostService {
	private static final String BLACKLIST_FILENAME_INIT_PARAM = "blacklistFilename";
	private static final Log LOG = LogFactory.getLog(BlogPostServiceImpl.class);

	@Override
	public int getMaxPostId() throws IOException {
		return new FetteMamaScraper().getMaxPostId();
	}

	@Override
	public List<BlogPost> getPosts() throws IOException {
		return new FetteMamaScraper().getBlogPosts();
	}

	@Override
	public BlogPost getPost(int postId) throws IOException {
		return new FetteMamaScraper().getBlogPost(postId);
	}

	@Override
	public void addComment(int postId, BlogPostComment comment) {
		logAddComment(postId, comment);
		try {
			if (!CommentBlacklistManager.isCommentAllowed(getServletContext().getInitParameter(BLACKLIST_FILENAME_INIT_PARAM), comment)) {
				return;
			}
			new FetteMamaTelnetClient().addComment(postId, comment);
		} catch (Exception exception) {
			LOG.error(exception);
		}
	}

	@Override
	public List<Integer> searchPosts(String text) throws IOException {
		logSearchPosts(text);
		List<Integer> result = new LinkedList<Integer>();
		List<BlogPost> allPosts = new FetteMamaScraper().getBlogPosts();
		
		for (BlogPost post : allPosts) {
			if (post.getText().contains(text)) {
				result.add(post.getPostId());
			}
		}
		
		return result;
	}
	
	private void logAddComment(int postId, BlogPostComment comment) {
		String remoteAddress = getThreadLocalRequest().getRemoteAddr();
		LOG.info("User `" + comment.getUsername() + "' (from " + remoteAddress + ") wants to comment on post #" + postId + ". Comment: `" + comment.getComment() + "'");
	}

	private void logSearchPosts(String text) {
		String remoteAddress = getThreadLocalRequest().getRemoteAddr();
		LOG.info(remoteAddress + " is searching for `" + text + "'");
	}
}
