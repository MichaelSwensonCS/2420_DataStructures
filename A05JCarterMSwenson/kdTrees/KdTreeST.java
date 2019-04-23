package kdTrees;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;

/**
 * Class KdTreeST provides an efficient way to insert and 
 * search a 2D graph of points.  Each point splits the graph vertically
 * or horizontally creating sectors associated with each point.
 * This information is used to efficiently search a rectangular 
 * query or for a point closest to a searched point.
 * 
 * @author Michael Swenson, Jason Carter
 * 
 *
 * @param <Value>
 */
public class KdTreeST<Value> {
	private int totalNodes;
	private Node root;

	private Point2D nearestPoint;

	private class Node {
		private Node left;
		private Node right;
		private boolean isVertical = true;
		private Point2D currentPoint;
		private RectHV currentRectangle;
		private Value currentVal;
		
		//Every Node contains all the information needed to put it in the correct place in the structure
		private Node(Point2D p, Value v, Node right, Node left, RectHV rec, boolean isVertical) {
			this.currentPoint = p;
			this.currentVal = v;
			this.right = right;
			this.left = left;
			this.currentRectangle = rec;
			this.isVertical = isVertical;
		}

	}

	/**
	 * Creates an empty tree to be used
	 */
	public KdTreeST() {
		Node root = null;
		totalNodes = 0;
	}

	/**
	 * Checks if there are any points
	 * @return boolean
	 */
	public boolean isEmpty() {
		return totalNodes == 0;
	}

	/**
	 * Return total number of points
	 * @return int
	 */
	public int size() {
		return totalNodes;
	}


	/**
	 * Inserts a point into the structure
	 * @param Point2D
	 * @param Associated Value
	 */
	public void put(Point2D p, Value v) {
		if (p == null || v == null) {
			throw new NullPointerException();
		}
		root = put(root, p, v, new RectHV(Double.MIN_VALUE, Double.MIN_VALUE, Double.MAX_VALUE, Double.MAX_VALUE),
				true);
	}

	//Complex method that calculates the rectangle of each point and creates a node associated with the given point
	private Node put(KdTreeST<Value>.Node node, Point2D p, Value v, RectHV r, boolean verticalLine) {
		boolean pointLessThan = false;
		boolean isVertical = verticalLine;

		//Every node's rectangle is related to it's parent
		//This modifies the nodes rectangle in the recursive call
		double xMin = r.xmin();
		double xMax = r.xmax();
		double yMin = r.ymin();
		double yMax = r.ymax();

		//Creates the root here also any time the recursion reaches a branch that is null
		if (node == null) {
			r = new RectHV(xMin, yMin, xMax, yMax);
			totalNodes++;
			return new Node(p, v, null, null, r, isVertical);
		}

		//Decide how we are going to compare points based on how they are splitting the graph
		double cmp = node.isVertical ? Double.compare(p.x(), node.currentPoint.x())
				: Double.compare(p.y(), node.currentPoint.y());
		if (cmp < 0) {
			pointLessThan = true;
		}
		//The four possibilities of how a rectangle is compared and how the child is affected
		
		//Note these need to be looked at and changed into if/elseif/else where applicable
		if (pointLessThan && node.isVertical) {
			xMax = node.currentPoint.x();
		}
		if (!pointLessThan && node.isVertical)
			xMin = node.currentPoint.x();

		if (pointLessThan && !node.isVertical) {
			yMax = node.currentPoint.y();
		}
		if (!pointLessThan && !node.isVertical) {
			yMin = node.currentPoint.y();
		}

		r = new RectHV(xMin, yMin, xMax, yMax);

		//Going Left
		if (pointLessThan) {
			//Every child splits the graph opposite it's parent
			isVertical = !node.isVertical;
			node.left = put(node.left, p, v, r, isVertical);
		
		//Going Right
		} else if (!pointLessThan) {
			//Every child splits the graph opposite it's parent
			isVertical = !node.isVertical;
			node.right = put(node.right, p, v, r, isVertical);
		}
		//If cmp is 0 point already exists, update it's value
		if (cmp == 0) {
			totalNodes--;
			node.currentVal = v;
		}
		return node;
	}

	/**
	 * Returns the value associated with
	 * the queried point
	 * @param p
	 * @return Value
	 */
	public Value get(Point2D p) {
		if (p == null) {
			throw new NullPointerException();
		}

		return get(root, p);
	}

	// Recursively checks for the point based on the comparator value
	private Value get(KdTreeST<Value>.Node node, Point2D p) {
		if (node == null)
			return null;
		
		//Decide how we are going to compare points based on how they are splitting the graph
		double cmp = node.isVertical ? Double.compare(p.x(), node.currentPoint.x())
				: Double.compare(p.y(), node.currentPoint.y());
		if (cmp < 0)
			return get(node.left, p);
		else if (cmp > 0)
			return get(node.right, p);
		else
			return node.currentVal;

	}

	/**
	 * Looks for the queried point
	 * and returns true if it exists
	 * @param p
	 * @return boolean
	 */
	public boolean contains(Point2D p) {
		return get(p) != null;
	}

	/**
	 * Creates an iterable that contains all the points in the structure
	 * in level order
	 * @return Iterable<Point2D>
	 */
	public Iterable<Point2D> points() {
		Queue<Point2D> points = new Queue<Point2D>();
		Queue<Node> queue = new Queue<Node>();
		queue.enqueue(root);
		while (!queue.isEmpty()) {
			Node temp = queue.dequeue();
			points.enqueue(temp.currentPoint);
			if (temp.left != null)
				queue.enqueue(temp.left);
			if (temp.right != null)
				queue.enqueue(temp.right);
		}
		return points;
	}

	/**
	 * Searches a 2D graph using a rectangular area 
	 * query and returns any points contained inside 
	 * the queried area. Note that a point falling on
	 * the boundary of the rectangle will be treated as 
	 * being inside of it. See also {@code RectHV} 
	 * @param RectHV(xmin, ymin, xmax, ymax)
	 * @return Iterable of all contained points
	 */
	public Iterable<Point2D> range(RectHV rect) {
		if (rect == null) {
			throw new NullPointerException();
		}
		Queue<Point2D> rectQueryQ = new Queue<Point2D>();
		range(rect, rectQueryQ, root);
		return rectQueryQ;
	}

	//Helper method that looks at all appropriate rectangles and compares it to the query rectangle for possible points
	private void range(RectHV rect, Queue<Point2D> rectQuery, KdTreeST<Value>.Node node) {
		// Reached the end
		if (node == null) {
			return;
		}
		// Check intersection to save time looking down wrong part
		if (!rect.intersects(node.currentRectangle)) {
			return;
		// If it intersects check to see if it is contained
		} else if (rect.contains(node.currentPoint)) {
			rectQuery.enqueue(node.currentPoint);
		}
		// Check both sides
		range(rect, rectQuery, node.left);
		range(rect, rectQuery, node.right);
	}

	/**
	 * Given a Point2D in the form(x,y) it will
	 * return the point closest to the queried 
	 * point.
	 * @param queryPoint
	 * @return Point2D
	 */
	public Point2D nearest(Point2D queryPoint) {
		if (queryPoint == null) {
			throw new NullPointerException();
		}
		nearestPoint = nearest(root, queryPoint);
		return nearestPoint;
	}

	private Point2D nearest(KdTreeST<Value>.Node node, Point2D p) {
		Node insertionDecision = null;
		Node everythingElse = null;
		if (node == null) {
			return nearestPoint;
		}
		//Just in case there is only one point, also a starting point
		if (nearestPoint == null) {
			nearestPoint = root.currentPoint;
		}

		//Makes cleaner code later on
		boolean pointLessThan = false;
		
		//Decide how we are going to compare points based on how they are splitting the graph
		double cmp = node.isVertical ? Double.compare(p.x(), node.currentPoint.x())
				: Double.compare(p.y(), node.currentPoint.y());
		if (cmp < 0)
			pointLessThan = true;
		//We make the logical decision first, and then we check nonintuitive rectangles for possibilities
		if (pointLessThan) {
			insertionDecision = node.left;
			everythingElse = node.right;
		} else {
			insertionDecision = node.right;
			everythingElse = node.left;
		}
		//Check how it is split to verify what part of the rectangle we need to check
		if (node.isVertical) {
			if (p.x() > node.currentRectangle.xmin() || p.x() < node.currentRectangle.xmax()) {
				if (node.currentPoint.distanceSquaredTo(p) < nearestPoint.distanceSquaredTo(p)) {
					nearestPoint = node.currentPoint;
				}
				//If it is inside the appropriate boundaries of the rectangle, look deeper
				nearestPoint = nearest(insertionDecision, p);
				nearestPoint = nearest(everythingElse, p);
			}
		} else {
			if (p.y() > node.currentRectangle.ymin() || p.y() < node.currentRectangle.ymax()) {
				if (node.currentPoint.distanceSquaredTo(p) < nearestPoint.distanceSquaredTo(p)) {
					nearestPoint = node.currentPoint;
				}
				//If it is inside the appropriate boundaries of the rectangle, look deeper
				nearestPoint = nearest(insertionDecision, p);
				nearestPoint = nearest(everythingElse, p);
			}
		}
		return nearestPoint;
	}

	public static void main(String[] args) {
/*		KdTreeST<String> tree;
		KdTreeST<String> emptyTree;

		final Point2D[] points = { new Point2D(0.7, 0.2), new Point2D(0.5, 0.4), new Point2D(0.2, 0.3),
				new Point2D(0.4, 0.7), new Point2D(0.9, 0.6) };

		final Point2D[] pointsInLevelOrder = { new Point2D(0.7, 0.2), new Point2D(0.5, 0.4), new Point2D(0.9, 0.6),
				new Point2D(0.2, 0.3), new Point2D(0.4, 0.7) };

		tree = new KdTreeST<>();
		for (Point2D point : points) {
			tree.put(point, point.toString());
		}
		Point2D nearestTester = new Point2D(.8, .1);
		System.out.println(tree.nearest(nearestTester));
		
		System.out.println("Root: " + tree.getRoot().currentRectangle);
		System.out.println("Right: " + tree.getRoot().right.currentRectangle);
		System.out.println("Left : " + tree.getRoot().left.currentRectangle);
		System.out.println("LeftLeft : " + tree.getRoot().left.left.currentRectangle);
		System.out.println("LeftRight : " + tree.getRoot().left.right.currentRectangle);
		// xmin xmax ymin ymax
		
		System.out.println(tree.range(new RectHV(.1,.1,.6,.6)));*/

	}
}
