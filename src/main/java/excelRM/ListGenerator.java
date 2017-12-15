package excelRM;

import java.util.List;

/**
 * This class will specify which type of list implementation to use while
 * collecting excel beans.
 * 
 * @author Prashant Chaubey created on: Dec 15, 2017
 */
public interface ListGenerator {

	/**
	 * This method will provide the necessary method to generate the list
	 * implementation.
	 * 
	 * @return
	 */
	List<Object> generate();
}
