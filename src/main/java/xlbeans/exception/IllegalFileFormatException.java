package xlbeans.exception;

/**
 * Exception to send when apache poi could not able to parse workbook from the
 * excel file given.
 * 
 * @author Prashant Chaubey created on 18-Dec-2017
 */
public class IllegalFileFormatException extends Exception {

	private static final long serialVersionUID = 1L;

	public IllegalFileFormatException() {
		super();
	}

	public IllegalFileFormatException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public IllegalFileFormatException(String message, Throwable cause) {
		super(message, cause);
	}

	public IllegalFileFormatException(String message) {
		super(message);
	}

	public IllegalFileFormatException(Throwable cause) {
		super(cause);
	}

}
