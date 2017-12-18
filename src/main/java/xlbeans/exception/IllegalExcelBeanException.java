package xlbeans.exception;

/**
 * Exception to send when xlbeans are not properly configured.
 * 
 * @author Prashant Chaubey created on 18-Dec-2017
 */
public class IllegalExcelBeanException extends Exception {

	private static final long serialVersionUID = 1L;

	public IllegalExcelBeanException() {
		super();
	}

	public IllegalExcelBeanException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public IllegalExcelBeanException(String message, Throwable cause) {
		super(message, cause);
	}

	public IllegalExcelBeanException(String message) {
		super(message);
	}

	public IllegalExcelBeanException(Throwable cause) {
		super(cause);
	}

}
