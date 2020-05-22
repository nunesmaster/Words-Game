package wwwordz.puzzle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import wwwordz.puzzle.Trie.Search;
import wwwordz.shared.Puzzle;
import wwwordz.shared.Table;
import wwwordz.shared.Puzzle.Solution;
import wwwordz.shared.Table.Cell;
/**
 * An organized collection of words, optimized for searching them. This class is
 * a singleton, meaning that there is, at most, a single instance of this class
 * per application. This dictionary uses a collection of Portuguese words loaded
 * as a resource from a file in this package. It is backed by a Trie to index
 * words and speedup searches.
 * 
 * @author vasco
 * @author Nuno
 *
 */
public class Generator extends Object {

	private final Dictionary dictionary = Dictionary.getInstance();
	private final Random random = new Random();

	public Generator() {
	}
	/**
	 * Generate a puzzle
	 * 
	 * @return puzzle in a table
	 */
	public Puzzle generate() {

		Puzzle puzzle = new Puzzle();
		Table table = new Table();
		List<Cell> list = table.getEmptyCells();
		while (list.size() > 0) {

			Cell cell = list.get(random.nextInt(list.size()));
			String word = dictionary.getRandomLargeWord();
			generate(table, cell, word, 0);
			list = table.getEmptyCells();
		}

		puzzle.setTable(table);
		puzzle.setSolutions(getSolutions(table));

		return puzzle;
	}

	public void generate(Table table, Cell cell, String word, int pos) {

		cell.setLetter(word.charAt(pos));
		if (pos + 1 < word.length()) {

			List<Cell> neighbours = table.getNeighbors(cell);
			for (Cell c : neighbours) {
				if (c.isEmpty() || cell.getLetter() == word.charAt(pos + 1)) {
					generate(table, c, word, pos + 1);
					break;
				}
			}
		}
	}
	/**
	 * return the solutions of the table
	 * 
	 * @param table
	 * @return list of the solutions
	 */
	public List<Solution> getSolutions(Table table) {

		List<Solution> solutions = new ArrayList<Solution>();
		for (Cell cell : table) {
			Search search = dictionary.startSearch();
			List<Cell> visited = new ArrayList<Cell>();
			StringBuilder str = new StringBuilder();
			getSolutions(solutions, visited, cell, table, str, search);
		}
		return solutions;
	}
	/**
	 * recursive method for finding solutions
	 * 
	 * @param solutions
	 * @param visited
	 * @param cell
	 * @param table
	 * @param word
	 * @param search
	 */
	public void getSolutions(List<Solution> solutions, List<Cell> visited,
			Cell cell, Table table, StringBuilder word, Search search) {
		if (visited.contains(cell)) {
			return;
		}

		visited.add(cell);
		if (!search.continueWith(cell.getLetter())) {
			return;
		}
		word.append(cell.getLetter());
		String str = word.toString();

		if (search.isWord() && (str.length() >= 3)) {

			Solution s = new Solution(visited, str);
			if (!contains(solutions, s.getWord())) {
				Solution solution = new Solution(visited, str);
				solutions.add(solution);
			}
		}

		List<Cell> neighbours = table.getNeighbors(cell);

		for (Cell c : neighbours) {
			List<Cell> copyVisited = new ArrayList<>(visited);
			Search copySearch = new Search(search);
			StringBuilder copyWord = new StringBuilder(word);
			getSolutions(solutions, copyVisited, c, table, copyWord,
					copySearch);
		}

	}
	/**
	 * verify if the list of the solutions contains a word
	 * 
	 * @param solutions
	 * @param word
	 * @return
	 */
	private boolean contains(List<Solution> solutions, String word) {
		for (Solution solution : solutions)
			if (solution.getWord().equals(word))
				return true;
		return false;
	}

	public Puzzle random() {
		Puzzle puzzle = new Puzzle();
		Table table = new Table();

		for (int i = 1; i < 5; i++) {
			for (int j = 1; j < 5; j++) {
				char c = (char) (random.nextInt(26) + 'A');
				table.setLetter(i, j, c);
			}
		}
		puzzle.setTable(table);
		puzzle.setSolutions(getSolutions(table));
		return puzzle;
	}

}
