package wwwordz.puzzle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

public class Trie extends Object implements Iterable<String> {

	private Node root = new Node();

	public static class Search extends Object {
		Node node;
		/**
		 * Create a search starting in given node
		 * 
		 * @param node
		 */
		public Search(Node node) {
			this.node = node;
		}
		/**
		 * Create a clone of the given search, with the same fields.
		 * 
		 * @param search
		 */
		public Search(Search search) {
			this.node = search.node;
		}
		/**
		 * Check if the search can continue with the given letter. Internal node
		 * is updated if the search is valid.
		 * 
		 * @param letter
		 * @return
		 */
		public boolean continueWith(char letter) {
			if (node == null)
				return false;
			if (node.containsKey(letter)) {
				node = node.get(letter);
			} else
				node = null;
			return node != null;
		}
		/**
		 * Check if characters searched so far correspond to a word
		 * 
		 * @return
		 */
		public boolean isWord() {
			return node.completeWord;
		}

	}

	public void put(String word) {
		root.put(word, 0);
	}
	/**
	 * Start a word search from the root.
	 * 
	 * @return
	 */
	public Search startSearch() {
		return new Search(root);
	}

	public String getRandomLargeWord() {

		StringBuilder str = new StringBuilder();
		root.getRandomLargeWord(str);
		return str.toString();
	}

	private class Node extends HashMap<Character, Node> {

		private static final long serialVersionUID = 1L;
		public boolean completeWord = false;

		/**
		 * Insert a word in the structure, starting from the root.
		 * 
		 * @param word
		 * @param n
		 */
		public void put(String word, int n) {

			if (n < word.length()) {
				Node node = new Node();
				char letter = word.charAt(n);
				if (containsKey(letter)) {
					node = get(letter);
				}

				else {
					put(letter, node);
				}
				node.put(word, n + 1);
			} else {
				completeWord = true;
			}
		}

		/**
		 * Performs a random walk in the data structure, randomly selecting a
		 * path in each node, until reaching a leaf (a node with no
		 * descendants).
		 * 
		 * @param str
		 */
		public void getRandomLargeWord(StringBuilder str) {

			ArrayList<Character> arrayList = new ArrayList<Character>(keySet());
			if (isEmpty())
				return;

			Random random = new Random();
			char character = arrayList.get(random.nextInt(arrayList.size()));
			str.append(character);
			Node node = get(character);
			node.getRandomLargeWord(str);
		}
	}

	public class NodeIterator extends Object
			implements
				Iterator<String>,
				Runnable {

		String nextWord;
		boolean terminated;
		Thread thread;

		NodeIterator() {

			thread = new Thread(this, "Node iterator");
			thread.start();
		}

		@Override
		public void run() {
			terminated = false;
			visitNodes(root);

			synchronized (this) {
				terminated = true;
				handshake();
			}
		}

		@Override
		public boolean hasNext() {
			synchronized (this) {
				if (!terminated)
					handshake();
			}
			return nextWord != null;
		}

		@Override
		public String next() {
			String word = nextWord;
			synchronized (this) {
				nextWord = null;
			}
			return word;
		}
		/**
		 * verify the visted nodes
		 * 
		 * @param node
		 */
		private void visitNodes(Node node) {

			Set<Character> list = node.keySet();
			StringBuilder str;

			for (Iterator<Character> it = list.iterator(); it.hasNext();) {
				str = new StringBuilder();
				char c = it.next();
				str.append(c);

				Node curr = node.get(c);

				if (!curr.isEmpty())
					visitNodes(curr, str);
				if (curr.completeWord) {
					synchronized (this) {
						if (nextWord != null)
							handshake();
						nextWord = str.toString();
						handshake();
					}
				}
			}
		}
		/**
		 * visited nodes with a recursive method
		 * 
		 * @param node
		 * @param str
		 */
		private void visitNodes(Node node, StringBuilder str) {

			Set<Character> list = node.keySet();
			StringBuilder copy = new StringBuilder(str);
			if (node.completeWord) {
				synchronized (this) {
					if (nextWord != null)
						handshake();
					nextWord = str.toString();
					handshake();
				}
			}
			for (Iterator<Character> it = list.iterator(); it.hasNext();) {
				char c = it.next();
				str.append(c);
				Node curr = node.get(c);

				if (!curr.isEmpty())
					visitNodes(curr, str);
				if (curr.completeWord) {
					synchronized (this) {
						if (nextWord != null)
							handshake();
						nextWord = str.toString();
						str = copy;
						handshake();
					}
				}
			}
		}

		private void handshake() {
			notify();
			try {
				wait();
			} catch (InterruptedException cause) {

				throw new RuntimeException(
						"Unexpected interuption while waiting", cause);
			}
		}
	}

	@Override
	public Iterator<String> iterator() {
		// TODO Auto-generated method stub
		return new NodeIterator();
	}

}
