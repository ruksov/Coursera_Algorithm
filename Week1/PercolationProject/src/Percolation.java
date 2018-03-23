import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
private final WeightedQuickUnionUF quickUnionUF;
private boolean[] isOpenSites;
private int openSiteCount = 0;
private final int topSite = 0;
private final int botSite = 1;
private final int siteOffset = 2;
private final int gridSize;

public Percolation(int n)
{
    if (n <= 0)
    {
        throw new IllegalArgumentException("Ctor wrong grid size: " + n);
    }

    quickUnionUF = new WeightedQuickUnionUF(n * n + 2);
    isOpenSites = new boolean[n * n];
    gridSize = n;
}

public void open(int row, int col)
{
    if (!isInRange(row, col))
    {
        throw new IllegalArgumentException("Wrong arguments row: " + row + " col: " + col);
    }

    if (isOpen(row, col))
    {
        return;
    }

    ++openSiteCount;

    int siteIndex = getSiteIndex(row, col);
    isOpenSites[siteIndex] = true;

    if (siteIndex % gridSize < gridSize - 1 && isOpenSites[siteIndex + 1])
        quickUnionUF.union(siteIndex + siteOffset, siteIndex + 1 + siteOffset);
    if (siteIndex % gridSize > 0 && isOpenSites[siteIndex - 1])
        quickUnionUF.union(siteIndex + siteOffset, siteIndex - 1 + siteOffset);
    if (siteIndex / gridSize > 0 && isOpenSites[siteIndex - gridSize])
        quickUnionUF.union(siteIndex + siteOffset, siteIndex - gridSize + siteOffset);
    if (siteIndex / gridSize < gridSize - 1 && isOpenSites[siteIndex + gridSize])
        quickUnionUF.union(siteIndex + siteOffset, siteIndex + gridSize + siteOffset);

    // Try to connect with virtual top and bottom site
    if (row == 1)
        quickUnionUF.union(topSite, siteIndex + siteOffset);

    boolean isNearFull = (siteIndex % gridSize < gridSize - 1 && quickUnionUF.connected(topSite, siteIndex + 1 + siteOffset))
            || (siteIndex % gridSize > 0 && quickUnionUF.connected(topSite, siteIndex - 1 + siteOffset))
            || (siteIndex / gridSize > 0 && quickUnionUF.connected(topSite, siteIndex - gridSize + siteOffset));

    if (row == gridSize && isNearFull)
        quickUnionUF.union(botSite, siteIndex + siteOffset);
}

public boolean isOpen(int row, int col)
{
    if (!isInRange(row, col))
    {
        throw new IllegalArgumentException("Wrong arguments row: " + row + " col: " + col);
    }

    int siteIndex = getSiteIndex(row, col);
    return isOpenSites[siteIndex];
}

public boolean isFull(int row, int col)
{
    if (!isInRange(row, col))
    {
        throw new IllegalArgumentException("Wrong arguments row: " + row + " col: " + col);
    }

    return quickUnionUF.connected(topSite, getSiteIndex(row, col) + siteOffset);
}

public int numberOfOpenSites()
{
    return openSiteCount;
}

public boolean percolates()
{
    return quickUnionUF.connected(topSite, botSite);
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
