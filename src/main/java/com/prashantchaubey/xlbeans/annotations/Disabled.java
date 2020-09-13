package com.prashantchaubey.xlbeans.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * This annotation will disable the fields for the provided bean during extraction of data from
 * excel sheet.
 */
@Documented
@Retention(RUNTIME)
@Target(FIELD)
public @interface Disabled {}
