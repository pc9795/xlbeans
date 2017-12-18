package xlbeans.exception;

/**
 * Exception to send when file format is not right.
 * 
 * @author Prashant Chaubey created on 18-Dec-2017
 */
public class IllegalFileException extends Exception {

	private static final long serialVersionUID = 1L;

	public IllegalFileException() {
		super();
	}

	public IllegalFileException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public IllegalFileException(String message, Throwable cause) {
		super(message, cause);
	}

	public IllegalFileException(String message) {
		super(message);
	}

	public IllegalFileException(Throwable cause) {
		super(cause);
	}

}
