package xlbeans.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * This annotation will be placed on the fields of the bean which will recieve
 * the parameters.
 * 
 * @author Prashant Chaubey created on: Dec 15, 2017
 */
@Documented
@Retention(RUNTIME)
@Target(FIELD)
public @interface ExcelCell {

	/**
	 * It will tell the position of the field in the cell.
	 * 
	 * @return
	 */
	int position();

	/**
	 * It will tell the title of the excel field.<Optional>
	 * 
	 * @return
	 */
	String title() default "";
}
