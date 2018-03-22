import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
private WeightedQuickUnionUF m_wq;
private boolean[] m_isOpen;
private int m_openSiteNum = 0;
private int m_top = 0;
private int m_bot = 1;
private int m_offset = 2;
private int m_len;

public Percolation(int n)
{
    if (n <= 0)
    {
        throw new IllegalArgumentException("Ctor wrong grid size: " + n);
    }

    m_wq = new WeightedQuickUnionUF( n * n + 2);
    m_isOpen = new boolean[n * n];
    m_len = n;
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

    ++m_openSiteNum;

    int siteIndex = getSiteIndex(row, col);
    m_isOpen[siteIndex] = true;

    if (siteIndex % m_len < m_len - 1 && m_isOpen[siteIndex + 1])
        m_wq.union(siteIndex + m_offset, siteIndex + 1 + m_offset);
    if (siteIndex % m_len > 0 && m_isOpen[siteIndex - 1])
        m_wq.union(siteIndex + m_offset, siteIndex - 1 + m_offset);
    if (siteIndex / m_len > 0 && m_isOpen[siteIndex - m_len])
        m_wq.union(siteIndex + m_offset, siteIndex - m_len + m_offset);
    if (siteIndex / m_len < m_len - 1 && m_isOpen[siteIndex + m_len])
        m_wq.union(siteIndex + m_offset, siteIndex + m_len + m_offset);

    //
    // Try to connect with virtual top and bottom site
    //
    if (row == 1)
        m_wq.union(m_top, siteIndex + m_offset);
    if (row == m_len)
        m_wq.union(m_bot, siteIndex + m_offset);
}

public boolean isOpen(int row, int col)
{
    if (!isInRange(row, col))
    {
        throw new IllegalArgumentException("Wrong arguments row: " + row + " col: " + col);
    }

    int siteIndex = getSiteIndex(row, col);
    return m_isOpen[siteIndex];
}

public boolean isFull(int row, int col)
{
    if (!isInRange(row, col))
    {
        throw new IllegalArgumentException("Wrong arguments row: " + row + " col: " + col);
    }

    return m_wq.connected(m_top, getSiteIndex(row, col) + m_offset);
}

public int numberOfOpenSites()
{
    return m_openSiteNum;
}

public boolean percolates()
{
    return m_wq.connected(m_top, m_bot);
}

private int getSiteIndex(int row, int col)
{
    return (col - 1) + (row - 1) * m_len;
}

private boolean isInRange(int row, int col)
{
    return row >= 1 && row <= m_len && col >= 1 && col <= m_len;
}

}
