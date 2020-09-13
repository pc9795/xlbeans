package com.prashantchaubey.xlbeans;

/**
 * An enum comparable to MissingCellPolicy of apache poi so that user don't have to use poi for this
 * enum only.
 */
public enum MissingCellAction {
  CREATE_NULL_AS_BLANK,
  RETURN_BLANK_AS_NULL,
  RETURN_NULL_AND_BLANK
}
