package xlbeans.beans;

import java.lang.reflect.Field;

/**
 * This bean will contain information about a excel cell;
 * 
 * @author Prashant Chaubey created on 18-Dec-2017
 */
public class ExcelCellInfo {

	private int position;
	private String title;
	private Field field;

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

}
