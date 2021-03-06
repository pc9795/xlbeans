package com.prashantchaubey.xlbeans.util;

import java.util.Optional;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;

import com.prashantchaubey.xlbeans.MissingCellAction;

public final class XlBeansUtils {

  private XlBeansUtils() {
    throw new IllegalStateException("This should never be constructed");
  }

  public static boolean isPermissibleFieldType(String fieldType) {
    return XlBeansConstants.PERMISSIBLE_FIELD_TYPES.contains(fieldType);
  }

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

  @Deprecated
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
        return fieldType.equals("boolean") || fieldType.equals("java.lang.Boolean");
      case ERROR:
        return false;
      case FORMULA:
        return false;
      case NUMERIC:
        return XlBeansConstants.PERMISSIBLE_NUMERIC_FIELD_TYPES.contains(fieldType);
      case STRING:
        return XlBeansConstants.PERMISSIBLE_STRING_TYPES.contains(fieldType);
      default:
        return false;
    }
  }

  public static Optional<Object> extractDataFromCellUsingFieldType(Cell cell, String fieldType) {
    if (cell == null || fieldType == null) {
      return Optional.empty();
    }
    switch (cell.getCellTypeEnum()) {
      case _NONE:
        return Optional.empty();
      case BLANK:
        return Optional.empty();
      case BOOLEAN:
        return (fieldType.equals("boolean") || fieldType.equals("java.lang.Boolean"))
            ? Optional.of(getFieldValue(fieldType, cell))
            : Optional.empty();
      case ERROR:
        return Optional.empty();
      case FORMULA:
        return Optional.empty();
      case NUMERIC:
        return (XlBeansConstants.PERMISSIBLE_NUMERIC_FIELD_TYPES.contains(fieldType))
            ? Optional.of(getFieldValue(fieldType, cell))
            : Optional.empty();
      case STRING:
        return (XlBeansConstants.PERMISSIBLE_STRING_TYPES.contains(fieldType))
            ? Optional.of(getFieldValue(fieldType, cell))
            : Optional.empty();
      default:
        return Optional.empty();
    }
  }

  private static Object getFieldValue(String fieldType, Cell cell) {
    switch (fieldType) {
      case "int":
      case "java.lang.Integer":
        return (int) cell.getNumericCellValue();
      case "byte":
      case "java.lang.Byte":
        return (int) cell.getNumericCellValue();
      case "short":
      case "Short":
        return (short) cell.getNumericCellValue();
      case "long":
      case "java.lang.Long":
        return (long) cell.getNumericCellValue();
      case "float":
      case "java.lang.Float":
        return (float) cell.getNumericCellValue();
      case "double":
      case "java.lang.Double":
        return cell.getNumericCellValue();
      case "char":
      case "java.lang.Character":
        return cell.getStringCellValue().charAt(0);
      case "java.lang.String":
        return cell.getStringCellValue();
      case "java.lang.Boolean":
      case "boolean":
        return cell.getBooleanCellValue();
      default:
        throw new RuntimeException("Unsupported field type");
    }
  }
}
