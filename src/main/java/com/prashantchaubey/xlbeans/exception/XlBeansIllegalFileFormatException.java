package com.prashantchaubey.xlbeans.exception;

public class XlBeansIllegalFileFormatException extends RuntimeException {

  public XlBeansIllegalFileFormatException(String message, Throwable cause) {
    super(message, cause);
  }

  public XlBeansIllegalFileFormatException(String message) {
    super(message);
  }
}
