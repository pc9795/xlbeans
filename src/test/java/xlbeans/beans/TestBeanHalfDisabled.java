package xlbeans.beans;

import xlbeans.annotations.Disabled;
import xlbeans.annotations.XlCell;

public class TestBeanHalfDisabled {

	@XlCell(position = 2)
	@Disabled
	private int i1;
	@XlCell(position = 3)
	@Disabled
	private Integer i2;
	@XlCell(position = 4)
	@Disabled
	private byte b1;
	@XlCell(position = 5)
	private Byte b2;
	@XlCell(position = 6)
	private short s1;
	@XlCell(position = 7)
	private Short s2;
}
