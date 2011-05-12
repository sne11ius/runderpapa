package de.fefe.runderpapa.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.fefe.runderpapa.shared.BlogPost;
import de.fefe.runderpapa.shared.BlogPostComment;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface BlogPostServiceAsync {
	
	void getPosts(AsyncCallback<List<BlogPost>> callback);

	void getPost(int index, AsyncCallback<BlogPost> callback);

	void addComment(int postId, BlogPostComment comment, AsyncCallback<Void> callback);

	void getMaxPostId(AsyncCallback<Integer> callback);
	
}
