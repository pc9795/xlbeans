package xlbeans.exception;

/**
 * General exception class.
 * 
 * @author Prashant Chaubey created on 19-Dec-2017
 */
public class XLBeansException extends Exception {

	private static final long serialVersionUID = 1L;

	public XLBeansException() {
		super();
	}

	public XLBeansException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public XLBeansException(String message, Throwable cause) {
		super(message, cause);
	}

	public XLBeansException(String message) {
		super(message);
	}

	public XLBeansException(Throwable cause) {
		super(cause);
	}

}
