package xlbeans;

/**
 * An enum comparable to MissingCellPolicy of apache poi so that user don't have
 * to use poi for this enum only.
 * 
 * @author Prashant Chaubey created on 19-Dec-2017
 */
public enum MissingCellAction {

	CREATE_NULL_AS_BLANK, RETURN_BLANK_AS_NULL, RETURN_NULL_AND_BLANK

}
