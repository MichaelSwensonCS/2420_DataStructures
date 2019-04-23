package a01;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * Models a percolation system using an N by N
 * grid of sites. Each site is either open or blocked. A
 * full site is an open site that can be connected to an open site in the top row
 * via a chain of neighboring (left,right, up, down) open sites. 
 * We say the system percolates if there is a full site in the bottom row.
 * 
 * For example:
 * The open sites correspond to empty space through which water might flow, so that a
 * system that percolates lets water fill open sites, flowing from top to bottom.
 * 
 * Assignment A01 Percolation
 * CSIS 2420
 * @author Michael Swenson, Tara Swenson
 *
 */
public class Percolation {
	private boolean[] grid;
	private int columnRowSize;
	private int virtualTop;
	private int virtualBottom;
	private WeightedQuickUnionUF percolationTracker;
	//Used to manage Back wash (Fullness flowing appropriately in the bottom row)
	private WeightedQuickUnionUF weightedFullnessUF;
	
	/**
	 * Creates N­by­N grid, with all sites blocked
	 * 
	 * @param N
	 */
	public Percolation(int N){
		if(N <= 0){
			throw new IllegalArgumentException("N Must not be equal or Less than 0");
		}
		this.columnRowSize = N;
		
		//+2 accounts for virtualTop and VirtualBottom
		this.percolationTracker = new WeightedQuickUnionUF(N * N + 2);
		//+1 Accounts for virtualTop, Does not include virtualBottom
		this.weightedFullnessUF = new WeightedQuickUnionUF(N * N + 1);
		
		this.grid = new boolean[N * N + 2];
		this.virtualTop = getGridID(N-1, N-1) + 1;
		this.virtualBottom = getGridID(N-1, N-1) + 2;
		
	}
	
	////////////////////////////// PUBLIC METHODS ////////////////////////////////////////////////////////////////////
	/**
	 * Opens site (row i, column j) if it is not open already
	 * 
	 * @param i
	 * @param j
	 */
	public void open(int i, int j){
		checkBoundaries(i, j);
		
		if (!isOpen(i,j)){
			grid[getGridID(i,j)] = true;
			checkSurroundingTiles(i, j);
		}
	}
	
	/**
	 * Checks whether site (row i, column j) is open.
	 * 
	 * @param i
	 * @param j
	 * @return
	 */
	public boolean isOpen(int i, int j){
		checkBoundaries(i,j);
		return grid[getGridID(i,j)];
	}
	
	/**
	 * Checks whether the site (row i, column j) is full.
	 * 
	 * @param i
	 * @param j
	 * @return Boolean
	 */
	public boolean isFull(int i, int j){
		checkBoundaries(i,j);
		return weightedFullnessUF.connected(getGridID(i,j), virtualTop);
	} 
	
	/**
	 * Checks whether the system system percolates.
	 * 
	 * @return Boolean
	 */
	public boolean percolates(){
		return percolationTracker.connected(virtualTop, virtualBottom);
	} 
	////PUBLIC METHODS END///////////////////////////////////////////////////////////////////////////
	
	////////////////////////// REFACTORED METHODS ///////////////////////////////////////////////////
	/**
	 * Takes the 2d grid position and returns the value 
	 * into a 1d grid position
	 * 
	 * @param i
	 * @param j
	 * @return
	 */
	private int getGridID(int i, int j){
		checkBoundaries(i,j);
		return  j * columnRowSize + i;
	}
	/** 
	 * Checks the surrounding tiles of the newly opened tile and
	 * performs a union it also unions on the fullnessQuickFind
	 * to manage back wash
	 * 
	 * @param i
	 * @param j
	 */
	private void checkSurroundingTiles(int i, int j) {
		int gridSpace = getGridID(i, j);
		
		//If i is in the first row union to virtual top and fullness
		if(i == 0){
			weightedFullnessUF.union(virtualTop, gridSpace);
			percolationTracker.union(virtualTop, gridSpace);
		}
		//If I is in the last row union to bottom
		if(i == columnRowSize - 1){
			percolationTracker.union(virtualBottom, gridSpace);
		}
		//Checks the Top
		if(i > 0 && isOpen(i-1, j)){ 
			percolationTracker.union(gridSpace, getGridID(i-1, j));
			weightedFullnessUF.union(gridSpace, getGridID(i-1, j));
		}
		//Checks the Bottom
		if(i < columnRowSize - 1 && isOpen(i+1, j)){
			percolationTracker.union(gridSpace, getGridID(i+1, j));
			weightedFullnessUF.union(gridSpace, getGridID(i+1, j));
		}
		//Checks the Left
		if(j > 0 && isOpen(i, j-1)){ 
			percolationTracker.union(gridSpace, getGridID(i, j-1));
			weightedFullnessUF.union(gridSpace, getGridID(i, j-1));
		}
		//Checks the Bottom
		if(j < columnRowSize - 1 && isOpen(i, j+1)){
			percolationTracker.union(gridSpace, getGridID(i, j+1));
			weightedFullnessUF.union(gridSpace, getGridID(i, j+1));
		}
	}

	/**
	 * Checks to make sure that the value of i and j are within 
	 * our boundaries
	 * 
	 * @param i
	 * @param j
	 */
	private void checkBoundaries(int i, int j) {
		if(i < 0 || i > columnRowSize - 1 || j < 0 || j > columnRowSize - 1){
			throw new IndexOutOfBoundsException("N Must not be equal or Less than 0");
		}
	}
	//////// END REFACTORED METHODS //////////////////////////////////////////////////////////////////////
}