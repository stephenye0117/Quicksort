import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.Iterator;
import student.TestCase;

/**
 * This class is responsible for testing the BufferLinkedList class, ensuring
 * that all methods operate correctly.
 *
 * @author {Stephen Ye}
 * @version {10/19/2023}
 */
public class BufferLinkedListTest extends TestCase {

    private BufferLinkedList bufferList;
    private Buffer buffer1;
    private Buffer buffer2;

    /**
     * Setup for the test methods, initializes objects used in the tests.
     */
    public void setUp() throws FileNotFoundException {
        bufferList = new BufferLinkedList();
        RandomAccessFile file1 = new RandomAccessFile("testfile1", "rw");
        RandomAccessFile file2 = new RandomAccessFile("testfile2", "rw");
        buffer1 = new Buffer(file1, 0);
        buffer2 = new Buffer(file2, 1);
    }


    /**
     * Test the add method of BufferLinkedList class.
     */
    public void testAdd() {
        bufferList.add(buffer1);
        assertEquals(1, bufferList.size());
        assertEquals(buffer1, bufferList.getFirstBuffer());

        bufferList.add(buffer2);
        assertEquals(2, bufferList.size());
        assertEquals(buffer2, bufferList.getFirstBuffer());
    }


    /**
     * Test the remove method of BufferLinkedList class.
     */
    public void testRemove() {
        bufferList.add(buffer1);
        bufferList.add(buffer2);

        bufferList.remove(buffer1.getPos());
        assertEquals(1, bufferList.size());

        bufferList.remove(buffer2.getPos());
        assertEquals(0, bufferList.size());
    }


    /**
     * Test the get method of BufferLinkedList class.
     */
    public void testGet() {
        bufferList.add(buffer1);
        assertEquals(buffer1, bufferList.get(buffer1.getPos()));
        assertNull(bufferList.get(buffer2.getPos()));
    }


    /**
     * Test the iterator functionality of BufferLinkedList class.
     */
    public void testIterator() {
        bufferList.add(buffer1);
        bufferList.add(buffer2);

        Iterator<Buffer> iterator = bufferList.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(buffer2, iterator.next());
        assertEquals(buffer1, iterator.next());
        assertFalse(iterator.hasNext());
    }


    /**
     * Test the clear method of BufferLinkedList class.
     */
    public void testClear() {
        bufferList.add(buffer1);
        bufferList.add(buffer2);
        bufferList.clear();
        assertEquals(0, bufferList.size());
        assertNull(bufferList.getFirstBuffer());
    }


    /**
     * Test the isEmpty method of BufferLinkedList class.
     */
    public void testIsEmpty() {
        assertTrue(bufferList.isEmpty());
        bufferList.add(buffer1);
        assertFalse(bufferList.isEmpty());
    }


    /**
     * Test the size method of BufferLinkedList class.
     */
    public void testSize() {
        assertEquals(0, bufferList.size());
        bufferList.add(buffer1);
        assertEquals(1, bufferList.size());
    }
}
