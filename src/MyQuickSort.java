import java.io.IOException;
import java.util.Random;
import java.io.File;

/**
 * class designed to perform a quicksort on data, managing various optimization
 * techniques such as pivot selection and partitioning
 *
 * @author {Stephen Ye}
 * @version {10/19/2023}
 */
public class MyQuickSort {
    private BufferPool pool;
    private int diskSize;
    private int numBuffer;
    private final static int BLOCK_SIZE = 4096;
    private final static int RECORD_SIZE = 4;

    /**
     * Create a new Sorting object.
     *
     * @param name
     *            of the file
     * @param numBuffer
     *            the number of buffers
     * @throws IOException
     *             exception
     */
    public MyQuickSort(String name, int numBuffer) throws IOException {
        File file = new File(name);
        pool = new BufferPool(file, numBuffer);
        this.numBuffer = numBuffer;
        diskSize = pool.numBlocks() * BLOCK_SIZE / RECORD_SIZE;
    }


    /**
     * get the size of the disk.
     *
     * @return diskSize
     */
    public int getSize() {
        return diskSize;
    }


    /**
     * perform quicksort.
     *
     * @throws IOException
     */
    public void sort() throws IOException {
        quicksortHelp(0, this.getSize() - 1);
    }


    /**
     * perform quicksorthelp.
     * 
     * @param i
     *            index i
     * @param j
     *            index j
     * @throws IOException
     */
    private void quicksortHelp(int i, int j) throws IOException {
        if (j - i + 1 < 41) {
            insertionSort(i, j);
        }
        else {
            int pivotIndex = findPivot(i, j);
            swap(pivotIndex, j);
            short pivot = pool.getKey(j);
            int k = pivotPartitioning(i, j - 1, pivot);
            swap(k, j);
            int g = rearrangeEqualToKey(k + 1, j, pool.getKey(k));

            if ((k - i) > 1)
                quicksortHelp(i, k - 1);
            if ((j - g) > 1)
                quicksortHelp(g + 1, j);
        }
    }


    /**
     * find pivot.
     *
     * @param l
     *            left index
     * @param r
     *            right index
     * @return pivot
     */
    public int findPivot(int l, int r) throws IOException {
        int mid = (l + r) / 2;

        short lVal = pool.getKey(l);
        short mVal = pool.getKey(mid);
        short rVal = pool.getKey(r);

        if ((lVal > mVal) != (lVal > rVal)) {
            return l;
        }
        else if ((mVal > lVal) != (mVal > rVal)) {
            return mid;
        }
        else {
            return r;
        }
    }


    /**
     * partition method
     *
     * @param l
     *            left index
     * @param r
     *            right index
     * @param pivot
     *            pivot short
     * @return the first index of rightSub array.
     * @throws IOException
     */
    public int pivotPartitioning(int l, int r, short pivot) throws IOException {
        int left = l;
        int right = r;

        while (left <= right) {

            while (pool.getKey(left) < pivot) {
                left++;

            }
            while (right >= left && pool.getKey(right) >= pivot) {
                right--;

            }
            if (right > left) {
                swap(left, right);
            }
        }
        return left;
    }


    /**
     * The pivotPartitioning method performs partitioning around a pivot.
     *
     * @param l
     *            index
     * @param r
     *            index
     * @param k
     *            short
     * @return right index
     * @throws IOException
     */
    public int rearrangeEqualToKey(int l, int r, short k) throws IOException {

        int left = l;
        int right = r;
        while (left <= right) {
            if (pool.getKey(left) == k) {
                left++;
            }
            else if (pool.getKey(right) == k) {
                swap(left, right);
                left++;
                right--;
            }
            else {
                right--;
            }
        }
        return right;
    }


    /**
     * The rearrangeEqualToKey method reorganizes elements in the array
     * based on their equality to a specific key.
     *
     * @param low
     *            lower bound
     * @param high
     *            higher bound
     * @throws IOException
     */
    private void insertionSort(int low, int high) throws IOException {
        for (int i = low + 1; i <= high; i++) {
            int j = i;
            while (j > low && pool.getKey(j) < pool.getKey(j - 1)) {
                swap(j, j - 1);
                j--;
            }
        }
    }


    /**
     * Make the necessary swaps for sorting
     *
     * @param l
     *            left index
     * @param r
     *            right index
     * @throws IOException
     *             exception
     */
    public void swap(int l, int r) throws IOException {
        int lBlock = (l * RECORD_SIZE) / BLOCK_SIZE;
        int lPosition = (l * RECORD_SIZE) % BLOCK_SIZE;
        int rBlock = (r * RECORD_SIZE) / BLOCK_SIZE;
        int rPosition = (r * RECORD_SIZE) % BLOCK_SIZE;

        byte[] lRec = new byte[RECORD_SIZE];
        byte[] rRec = new byte[RECORD_SIZE];
        byte[] buffer;
        Buffer lBuffer = pool.acquireBuffer(lBlock);
        Buffer rBuffer = pool.acquireBuffer(rBlock);

        buffer = lBuffer.read();
        System.arraycopy(buffer, lPosition, lRec, 0, RECORD_SIZE);

        buffer = rBuffer.read();
        System.arraycopy(buffer, rPosition, rRec, 0, RECORD_SIZE);

        System.arraycopy(lRec, 0, buffer, rPosition, RECORD_SIZE);
        rBuffer.write(buffer);

        buffer = lBuffer.read();
        System.arraycopy(rRec, 0, buffer, lPosition, RECORD_SIZE);
        lBuffer.write(buffer);

    }


    /**
     * Flush when sorting is done.
     *
     * @throws IOException
     */
    public void flush() throws IOException {
        pool.flush();
    }

}
