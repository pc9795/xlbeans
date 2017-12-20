package xlbeans;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import xlbeans.MappingOptions.Index;
import xlbeans.MappingOptions.MappingOptionsBuilder;
import xlbeans.beans.TestBean;
import xlbeans.exception.XlBeansException;

public class MapperProcessXlSheetTest {

	static Method validateAndConfigurePath;
	static Method createMapForXlBeanTypes;
	static Method processXlSheet;
	static Path path;

	@BeforeAll
	public static void init() throws NoSuchMethodException, SecurityException {

		validateAndConfigurePath = Mapper.class.getDeclaredMethod("validateAndConfigure", Path.class, Class.class,
				MappingOptions.class, Transformer.class);
		validateAndConfigurePath.setAccessible(true);
		createMapForXlBeanTypes = Mapper.class.getDeclaredMethod("createMapForXlBeanTypes", Class.class);
		createMapForXlBeanTypes.setAccessible(true);
		processXlSheet = Mapper.class.getDeclaredMethod("processXlSheet");
		processXlSheet.setAccessible(true);
		path = Paths
				.get(MapperProcessXlSheetTest.class.getClassLoader().getResource("data.xlsx").getFile().substring(1));
	}

	@Test
	@DisplayName("Invalid indexes in mapping options")
	public void test1() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchFieldException, SecurityException {
		Mapper<TestBean> mapper = new Mapper<>();
		validateAndConfigurePath.invoke(mapper, path, TestBean.class,
				new MappingOptionsBuilder().setStartingPosition(new Index(-1, -1)).build(), null);
		createMapForXlBeanTypes.invoke(mapper, TestBean.class);
		InvocationTargetException ex = assertThrows(InvocationTargetException.class, () -> {
			processXlSheet.invoke(mapper);
		});
		assertThat(ex.getCause().getClass()).isEqualTo(XlBeansException.class);
		assertThat(ex.getCause().getMessage()).contains("Invalid index to start");

	}

	@Test
	@DisplayName("Invalid sheet index in mapping options")
	public void test2() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchFieldException, SecurityException {
		Mapper<TestBean> mapper = new Mapper<>();
		validateAndConfigurePath.invoke(mapper, path, TestBean.class,
				new MappingOptionsBuilder().setTargetSheet(-1).build(), null);
		createMapForXlBeanTypes.invoke(mapper, TestBean.class);
		InvocationTargetException ex = assertThrows(InvocationTargetException.class, () -> {
			processXlSheet.invoke(mapper);
		});
		assertThat(ex.getCause().getClass()).isEqualTo(XlBeansException.class);
		assertThat(ex.getCause().getMessage()).contains("Invalid sheet index");
	}
}
