import student.TestCase;

/**
 * Tests for the Deck class.
 * 
 * @author Angelina Gust
 * @version October 2026
 */
public class DeckTest
    extends TestCase
{
    private Deck deck;

    /**
     * 
     */
    public void setUp()
    {
        deck = new Deck();
    }


    /**
     * Test method for build with valid counts.
     */
    public void testBuildNormalValidRanges()
    {
        boolean success1 = true;
        try
        {
            deck.build(1);
        }
        catch (Exception e)
        {
            success1 = false;
        }
        assertTrue(success1);

        boolean success6 = true;
        try
        {
            deck.build(6);
        }
        catch (Exception e)
        {
            success6 = false;
        }
        assertTrue(success6);
    }


    /**
     * Test method for build with invalid low counts.
     */
    public void testBuildBadInputTooFewDecks()
    {
        boolean exceptionThrown = false;
        try
        {
            deck.build(0);
        }
        catch (IllegalArgumentException e)
        {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);

        exceptionThrown = false;
        try
        {
            deck.build(-2);
        }
        catch (IllegalArgumentException e)
        {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }


    /**
     * Test method for build with invalid high counts.
     */
    public void testBuildBadInputTooManyDecks()
    {
        boolean exceptionThrown = false;
        try
        {
            deck.build(7);
        }
        catch (IllegalArgumentException e)
        {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }


    /**
     * Test method for dealCard when the draw pile contains cards.
     */
    public void testDealCardNormal()
    {
        deck.build(1);
        assertEquals(52, deck.drawPileSize());
        Card dealtCard = deck.dealCard();

        assertEquals(51, deck.drawPileSize());
        assertNotNull("Dealt card should not be null.", dealtCard);
        assertNotNull(
            "Dealt card must have a valid suit.",
            dealtCard.getSuit());
        assertNotNull(
            "Dealt card must have a valid rank.",
            dealtCard.getRank());
    }


    /**
     * Test method for dealCard when the draw pile is empty.
     */
    public void testDealCardEmptyDeckException()
    {
        boolean exceptionThrown = false;
        try
        {
            Deck emptyDeck = new Deck();
            emptyDeck.dealCard();
        }
        catch (IllegalStateException e)
        {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }


    /**
     * Test method for discard and shuffle (combined).
     */
    public void testDiscardAndAutoRecycleShuffle()
    {
        deck.build(1);

        for (int i = 0; i < 52; i++)
        {
            Card card = deck.dealCard();
            deck.discard(card);
        }

        boolean shuffleSucceeded = true;
        Card recycledCard = null;
        try
        {
            recycledCard = deck.dealCard();
        }
        catch (Exception e)
        {
            shuffleSucceeded = false;
        }

        assertTrue(shuffleSucceeded);
        assertNotNull(
            "Card should be successfully dealt after auto-recycling.",
            recycledCard);
    }


    /**
     * Test method for discard with a null card.
     */
    public void testDiscardNull()
    {
        boolean exceptionThrown = false;
        try
        {
            deck.discard(null);
        }
        catch (IllegalArgumentException e)
        {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }


    /**
     * Test method for drawPileSize after building two decks.
     */
    public void testDrawPileSize()
    {
        deck.build(2);
        assertEquals(104, deck.drawPileSize());
    }


    /**
     * Test method for drawPileSize on a deck that was never built.
     */
    public void testDrawPileSizeEmpty()
    {
        assertEquals(0, deck.drawPileSize());
    }

}
