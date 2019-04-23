package a01;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/**
 * PercolationStats is used in combination with Percolation
 * to test T many percolating systems(of size N * N) for the number of open sites
 * as a percentage required for percolation to occur.  This data is
 * then taken and an overall mean is calculated along with a standard 
 * deviation. Additionally, a confidence interval is calculated to help provide
 * an idea of accuracy to our other results.
 * 
 * @author Michael Swenson, Tara Swenson
 *
 */
public class PercolationStats {
	
	private int timesTested;
	private double[] openSitePercentages;
	private Percolation percolationStatsTester;

	/**
	 * Performs T independent experiments on an N­by­N grid
	 * 
	 * @param N
	 * @param T
	 */
	public PercolationStats(int N, int T){
		if(N <= 0 || T <= 0){
			throw new IllegalArgumentException("N  & T Must not be equal or Less than 0");
		}
		
		int randomI;
		int randomJ;
		this.timesTested = 0;
		this.openSitePercentages = new double[T];
		
		for(int i = 0; i < T; i++){
			double openSites = 0;
			percolationStatsTester = new Percolation(N);
			
			while(!percolationStatsTester.percolates()){
				randomI = StdRandom.uniform(0, N);
				randomJ = StdRandom.uniform(0, N);
				
				//You must check if the site isOpen otherwise openSites will increment
				//on sites that are possibly open already
				if(!percolationStatsTester.isOpen(randomI, randomJ)){
					percolationStatsTester.open(randomI, randomJ);
					openSites++;
				}
			}
			
			//Expectation is this will be ~59% per entry and overall
			openSitePercentages[i] = openSites/(N * N);
			timesTested++;
		}
	}
	
	/**
	 * Samples mean of percolation threshold
	 * 
	 * @return
	 */
	public double mean(){
		return StdStats.mean(openSitePercentages);
	} 
	
	/**
	 * Samples standard deviation of percolation threshold
	 * 
	 * @return
	 */
	public double stddev(){
		return StdStats.stddev(openSitePercentages);
	}
	
	/**
	 * Returns the low endpoint of 95% confidence interval.
	 * 
	 * @return
	 */
	public double confidenceLow(){
		return (mean() - ((1.96 * stddev())) / Math.sqrt(timesTested));
	}
	
	/**
	 * Returns the high endpoint of 95% confidence interval.
	 * 
	 * @return
	 */
	public double confidenceHigh(){
		return mean() + ((1.96 * stddev()) / Math.sqrt(timesTested));
	}
}