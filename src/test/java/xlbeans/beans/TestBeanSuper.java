package xlbeans.beans;

import xlbeans.annotations.XlCell;

public class TestBeanSuper {

	@XlCell(position = 0)
	private Integer iSuper;
	@XlCell(position = 1)
	private String strSuper;

	public Integer getiSuper() {
		return iSuper;
	}

	public void setiSuper(Integer iSuper) {
		this.iSuper = iSuper;
	}

	public String getStrSuper() {
		return strSuper;
	}

	public void setStrSuper(String strSuper) {
		this.strSuper = strSuper;
	}

}
