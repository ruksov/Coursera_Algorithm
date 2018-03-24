import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

private final WeightedQuickUnionUF ufPerc;
private final WeightedQuickUnionUF uf;
private byte[] sites;           // 0 - blocked, 1 - open
private int openSiteCount = 0;
private final int topSite;
private final int botSite;
private final int gridSize;

public Percolation(int n)
{
    if (n <= 0)
    {
        throw new IllegalArgumentException("Ctor wrong grid size: " + n);
    }

    uf = new WeightedQuickUnionUF(n * n + 2);
    ufPerc = new WeightedQuickUnionUF(n * n + 2);
    sites = new byte[n * n];
    topSite = n * n;
    botSite = n * n + 1;
    gridSize = n;
}

public void open(int row, int col)
{
    if (!isInRange(row, col))
    {
        throw new IllegalArgumentException("Wrong arguments row: " + row + " col: " + col);
    }

    if (isOpen(row, col) || isFull(row, col))
    {
        return;
    }

    ++openSiteCount;
    int siteIndex = getSiteIndex(row, col);
    sites[siteIndex] = 1;

    // Try to connect with virtual top site
    if (row == 1)
    {
        uf.union(siteIndex, topSite);
        ufPerc.union(siteIndex, topSite);
    }

    // Try to connect with virtual bottom site
    if (row == gridSize)
    {
        // Not union in uf container to avoid errors with isFull method
        ufPerc.union(siteIndex, botSite);
    }

    // Try to connect with up site
    if (row > 1 && isOpen(row - 1, col))
    {
        uf.union(siteIndex, getSiteIndex(row - 1, col));
        ufPerc.union(siteIndex, getSiteIndex(row - 1, col));
    }

    // Try to connect with left site
    if (col > 1 && isOpen(row, col - 1))
    {
        uf.union(siteIndex, getSiteIndex(row, col - 1));
        ufPerc.union(siteIndex, getSiteIndex(row, col - 1));
    }

    // Try to connect with down sil
    if (row < gridSize && isOpen(row + 1, col))
    {
        uf.union(siteIndex, getSiteIndex(row + 1, col));
        ufPerc.union(siteIndex, getSiteIndex(row + 1, col));
    }

    // Try to connect with left sil
    if (col < gridSize && isOpen(row, col + 1))
    {
        uf.union(siteIndex, getSiteIndex(row, col + 1));
        ufPerc.union(siteIndex, getSiteIndex(row, col + 1));
    }
}

public boolean isOpen(int row, int col)
{
    if (!isInRange(row, col))
    {
        throw new IllegalArgumentException("Wrong arguments row: " + row + " col: " + col);
    }

    return sites[getSiteIndex(row, col)] == 1;
}

public boolean isFull(int row, int col)
{
    if (!isInRange(row, col))
    {
        throw new IllegalArgumentException("Wrong arguments row: " + row + " col: " + col);
    }

    return uf.connected(getSiteIndex(row, col), topSite);
}

public int numberOfOpenSites()
{
    return openSiteCount;
}

public boolean percolates()
{
    return ufPerc.connected(topSite, botSite);
}

private int getSiteIndex(int row, int col)
{
    return (col - 1) + (row - 1) * gridSize;
}

private boolean isInRange(int row, int col)
{
    return row >= 1 && row <= gridSize && col >= 1 && col <= gridSize;
}

}
