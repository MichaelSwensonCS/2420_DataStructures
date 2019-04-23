package autocomplete;

import java.util.Comparator;

/**
 * Return the number of terms that start with the given prefix.
 * 
 * @param prefix
 * @return number of terms with matching prefix
 */
public class Term implements Comparable<Term> {
	private String query;
	private double weight;

	/**
	 * Initialize a term with the given query string and weight.
	 * 
	 * @param query
	 * @param weight
	 */
	public Term(String query, double weight) {
		if (query == null) {
			throw new NullPointerException();
		}
		if (weight < 0) {
			throw new IllegalArgumentException();
		}
		this.query = query;
		this.weight = weight;
	}

	/**
	 * Compare the terms in descending order by weight.
	 * 
	 * @return a new ReverseWeightComparator
	 */
	public static Comparator<Term> byReverseWeightOrder() {
		return new ReverseWeightComparator();
	}

	/**
	 * Compares terms in descending weight order.
	 */
	private static class ReverseWeightComparator implements Comparator<Term> {
		public int compare(Term t1, Term t2) {
			return Double.compare(t2.weight, t1.weight);
		}
	}

	/**
     * Compare the terms in lexicographic order but using only the first r 
     * characters of each query.
     * @param r number of characters in the prefix
     * @return
     */
	public static Comparator<Term> byPrefixOrder(int r) {
		if (r < 0) {
			throw new NullPointerException();
		}

		final int numberOfCharactersToRead = r;
		return new Comparator<Term>() {

			@Override
			public int compare(Term t1, Term t2) {
				int t1Length = t1.query.length();
				int t2Length = t2.query.length();

				int smallestString = t1Length < t2Length ? t1Length : t2Length;

				if (numberOfCharactersToRead <= smallestString) {
					return t1.query.substring(0, numberOfCharactersToRead)
							.compareToIgnoreCase(t2.query.substring(0, numberOfCharactersToRead));
				} else {
					return t1.query.substring(0, smallestString)
							.compareToIgnoreCase(t2.query.substring(0, smallestString));
				}
			}
		};
	}

	/**
     * Compare the terms in lexicographic order by query.
     */
   	public int compareTo(Term that) {
		return this.query.compareTo(that.query);
	}

   	/**
     *  Return a string representation of the term in the following format:
     *  the weight, followed by a tab, followed by the query.
     */
	public String toString() {
		return String.format("%.1f\t%s", weight, query);
	}
}
