import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 ** This class represents a linked list of Buffer objects. It allows for
 * iteration, addition, removal, and other utility operations on the Buffer
 * objects contained within the list.
 * 
 * @author {Stephen Ye}
 * @version {10/19/2023}
 */
public class BufferLinkedList implements Iterable<Buffer> {
    private BufferNode head;

    /**
     * Returns an iterator to allow iterating over Buffer objects
     * 
     * * @return an Iterator over Buffer objects
     */
    public Iterator<Buffer> iterator() {
        return new Iterator<Buffer>() {
            private BufferNode current = head;

            public boolean hasNext() {
                return current != null;
            }


            public Buffer next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                Buffer buffer = current.buffer;
                current = current.next;
                return buffer;
            }
        };
    }


    /**
     * Adds a Buffer object to the beginning of the list.
     *
     * @param buffer
     *            The Buffer object to be added.
     */
    public void add(Buffer buffer) {
        BufferNode newNode = new BufferNode(buffer);
        newNode.next = head;
        head = newNode;
    }


    /**
     * Removes a Buffer object identified by a specific index.
     *
     * @param index
     *            The index of the Buffer object to be removed.
     */
    public void remove(int index) {
        if (head == null)
            return;

        if (head.buffer.getPos() == index) {
            head = head.next;
            return;
        }

        BufferNode current = head;
        while (current.next != null && current.next.buffer.getPos() != index) {
            current = current.next;
        }

        if (current.next == null)
            return;

        current.next = current.next.next;
    }


    /**
     * Checks if the list is empty
     * 
     * @return true if the list is empty, false otherwise.
     */
    public boolean isEmpty() {
        return head == null;
    }


    /**
     * Retrieves the first Buffer from the list
     * 
     * @return The first Buffer object in the list or null if the list is empty.
     */
    public Buffer getFirstBuffer() {
        return head == null ? null : head.buffer;
    }


    /**
     * Counts and returns the number of Buffers in the list
     * 
     * @return The number of Buffer objects in the list.
     */
    public int size() {
        int count = 0;
        BufferNode current = head;
        while (current != null) {
            count++;
            current = current.next;
        }
        return count;
    }


    /**
     * Retrieves a Buffer by index.
     *
     * @param index
     *            The index of the Buffer object to retrieve.
     * @return The Buffer object with the specified index or null if not found.
     */
    public Buffer get(int index) {
        BufferNode current = head;
        while (current != null) {
            if (current.buffer.getPos() == index) {
                return current.buffer;
            }
            current = current.next;
        }
        return null; // Buffer not found
    }


    /**
     * Clears all Buffer objects from the list.
     */
    public void clear() {
        head = null; // Set the head to null, effectively clearing the list
    }

    /**
     * Inner class representing a node in the linked list, which contains a
     * Buffer object and a reference to the next node.
     * 
     * @author {Stephen Ye}
     * 
     * @version {10/19/2023}
     */
    private class BufferNode {
        private Buffer buffer;
        private BufferNode next;

        /**
         * Constructor for the BufferNode, initializing it with a Buffer object.
         *
         * @param buffer
         *            The Buffer object to be stored in the node.
         */
        BufferNode(Buffer buffer) {
            this.buffer = buffer;
            this.next = null;
        }
    }
}
