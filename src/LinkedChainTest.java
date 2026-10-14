import student.TestCase;

/**
 * Tests for the LinkedChain class.
 *
 * @author Sami Saleh
 * @version October 2026
 */
public class LinkedChainTest extends TestCase
{
    private LinkedChain<Card> chain;
    private LinkedChain<Card> empty;
    private Card a;
    private Card b;
    private Card c;
    private Card missing;

    /**
     * Sets up a chain holding a, b, c (c is the head).
     */
    public void setUp()
    {
        a = new Card("Spades", "A");
        b = new Card("Hearts", "K");
        c = new Card("Clubs", "7");
        missing = new Card("Diamonds", "2");
        empty = new LinkedChain<Card>();
        chain = new LinkedChain<Card>();
        chain.add(a);
        chain.add(b);
        chain.add(c);
    }

    /**
     * Tests add on an empty chain.
     */
    public void testAddToEmpty()
    {
        assertTrue(empty.add(a));
        assertEquals(1, empty.size());
        assertTrue(empty.contains(a));
        assertEquals(a, empty.get(0));
    }

    /**
     * Tests add on a non-empty chain puts the new item at the head.
     */
    public void testAddToNonEmpty()
    {
        empty.add(a);
        empty.add(b);
        assertEquals(b, empty.get(0));
        assertTrue(empty.contains(a));
        assertEquals(2, empty.size());
    }

    /**
     * Tests add rejects null.
     */
    public void testAddNull()
    {
        Exception thrown = null;
        try
        {
            empty.add(null);
        }
        catch (Exception e)
        {
            thrown = e;
        }
        assertTrue(thrown instanceof IllegalArgumentException);
        assertEquals(0, empty.size());
    }

    /**
     * Tests addToEnd keeps insertion order.
     */
    public void testAddToEnd()
    {
        assertTrue(empty.addToEnd(a));
        empty.addToEnd(b);
        assertEquals(a, empty.get(0));
        assertEquals(b, empty.get(1));
        assertEquals(2, empty.size());
    }

    /**
     * Tests addToEnd rejects null.
     */
    public void testAddToEndNull()
    {
        Exception thrown = null;
        try
        {
            empty.addToEnd(null);
        }
        catch (Exception e)
        {
            thrown = e;
        }
        assertTrue(thrown instanceof IllegalArgumentException);
    }

    /**
     * Tests remove(T) on a middle item and on the head.
     */
    public void testRemoveItem()
    {
        assertEquals(b, chain.remove(b));
        assertEquals(2, chain.size());
        assertFalse(chain.contains(b));
        assertEquals(c, chain.remove(c));
        assertEquals(1, chain.size());
    }

    /**
     * Tests remove(T) with a missing item, an empty chain, and null.
     */
    public void testRemoveItemBadInput()
    {
        assertNull(chain.remove(missing));
        assertEquals(3, chain.size());
        assertNull(empty.remove(a));
        assertEquals(0, empty.size());
        assertNull(chain.remove(null));
    }

    /**
     * Tests contains with an item in the chain.
     */
    public void testContains()
    {
        assertTrue(chain.contains(a));
        assertTrue(chain.contains(new Card("Hearts", "K")));
    }

    /**
     * Tests contains with a missing item, an empty chain, and null.
     */
    public void testContainsBadInput()
    {
        assertFalse(chain.contains(missing));
        assertFalse(empty.contains(a));
        assertFalse(chain.contains(null));
    }

    /**
     * Tests removeFirst on a three-item chain.
     */
    public void testRemoveFirst()
    {
        assertEquals(c, chain.removeFirst());
        assertEquals(b, chain.get(0));
        assertEquals(2, chain.size());
    }

    /**
     * Tests removeFirst on an empty chain.
     */
    public void testRemoveFirstEmpty()
    {
        assertNull(empty.removeFirst());
        assertEquals(0, empty.size());
    }

    /**
     * Tests remove() on a full and an empty chain.
     */
    public void testRemove()
    {
        assertEquals(c, chain.remove());
        assertEquals(2, chain.size());
        assertNull(empty.remove());
    }

    /**
     * Tests size on a full and an empty chain.
     */
    public void testSize()
    {
        assertEquals(3, chain.size());
        assertEquals(0, empty.size());
    }

    /**
     * Tests isEmpty on a full and an empty chain.
     */
    public void testIsEmpty()
    {
        assertFalse(chain.isEmpty());
        assertTrue(empty.isEmpty());
    }

    /**
     * Tests clear empties the chain.
     */
    public void testClear()
    {
        chain.clear();
        assertTrue(chain.isEmpty());
        assertFalse(chain.contains(a));
        empty.clear();
        assertEquals(0, empty.size());
    }

    /**
     * Tests get with valid indexes.
     */
    public void testGet()
    {
        assertEquals(c, chain.get(0));
        assertEquals(a, chain.get(2));
    }

    /**
     * Tests get with out-of-bounds indexes.
     */
    public void testGetBadInput()
    {
        Exception thrown = null;
        try
        {
            chain.get(-1);
        }
        catch (Exception e)
        {
            thrown = e;
        }
        assertTrue(thrown instanceof IndexOutOfBoundsException);

        thrown = null;
        try
        {
            chain.get(3);
        }
        catch (Exception e)
        {
            thrown = e;
        }
        assertTrue(thrown instanceof IndexOutOfBoundsException);
    }

    /**
     * Tests count and that duplicates are allowed.
     */
    public void testCountDuplicates()
    {
        chain.add(new Card("Spades", "A"));
        assertEquals(2, chain.count(a));
        assertEquals(4, chain.size());
        chain.remove(a);
        assertTrue(chain.contains(a));
        assertEquals(1, chain.count(a));
    }

    /**
     * Tests count with a missing item and null.
     */
    public void testCountBadInput()
    {
        assertEquals(0, chain.count(missing));
        assertEquals(0, chain.count(null));
    }

    /**
     * Tests toArray copies items head first.
     */
    public void testToArray()
    {
        Card[] cards = chain.toArray(new Card[3]);
        assertEquals(c, cards[0]);
        assertEquals(b, cards[1]);
        assertEquals(a, cards[2]);
    }

    /**
     * Tests toArray with a null or too-small array.
     */
    public void testToArrayBadInput()
    {
        Exception thrown = null;
        try
        {
            chain.toArray(null);
        }
        catch (Exception e)
        {
            thrown = e;
        }
        assertTrue(thrown instanceof IllegalArgumentException);

        thrown = null;
        try
        {
            chain.toArray(new Card[2]);
        }
        catch (Exception e)
        {
            thrown = e;
        }
        assertTrue(thrown instanceof IllegalArgumentException);
    }

    /**
     * Tests toString on a full and an empty chain.
     */
    public void testToString()
    {
        assertEquals("[7 of Clubs, K of Hearts, A of Spades]", chain.toString());
        assertEquals("[]", empty.toString());
    }

    /**
     * Tests the same chain class works with a second element type.
     */
    public void testStringChain()
    {
        LinkedChain<String> words = new LinkedChain<String>();
        words.add("Win");
        words.add("Loss");
        assertEquals(2, words.size());
        assertTrue(words.contains("Win"));
        assertEquals("Loss", words.removeFirst());
        assertNull(words.remove("Tie"));
    }
}
