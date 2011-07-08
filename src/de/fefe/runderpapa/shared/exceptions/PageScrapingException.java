package de.fefe.runderpapa.shared.exceptions;

public class PageScrapingException extends Exception {

	private static final long serialVersionUID = 1L;

	public PageScrapingException() {
	}

	public PageScrapingException(String message) {
		super(message);
	}

	public PageScrapingException(Throwable cause) {
		super(cause);
	}

	public PageScrapingException(String message, Throwable cause) {
		super(message, cause);
	}

}
