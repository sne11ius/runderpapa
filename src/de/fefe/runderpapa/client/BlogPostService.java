package de.fefe.runderpapa.client;

import java.io.IOException;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.fefe.runderpapa.shared.BlogPost;
import de.fefe.runderpapa.shared.BlogPostComment;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("BlogPostService")
public interface BlogPostService extends RemoteService {
	int getMaxPostId() throws IOException;
	List<BlogPost> getPosts() throws IllegalArgumentException, IOException;
	BlogPost getPost(int index) throws IOException;
	List<Integer> searchPosts(String text) throws IOException;
	void addComment(int postId, BlogPostComment comment);
}
