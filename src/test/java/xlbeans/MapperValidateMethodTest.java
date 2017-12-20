package xlbeans;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import xlbeans.MappingOptions.Index;
import xlbeans.MappingOptions.MappingOptionsBuilder;
import xlbeans.beans.TestBeanAllFieldsAnnotated;
import xlbeans.exception.XlBeansException;
import xlbeans.exception.XlBeansIllegalFileFormatException;

public class MapperValidateMethodTest {

	static MappingOptions options;
	static Transformer<TestBeanAllFieldsAnnotated> transformerfn;
	static ListGenerator generator;
	static Mapper<TestBeanAllFieldsAnnotated> mapper;
	static Path nullPath;
	static Path notExistingFile;
	static Path invalidExtensionFile;
	static Path xlsxFile;
	static Path xlsFile;
	static Path noExtensionFile;
	static Path corruptedFile;
	static Method validateAndConfigurePath;
	static Field xlWorkbook;
	static Field mappingOptions;
	static Field transformer;

	@BeforeAll
	public static void init() throws NoSuchMethodException, SecurityException, NoSuchFieldException {
		generator = () -> new ArrayList<>();
		options = new MappingOptionsBuilder().setCompareTitles(true).setGenerator(generator)
				.setMissingCellAction(MissingCellAction.RETURN_BLANK_AS_NULL).setStartingPosition(new Index(0, 1))
				.setTargetSheet(0).build();
		transformerfn = (bean) -> bean;
		mapper = new Mapper<>();
		nullPath = null;
		notExistingFile = Paths.get("not_a_file");
		ClassLoader loader = MapperValidateMethodTest.class.getClassLoader();
		invalidExtensionFile = Paths.get(loader.getResource("sample.txt").getFile().substring(1));
		noExtensionFile = Paths.get(loader.getResource("sample").getFile().substring(1));
		xlsxFile = Paths.get(loader.getResource("sample.xlsx").getFile().substring(1));
		xlsFile = Paths.get(loader.getResource("sample.xls").getFile().substring(1));
		corruptedFile = Paths.get(loader.getResource("corrupted.xlsx").getFile().substring(1));
		validateAndConfigurePath = Mapper.class.getDeclaredMethod("validateAndConfigure", Path.class, Class.class,
				MappingOptions.class, Transformer.class);
		validateAndConfigurePath.setAccessible(true);
		xlWorkbook = Mapper.class.getDeclaredField("xlWorkbook");
		xlWorkbook.setAccessible(true);
		mappingOptions = Mapper.class.getDeclaredField("mappingOptions");
		mappingOptions.setAccessible(true);
		transformer = Mapper.class.getDeclaredField("transformer");
		transformer.setAccessible(true);
	}

	@Test
	@DisplayName("Testing null file")
	public void test1() {

		InvocationTargetException ex = assertThrows(InvocationTargetException.class, () -> {
			assertThat(validateAndConfigurePath.invoke(mapper, nullPath, null, null, null));
		});
		assertThat(ex.getCause().getClass()).isEqualTo(XlBeansIllegalFileFormatException.class);
		assertThat(ex.getCause().getMessage()).isEqualTo("Null object recieved");
	}

	@Test
	@DisplayName("Testing non existing file")
	public void test2() {
		InvocationTargetException ex = assertThrows(InvocationTargetException.class, () -> {
			assertThat(validateAndConfigurePath.invoke(mapper, notExistingFile, null, null, null));
		});
		assertThat(ex.getCause().getClass()).isEqualTo(XlBeansIllegalFileFormatException.class);
		assertThat(ex.getCause().getMessage()).isEqualTo("File doesn't exists");
	}

	@Test
	@DisplayName("Testing invalid extension file")
	public void test3() {
		InvocationTargetException ex = assertThrows(InvocationTargetException.class, () -> {
			assertThat(validateAndConfigurePath.invoke(mapper, invalidExtensionFile, null, null, null));
		});
		assertThat(ex.getCause().getClass()).isEqualTo(XlBeansIllegalFileFormatException.class);
		assertThat(ex.getCause().getMessage()).isEqualTo("Not a valid file(.xls or .xlsx)");
	}

	@Test
	@DisplayName("Testing null class")
	public void test4() {
		InvocationTargetException ex = assertThrows(InvocationTargetException.class, () -> {
			assertThat(validateAndConfigurePath.invoke(mapper, xlsxFile, null, null, null));
		});
		assertThat(ex.getCause().getClass()).isEqualTo(XlBeansException.class);
		assertThat(ex.getCause().getMessage()).isEqualTo("Cannot supply a null value for class object");
	}

	@Test
	@DisplayName("Testing creating on HSSFWorkbook")
	public void test5() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Mapper<TestBeanAllFieldsAnnotated> _mapper = new Mapper<>();
		validateAndConfigurePath.invoke(_mapper, xlsFile, TestBeanAllFieldsAnnotated.class, null, null);
		assertThat(xlWorkbook.get(_mapper)).isInstanceOf(HSSFWorkbook.class);
	}

	@Test
	@DisplayName("Testing creating on XSSFWorkbook")
	public void test6() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Mapper<TestBeanAllFieldsAnnotated> _mapper = new Mapper<>();
		validateAndConfigurePath.invoke(_mapper, xlsxFile, TestBeanAllFieldsAnnotated.class, null, null);
		assertThat(xlWorkbook.get(_mapper)).isInstanceOf(XSSFWorkbook.class);
	}

	@Test
	@DisplayName("Testing creating of empty Mapping options optional")
	public void test7() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Mapper<TestBeanAllFieldsAnnotated> _mapper = new Mapper<>();
		validateAndConfigurePath.invoke(_mapper, xlsxFile, TestBeanAllFieldsAnnotated.class, null, null);
		assertThat(mappingOptions.get(_mapper)).isEqualTo(Optional.empty());
	}

	@Test
	@DisplayName("Testing creating of Mapping options")
	public void test8() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Mapper<TestBeanAllFieldsAnnotated> _mapper = new Mapper<>();
		validateAndConfigurePath.invoke(_mapper, xlsxFile, TestBeanAllFieldsAnnotated.class, options, null);
		Object obj = mappingOptions.get(_mapper);
		if (obj instanceof Optional) {
			Optional<?> optional = (Optional<?>) obj;
			assertThat(optional.get()).isInstanceOf(MappingOptions.class);
		} else {
			Assertions.fail("Optional value is not recieved");
		}
	}

	@Test
	@DisplayName("Testing creating of empty Transformer optional")
	public void test9() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Mapper<TestBeanAllFieldsAnnotated> _mapper = new Mapper<>();
		validateAndConfigurePath.invoke(_mapper, xlsxFile, TestBeanAllFieldsAnnotated.class, null, null);
		assertThat(transformer.get(_mapper)).isEqualTo(Optional.empty());
	}

	@Test
	@DisplayName("Testing creating on Transformer")
	public void test10() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchFieldException, SecurityException {
		Mapper<TestBeanAllFieldsAnnotated> _mapper = new Mapper<>();
		validateAndConfigurePath.invoke(_mapper, xlsxFile, TestBeanAllFieldsAnnotated.class, null, transformerfn);
		Object obj = transformer.get(_mapper);
		if (obj instanceof Optional) {
			Optional<?> optional = (Optional<?>) obj;
			assertThat(optional.get()).isInstanceOf(Transformer.class);
		} else {
			Assertions.fail("Optional value is not recieved");
		}
	}
}
