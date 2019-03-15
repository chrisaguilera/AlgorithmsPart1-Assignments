/* *****************************************************************************
 *  Name: Christopher Aguilera
 *  Date: March 13, 2019
 *  Description: Models a percolation system.
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final int n;
    private final WeightedQuickUnionUF quPerc;
    private final WeightedQuickUnionUF quFull;
    private boolean[] openSite;
    private int numOpenSites;

    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        this.n = n;
        quPerc = new WeightedQuickUnionUF((n * n) + 2);
        quFull = new WeightedQuickUnionUF((n * n) + 1);
        openSite = new boolean[n * n];
        numOpenSites = 0;
    }

    public void open(int row, int col) {

        if (row > n || col > n || row < 1 || col < 1) {
            throw new IllegalArgumentException();
        }

        if (!isOpen(row, col)) {
            numOpenSites += 1;
            int oneDimensionalIndex = getOneDimensionalIndex(row, col);
            openSite[oneDimensionalIndex] = true;
            int[] neighbors = neighbors(row, col);
            // For-loop is upper-bound constant - O(1)
            // Max number of nieghbors for any site is 4
            for (int neighbor : neighbors) {
                // Neighbor is top-virtual
                if (neighbor == (n * n)) {
                    quPerc.union(oneDimensionalIndex, neighbor);
                    quFull.union(oneDimensionalIndex, neighbor);
                }
                // Neighbor is bottom-virtual
                else if (neighbor == (n * n) + 1) {
                    quPerc.union(oneDimensionalIndex, neighbor);
                }
                else {
                    int nRow = getRow(neighbor);
                    int nCol = getCol(neighbor);
                    if (isOpen(nRow, nCol)) {
                        quPerc.union(oneDimensionalIndex, neighbor);
                        quFull.union(oneDimensionalIndex, neighbor);
                    }
                }

            }

        }

    }

    public boolean isOpen(int row, int col) {
        if (row > n || col > n || row < 1 || col < 1) {
            throw new IllegalArgumentException();
        }
        int oneDimensionalIndex = getOneDimensionalIndex(row, col);
        return openSite[oneDimensionalIndex];
    }

    public boolean isFull(int row, int col) {
        if (row > n || col > n || row < 1 || col < 1) {
            throw new IllegalArgumentException();
        }
        int oneDimensionalIndex = getOneDimensionalIndex(row, col);
        // Check if connected to top-virtual
        return quFull.connected(oneDimensionalIndex, (n * n));
    }

    public int numberOfOpenSites() {
        return numOpenSites;
    }

    public boolean percolates() {
        // Check if top-virtual and bottom-virtual connected
        return quPerc.connected((n * n), (n * n) + 1);
    }

    private int[] neighbors(int row, int col) {
        int oneDimensionalIndex = getOneDimensionalIndex(row, col);
        // Special case with 1x1 grid
        if (n == 1) {
            // Top-virtual and bottom-virtual
            int[] neighborArray = { (n * n), (n * n) + 1 };
            return neighborArray;
        }
        // Top Row
        if (oneDimensionalIndex < n) {
            // Top-Left Corner
            if (oneDimensionalIndex == 0) {
                // Neighbors are top-virtual, right, and bottom
                int[] neighborArray = {
                        (n * n), oneDimensionalIndex + 1, oneDimensionalIndex + n
                };
                return neighborArray;
            }
            // Top-Right Corner
            if (oneDimensionalIndex == n - 1) {
                // Neighbors are top-virtual, left, and bottom
                int[] neighborArray = {
                        (n * n), oneDimensionalIndex - 1, oneDimensionalIndex + n
                };
                return neighborArray;
            }
            // Neighbors are top-virtual, left, right, and bottom
            int[] neighborArray = {
                    (n * n), oneDimensionalIndex - 1, oneDimensionalIndex + 1,
                    oneDimensionalIndex + n
            };
            return neighborArray;

        }
        // Bottom Row
        if (oneDimensionalIndex / n >= (n - 1)) {
            // Bottom-Left Corner
            if (oneDimensionalIndex % n == 0) {
                // Neighbors are bottom-virtual, right, and top
                int[] neighborArray = {
                        (n * n) + 1, oneDimensionalIndex + 1, oneDimensionalIndex - n
                };
                return neighborArray;
            }
            // Bottom-Right Corner
            if (oneDimensionalIndex == (n * n) - 1) {
                // Neighbors are bottom-virtual, left, and top
                int[] neighborArray = {
                        (n * n) + 1, oneDimensionalIndex - 1, oneDimensionalIndex - n
                };
                return neighborArray;
            }
            // Neighbors are bottom-virtual, left, right, and top
            int[] neighborArray = {
                    (n * n) + 1, oneDimensionalIndex - 1, oneDimensionalIndex + 1,
                    oneDimensionalIndex - n
            };
            return neighborArray;

        }
        // Left Edge
        if (oneDimensionalIndex % n == 0) {
            // Neighbors are top, right, bottom
            int[] neighborArray = {
                    oneDimensionalIndex - n, oneDimensionalIndex + 1, oneDimensionalIndex + n
            };
            return neighborArray;
        }
        // Right Edge
        if ((oneDimensionalIndex + 1) % n == 0) {
            // Neighbors are top, left, bottom
            int[] neighborArray = {
                    oneDimensionalIndex - n, oneDimensionalIndex - 1, oneDimensionalIndex + n
            };
            return neighborArray;
        }
        // Middle Node
        // Neighbors are top, left, right, and bottom
        int[] neighborArray = {
                oneDimensionalIndex - n, oneDimensionalIndex - 1, oneDimensionalIndex + 1,
                oneDimensionalIndex + n
        };
        return neighborArray;
    }

    private int getOneDimensionalIndex(int row, int col) {
        return ((row - 1) * n) + (col - 1);
    }

    private int getRow(int oneDimensionalIndex) {
        return (oneDimensionalIndex / n) + 1;
    }

    private int getCol(int oneDimensionalIndex) {
        return (oneDimensionalIndex % n) + 1;
    }

    public static void main(String[] args) {
        Percolation perc = new Percolation(4);

        perc.open(1, 1);
        perc.open(2, 1);
        perc.open(3, 1);

        perc.open(1, 4);
        perc.open(2, 4);
        perc.open(3, 4);

        perc.open(4, 2);

        perc.open(4, 4);

        System.out.println(perc.isFull(4, 2));
        System.out.println(perc.percolates());
    }
}
