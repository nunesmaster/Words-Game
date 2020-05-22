package wwwordz.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import wwwordz.shared.Table.Cell;

/**
 * A table composed of a collection of cells indexed by row and column
 * positions.
 * 
 * @author vasco
 * @author Nuno
 */

public class Table extends Object implements Iterable<Cell>, Serializable {

	private static final long serialVersionUID = 1L;
	private static final int SIZE = 5;
	private static final int MIN_X = 1;
	private static final int MIN_Y = 1;
	private static final int MAX_X = 4;
	private static final int MAX_Y = 4;
	Cell table[][] = new Cell[SIZE][SIZE];

	/**
	 * Create a table with empty cells
	 * 
	 */
	

	public Table() {}
	
	public Table(int k) {

		for (int i = 1; i < SIZE; i++) {
			for (int j = 1; j < SIZE; j++) {
				table[i][j] = new Cell(i, j);
			}
		}
	}

	/**
	 * Create table with given data
	 * 
	 * @param data
	 */
	public Table(String[] data) {
		for (int i = 1; i < SIZE; i++) {
			for (int j = 1; j < SIZE; j++) {
				table[i][j] = new Cell(i, j, data[i - 1].charAt(j - 1));
			}
		}
	}

	/**
	 * Return letter in given row and column
	 * 
	 * @param row
	 * @param column
	 * @return
	 */
	public char getLetter(int row, int column) {
		return table[row][column].getLetter();
	}

	/**
	 * Return cell in given row and column
	 * 
	 * @param row
	 * @param column
	 * @return
	 */
	public Cell getCell(int row, int column) {
		return table[row][column];
	}

	/**
	 * set letter in given row and column
	 * 
	 * @param row
	 * @param column
	 * @param letter
	 */
	public void setLetter(int row, int column, char letter) {
		table[row][column].setLetter(letter);
	}

	/**
	 * return empty cells
	 * 
	 * @return
	 */
	public List<Cell> getEmptyCells() {

		List<Cell> list = new ArrayList<>();

		for (int i = 1; i < SIZE; i++) {
			for (int j = 1; j < SIZE; j++) {
				if (getLetter(i, j) == ' ') {
					list.add(getCell(i, j));
				}
			}
		}
		return list;
	}

	/**
	 * give the list of the neighbors
	 * 
	 * @param cell
	 * @return list
	 */
	public List<Cell> getNeighbors(Cell cell) {

		List<Cell> list = new ArrayList<>();

		int thisPosX = cell.row;
		int thisPosY = cell.column;

		int startPosX = (thisPosX - 1 < MIN_X) ? thisPosX : thisPosX - 1;
		int startPosY = (thisPosY - 1 < MIN_Y) ? thisPosY : thisPosY - 1;
		int endPosX = (thisPosX + 1 > MAX_X) ? thisPosX : thisPosX + 1;
		int endPosY = (thisPosY + 1 > MAX_Y) ? thisPosY : thisPosY + 1;
		// See how many are alive
		for (int i = startPosX; i <= endPosX; i++) {
			for (int j = startPosY; j <= endPosY; j++) {
				// All the neighbors will be grid[i][j]
				if (i != cell.row || j != cell.column) {
					list.add(getCell(i, j));
				}
			}
		}

		return list;
	}

	@Override
	public String toString() {

		String str = "Table: ";
		for (int i = 1; i < SIZE; i++) {
			for (int j = 1; j < SIZE; j++) {
				str += table[i][j] + " ";
			}
		}

		return str;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.deepHashCode(table);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Table other = (Table) obj;
		if (!Arrays.deepEquals(table, other.table))
			return false;
		return true;
	}

	public static class Cell implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		int row, column;
		char letter;

		/**
		 * Create an empty cell at the given row an column
		 * 
		 * @param row
		 * @param column
		 */
		
		public Cell() {}
		public Cell(int row, int column) {
			super();
			this.row = row;
			this.column = column;
			this.letter = ' ';
		}

		/**
		 * Create an empty cell at the given row an column
		 * 
		 * @param row
		 * @param column
		 * @param letter
		 */
		public Cell(int row, int column, char letter) {
			super();
			this.row = row;
			this.column = column;
			this.letter = letter;
		}

		/**
		 * Letter in this cell
		 * 
		 * @return
		 */
		public char getLetter() {
			return letter;
		}

		/**
		 * Change letter in this cell
		 * 
		 * @param letter
		 */
		public void setLetter(char letter) {
			this.letter = letter;
		}

		/**
		 * Check if cell is empty
		 * 
		 * @return
		 */
		public boolean isEmpty() {

			return letter == ' ';
		}

		@Override
		public String toString() {
			return "Cell [row=" + row + ", column=" + column + ", letter="
					+ letter + "]";
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getEnclosingInstance().hashCode();
			result = prime * result + column;
			result = prime * result + letter;
			result = prime * result + row;
			return result;
		}

		private Class<Table> getEnclosingInstance() {
			return Table.class;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Cell other = (Cell) obj;
			if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
				return false;
			if (column != other.column)
				return false;
			if (letter != other.letter)
				return false;
			if (row != other.row)
				return false;
			return true;
		}

	}

	public class CellIterator implements Iterator<Cell> {

		private int column = MIN_Y;
		private int row = MIN_X;

		@Override
		public boolean hasNext() {

			if (row > 4) {
				return false;
			}
			return true;
		}

		@Override
		public Cell next() {

			Cell cell = table[row][column];
			column++;
			if (column > 4) {
				column = 1;
				row++;
			}

			return cell;
		}

		@Override
		public void remove() {
		}
	}

	@Override
	public Iterator<Cell> iterator() {
		return new CellIterator();
	}
}
