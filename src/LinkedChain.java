/**
 * A generic chain of linked nodes. Use it as the storage inside your
 * own classes, and extend or modify it as your project needs.
 *
 * This class owns storage only: nodes, the head reference, and the
 * count. It contains no application logic. A class that needs a
 * collection should hold a LinkedChain field and call its methods.
 *
 * add() places the new entry at the FRONT of the chain, so entries added
 * with add() come back from toArray() in reverse order of insertion.
 * addToEnd() preserves insertion order.
 *
 * Time complexity, where n is the number of entries:
 *   add              O(1)
 *   addToEnd         O(n)
 *   remove()         O(1)
 *   remove(T)        O(n)
 *   clear            O(1)
 *   get              O(n)
 *   contains         O(n)
 *   count            O(n)
 *   size             O(1)
 *   isEmpty          O(1)
 *   toArray          O(n)
 *
 * @author CS 2114 Staff
 * @version September 2026
 *
 * @param <T> the type of entries stored in the chain
 */
public class LinkedChain<T>
{
    private Node<T> firstNode;   // reference to the first node, or null
    private int numberOfEntries; // number of nodes in the chain

    /**
     * Creates an empty chain.
     */
    public LinkedChain()
    {
        firstNode = null;
        numberOfEntries = 0;
    }

    /**
     * Adds a new entry to the front of this chain.
     *
     * @param newEntry the object to be added; must not be null
     * @return true
     * @throws IllegalArgumentException if newEntry is null
     */
    public boolean add(T newEntry)
    {
        requireNonNull(newEntry);
        firstNode = new Node<T>(newEntry, firstNode);
        numberOfEntries++;
        return true;
    }

    /**
     * Adds a new entry to the end of this chain.
     *
     * @param newEntry the object to be added; must not be null
     * @return true
     * @throws IllegalArgumentException if newEntry is null
     */
    public boolean addToEnd(T newEntry)
    {
        requireNonNull(newEntry);
        Node<T> newNode = new Node<T>(newEntry);
        if (firstNode == null)
        {
            firstNode = newNode;
        }
        else
        {
            Node<T> current = firstNode;
            while (current.next != null)
            {
                current = current.next;
            }
            current.next = newNode;
        }
        numberOfEntries++;
        return true;
    }

    /**
     * Removes and returns the first entry in this chain, if possible.
     *
     * @return the removed entry, or null if the chain is empty
     */
    public T remove()
    {
        if (firstNode == null)
        {
            return null;
        }
        T result = firstNode.data;
        firstNode = firstNode.next;
        numberOfEntries--;
        return result;
    }

    /**
     * Removes and returns the first entry in this chain.
     *
     * @return the removed entry, or null if the chain is empty
     */
    public T removeFirst()
    {
        return remove();
    }

    /**
     * Removes the first occurrence of a given entry from this chain.
     *
     * @param anEntry the entry to be removed
     * @return the removed entry, or null if it was not found
     */
    public T remove(T anEntry)
    {
        if (anEntry == null || firstNode == null)
        {
            return null;
        }
        if (anEntry.equals(firstNode.data))
        {
            T result = firstNode.data;
            firstNode = firstNode.next;
            numberOfEntries--;
            return result;
        }
        Node<T> previous = firstNode;
        Node<T> current = firstNode.next;
        while (current != null)
        {
            if (anEntry.equals(current.data))
            {
                previous.next = current.next;
                numberOfEntries--;
                return current.data;
            }
            previous = current;
            current = current.next;
        }
        return null;
    }

    /**
     * Removes all entries from this chain.
     */
    public void clear()
    {
        firstNode = null;
        numberOfEntries = 0;
    }

    /**
     * Gets the entry at the given position without removing it.
     *
     * @param index the zero-based position
     * @return the entry at that position
     * @throws IndexOutOfBoundsException if index is negative or
     *         index is greater than or equal to size()
     */
    public T get(int index)
    {
        if (index < 0 || index >= numberOfEntries)
        {
            throw new IndexOutOfBoundsException(
                "Index " + index + " out of bounds for size "
                + numberOfEntries);
        }
        Node<T> current = firstNode;
        for (int i = 0; i < index; i++)
        {
            current = current.next;
        }
        return current.data;
    }

    /**
     * Tests whether this chain contains a given entry.
     *
     * @param anEntry the entry to locate
     * @return true if the chain contains anEntry, or false if not
     */
    public boolean contains(T anEntry)
    {
        return count(anEntry) > 0;
    }

    /**
     * Counts the number of times a given entry appears in this chain.
     *
     * @param anEntry the entry to be counted
     * @return the number of times anEntry appears in the chain
     */
    public int count(T anEntry)
    {
        if (anEntry == null)
        {
            return 0;
        }
        int frequency = 0;
        Node<T> current = firstNode;
        while (current != null)
        {
            if (anEntry.equals(current.data))
            {
                frequency++;
            }
            current = current.next;
        }
        return frequency;
    }

    /**
     * Gets the number of entries currently in this chain.
     *
     * @return the integer number of entries in the chain
     */
    public int size()
    {
        return numberOfEntries;
    }

    /**
     * Sees whether this chain is empty.
     *
     * @return true if the chain has no entries, or false if not
     */
    public boolean isEmpty()
    {
        return numberOfEntries == 0;
    }

    /**
     * Copies every entry in this chain into the given array, first entry
     * first.
     *
     * @param values an array of the correct type to fill; it must have
     *               length at least size()
     * @return the array containing all entries in the chain
     * @throws IllegalArgumentException if values is null or too small
     */
    public T[] toArray(T[] values)
    {
        if (values == null)
        {
            throw new IllegalArgumentException("Array must not be null");
        }
        if (values.length < numberOfEntries)
        {
            throw new IllegalArgumentException(
                "Array of length " + values.length
                + " is too small for " + numberOfEntries + " entries");
        }
        int index = 0;
        Node<T> current = firstNode;
        while (current != null)
        {
            values[index] = current.data;
            index++;
            current = current.next;
        }
        return values;
    }

    /**
     * Returns a readable listing of the chain, first entry first,
     * e.g. "[a, b, c]".
     *
     * @return a string representation of this chain
     */
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder("[");
        Node<T> current = firstNode;
        while (current != null)
        {
            sb.append(current.data);
            if (current.next != null)
            {
                sb.append(", ");
            }
            current = current.next;
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * Rejects null entries.
     *
     * @param entry the entry to check
     * @throws IllegalArgumentException if entry is null
     */
    private void requireNonNull(T entry)
    {
        if (entry == null)
        {
            throw new IllegalArgumentException("Cannot add null to chain");
        }
    }

    /**
     * A node in the chain. Holds one entry and a link to the next node.
     *
     * @param <E> the type of data held in the node
     */
    private static class Node<E>
    {
        private E data;        // entry stored in this node
        private Node<E> next;  // link to the next node, or null

        /**
         * Creates a node with the given data and no next node.
         *
         * @param dataPortion the entry to store
         */
        private Node(E dataPortion)
        {
            this(dataPortion, null);
        }

        /**
         * Creates a node with the given data and next link.
         *
         * @param dataPortion the entry to store
         * @param nextNode the node that follows this one
         */
        private Node(E dataPortion, Node<E> nextNode)
        {
            data = dataPortion;
            next = nextNode;
        }
    }
}
