package de.fefe.runderpapa.server;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import de.fefe.runderpapa.shared.BlogPost;
import de.fefe.runderpapa.shared.BlogPostComment;
import de.fefe.runderpapa.shared.exceptions.PageScrapingException;

public class FetteMamaScraper {
	
	// private static final Log LOG = LogFactory.getLog(FetteMamaScraper.class);

	private static final String URL = "http://fettemama.org";
	private static final int TIMEOUT = 1000;
	
	private static final String TAG_LINK = "a";
	private static final String TAG_LIST = "ul";
	private static final String TAG_LIST_ENTRY = "li";
	
	private static final String ATTRIBUTE_HREF = "href";

	public int getMaxPostId() throws MalformedURLException, IOException, PageScrapingException {
		Document mainPage = Jsoup.parse(new URL(URL), TIMEOUT);
		for (Element element : mainPage.getElementsByTag(TAG_LINK)) {
			if (element.attr(ATTRIBUTE_HREF).contains("=")) {
				String href = element.attr(ATTRIBUTE_HREF);
				return Integer.parseInt(href.substring(href.indexOf("=") + 1)); 
			}
		}
		
		throw new PageScrapingException("I failed @ finding link with maxPostId :(");
	}
	
	public List<BlogPost> getBlogPosts() throws MalformedURLException, IOException, PageScrapingException {
		List<BlogPost> posts = new LinkedList<BlogPost>();
		for (int postId = getMaxPostId(); postId > 0; --postId) {
			posts.add(getBlogPost(postId));
		}
		return posts;
	}
	
	public BlogPost getBlogPost(int postId) throws MalformedURLException, IOException {
		Document postPage = Jsoup.parse(getPostUrl(postId), TIMEOUT);
		String postText = getPostText(postPage);
		List<BlogPostComment> comments = getPostComments(postPage);
		
		return new BlogPost(postId, postText, comments);
	}

	private String getPostText(Document postPage) {
		Element postElement = postPage.getElementsByTag(TAG_LIST).get(0).getElementsByTag(TAG_LIST_ENTRY).get(0);
		Node[] nodes = postElement.childNodes().toArray(new Node[0]);
		String postText = "";
		for (int i = 2; i < nodes.length; ++i) {
			postText += nodes[i].toString();
		}
		return postText.trim();
	}
	
	private List<BlogPostComment> getPostComments(Document postPage) {
		List<BlogPostComment> comments = new LinkedList<BlogPostComment>();
		Elements elements = postPage.getElementsByTag(TAG_LIST).get(2).getElementsByTag(TAG_LIST_ENTRY);
		for (Element element : elements) {
			String commentString = element.html();
			int usernameEnd = commentString.indexOf("]");
			String username = commentString.substring(1, usernameEnd);
			String commentText = commentString.substring(usernameEnd + 1);
			comments.add(new BlogPostComment(username.trim(), commentText.trim()));
		}
		return comments;
	}
	
	private URL getPostUrl(int postId) throws MalformedURLException {
		URL result = new URL(URL + "/post?id=" + postId);
		return result;
	}
	
}
