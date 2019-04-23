package autocomplete;

import java.util.Comparator;

/**
 * Class BinarySearchDeluxe is a modification of normal Binary Search
 * that allows for the first and last index of any key to be found in
 * a pre-sorted array.
 * 
 * @author Michael Swenson, GarretRueckert
 *
 */
public class BinarySearchDeluxe {

	/**
     * Return the index of the first key in a[] that equals the search key, or -1 if no such key.
     * @param a array being searched
     * @param key Key to be found
     * @param comparator Comparator used for searches
     * @return  -1 if not present, first index of key if present
     */
	public static <Key> int firstIndexOf(Key[] a, Key key, Comparator<Key> comparator){
    	if(a == null || key == null || comparator == null){
    		throw new NullPointerException();
    	}
        int lo = 0;
        int hi = a.length - 1;
        int result = -1;
        while (lo <= hi) {
            // Key is in a[lo..hi] or not present.
            int mid = lo + (hi - lo) / 2;
            if (comparator.compare(key, a[mid]) == 0){
            	result = mid;
            	hi = mid -1;
            }
            else if (comparator.compare(key, a[mid]) < 0) 
            	hi = mid - 1;
            else  
            	lo = mid + 1;
        }
        return result;
    }

	/**
     *  Return the index of the last key in a[] that equals the search key, or -1 if no such key.
     * @param a array being searched
     * @param key Key to be found
     * @param comparator comparator used for searches
     * @return -1 if not present, last index of key in a if present
     */
	public static <Key> int lastIndexOf(Key[] a, Key key, Comparator<Key> comparator){
        	if(a == null || key == null || comparator == null){
        		throw new NullPointerException();
        	}
            int lo = 0;
            int hi = a.length - 1;
            int result = -1;
            while (lo <= hi) {
                // Key is in a[lo..hi] or not present.
                int mid = lo + (hi - lo) / 2;
                if (comparator.compare(key, a[mid]) == 0){
                	result = mid;
                	lo = mid + 1;
                }
                else if (comparator.compare(key, a[mid]) < 0) 
                	hi = mid - 1;
                else  
                	lo = mid + 1;
            }
            return result; 
    }
}
