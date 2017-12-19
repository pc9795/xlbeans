package xlbeans.exception;

/**
 * Exception to send when xlbeans are not properly configured.
 * 
 * @author Prashant Chaubey created on 18-Dec-2017
 */
public class XlBeansIllegalExcelBeanException extends Exception {

	private static final long serialVersionUID = 1L;

	public XlBeansIllegalExcelBeanException() {
		super();
	}

	public XlBeansIllegalExcelBeanException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public XlBeansIllegalExcelBeanException(String message, Throwable cause) {
		super(message, cause);
	}

	public XlBeansIllegalExcelBeanException(String message) {
		super(message);
	}

	public XlBeansIllegalExcelBeanException(Throwable cause) {
		super(cause);
	}

}
