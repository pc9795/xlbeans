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
import xlbeans.annotations.ExcelCell;
import xlbeans.beans.ExcelCellInfo;
import xlbeans.exception.IllegalExcelBeanException;
import xlbeans.exception.IllegalFileException;
import xlbeans.exception.IllegalFileFormatException;
import xlbeans.exception.XLBeansException;
import xlbeans.util.Util;

/**
 * Main class to map excel sheet into java objects.
 * 
 * @author Prashant Chaubey created on: Dec 15, 2017
 */
public final class Mapper<T> implements AutoCloseable {
	private Workbook excelWorkbook;
	private Optional<MappingOptions> mappingOptions;
	private Optional<Transformer<T>> transformer;
	private Map<Integer, ExcelCellInfo> excelDataTypeMap = new HashMap<>();
	private Class<T> clazz;

	/**
	 * Method exposed to generate xlbeans from give excelFile.
	 * 
	 * @param excelFile
	 * @param clazz
	 * @throws IllegalFileException
	 * @throws IllegalFileFormatException
	 * @throws IllegalExcelBeanException
	 * @throws XLBeansException
	 */
	public List<T> map(Path excelFile, Class<T> clazz)
			throws IllegalFileException, IllegalFileFormatException, IllegalExcelBeanException, XLBeansException {
		return _map(excelFile, clazz, null, null);
	}

	/**
	 * Method exposed to generate xlbeans from give excelFile.
	 * 
	 * @param excelFile
	 * @param clazz
	 * @param mappingOptions
	 * @throws IllegalFileException
	 * @throws IllegalFileFormatException
	 * @throws IllegalExcelBeanException
	 * @throws XLBeansException
	 */
	public List<T> map(Path excelFile, Class<T> clazz, MappingOptions mappingOptions)
			throws IllegalFileException, IllegalFileFormatException, IllegalExcelBeanException, XLBeansException {
		return _map(excelFile, clazz, mappingOptions, null);
	}

	/**
	 * Method exposed to generate xlbeans from give excelFile.
	 * 
	 * @param excelFile
	 * @param clazz
	 * @param transformer
	 * @throws IllegalFileException
	 * @throws IllegalFileFormatException
	 * @throws IllegalExcelBeanException
	 * @throws XLBeansException
	 */
	public List<T> map(Path excelFile, Class<T> clazz, Transformer<T> transformer)
			throws IllegalFileException, IllegalFileFormatException, IllegalExcelBeanException, XLBeansException {
		return _map(excelFile, clazz, null, transformer);
	}

	/**
	 * Method exposed to generate xlbeans from give excelFile.
	 * 
	 * @param excelFile
	 * @param clazz
	 * @param mappingOptions
	 * @param transformer
	 * @throws IllegalFileException
	 * @throws IllegalFileFormatException
	 * @throws IllegalExcelBeanException
	 * @throws XLBeansException
	 */
	public List<T> map(Path excelFile, Class<T> clazz, MappingOptions mappingOptions, Transformer<T> transformer)
			throws IllegalFileException, IllegalFileFormatException, IllegalExcelBeanException, XLBeansException {
		return _map(excelFile, clazz, mappingOptions, transformer);
	}

	/**
	 * driver method which will process the excelFile.
	 * 
	 * @param excelFile
	 * @param clazz
	 * @param mappingOptions
	 * @param transformer
	 * @throws IllegalFileException
	 * @throws IllegalFileFormatException
	 * @throws IllegalExcelBeanException
	 * @throws XLBeansException
	 */
	private List<T> _map(Path excelFile, Class<T> clazz, MappingOptions mappingOptions, Transformer<T> transformer)
			throws IllegalFileException, IllegalFileFormatException, IllegalExcelBeanException, XLBeansException {
		if (excelFile == null) {
			throw new IllegalFileException("Null object recieved");
		}
		if (!Files.exists(excelFile)) {
			throw new IllegalFileException("File doesn't exists");
		}
		if (!excelFile.endsWith(".xlsx") || !excelFile.endsWith(".xls")) {
			throw new IllegalFileException("Not a valid file(.xls or .xlsx)");
		}
		try {
			if (excelFile.endsWith(".xls")) {
				this.excelWorkbook = new HSSFWorkbook(Files.newInputStream(excelFile));
			} else if (excelFile.endsWith(".xlsx")) {
				this.excelWorkbook = new XSSFWorkbook(Files.newInputStream(excelFile));
			}
		} catch (IOException e) {
			throw new IllegalFileFormatException("Could not able to get the workbook from the excel file", e);
		}

		this.clazz = clazz;
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
		createMapForExcelBeanTypes(clazz);
		return processExcelSheet();
	}

	/**
	 * Generate a map which contain the data of excel-cell to java data types
	 * mapping.
	 * 
	 * @param clazz
	 * @throws IllegalExcelBeanException
	 */
	private void createMapForExcelBeanTypes(Class<? super T> clazz) throws IllegalExcelBeanException {
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
			ExcelCell cell = field.getAnnotation(ExcelCell.class);
			if (cell == null) {
				continue;
			} else {
				Class<? extends Object> fieldClazz = field.getType();
				if (Util.isPermissibleFieldType(fieldClazz.getName())) {
					ExcelCellInfo info = new ExcelCellInfo();
					info.setPosition(cell.position());
					info.setTitle(cell.title());
					info.setField(field);
					if (this.excelDataTypeMap.containsKey(cell.position())) {
						throw new IllegalExcelBeanException("Dupliate positions found");
					} else {
						this.excelDataTypeMap.put(cell.position(), info);
					}
				} else {
					throw new IllegalExcelBeanException(field.getType() + " is not supported");
				}

			}
		}
		createMapForExcelBeanTypes(clazz.getSuperclass());

	}

	/**
	 * With the help of xlbeans metadata map and excel file object this method will
	 * extract list of xlbeans.
	 * 
	 * @return
	 * @throws IllegalFileException
	 * @throws XLBeansException
	 */
	private List<T> processExcelSheet() throws IllegalFileException, XLBeansException {
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
				missingCellPolicy = Util.getMissingCellPolicy(this.mappingOptions.get().getMissingCellAction());
			}
			if (x < 0 || y < 0) {
				throw new IllegalFileException("Invalid index to start parsing file(" + x + "," + y + ")");
			}

			this.excelWorkbook.setMissingCellPolicy(missingCellPolicy);
			Sheet sheet = this.excelWorkbook.getSheetAt(sheetNo);

			if (compareTitles) {
				if (y == 0) {
					throw new IllegalFileException("Cannot compare titles if the data is in the first row");
				}
				Row titleRow = sheet.getRow(y - 1);
				int max = getMaximumKeyFromMap();
				if (max > titleRow.getLastCellNum()) {
					throw new IllegalFileException(
							"Last cell number can't be smaller than maximum configured position");
				}
				for (int i = x; i < max; i++) {
					if (!this.excelDataTypeMap.containsKey(i)) {
						continue;
					}
					CellType type = titleRow.getCell(i).getCellTypeEnum();
					if (type != CellType.STRING) {
						throw new IllegalFileException("Title at (" + i + "," + (y - 1) + ") is not a string");
					}
					String title = titleRow.getCell(i).getStringCellValue();
					if (!title.equals(this.excelDataTypeMap.get(i).getTitle())) {
						throw new IllegalFileException("At (" + i + "," + (y - 1) + ") Expected:"
								+ this.excelDataTypeMap.get(i).getTitle() + ",Found:" + title);
					}

				}

				int rowIndex = 0;
				for (Row row : sheet) {
					if (rowIndex < y) {
						continue;
					}
					T obj = clazz.newInstance();
					for (int i = x; i < max; i++) {
						if (!this.excelDataTypeMap.containsKey(i)) {
							continue;
						}
						ExcelCellInfo info = this.excelDataTypeMap.get(i);
						Cell cell = row.getCell(x);
						CellType cellType = cell.getCellTypeEnum();
						if (!Util.compareCellTypeEnumWithDataTypeString(cellType,
								info.getField().getType().getName())) {
							// TODO
							throw new IllegalFileException();
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
		Set<Integer> keys = this.excelDataTypeMap.keySet();
		int max = Integer.MIN_VALUE;
		for (Integer key : keys) {
			if (key > max) {
				max = key;
			}
		}
		return max;
	}

	@Override
	public void close() throws Exception {
		if (this.excelWorkbook != null) {
			this.excelWorkbook.close();
		}
	}

}
