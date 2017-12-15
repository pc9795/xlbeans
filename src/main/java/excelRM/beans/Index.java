package excelRM.beans;

/**
 * This class will provide the starting point of the excel sheet.
 * 
 * @author Prashant Chaubey created on: Dec 15, 2017
 */
public class Index {

	private int x;
	private int y;

	public Index(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}
