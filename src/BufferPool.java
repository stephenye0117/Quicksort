import java.nio.ByteBuffer;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.io.File;

/**
 * Buffer Pool connect file and buffers.
 *
 * @author {Stephen Ye}
 * @version {10/19/2023}
 */
public class BufferPool {
    private RandomAccessFile disk;
    private BufferLinkedList pool;
    private int numBlocks;
    private final static int BLOCK_SIZE = 4096;
    private final static int RECORD_SIZE = 4;

    /**
     * Create a new BufferPool object.
     *
     * @param file
     *            the data file
     * @param numBuffer
     *            the number of buffers
     * @throws IOException
     * @throws FileNotFoundException
     */
    public BufferPool(File file, int numBuffer) throws IOException {
        disk = new RandomAccessFile(file, "rw");

        numBlocks = (int)disk.length() / BLOCK_SIZE;

        pool = new BufferLinkedList();
    }


    /**
     * Insert buffer into buffer pool.
     *
     * @param buffer
     *            to be inserted
     * @throws IOException
     */
    public void insert(Buffer buffer) throws IOException {
        pool.add(buffer);
        if (pool.size() > numBlocks) {
            Buffer removedBuffer = pool.getFirstBuffer();
            removedBuffer.writeBack();
            pool.remove(removedBuffer.block());
        }
    }


    /**
     * Relate buffer and block.
     *
     * @param block
     *            the block index
     * @return the buffer related to the block
     * @throws IOException
     */
    public Buffer acquireBuffer(int block) throws IOException {
        for (Buffer buffer : pool) {
            if (buffer.block() == block) {
                return buffer;
            }
        }

        Buffer buffer = new Buffer(disk, block);
        buffer.diskRead();
        this.insert(buffer);

        return buffer;
    }


    /**
     * get key from buffer.
     *
     * @param index
     *            where to get key
     * @return key short integer
     * @throws IOException
     *             exception
     */
    public short getKey(int index) throws IOException {
        int block = index * RECORD_SIZE / BLOCK_SIZE;
        int position = (index * RECORD_SIZE) % BLOCK_SIZE;

        short key = ByteBuffer.wrap(acquireBuffer(block).read()).getShort(
            position);
        return key;
    }


    /**
     * get the number of blocks in disk.
     *
     * @return the number of blocks in disk
     */
    public int numBlocks() {
        return numBlocks;
    }


    /**
     * Flush when sorting finish.
     *
     * @throws IOException
     */
    public void flush() throws IOException {
        for (Buffer buffer : pool) {
            buffer.writeBack();
        }
        pool.clear(); // Clears the pool after flushing
    }
}
