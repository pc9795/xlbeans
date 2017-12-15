package xlbeans.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * This annotation will disable the fields for the provided bean during
 * extraction of data from excel sheet.<Marker Annotation>
 * 
 * @author Prashant Chaubey created on: Dec 15, 2017
 */
@Documented
@Retention(RUNTIME)
@Target(FIELD)
public @interface Disabled {

}
