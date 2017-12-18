package xlbeans.util;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;

import xlbeans.MissingCellAction;

/**
 * Utility methods for the library.
 * 
 * @author Prashant Chaubey created on 18-Dec-2017
 */
public class Util {

	/**
	 * To check whether the annotated field's data type is supported by library or
	 * not.
	 * 
	 * @param fieldType
	 * @return
	 */
	public static boolean isPermissibleFieldType(String fieldType) {
		if (Constants.permissibleFieldTypes.contains(fieldType)) {
			return true;
		}
		return false;
	}

	public static MissingCellPolicy getMissingCellPolicy(MissingCellAction action) {
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

	public static boolean compareCellTypeEnumWithDataTypeString(CellType cellType, String fieldType) {
		switch (cellType) {
		case _NONE:
			return false;
		case BLANK:
			return false;
		case BOOLEAN:
			return (fieldType.equals("boolean") || fieldType.equals("Boolean")) ? true : false;
		case ERROR:
			return false;
		case FORMULA:
			return false;
		case NUMERIC:
			return (fieldType.equals("byte") || fieldType.equals("Byte") || fieldType.equals("short")
					|| fieldType.equals("Short") || fieldType.equals("int") || fieldType.equals("Integer")
					|| fieldType.equals("long") || fieldType.equals("Long") || fieldType.equals("float")
					|| fieldType.equals("Float") || fieldType.equals("double") || fieldType.equals("Double")) ? true
							: false;
		case STRING:
			return (fieldType.equals("java.util.String") || fieldType.equals("java.util.Date")
					|| fieldType.equals("java.time.LocalDate")) ? true : false;
		default:
			return false;
		}
	}
}
