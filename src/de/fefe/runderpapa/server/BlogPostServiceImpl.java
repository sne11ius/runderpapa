package de.fefe.runderpapa.server;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.fefe.runderpapa.client.BlogPostService;
import de.fefe.runderpapa.shared.BlogPost;
import de.fefe.runderpapa.shared.BlogPostComment;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class BlogPostServiceImpl extends RemoteServiceServlet implements BlogPostService {
	
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
		try {
			new FetteMamaTelnetClient().addComment(postId, comment);
		} catch (Exception e) {
			LOG.error(e);
		}
	}
}
