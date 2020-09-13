package com.prashantchaubey.xlbeans;

import java.util.List;

/**
 * This class will specify which type of list implementation to use while collecting excel beans.
 */
public interface ListGenerator<T> {

  List<T> generate();
}
