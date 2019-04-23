package autocomplete;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import autocomplete.Term;

public class TermTest {

	private List<Term> terms;
	private Term t = new Term("company", 133159.0);
	
	
	@Before
	public void setUp() throws Exception {
		terms = Arrays.asList(
				new Term("company", 133159.0),
				new Term("complete", 78039.8),
				new Term("companion", 60384.9),
				new Term("completely", 52050.3),
				new Term("comply", 44817.7)
		);				
	}

	@Test(expected = NullPointerException.class)
	public void testTerm_nullQuery() {
		new Term(null, 1);
	}
	
	@Test(expected = IllegalArgumentException .class)
	public void testTerm_weightNegative() {
		new Term("query", -1);
	}
	
	@Test
	public void testByReverseOrder() {
		String termsToString = "[133159.0\tcompany, 78039.8\tcomplete, " +
				"60384.9\tcompanion, 52050.3\tcompletely, 44817.7\tcomply]";
			Collections.sort(terms, Term.byReverseWeightOrder());
			assertEquals(termsToString, terms.toString());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testByPrefixOrder_rNegative() {
		Term.byPrefixOrder(-1);
	}
	
	@Test
	public void testByPrefixOrder() {
		String termsToString = "[133159.0\tcompany, 60384.9\tcompanion, " +
				"78039.8\tcomplete, 52050.3\tcompletely, 44817.7\tcomply]";
			Collections.sort(terms, Term.byPrefixOrder(5));
			assertEquals(termsToString, terms.toString());
	}

	@Test
	public void testCompareTo() {
		String termsToString = "[60384.9\tcompanion, 133159.0\tcompany, " +
			"78039.8\tcomplete, 52050.3\tcompletely, 44817.7\tcomply]";
		Collections.sort(terms);
		assertEquals(termsToString, terms.toString());
	}

	@Test
	public void testToString() {
		assertEquals("133159.0\tcompany", t.toString());
	}
	
	//////////////////////////////////////////////////////////////////////
	
	private final Term[] terms2 = { new Term("January", 1.0), new Term("February", 2.0), new Term("March", 3.0) };

	@Test
	public void testByReverseWeightOrder() throws Exception
	{
		Term[] actual = terms2.clone();
		Term[] expected = new Term[actual.length];

		for(int u = 0, v = actual.length - 1; v >= 0 && u < actual.length; ++u, --v)
		{
			expected[u] = actual[v];
		}

		Arrays.sort(actual, Term.byReverseWeightOrder());

		assertTrue(Arrays.equals(actual, expected));
	}

	@Test
	public void testByPrefixOrder2() throws Exception
	{
		Term term1 = new Term("Elephant", 1.0);
		Term term2 = new Term("Eleanor", 2.0);
		Term term3 = new Term("Elective", 3.0);
		Term term4 = new Term("Election", 4.0);

		Term[] actual = {term1, term2, term3, term4 };

		Arrays.sort(actual, Term.byPrefixOrder(3));
		Term[] expected = {term1, term2, term3, term4};
		assertTrue(Arrays.equals(actual, expected));

		Arrays.sort(actual, Term.byPrefixOrder(5));
		expected = new Term[]{term2, term3, term4, term1};
		assertTrue(Arrays.equals(actual, expected));
	}

	@Test
	public void testCompareTo2() throws Exception
	{
		assertTrue(terms2[0].compareTo(terms2[0]) == 0);
		assertTrue(terms2[0].compareTo(terms2[1]) > 0);
		assertTrue(terms2[1].compareTo(terms2[0]) < 0);
	}

	@Test
	public void testToString2() throws Exception
	{
		assertTrue(terms2[0].toString().equals("1.0\tJanuary"));
		assertTrue(terms2[1].toString().equals("2.0\tFebruary"));
		assertTrue(terms2[2].toString().equals("3.0\tMarch"));
	}

}
