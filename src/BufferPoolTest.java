
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import student.TestCase;

/**
 * BufferPoolTest class is responsible for testing the functionality of the
 * BufferPool class.
 * It ensures that operations such as insert, acquireBuffer, getKey, numBlocks,
 * and flush work as expected.
 *
 * @author {Stephen Ye}
 * @version {10/19/2023}
 */
public class BufferPoolTest extends TestCase {

    private BufferPool bufferPool;
    private File testFile;
    private RandomAccessFile randomAccessFile;

    /**
     * Setup method to initialize necessary components for the tests.
     */
    public void setUp() throws IOException {
        testFile = new File("testfile");
        randomAccessFile = new RandomAccessFile(testFile, "rw");

        // Writing some predefined data into the file.
        for (int i = 0; i < 10; i++) {
            randomAccessFile.writeInt(i);
        }

        bufferPool = new BufferPool(testFile, 2);
    }


    /**
     * Tests the insert method to add a buffer into the pool and
     * then acquires it back to check its correctness.
     */
    public void testInsertAndAcquireBuffer() throws IOException {
        Buffer buffer = new Buffer(new RandomAccessFile(testFile, "rw"), 0);
        bufferPool.insert(buffer);

        Buffer acquiredBuffer = bufferPool.acquireBuffer(0);
        assertNotNull(acquiredBuffer);
        assertEquals(buffer, acquiredBuffer);

        Buffer buffer1 = bufferPool.acquireBuffer(1);

        assertNotNull(buffer1);
        assertEquals(1, buffer1.block());
    }


    /**
     * Tests the getKey method which retrieves a key based on
     * block number from a buffer in the pool.
     */
    public void testGetKey() throws IOException {
        Buffer buffer = new Buffer(new RandomAccessFile(testFile, "rw"), 0);
        byte[] data = new byte[4096];
        data[0] = 10;
        buffer.write(data);
        bufferPool.insert(buffer);

        short key = bufferPool.getKey(0);
        assertEquals(2560, key);
    }


    /**
     * Tests the numBlocks method to ensure it returns the correct
     * number of blocks/buffers present in the pool.
     */
    public void testNumBlocks() {
        int numBlocks = bufferPool.numBlocks();
        assertTrue(numBlocks >= 0);
    }


    /**
     * Tests the flush method to ensure all the buffers are flushed
     * back to the file and correctly managed in the pool.
     */
    public void testFlush() throws IOException {
        Buffer buffer1 = new Buffer(new RandomAccessFile(testFile, "rw"), 0);
        Buffer buffer2 = new Buffer(new RandomAccessFile(testFile, "rw"), 1);

        bufferPool.insert(buffer1);
        bufferPool.insert(buffer2);

        bufferPool.flush();

        assertEquals(2, bufferPool.numBlocks());
    }
}
