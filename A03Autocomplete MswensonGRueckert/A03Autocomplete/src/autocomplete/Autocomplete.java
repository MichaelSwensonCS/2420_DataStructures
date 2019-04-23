package autocomplete;

import java.util.Arrays;

import edu.princeton.cs.algs4.Merge;
import edu.princeton.cs.algs4.Quick;

/**
 * Autocompletion based on array of terms given, which is then merge-sorted.
 * Users will be given a list of matching terms as they type along with the
 * weights of the terms in weighted order.
 *
 * @author Michael Swenson, GarretRueckert
 *
 */
public class Autocomplete {
	private final Term[] terms;

	/**
	 * Initialize the data structure from the given array of terms.
	 * 
	 * @param terms
	 *            list of terms to be sorted
	 * 
	 */
	public Autocomplete(Term[] terms) {
		if (terms == null) {
			throw new IllegalArgumentException();
		}

		this.terms = terms.clone();
		Quick.sort(this.terms);
	}

	/**
	 * Return all terms that start with the given prefix, in descending order of
	 * weight.
	 * 
	 * @param prefix
	 *            the prefix entered
	 * @return array of matching terms sorted by weight
	 */
	public Term[] allMatches(String prefix) {
		if (prefix == null) {
			throw new NullPointerException();
		}
		Term placeHolder = new Term(prefix, 0);
		int firstIndex = BinarySearchDeluxe.firstIndexOf(terms, placeHolder, Term.byPrefixOrder(prefix.length()));
		int lastIndex = BinarySearchDeluxe.lastIndexOf(terms, placeHolder, Term.byPrefixOrder(prefix.length()));

		Term[] matches;

		if (firstIndex == -1) {
			return new Term[0];
		}

		matches = new Term[lastIndex - firstIndex + 1];
		for (int i = 0; i < matches.length; i++) {
			matches[i] = terms[firstIndex++];
		}
		Arrays.sort(matches, Term.byReverseWeightOrder());
		return matches;
	}

	/**
	 * Return the number of terms that start with the given prefix.
	 * 
	 * @param prefix
	 * @return number of terms with matching prefix
	 */
	public int numberOfMatches(String prefix) {
		if (prefix == null)
			throw new NullPointerException();
		return BinarySearchDeluxe.lastIndexOf(terms, new Term(prefix, 0), Term.byPrefixOrder(prefix.length()))
				- BinarySearchDeluxe.firstIndexOf(terms, new Term(prefix, 0), Term.byPrefixOrder(prefix.length()));
	}

	public static void main(String[] args) {
		Term[] emptyTerm = new Term[0];
		Autocomplete test = new Autocomplete(emptyTerm);
		int first = BinarySearchDeluxe.firstIndexOf(emptyTerm, new Term("Beef", 0),
				Term.byPrefixOrder("Beef".length()));
		int last = BinarySearchDeluxe.lastIndexOf(emptyTerm, new Term("Beef", 0), Term.byPrefixOrder("Beef".length()));
		int result = first - last;
		System.out.println(result);
		System.out.println(test.numberOfMatches("Beef"));

	}
}
