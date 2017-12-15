package excelRM;

/**
 * Functional interface which will provide a method to transform the extracted
 * java objects from the excel file.
 * 
 * @author Prashant Chaubey created on: Dec 15, 2017
 */
public interface Transformer<T> {

	/**
	 * Method which will provide the logic to transform the extracted objects. It is
	 * supposed to return the same object reference after executing the necessary
	 * logic.
	 * 
	 * @param excelBean
	 * @return
	 */
	T transform(T excelBean);
}
