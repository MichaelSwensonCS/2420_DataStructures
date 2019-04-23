package autocomplete;

import static org.junit.Assert.*;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import autocomplete.BinarySearchDeluxe;


public class BinarySearchDeluxeTest{
	private final String[] keys = {"apple", "banana", "orange"};
	private final  Integer[] numbers1 = {10,  9,  8, 7, 6, 5, 4, 3, 2, 1};
	private final  Integer[] numbers2 = {10,  9,  8, 8, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 6, 5, 4, 3, 2, 1};
	private final  Integer[] numbers3 = {10,  9,  8, 8, 8, 7, 6, 5, 4, 3, 2, 1};


	@Before
	public void setUp() throws Exception {
	}

	@Test 
	public void testFirstIndexOf_nullArgument() {
		String message = "NullPoiterException expected ";

		try{ BinarySearchDeluxe.firstIndexOf(null, "banana", String.CASE_INSENSITIVE_ORDER); }
		catch (NullPointerException e) { }
		catch (Exception e) { message += "when array is null "; }
		
		try{ BinarySearchDeluxe.firstIndexOf(keys, null, String.CASE_INSENSITIVE_ORDER); }
		catch (NullPointerException e) { }
		catch (Exception e) { message += "when key is null "; }		
		
		try{ BinarySearchDeluxe.firstIndexOf(keys, "banana", null); }
		catch (NullPointerException e) { }
		catch (Exception e) {  message += "when comparator is null "; }
		
		assertEquals("NullPoiterException expected ", message);
	}

	@Test 
	public void testFirstIndexOf_singleOccurance() {
		int firstIndex = BinarySearchDeluxe.firstIndexOf(numbers1, 7, Collections.reverseOrder());
		assertEquals(3, firstIndex);
		
		firstIndex = BinarySearchDeluxe.firstIndexOf(numbers1, 10, Collections.reverseOrder());
		assertEquals(0, firstIndex);
	}

	@Test 
	public void testFirstIndexOf_multipleOccurance() {
		int firstIndex = BinarySearchDeluxe.firstIndexOf(numbers2, 7, Collections.reverseOrder());
		assertEquals(4, firstIndex);
		
		firstIndex = BinarySearchDeluxe.firstIndexOf(numbers3, 8, Collections.reverseOrder());
		assertEquals(2, firstIndex);
	}
	
	@Test 
	public void testLastIndexOf_nullArgument() {
		String message = "NullPoiterException expected ";

		try{ BinarySearchDeluxe.lastIndexOf(null, "banana", String.CASE_INSENSITIVE_ORDER); }
		catch (NullPointerException e) { }
		catch (Exception e) { message += "when array is null "; }
		
		try{ BinarySearchDeluxe.lastIndexOf(keys, null, String.CASE_INSENSITIVE_ORDER); }
		catch (NullPointerException e) { }
		catch (Exception e) { message += "when key is null "; }		
		
		try{ BinarySearchDeluxe.lastIndexOf(keys, "banana", null); }
		catch (NullPointerException e) { }
		catch (Exception e) {  message += "when comparator is null "; }
		
		assertEquals("NullPoiterException expected ", message);
	}
	
	@Test 
	public void testLastIndexOf_singleOccurance() {
		int firstIndex = BinarySearchDeluxe.lastIndexOf(numbers1, 7, Collections.reverseOrder());
		assertEquals(3, firstIndex);
		
		firstIndex = BinarySearchDeluxe.lastIndexOf(numbers1, 1, Collections.reverseOrder());
		assertEquals(9, firstIndex);

	}

	@Test 
	public void testLastIndexOf_multipleOccurance() {
		int firstIndex = BinarySearchDeluxe.lastIndexOf(numbers3, 8, Collections.reverseOrder());
		assertEquals(4, firstIndex);
		
		firstIndex = BinarySearchDeluxe.lastIndexOf(numbers2, 7, Collections.reverseOrder());
		assertEquals(23, firstIndex);
	}

}
