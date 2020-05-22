package wwwordz.puzzle;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import wwwordz.puzzle.Trie.Search;

public class Dictionary extends Object {

	private static Dictionary dictionary = null;
	final String DIC_FILE = "wwwordz/puzzle/pt-PT-AO.dic";
	private Trie trie = new Trie();

	private Dictionary() {

		try {

			InputStream in = ClassLoader.getSystemResourceAsStream(DIC_FILE);
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(in));

			String line;
			Pattern pattern = Pattern.compile("/|\\s");
			Pattern allLetterPattern = Pattern.compile("[A-Z]+");
			while ((line = reader.readLine()) != null) {
				String[] str = pattern.split(line);
				String word = str[0];
				if (word.length() >= 3) {
					word = Normalizer
							.normalize(word.toUpperCase(Locale.ENGLISH),
									Form.NFD)
							.replaceAll("\\p{InCombiningDiacriticalMarks}+",
									"");
					Matcher matcher = allLetterPattern.matcher(word);
					if (matcher.matches())
						trie.put(word);
				}
			}
			reader.close();
		} catch (Exception e) {
			System.out.println(e);
		}

	}

	/**
	 * Obtain the sole instance of this class. Multiple invocations will receive
	 * the exact same instance.
	 * 
	 * @return
	 */
	public static Dictionary getInstance() {
		if (dictionary == null)
			return dictionary = new Dictionary();
		else
			return dictionary;
	}

	/**
	 * Start a dictionary search.
	 * 
	 * @return
	 */
	public Search startSearch() {
		return trie.startSearch();
	}

	/**
	 * Return a large random word from the trie
	 * 
	 * @return
	 */
	public String getRandomLargeWord() {
		return trie.getRandomLargeWord();
	}
}
