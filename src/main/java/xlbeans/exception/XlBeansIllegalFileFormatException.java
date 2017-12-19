package xlbeans.exception;

/**
 * Exception to send when apache poi could not able to parse workbook from the
 * excel file given.
 * 
 * @author Prashant Chaubey created on 18-Dec-2017
 */
public class XlBeansIllegalFileFormatException extends Exception {

	private static final long serialVersionUID = 1L;

	public XlBeansIllegalFileFormatException() {
		super();
	}

	public XlBeansIllegalFileFormatException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public XlBeansIllegalFileFormatException(String message, Throwable cause) {
		super(message, cause);
	}

	public XlBeansIllegalFileFormatException(String message) {
		super(message);
	}

	public XlBeansIllegalFileFormatException(Throwable cause) {
		super(cause);
	}

}
