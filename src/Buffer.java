import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Buffer is a class that represents a block of data in memory, interacting with
 * disk I/O operations, and managing the state of the data such as reading,
 * writing, and tracking modifications
 *
 * @author {Stephen Ye}
 * @version {10/19/2023}
 */
public class Buffer {
    private int block;
    private byte[] data;
    private RandomAccessFile disk;
    private int position;
    private boolean dirty;
    private final static int BLOCK_SIZE = 4096;

    /**
     * Create a new Buffer object.
     *
     * @param disk
     *            the disk file
     * @param block
     *            the index of the block
     */
    public Buffer(RandomAccessFile disk, int block) {
        data = new byte[BLOCK_SIZE];
        this.disk = disk;
        this.block = block;
        position = block * BLOCK_SIZE;
        dirty = false;
    }


    /**
     * get data from buffer.
     *
     * @return data
     */
    public byte[] read() {
        Writer.incrementCacheHits();
        return data;
    }


    /**
     * set data.
     *
     * @param newData
     *            to be set
     */
    public void write(byte[] newData) {
        data = newData;
        dirty = true;
    }


    /**
     * write data back to disk.
     *
     * @throws IOException
     */
    public void writeBack() throws IOException {
        if (dirty) {
            disk.seek(position);
            disk.write(data);
            dirty = false;
            // update stat info.
            Writer.incrementDiskWrites();
        }
    }


    /**
     * read from disk.
     *
     * @throws IOException
     */
    public void diskRead() throws IOException {

        data = new byte[BLOCK_SIZE];
        disk.seek(position);
        disk.read(data);
        // update stat info.
        Writer.incrementDiskReads();
    }


    /**
     * block index.
     *
     * @return the number of block
     */
    public int block() {
        return block;
    }


    /**
     * Get the position in the disk where this block resides.
     *
     * @return The position of the block in the disk.
     */
    public int getPos() {
        return position;
    }
}
