package org.tmsframework.demo.access;

/**
 * 
 * @author fish
 * 
 */
public class AdminAccessDeniedException extends RuntimeException {

	private static final long serialVersionUID = -4757581999998896852L;

	public AdminAccessDeniedException() {
		super();
	}

	public AdminAccessDeniedException(String message, Throwable cause) {
		super(message, cause);
	}

	public AdminAccessDeniedException(String message) {
		super(message);
	}

	public AdminAccessDeniedException(Throwable cause) {
		super(cause);
	}

}
