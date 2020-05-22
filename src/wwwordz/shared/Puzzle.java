	package wwwordz.shared;

import java.io.Serializable;
import java.util.List;

import wwwordz.shared.Table.Cell;
/**
 * A puzzle, containing a table and list of solutions. A table is a square grid
 * of letters and a solution is a word contained in the grid, where consecutive
 * letters are in neighboring cells on the grid and the letter in each cell is
 * used only once.
 * 
 * @author vasco
 * @author Nuno
 *
 */
public class Puzzle extends Object implements Serializable {

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;

	List<Solution> solutions;
	Table table;


	public Puzzle() {}
	public List<Solution> getSolutions() {
		return solutions;
	}

	public void setSolutions(List<Solution> solutions) {
		this.solutions = solutions;
	}

	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	}

	public static class Solution {

		List<Cell> cells;
		String word;
		public Solution() {}
		public Solution(List<Cell> cells, String word) {
			super();
			this.cells = cells;
			this.word = word;
		}
		public List<Cell> getCells() {
			return cells;
		}

		public String getWord() {
			return word;
		}

		public int getPoints() {
			int points = 1;
			if (word.length() == 3)
				return points;
			else
				return points();
		}

		public int points() {
			int size = 4;
			int wordPoints = 1;
			int moreLetterPoints = (wordPoints * 2) + 1;
			points(size, moreLetterPoints);
			return moreLetterPoints;
		}

		public void points(int size, int points) {
			if (size == word.length()) {
				return;
			} else {
				points = (points * 2) + 1;
				points(size++, points);
			}
		}
	}
}
