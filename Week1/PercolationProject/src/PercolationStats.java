import edu.princeton.cs.algs4.*;

public class PercolationStats {
    private Percolation perc;
    private double[] experiments;

    public PercolationStats(int n, int trials)
    {
        if (n <= 0 || trials <= 0)
        {
            throw new IllegalArgumentException("Ctor wrong arguments");
        }

        experiments = new double[trials];

        boolean needDraw = true;

        if (needDraw)
            StdDraw.enableDoubleBuffering();

        for(int i = 0; i < trials; ++i)
        {
            Stopwatch timer = new Stopwatch();
            perc = new Percolation(n);

            if (needDraw)
            {
                PercolationVisualizer.draw(perc, n);
                StdDraw.show();
                StdDraw.pause(100);
            }

            while (!perc.percolates())
            {
                perc.open(StdRandom.uniform(n) + 1, StdRandom.uniform(n) + 1);

                if (needDraw)
                {
                    PercolationVisualizer.draw(perc, n);
                    StdDraw.show();
                    StdDraw.pause(100);
                }
            }

            double time = timer.elapsedTime();
            StdOut.println("Experiment " + i + " time: " + time + " sec");
            experiments[i] = (double)perc.numberOfOpenSites()/(n * n);
        }
    }

    public double mean()
    {
        return StdStats.mean(experiments);
    }

    public double stddev()
    {
        return StdStats.stddev(experiments);
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
