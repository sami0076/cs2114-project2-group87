import student.TestCase;

/**
 * Tests for the Card class.
 *
 * @author Sami Saleh
 * @version October 2026
 */
public class CardTest extends student.TestCase
{
    private Card ace;
    private Card king;
    private Card seven;

    /**
     * Sets up cards for each test.
     */
    public void setUp()
    {
        ace = new Card("Spades", "A");
        king = new Card("Hearts", "K");
        seven = new Card("Clubs", "7");
    }

    /**
     * Tests creating a valid card.
     */
    public void testCreatingCard()
    {
        Card card = new Card("Diamonds", "7");
        assertEquals("Diamonds", card.getSuit());
        assertEquals("7", card.getRank());
    }

    /**
     * Tests that bad suits and ranks are rejected.
     */
    public void testConstructorBadInput()
    {
        Exception thrown = null;
        try
        {
            new Card("Stars", "A");
        }
        catch (Exception e)
        {
            thrown = e;
        }
        assertTrue(thrown instanceof IllegalArgumentException);

        thrown = null;
        try
        {
            new Card("Hearts", "1");
        }
        catch (Exception e)
        {
            thrown = e;
        }
        assertTrue(thrown instanceof IllegalArgumentException);

        thrown = null;
        try
        {
            new Card(null, null);
        }
        catch (Exception e)
        {
            thrown = e;
        }
        assertTrue(thrown instanceof IllegalArgumentException);
    }

    /**
     * Tests getSuit method.
     */
    public void testGetSuit()
    {
        assertEquals("Spades", ace.getSuit());
        assertFalse(king.getSuit().equals("Spades"));
    }

    /**
     * Tests getRank method.
     */
    public void testGetRank()
    {
        assertEquals("K", king.getRank());
        assertFalse(seven.getRank().equals("K"));
    }

    /**
     * Tests getValue method for number, face, and Ace cards.
     */
    public void testGetValue()
    {
        assertEquals(7, seven.getValue());
        assertEquals(10, king.getValue());
        assertEquals(11, ace.getValue());
    }

    /**
     * Tests getAltValue method for normal cards and Aces.
     */
    public void testGetAltValue()
    {
        assertEquals(7, seven.getAltValue());
        assertEquals(10, king.getAltValue());
        assertEquals(1, ace.getAltValue());
    }

    /**
     * Tests isAce.
     */
    public void testIsAce()
    {
        assertTrue(ace.isAce());
        assertFalse(seven.isAce());
    }

    /**
     * Tests equals methodwith matching cards.
     */
    public void testEquals()
    {
        assertTrue(ace.equals(ace));
        assertTrue(ace.equals(new Card("Spades", "A")));
    }

    /**
     * Tests equals method with non-matching objects.
     */
    public void testEqualsBadInput()
    {
        assertFalse(ace.equals(null));
        assertFalse(ace.equals("A of Spades"));
        assertFalse(ace.equals(new Card("Hearts", "A")));
        assertFalse(ace.equals(new Card("Spades", "K")));
    }

    /**
     * Tests toString method.
     */
    public void testToString()
    {
        assertEquals("A of Spades", ace.toString());
        assertEquals("10 of Hearts", new Card("Hearts", "10").toString());
    }
}
