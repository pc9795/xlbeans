package xlbeans.util;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;

import xlbeans.MissingCellAction;

/**
 * Utility methods for the library.
 * 
 * @author Prashant Chaubey created on 18-Dec-2017
 */
public class XlBeansUtil {

	/**
	 * To check whether the annotated field's data type is supported by library or
	 * not.
	 * 
	 * @param fieldType
	 * @return
	 */
	public static boolean isPermissibleFieldType(String fieldType) {
		if (XlBeansConstants.PERMISSIBLE_FIELD_TYPES.contains(fieldType)) {
			return true;
		}
		return false;
	}

	/**
	 * Convert over custom enum for missing cell problems to the standard poi one
	 * 
	 * @param action
	 * @return
	 */
	public static MissingCellPolicy getMissingCellPolicy(MissingCellAction action) {
		if (action == null) {
			throw new RuntimeException("Illegal missing cell action recieved<null>");
		}
		switch (action) {
		case CREATE_NULL_AS_BLANK:
			return MissingCellPolicy.CREATE_NULL_AS_BLANK;
		case RETURN_BLANK_AS_NULL:
			return MissingCellPolicy.RETURN_BLANK_AS_NULL;
		case RETURN_NULL_AND_BLANK:
			return MissingCellPolicy.RETURN_NULL_AND_BLANK;
		default:
			throw new RuntimeException("Illegal missing cell action recieved");
		}
	}

	/**
	 * Compare cell type with the permissible field types.
	 * 
	 * @param cellType
	 * @param fieldType
	 * @return
	 */
	public static boolean compareCellTypeEnumWithDataTypeString(CellType cellType, String fieldType) {
		if (cellType == null || fieldType == null) {
			return false;
		}
		switch (cellType) {
		case _NONE:
			return false;
		case BLANK:
			return false;
		case BOOLEAN:
			return (fieldType.equals("boolean") || fieldType.equals("java.lang.Boolean")) ? true : false;
		case ERROR:
			return false;
		case FORMULA:
			return false;
		case NUMERIC:
			return (XlBeansConstants.PERMISSIBLE_NUMERIC_FIELD_TYPES.contains(fieldType)) ? true : false;
		case STRING:
			return (XlBeansConstants.PERMISSIBLE_STRING_TYPES.contains(fieldType)) ? true : false;
		default:
			return false;
		}
	}
}
