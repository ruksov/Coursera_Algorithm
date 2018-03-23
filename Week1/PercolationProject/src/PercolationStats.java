import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private final double[] experiments;
    private double mMean = 0;
    private double mStdenv = -1;

    public PercolationStats(int n, int trials)
    {
        if (n <= 0 || trials <= 0)
        {
            throw new IllegalArgumentException("Ctor wrong arguments");
        }

        experiments = new double[trials];

        for (int i = 0; i < trials; ++i)
        {
            Percolation perc = new Percolation(n);

            while (!perc.percolates())
            {
                perc.open(StdRandom.uniform(n) + 1, StdRandom.uniform(n) + 1);
            }

            experiments[i] = (double) perc.numberOfOpenSites()/(n * n);
        }
    }

    public double mean()
    {
        return mMean == 0 ? mMean = StdStats.mean(experiments) : mMean;
    }

    public double stddev()
    {
        return mStdenv < 0 ? mStdenv = StdStats.stddev(experiments) : mStdenv;
    }

    public double confidenceLo()
    {
        return mean() - delta();
    }

    public double confidenceHi()
    {
        return mean() + delta();
    }

    private double delta()
    {
        return (1.96 * stddev())/Math.sqrt(experiments.length);
    }

    public static void main(String[] args) {
        PercolationStats ps = new PercolationStats(20, 40);

        StdOut.println("mean = " + ps.mean());
        StdOut.println("stdenv = " + ps.stddev());
        StdOut.println("conf interval = [" + ps.confidenceLo() + ", " + ps.confidenceHi() + "]");
    }
}
