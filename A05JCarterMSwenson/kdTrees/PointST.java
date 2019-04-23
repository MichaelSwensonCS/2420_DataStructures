package kdTrees;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.RedBlackBST;
import edu.princeton.cs.algs4.StdOut;

/**
 * The PointST class represents an ordered symbol table of generic key-value
 * pairs. Utilizes the RedBlackBST class created by Robert Sedgewick
 *
 * @author Jason Carter
 * @author Michael Swenson
 */
public class PointST<Value> {

	// RedBlackBST balanced tree field
	private RedBlackBST<Point2D, Value> tree;

	/**
	 * Constructs an empty table of symbol points
	 */
	public PointST() {
		tree = new RedBlackBST<>();
	}

	/**
	 * Is it empty?
	 * 
	 * @return {true} if this symbol table is empty
	 */
	public boolean isEmpty() {
		return tree.isEmpty();
	}

	/**
	 * Returns the number of key-value pairs.
	 * 
	 * @return the number of key-value pairs
	 */
	public int size() {
		return tree.size();
	}

	/**
	 * Inserts a key-value pair into the symbol table, and associates the Value
	 * val with the point p
	 * 
	 * @param p
	 *            the Point2D
	 * @param val
	 *            the value
	 * @throws IllegalArgumentException
	 *             if the Point2D or Value is null
	 */
	public void put(Point2D p, Value val) {
		if (p == null || val == null) {
			throw new java.lang.NullPointerException();
		}
		tree.put(p, val);
	}

	/**
	 * Returns the value associated with point p.
	 * 
	 * @param p
	 *            the Point2D
	 * @return the value associated with p if it is in the symbol table
	 * @throws IllegalArgumentException
	 *             if Point2D is null
	 */
	public Value get(Point2D p) {
		if (p == null) {
			throw new java.lang.NullPointerException();
		}
		return tree.get(p);

	}

	/**
	 * Does this symbol table contain the point p?
	 * 
	 * @param p
	 *            the Point2D
	 * @return true if this symbol table contains Point2D p
	 * @throws IllegalArgumentException
	 *             if Point2D is null
	 */
	public boolean contains(Point2D p) {
		if (p == null) {
			throw new java.lang.NullPointerException();
		}
		return get(p) != null;
	}

	/**
	 * Returns all keys in the tree as an Iterable.
	 * 
	 * @return all keys in the symbol table
	 */
	public Iterable<Point2D> points() {
		return tree.keys();
	}

	/**
	 * Returns all points within a rectangle using a queue.
	 * 
	 * @return all points inside the rectangle
	 * @throws IllegalArgumentException
	 *             if the rectangle is null
	 */
	public Iterable<Point2D> range(RectHV rect) {
		if (rect == null) {
			throw new java.lang.NullPointerException();
		}
		Queue<Point2D> pointQ = new Queue<Point2D>();
		for (Point2D p : tree.keys()) {
			if (rect.contains(p)) {
				pointQ.enqueue(p);
			}
		}
		return pointQ;

	}

	/**
	 * Returns nearest neighbor to a point. This uses the distanceTo method for
	 * comparison. It will return null if the symbol table is empty.
	 * 
	 * @return all points inside the rectangle
	 * @throws IllegalArgumentException
	 *             if the Point2D is null
	 */
	public Point2D nearest(Point2D p) {
		if (p == null) {
			throw new java.lang.NullPointerException();
		}

		if (tree.isEmpty())
			return null;

		Point2D p2 = null;
		for (Point2D current : tree.keys()) {
			if (p2 == null) {
				p2 = current;
			}
			if (current.distanceTo(p) < p2.distanceTo(p)) {
				p2 = current;
			}

		}
		return p2;
	}

	// unit testing of the methods
	public static void main(String[] args) {

		StdOut.println("Hello");

		PointST<Integer> trial = new PointST<Integer>();
		trial.put(new Point2D(1, 1), 1);
		trial.put(new Point2D(2.5, 2.5), 2);
		trial.put(new Point2D(3, 3), 3);
		trial.put(new Point2D(3.5, 3.5), 4);
		trial.put(new Point2D(4, 4), 5);
		trial.put(new Point2D(4.5, 4.5), 6);
		trial.put(new Point2D(5, 5), 7);

		RectHV trialRect = new RectHV(0, 0, 2, 2);
		StdOut.println(trial.range(trialRect));

		StdOut.println(trial.nearest(new Point2D(3, 3)));
		StdOut.println(trial.contains(new Point2D(3, 3)));

	}

}