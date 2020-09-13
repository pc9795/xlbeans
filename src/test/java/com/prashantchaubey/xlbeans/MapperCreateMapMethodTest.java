package com.prashantchaubey.xlbeans;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.prashantchaubey.xlbeans.beans.TestBeanAllDisabled;
import com.prashantchaubey.xlbeans.beans.TestBeanAllFieldsAnnotated;
import com.prashantchaubey.xlbeans.beans.TestBeanDuplicatePosition;
import com.prashantchaubey.xlbeans.beans.TestBeanHalfDisabled;
import com.prashantchaubey.xlbeans.beans.TestBeanUnsupported;
import com.prashantchaubey.xlbeans.exception.XlBeansIllegalExcelBeanException;

public class MapperCreateMapMethodTest {

  private static Method createMapForXlBeanTypes;
  private static Field excelDataTypeMap;

  @BeforeAll
  public static void init() throws NoSuchMethodException, SecurityException, NoSuchFieldException {
    createMapForXlBeanTypes =
        Mapper.class.getDeclaredMethod("createMapForXlBeanTypes", Class.class);
    createMapForXlBeanTypes.setAccessible(true);
    excelDataTypeMap = Mapper.class.getDeclaredField("xlDataTypeMap");
    excelDataTypeMap.setAccessible(true);
  }

  @Test
  @DisplayName("Empty object recieved")
  public void test1()
      throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    Mapper<TestBeanAllFieldsAnnotated> mapper = new Mapper<>();
    createMapForXlBeanTypes.invoke(mapper, new Object[] {null});
    Object obj = excelDataTypeMap.get(mapper);
    if (obj instanceof Map) {
      Map<?, ?> map = (Map<?, ?>) obj;
      assertThat(map.size()).isEqualTo(0);
    } else {
      Assertions.fail("field should contain a map object");
    }
  }

  @Test
  @DisplayName("Map with all possible data types is generating including supertype")
  public void test2()
      throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
    int totalFields = 0;
    Class<?> clazz = TestBeanAllFieldsAnnotated.class;
    for (; clazz != null; ) {
      Field[] fields = clazz.getDeclaredFields();
      totalFields += fields.length;
      clazz = clazz.getSuperclass();
    }
    Mapper<TestBeanAllFieldsAnnotated> mapper = new Mapper<>();
    createMapForXlBeanTypes.invoke(mapper, TestBeanAllFieldsAnnotated.class);
    Object obj = excelDataTypeMap.get(mapper);
    if (obj instanceof Map) {
      Map<?, ?> map = (Map<?, ?>) obj;
      assertThat(map.size()).isEqualTo(totalFields);
    } else {
      Assertions.fail("field should contain a map object");
    }
  }

  @Test
  @DisplayName("All disabled fields")
  public void test3()
      throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    Mapper<TestBeanAllDisabled> mapper = new Mapper<>();
    createMapForXlBeanTypes.invoke(mapper, TestBeanAllDisabled.class);
    Object obj = excelDataTypeMap.get(mapper);
    if (obj instanceof Map) {
      Map<?, ?> map = (Map<?, ?>) obj;
      assertThat(map.size()).isEqualTo(0);
    } else {
      Assertions.fail("field should contain a map object");
    }
  }

  @Test
  @DisplayName("Map with half field disabled and half annotated")
  public void test4()
      throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
    int totalFields = 0;
    Class<?> clazz = TestBeanHalfDisabled.class;
    for (; clazz != null; ) {
      Field[] fields = clazz.getDeclaredFields();
      totalFields += fields.length;
      clazz = clazz.getSuperclass();
    }
    Mapper<TestBeanHalfDisabled> mapper = new Mapper<>();
    createMapForXlBeanTypes.invoke(mapper, TestBeanHalfDisabled.class);
    Object obj = excelDataTypeMap.get(mapper);
    if (obj instanceof Map) {
      Map<?, ?> map = (Map<?, ?>) obj;
      assertThat(map.size()).isEqualTo(totalFields / 2);
      System.out.println(map);
    } else {
      Assertions.fail("field should contain a map object");
    }
  }

  @Test
  @DisplayName("Unsupported fields")
  public void test5() throws IllegalArgumentException {
    Mapper<TestBeanUnsupported> mapper = new Mapper<>();
    InvocationTargetException ex =
        assertThrows(
            InvocationTargetException.class,
            () -> {
              createMapForXlBeanTypes.invoke(mapper, TestBeanUnsupported.class);
            });
    assertThat(ex.getCause().getClass()).isEqualTo(XlBeansIllegalExcelBeanException.class);
    assertThat(ex.getCause().getMessage()).contains("is not supported");
  }

  @Test
  @DisplayName("Duplicate positions")
  public void test6() throws IllegalArgumentException {
    Mapper<TestBeanDuplicatePosition> mapper = new Mapper<>();
    InvocationTargetException ex =
        assertThrows(
            InvocationTargetException.class,
            () -> {
              createMapForXlBeanTypes.invoke(mapper, TestBeanDuplicatePosition.class);
            });
    assertThat(ex.getCause().getClass()).isEqualTo(XlBeansIllegalExcelBeanException.class);
    assertThat(ex.getCause().getMessage()).isEqualTo("Dupliate positions found");
  }
}
