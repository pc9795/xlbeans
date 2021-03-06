package com.prashantchaubey.xlbeans.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/** This annotation will be placed on the fields of the bean which will receive the parameters. */
@Documented
@Retention(RUNTIME)
@Target(FIELD)
public @interface XlCell {

  /** It will tell the position of the field in the cell. */
  int position();

  /** It will tell the title of the excel field. */
  String title() default "";
}
