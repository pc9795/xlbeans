package xlbeans.beans;

import xlbeans.annotations.Disabled;
import xlbeans.annotations.XlCell;

public class TestBeanAllDisabled {

	@Disabled
	public Integer i;

	@XlCell(position = 0)
	@Disabled
	public Long l;

	@Disabled
	public String str;
}
