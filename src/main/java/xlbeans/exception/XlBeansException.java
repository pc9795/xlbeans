package xlbeans.exception;

/**
 * General exception class.
 * 
 * @author Prashant Chaubey created on 19-Dec-2017
 */
public class XlBeansException extends Exception {

	private static final long serialVersionUID = 1L;

	public XlBeansException() {
		super();
	}

	public XlBeansException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public XlBeansException(String message, Throwable cause) {
		super(message, cause);
	}

	public XlBeansException(String message) {
		super(message);
	}

	public XlBeansException(Throwable cause) {
		super(cause);
	}

}
