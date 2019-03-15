/* *****************************************************************************
 *  Name: Christopher Aguilera
 *  Date: March 14, 2019
 *  Description: Estimates the percolation threshold for an n-by-n grid. Program
 *  accepts two command-line arguments n and T, performs T independent
 *  computational experiments on an n-by-n grid, and prints the sample mean,
 *  sample standard deviation, and the 95% confidence interval for the
 *  percolation threshold.
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private static final double CONFIDENCE_95 = 1.96;

    private final int trials;
    private final double mean;
    private final double stddev;

    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("N and Trials must be greater than 0");
        }

        this.trials = trials;

        double[] percThresholds = new double[trials];

        // Run trials
        for (int i = 0; i < trials; i++) {
            Percolation perc = new Percolation(n);
            while (!perc.percolates()) {
                //  Random row and col between 1 and n that is not currently open
                int randRow = StdRandom.uniform(n) + 1;
                int randCol = StdRandom.uniform(n) + 1;
                while (perc.isOpen(randRow, randCol)) {
                    randRow = StdRandom.uniform(n) + 1;
                    randCol = StdRandom.uniform(n) + 1;
                }
                perc.open(randRow, randCol);
            }
            double numOpenSites = (double) perc.numberOfOpenSites();
            percThresholds[i] = numOpenSites / (double) (n * n);
        }

        // Calculate mean and stddev once
        mean = StdStats.mean(percThresholds);
        if (trials == 1) {
            stddev = Double.NaN;
        }
        else {
            stddev = StdStats.stddev(percThresholds);
        }

    }

    public double mean() {
        return mean;
    }

    public double stddev() {
        return stddev;
    }

    public double confidenceLo() {
        return mean - ((CONFIDENCE_95 * stddev) / Math.sqrt((double) trials));
    }

    public double confidenceHi() {
        return mean + ((CONFIDENCE_95 * stddev) / Math.sqrt((double) trials));
    }


    public static void main(String[] args) {
        if (args.length != 2) {
            throw new IllegalArgumentException(
                    "Must include two arguments: n and number of trials");
        }
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats pStats = new PercolationStats(n, trials);
        System.out.println("mean                    = " + pStats.mean());
        System.out.println("stddev                  = " + pStats.stddev());
        System.out.println(
                "95% confidence interval = [" + pStats.confidenceLo() + ", " + pStats.confidenceHi()
                        + "]");
    }
}
