package xlbeans.util;

import java.util.Arrays;
import java.util.List;

/**
 * This class will contain the library constants.
 * 
 * @author Prashant Chaubey created on 18-Dec-2017
 */
public class XlBeansConstants {

	public static final List<String> PERMISSIBLE_FIELD_TYPES = Arrays.asList("java.lang.String", "int",
			"java.lang.Integer", "boolean", "java.lang.Boolean", "char", "java.lang.Character", "double",
			"java.lang.Double", "float", "java.lang.Float", "short", "java.lang.Short", "byte", "java.lang.Byte",
			"long", "java.lang.Long");
	public static final List<String> PERMISSIBLE_NUMERIC_FIELD_TYPES = Arrays.asList("int", "java.lang.Integer",
			"double", "java.lang.Double", "float", "java.lang.Float", "short", "java.lang.Short", "byte",
			"java.lang.Byte", "long", "java.lang.Long");
	public static final List<String> PERMISSIBLE_STRING_TYPES = Arrays.asList("java.lang.String", "char",
			"java.lang.Character");
}
