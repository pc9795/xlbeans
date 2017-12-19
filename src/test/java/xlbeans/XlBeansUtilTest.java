package xlbeans;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static xlbeans.util.XlBeansUtil.isPermissibleFieldType;
import static xlbeans.util.XlBeansUtil.getMissingCellPolicy;
import static xlbeans.util.XlBeansUtil.compareCellTypeEnumWithDataTypeString;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class XlBeansUtilTest {

	@Test
	@DisplayName("Testing XlBeansUtil.isPermissibleFieldType method")
	public void test1() {
		assertAll(
				() -> assertThat(isPermissibleFieldType(int.class.getName())).withFailMessage("Error in testing int")
						.isTrue(),
				() -> assertThat(isPermissibleFieldType(Integer.class.getName()))
						.withFailMessage("Error in testing Integer").isTrue(),
				() -> assertThat(isPermissibleFieldType(byte.class.getName())).withFailMessage("Error in testing byte")
						.isTrue(),
				() -> assertThat(isPermissibleFieldType(Byte.class.getName())).withFailMessage("Error in testing Byte")
						.isTrue(),
				() -> assertThat(isPermissibleFieldType(short.class.getName()))
						.withFailMessage("Error in testing short").isTrue(),
				() -> assertThat(isPermissibleFieldType(Short.class.getName()))
						.withFailMessage("Error in testing Short").isTrue(),
				() -> assertThat(isPermissibleFieldType(long.class.getName())).withFailMessage("Error in testing long")
						.isTrue(),
				() -> assertThat(isPermissibleFieldType(Long.class.getName())).withFailMessage("Error in testing Long")
						.isTrue(),
				() -> assertThat(isPermissibleFieldType(char.class.getName())).withFailMessage("Error in testing char")
						.isTrue(),
				() -> assertThat(isPermissibleFieldType(Character.class.getName()))
						.withFailMessage("Error in testing Character").isTrue(),
				() -> assertThat(isPermissibleFieldType(float.class.getName()))
						.withFailMessage("Error in testing float").isTrue(),
				() -> assertThat(isPermissibleFieldType(Float.class.getName()))
						.withFailMessage("Error in testing Float").isTrue(),
				() -> assertThat(isPermissibleFieldType(double.class.getName()))
						.withFailMessage("Error in testing double").isTrue(),
				() -> assertThat(isPermissibleFieldType(Double.class.getName()))
						.withFailMessage("Error in testing Double").isTrue(),
				() -> assertThat(isPermissibleFieldType(String.class.getName()))
						.withFailMessage("Error in testing String").isTrue(),
				() -> assertThat(isPermissibleFieldType(null)).withFailMessage("Error in testing null").isFalse(),
				() -> assertThat(isPermissibleFieldType("")).withFailMessage("Error in testing ''").isFalse(),
				() -> assertThat(isPermissibleFieldType("Prashant")).withFailMessage("Error in testing 'Prashant'")
						.isFalse());
	}

	@Test
	@DisplayName("Testing XlBeansUtil.getMissingCellPolicy method")
	public void test2() {
		assertAll(
				() -> assertThat(getMissingCellPolicy(MissingCellAction.CREATE_NULL_AS_BLANK))
						.withFailMessage("Error in checking create_null_as_blank")
						.isEqualTo(MissingCellPolicy.CREATE_NULL_AS_BLANK),
				() -> assertThat(getMissingCellPolicy(MissingCellAction.RETURN_BLANK_AS_NULL))
						.withFailMessage("Error in checking return_blank_as_null")
						.isEqualTo(MissingCellPolicy.RETURN_BLANK_AS_NULL),
				() -> assertThat(getMissingCellPolicy(MissingCellAction.RETURN_NULL_AND_BLANK))
						.withFailMessage("Error in checking return_null_and_blank")
						.isEqualTo(MissingCellPolicy.RETURN_NULL_AND_BLANK),
				() -> {
					assertThrows(RuntimeException.class, () -> assertThat(getMissingCellPolicy(null)));
				});
	}

	@Test
	@DisplayName("Testing XlBeansUtil.compareCellTypeEnumWithDataTypeString method")
	public void test3() {
		assertAll(
				() -> assertThat(compareCellTypeEnumWithDataTypeString(null, null)).withFailMessage("For null")
						.isFalse(),
				() -> assertThat(compareCellTypeEnumWithDataTypeString(CellType._NONE, null))
						.withFailMessage("For null string").isFalse(),
				() -> assertThat(compareCellTypeEnumWithDataTypeString(CellType._NONE, null))
						.withFailMessage("For _none").isFalse(),
				() -> assertThat(compareCellTypeEnumWithDataTypeString(CellType.BLANK, "string"))
						.withFailMessage("For blank").isFalse(),
				() -> assertThat(compareCellTypeEnumWithDataTypeString(CellType.BOOLEAN, boolean.class.getName()))
						.withFailMessage("For boolean with boolean").isTrue(),
				() -> assertThat(compareCellTypeEnumWithDataTypeString(CellType.BOOLEAN, Boolean.class.getName()))
						.withFailMessage("For boolean with Boolean").isTrue(),
				() -> assertThat(compareCellTypeEnumWithDataTypeString(CellType.BOOLEAN, "string"))
						.withFailMessage("For boolean with random string").isFalse(),
				() -> assertThat(compareCellTypeEnumWithDataTypeString(CellType.ERROR, "string"))
						.withFailMessage("For error").isFalse(),
				() -> assertThat(compareCellTypeEnumWithDataTypeString(CellType.FORMULA, "string"))
						.withFailMessage("For formula").isFalse(),
				() -> assertThat(compareCellTypeEnumWithDataTypeString(CellType.STRING, char.class.getName()))
						.withFailMessage("For string with char").isTrue(),
				() -> assertThat(compareCellTypeEnumWithDataTypeString(CellType.STRING, Character.class.getName()))
						.withFailMessage("For string with Character").isTrue(),
				() -> assertThat(compareCellTypeEnumWithDataTypeString(CellType.STRING, String.class.getName()))
						.withFailMessage("For string with string").isTrue(),
				() -> assertThat(compareCellTypeEnumWithDataTypeString(CellType.STRING, "string"))
						.withFailMessage("For string with random string").isFalse(),
				() -> assertThat(compareCellTypeEnumWithDataTypeString(CellType.NUMERIC, byte.class.getName()))
						.withFailMessage("For numeric with byte").isTrue(),
				() -> assertThat(compareCellTypeEnumWithDataTypeString(CellType.NUMERIC, Byte.class.getName()))
						.withFailMessage("For numeric with Byte").isTrue(),
				() -> assertThat(compareCellTypeEnumWithDataTypeString(CellType.NUMERIC, short.class.getName()))
						.withFailMessage("For numeric with short").isTrue(),
				() -> assertThat(compareCellTypeEnumWithDataTypeString(CellType.NUMERIC, Short.class.getName()))
						.withFailMessage("For numeric with Short").isTrue(),
				() -> assertThat(compareCellTypeEnumWithDataTypeString(CellType.NUMERIC, int.class.getName()))
						.withFailMessage("For numeric with int").isTrue(),
				() -> assertThat(compareCellTypeEnumWithDataTypeString(CellType.NUMERIC, Integer.class.getName()))
						.withFailMessage("For numeric with Integer").isTrue(),
				() -> assertThat(compareCellTypeEnumWithDataTypeString(CellType.NUMERIC, long.class.getName()))
						.withFailMessage("For numeric with long").isTrue(),
				() -> assertThat(compareCellTypeEnumWithDataTypeString(CellType.NUMERIC, Long.class.getName()))
						.withFailMessage("For numeric with Long").isTrue(),
				() -> assertThat(compareCellTypeEnumWithDataTypeString(CellType.NUMERIC, float.class.getName()))
						.withFailMessage("For numeric with float").isTrue(),
				() -> assertThat(compareCellTypeEnumWithDataTypeString(CellType.NUMERIC, Float.class.getName()))
						.withFailMessage("For numeric with Float").isTrue(),
				() -> assertThat(compareCellTypeEnumWithDataTypeString(CellType.NUMERIC, double.class.getName()))
						.withFailMessage("For numeric with double").isTrue(),
				() -> assertThat(compareCellTypeEnumWithDataTypeString(CellType.NUMERIC, Double.class.getName()))
						.withFailMessage("For numeric with Double").isTrue(),
				() -> assertThat(compareCellTypeEnumWithDataTypeString(CellType.NUMERIC, "string"))
						.withFailMessage("For numeric with random string").isFalse()

		);
	}
}
