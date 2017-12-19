package xlbeans;

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

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import xlbeans.annotations.Disabled;
import xlbeans.annotations.XlCell;
import xlbeans.beans.XlCellInfo;
import xlbeans.exception.XLBeansException;
import xlbeans.exception.XlBeansIllegalExcelBeanException;
import xlbeans.exception.XlBeansIllegalFileFormatException;
import xlbeans.util.XlBeansUtil;

/**
 * Main class to map excel sheet into java objects.
 * 
 * @author Prashant Chaubey created on: Dec 15, 2017
 */
public final class Mapper<T> implements AutoCloseable {
	private Workbook xlWorkbook;
	private Optional<MappingOptions> mappingOptions;
	private Optional<Transformer<T>> transformer;
	private Map<Integer, XlCellInfo> xlDataTypeMap = new HashMap<>();
	private Class<T> clazz;

	/**
	 * Method exposed to generate xlbeans from give excelFile.
	 * 
	 * @param xlFile
	 * @param clazz
	 * @throws XlBeansIllegalFileFormatException
	 * @throws XlBeansIllegalFileFormatException
	 * @throws XlBeansIllegalExcelBeanException
	 * @throws XLBeansException
	 */
	public List<T> map(Path xlFile, Class<T> clazz)
			throws XlBeansIllegalFileFormatException, XlBeansIllegalExcelBeanException, XLBeansException {
		return _map(xlFile, clazz, null, null);
	}

	/**
	 * Method exposed to generate xlbeans from give excelFile.
	 * 
	 * @param xlFile
	 * @param clazz
	 * @param mappingOptions
	 * @throws XlBeansIllegalFileFormatException
	 * @throws XlBeansIllegalFileFormatException
	 * @throws XlBeansIllegalExcelBeanException
	 * @throws XLBeansException
	 */
	public List<T> map(Path xlFile, Class<T> clazz, MappingOptions mappingOptions)
			throws XlBeansIllegalFileFormatException, XlBeansIllegalExcelBeanException, XLBeansException {
		return _map(xlFile, clazz, mappingOptions, null);
	}

	/**
	 * Method exposed to generate xlbeans from give excelFile.
	 * 
	 * @param xlFile
	 * @param clazz
	 * @param transformer
	 * @throws XlBeansIllegalFileFormatException
	 * @throws XlBeansIllegalFileFormatException
	 * @throws XlBeansIllegalExcelBeanException
	 * @throws XLBeansException
	 */
	public List<T> map(Path xlFile, Class<T> clazz, Transformer<T> transformer)
			throws XlBeansIllegalFileFormatException, XlBeansIllegalExcelBeanException, XLBeansException {
		return _map(xlFile, clazz, null, transformer);
	}

	/**
	 * Method exposed to generate xlbeans from give excelFile.
	 * 
	 * @param xlFile
	 * @param clazz
	 * @param mappingOptions
	 * @param transformer
	 * @throws XlBeansIllegalFileFormatException
	 * @throws XlBeansIllegalFileFormatException
	 * @throws XlBeansIllegalExcelBeanException
	 * @throws XLBeansException
	 */
	public List<T> map(Path xlFile, Class<T> clazz, MappingOptions mappingOptions, Transformer<T> transformer)
			throws XlBeansIllegalExcelBeanException, XLBeansException, XlBeansIllegalFileFormatException {
		return _map(xlFile, clazz, mappingOptions, transformer);
	}

	/**
	 * driver method which will process the excelFile.
	 * 
	 * @param xlFile
	 * @param clazz
	 * @param mappingOptions
	 * @param transformer
	 * @throws XlBeansIllegalFileFormatException
	 * @throws XlBeansIllegalFileFormatException
	 * @throws XlBeansIllegalExcelBeanException
	 * @throws XLBeansException
	 */
	private List<T> _map(Path xlFile, Class<T> clazz, MappingOptions mappingOptions, Transformer<T> transformer)
			throws XlBeansIllegalFileFormatException, XlBeansIllegalExcelBeanException, XLBeansException {
		validateAndConfigure(xlFile, clazz, mappingOptions, transformer);
		createMapForXlBeanTypes(clazz);
		return processExcelSheet();
	}

	/**
	 * Necessary validations before processing the file.
	 * 
	 * @param xlFile
	 * @param clazz
	 * @param mappingOptions
	 * @param transformer
	 * @throws XlBeansIllegalFileFormatException
	 * @throws XLBeansException
	 */
	private void validateAndConfigure(Path xlFile, Class<T> clazz, MappingOptions mappingOptions,
			Transformer<T> transformer) throws XlBeansIllegalFileFormatException, XLBeansException {
		if (xlFile == null) {
			throw new XlBeansIllegalFileFormatException("Null object recieved");
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
			throw new XlBeansIllegalFileFormatException("Could not able to get the workbook from the excel file", e);
		}
		if (clazz == null) {
			throw new XLBeansException("Cannot supply a null value for class object");
		}
		if (mappingOptions != null) {
			this.mappingOptions = Optional.of(mappingOptions);
		} else {
			this.mappingOptions = Optional.empty();
		}
		if (transformer != null) {
			this.transformer = Optional.of(transformer);
		} else {
			this.transformer = Optional.empty();
		}

	}

	/**
	 * Generate a map which contain the data of excel-cell to java data types
	 * mapping.
	 * 
	 * @param clazz
	 * @throws XlBeansIllegalExcelBeanException
	 */
	private void createMapForXlBeanTypes(Class<? super T> clazz) throws XlBeansIllegalExcelBeanException {
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
			} else {
				Class<? extends Object> fieldClazz = field.getType();
				if (XlBeansUtil.isPermissibleFieldType(fieldClazz.getName())) {
					XlCellInfo info = new XlCellInfo();
					info.setPosition(cell.position());
					info.setTitle(cell.title());
					info.setField(field);
					info.setFieldName(field.getName());
					if (this.xlDataTypeMap.containsKey(cell.position())) {
						throw new XlBeansIllegalExcelBeanException("Dupliate positions found");
					} else {
						this.xlDataTypeMap.put(cell.position(), info);
					}
				} else {
					throw new XlBeansIllegalExcelBeanException(field.getType() + " is not supported");
				}

			}
		}
		createMapForXlBeanTypes(clazz.getSuperclass());

	}

	/**
	 * With the help of xlbeans metadata map and excel file object this method will
	 * extract list of xlbeans.
	 * 
	 * @return
	 * @throws XlBeansIllegalFileFormatException
	 * @throws XLBeansException
	 */
	private List<T> processExcelSheet() throws XlBeansIllegalFileFormatException, XLBeansException {
		try {
			int sheetNo = 0;
			List<? extends Object> beansList = new ArrayList<>();
			int x = 0;
			int y = 0;
			MissingCellPolicy missingCellPolicy = MissingCellPolicy.RETURN_BLANK_AS_NULL;
			boolean compareTitles = false;
			if (this.mappingOptions.isPresent()) {
				sheetNo = this.mappingOptions.get().getTargetSheet();
				beansList = this.mappingOptions.get().getGenerator().generate();
				x = this.mappingOptions.get().getStartingPosition().getX();
				y = this.mappingOptions.get().getStartingPosition().getY();
				missingCellPolicy = XlBeansUtil.getMissingCellPolicy(this.mappingOptions.get().getMissingCellAction());
			}
			if (x < 0 || y < 0) {
				throw new XlBeansIllegalFileFormatException("Invalid index to start parsing file(" + x + "," + y + ")");
			}

			this.xlWorkbook.setMissingCellPolicy(missingCellPolicy);
			Sheet sheet = this.xlWorkbook.getSheetAt(sheetNo);

			if (compareTitles) {
				if (y == 0) {
					throw new XlBeansIllegalFileFormatException(
							"Cannot compare titles if the data is in the first row");
				}
				Row titleRow = sheet.getRow(y - 1);
				int max = getMaximumKeyFromMap();
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
						throw new XlBeansIllegalFileFormatException("At (" + i + "," + (y - 1) + ") Expected:"
								+ this.xlDataTypeMap.get(i).getTitle() + ",Found:" + title);
					}

				}

				int rowIndex = 0;
				for (Row row : sheet) {
					if (rowIndex < y) {
						continue;
					}
					T obj = clazz.newInstance();
					for (int i = x; i < max; i++) {
						if (!this.xlDataTypeMap.containsKey(i)) {
							continue;
						}
						XlCellInfo info = this.xlDataTypeMap.get(i);
						Cell cell = row.getCell(x);
						CellType cellType = cell.getCellTypeEnum();
						if (!XlBeansUtil.compareCellTypeEnumWithDataTypeString(cellType,
								info.getField().getType().getName())) {
							// TODO
							throw new XlBeansIllegalFileFormatException();
						}
						if (this.transformer.isPresent()) {
							obj = this.transformer.get().transform(obj);
						}
					}

					rowIndex++;
				}

			}
		} catch (InstantiationException e) {
			throw new XLBeansException(e);
		} catch (IllegalAccessException e) {
			throw new XLBeansException(e);
		}

		return null;
	}

	/**
	 * This will extract maximum configured position from xlbeans metadata.
	 * 
	 * @return
	 */
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

	/**
	 * close the excel workbook resource.
	 * 
	 * @throws IOException
	 */
	@Override
	public void close() throws IOException {
		if (this.xlWorkbook != null) {
			this.xlWorkbook.close();
		}
	}

}
