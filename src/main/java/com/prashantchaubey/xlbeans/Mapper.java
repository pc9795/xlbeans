package com.prashantchaubey.xlbeans;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.prashantchaubey.xlbeans.annotations.Disabled;
import com.prashantchaubey.xlbeans.annotations.XlCell;
import com.prashantchaubey.xlbeans.beans.XlCellInfo;
import com.prashantchaubey.xlbeans.exception.XlBeansException;
import com.prashantchaubey.xlbeans.exception.XlBeansIllegalExcelBeanException;
import com.prashantchaubey.xlbeans.exception.XlBeansIllegalFileFormatException;
import com.prashantchaubey.xlbeans.util.XlBeansUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/** Main class to map excel sheet into java objects. */
public final class Mapper<T> implements AutoCloseable {
  private Workbook xlWorkbook;
  private MappingOptions mappingOptions;
  private Transformer<T> transformer;
  private Map<Integer, XlCellInfo> xlDataTypeMap = new HashMap<>();
  private Class<T> clazz;

  public List<T> map(Path xlFile, Class<T> clazz)
      throws XlBeansIllegalFileFormatException, XlBeansIllegalExcelBeanException, XlBeansException {
    return _map(xlFile, clazz, null, null);
  }

  public List<T> map(Path xlFile, Class<T> clazz, MappingOptions mappingOptions)
      throws XlBeansIllegalFileFormatException, XlBeansIllegalExcelBeanException, XlBeansException {
    return _map(xlFile, clazz, mappingOptions, null);
  }

  public List<T> map(Path xlFile, Class<T> clazz, Transformer<T> transformer)
      throws XlBeansIllegalFileFormatException, XlBeansIllegalExcelBeanException, XlBeansException {
    return _map(xlFile, clazz, null, transformer);
  }

  public List<T> map(
      Path xlFile, Class<T> clazz, MappingOptions mappingOptions, Transformer<T> transformer)
      throws XlBeansIllegalExcelBeanException, XlBeansException, XlBeansIllegalFileFormatException {
    return _map(xlFile, clazz, mappingOptions, transformer);
  }

  private List<T> _map(
      Path xlFile, Class<T> clazz, MappingOptions mappingOptions, Transformer<T> transformer)
      throws XlBeansIllegalFileFormatException, XlBeansIllegalExcelBeanException, XlBeansException {
    validateAndConfigure(xlFile, clazz, mappingOptions, transformer);
    createMapForXlBeanTypes(clazz);
    return processXlSheet();
  }

  private void validateAndConfigure(
      Path xlFile, Class<T> clazz, MappingOptions mappingOptions, Transformer<T> transformer)
      throws XlBeansIllegalFileFormatException, XlBeansException {
    if (xlFile == null) {
      throw new XlBeansIllegalFileFormatException("Null object received");
    }
    if (!Files.exists(xlFile)) {
      throw new XlBeansIllegalFileFormatException("File doesn't exists");
    }
    if (!xlFile.toString().endsWith(".xlsx") && !xlFile.toString().endsWith(".xls")) {
      throw new XlBeansIllegalFileFormatException("Not a valid file(.xls or .xlsx)");
    }
    try {
      if (xlFile.toString().endsWith(".xls")) {
        this.xlWorkbook = new HSSFWorkbook(Files.newInputStream(xlFile));
      } else if (xlFile.toString().endsWith(".xlsx")) {
        this.xlWorkbook = new XSSFWorkbook(Files.newInputStream(xlFile));
      }
    } catch (IOException e) {
      throw new XlBeansIllegalFileFormatException(
          "Could not able to get the workbook from the excel file", e);
    }
    if (clazz == null) {
      throw new XlBeansException("Cannot supply a null value for class object");
    }
    this.clazz = clazz;
    this.mappingOptions = mappingOptions;
    this.transformer = transformer;
  }

  private void createMapForXlBeanTypes(Class<? super T> clazz)
      throws XlBeansIllegalExcelBeanException {
    if (clazz == null) {
      return;
    }
    Field[] fields = clazz.getDeclaredFields();
    for (Field field : fields) {
      field.setAccessible(true);
      Disabled disabled = field.getAnnotation(Disabled.class);
      if (disabled != null) {
        continue;
      }
      XlCell cell = field.getAnnotation(XlCell.class);
      if (cell == null) {
        continue;
      }
      Class<?> fieldClazz = field.getType();
      if (XlBeansUtils.isPermissibleFieldType(fieldClazz.getName())) {
        XlCellInfo info = new XlCellInfo();
        info.setPosition(cell.position());
        info.setTitle(cell.title());
        info.setField(field);
        info.setFieldName(field.getName());
        if (xlDataTypeMap.containsKey(cell.position())) {
          throw new XlBeansIllegalExcelBeanException("Dupliate positions found");
        } else {
          xlDataTypeMap.put(cell.position(), info);
        }
      } else {
        throw new XlBeansIllegalExcelBeanException(field.getType() + " is not supported");
      }
    }
    createMapForXlBeanTypes(clazz.getSuperclass());
  }

  private List<T> processXlSheet() throws XlBeansIllegalFileFormatException, XlBeansException {
    try {
      int sheetNo = 0;
      List<T> beansList = new ArrayList<>();
      int x = 0;
      int y = 0;
      MissingCellPolicy missingCellPolicy = MissingCellPolicy.RETURN_BLANK_AS_NULL;
      boolean compareTitles;
      if (mappingOptions != null) {
        sheetNo = mappingOptions.getTargetSheet();
        beansList = mappingOptions.getGenerator().generate();
        x = mappingOptions.getStartingPosition().getX();
        y = mappingOptions.getStartingPosition().getY();
        missingCellPolicy =
            XlBeansUtils.getMissingCellPolicy(mappingOptions.getMissingCellAction());
      }
      if (x < 0 || y < 0) {
        throw new XlBeansException("Invalid index to start parsing file(" + x + "," + y + ")");
      }

      if (sheetNo < 0) {
        throw new XlBeansException("Invalid sheet index(" + sheetNo + ")");
      }

      this.xlWorkbook.setMissingCellPolicy(missingCellPolicy);
      Sheet sheet = this.xlWorkbook.getSheetAt(sheetNo);

      // todo implement
      compareTitles = false;
      int max = getMaximumKeyFromMap();
      if (compareTitles) {
        if (y == 0) {
          throw new XlBeansIllegalFileFormatException(
              "Cannot compare titles if the data is in the first row");
        }
        Row titleRow = sheet.getRow(y - 1);

        if (max > titleRow.getLastCellNum()) {
          throw new XlBeansIllegalFileFormatException(
              "Last cell number can't be smaller than maximum configured position");
        }
        for (int i = x; i < max; i++) {
          if (!this.xlDataTypeMap.containsKey(i)) {
            continue;
          }
          CellType type = titleRow.getCell(i).getCellTypeEnum();
          if (type != CellType.STRING) {
            throw new XlBeansIllegalFileFormatException(
                "Title at (" + i + "," + (y - 1) + ") is not a string");
          }
          String title = titleRow.getCell(i).getStringCellValue();
          if (!title.equals(this.xlDataTypeMap.get(i).getTitle())) {
            throw new XlBeansIllegalFileFormatException(
                "At ("
                    + i
                    + ","
                    + (y - 1)
                    + ") Expected:"
                    + this.xlDataTypeMap.get(i).getTitle()
                    + ",Found:"
                    + title);
          }
        }
      }

      int rowIndex = 0;
      for (Row row : sheet) {
        if (rowIndex < y) {
          continue;
        }
        T obj = clazz.newInstance();
        for (int i = x; i <= max; i++) {
          if (!this.xlDataTypeMap.containsKey(i)) {
            continue;
          }
          XlCellInfo info = this.xlDataTypeMap.get(i);
          Cell cell = row.getCell(i);
          Optional<Object> value =
              XlBeansUtils.extractDataFromCellUsingFieldType(
                  cell, info.getField().getType().getName());
          if (!value.isPresent()) {
            throw new XlBeansException(
                "xlBean configuration and xlSheet format not match at("
                    + i
                    + ","
                    + rowIndex
                    + ")<Wanted:"
                    + info.getField().getType().getName()
                    + ",Found:"
                    + cell.getCellTypeEnum()
                    + ">");
          }

          info.getField().set(obj, value.get());

          // todo implement
          if (this.transformer != null) {
            obj = this.transformer.transform(obj);
          }
        }
        beansList.add(obj);
        rowIndex++;
      }
      return beansList;
    } catch (InstantiationException | IllegalAccessException e) {
      throw new XlBeansException(e);
    }
  }

  private int getMaximumKeyFromMap() {
    Set<Integer> keys = this.xlDataTypeMap.keySet();
    int max = Integer.MIN_VALUE;
    for (Integer key : keys) {
      if (key > max) {
        max = key;
      }
    }
    return max;
  }

  @Override
  public void close() throws IOException {
    if (this.xlWorkbook != null) {
      this.xlWorkbook.close();
    }
  }
}
