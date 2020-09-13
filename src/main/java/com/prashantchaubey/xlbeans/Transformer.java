package com.prashantchaubey.xlbeans;

/**
 * Functional interface which will provide a method to transform the extracted java objects from the
 * excel file.
 */
public interface Transformer<T> {

  T transform(T xlBean);
}
