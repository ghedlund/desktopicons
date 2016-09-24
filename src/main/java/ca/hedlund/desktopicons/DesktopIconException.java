package ca.hedlund.desktopicons;

public class DesktopIconException extends Exception {

	private static final long serialVersionUID = -8611604112173673565L;

	public DesktopIconException() {
		super();
	}

	public DesktopIconException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public DesktopIconException(String message, Throwable cause) {
		super(message, cause);
	}

	public DesktopIconException(String message) {
		super(message);
	}

	public DesktopIconException(Throwable cause) {
		super(cause);
	}
	
}
