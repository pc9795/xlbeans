package xlbeans.beans;

import java.lang.reflect.Field;

/**
 * This bean will contain information about a excel cell;
 * 
 * @author Prashant Chaubey created on 18-Dec-2017
 */
public class XlCellInfo {

	private int position;
	private String title;
	private Field field;
	private String fieldName;

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

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	@Override
	public String toString() {
		return "XlCellInfo [position=" + position + ", title=" + title + ", field=" + field + ", fieldName=" + fieldName
				+ "]";
	}

}
