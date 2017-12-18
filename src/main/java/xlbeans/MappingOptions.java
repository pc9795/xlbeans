package xlbeans;

import java.util.ArrayList;

import xlbeans.beans.Index;

/**
 * This class will provide various options to assist in extracting data from
 * excel sheet.
 * 
 * @author Prashant Chaubey created on: Dec 15, 2017
 */
public final class MappingOptions {

	private Index startingPosition;
	private MissingCellAction missingCellAction;
	private int targetSheet;
	private boolean compareTitles;
	private ListGenerator generator;

	private MappingOptions(MappingOptionsBuilder builder) {
		this.startingPosition = builder.startingPosition;
		this.missingCellAction = builder.missingCellAction;
		this.targetSheet = builder.targetSheet;
		this.compareTitles = builder.compareTitles;
		this.generator = builder.generator;
	}

	public Index getStartingPosition() {
		return startingPosition;
	}

	public MissingCellAction getMissingCellAction() {
		return missingCellAction;
	}

	public int getTargetSheet() {
		return targetSheet;
	}

	public boolean isCompareTitles() {
		return compareTitles;
	}

	public ListGenerator getGenerator() {
		return generator;
	}

	/**
	 * Builder class for Mapping options.
	 * 
	 * @author Prashant Chaubey created on:Dec 15,2017
	 */

	public static class MappingOptionsBuilder {

		private Index startingPosition = new Index(0, 0);
		private MissingCellAction missingCellAction = MissingCellAction.RETURN_BLANK_AS_NULL;
		private int targetSheet = 0;
		private boolean compareTitles = false;
		private ListGenerator generator = () -> new ArrayList<>();

		public MappingOptionsBuilder setStartingPosition(Index startingPosition) {
			this.startingPosition = startingPosition;
			return this;
		}

		public MappingOptionsBuilder setMissingCellPolicy(MissingCellAction missingCellAction) {
			this.missingCellAction = missingCellAction;
			return this;
		}

		public MappingOptionsBuilder setTargetSheet(int targetSheet) {
			this.targetSheet = targetSheet;
			return this;
		}

		public MappingOptionsBuilder setCompareTitles(boolean compareTitles) {
			this.compareTitles = compareTitles;
			return this;
		}

		public MappingOptionsBuilder setGenerator(ListGenerator generator) {
			this.generator = generator;
			return this;
		}

		public MappingOptions build() {
			return new MappingOptions(this);
		}

	}
}
