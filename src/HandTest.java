import student.TestCase;

/**
 * Tests for the Hand class.
 *
 * @author Pratham Singh
 * @version October 2026
 */
public class HandTest extends TestCase {
    private Hand hand;
    private Card card5;
    private Card cardKing;
    private Card cardAce;
    private Card cardAce2;
    private Card card9;
    private Card card2;

    /**
     * Sets up cards and hand for each test.
     */
    public void setUp() {
        hand = new Hand();
        card5 = new Card("Hearts", "5");
        cardKing = new Card("Spades", "K");
        cardAce = new Card("Diamonds", "A");
        cardAce2 = new Card("Clubs", "A");
        card9 = new Card("Hearts", "9");
        card2 = new Card("Spades", "2");
    }

    /**
     * Tests adding a card to an empty hand.
     */
    public void testAddCardToEmpty() {
        hand.addCard(card5);
        assertEquals(1, hand.size());
        assertTrue(hand.getCards().contains(card5));
        assertEquals(5, hand.getScore());
    }

    /**
     * Tests adding a card to a hand that already contains multiple cards.
     */
    public void testAddCardToNonEmpty() {
        hand.addCard(card5);
        hand.addCard(cardKing);
        assertEquals(2, hand.size());
        assertTrue(hand.getCards().contains(card5));
        assertTrue(hand.getCards().contains(cardKing));

        hand.addCard(cardAce);
        assertEquals(3, hand.size());
        assertTrue(hand.getCards().contains(cardAce));
    }

    /**
     * Tests that adding a null card throws an exception.
     */
    public void testAddCardBadInput() {
        Exception thrown = null;
        try {
            hand.addCard(null);
        } catch (Exception e) {
            thrown = e;
        }
        assertTrue(thrown instanceof IllegalArgumentException);
        assertEquals(0, hand.size());
    }

    /**
     * Tests getScore with normal cards (King and 5).
     */
    public void testGetScoreNormal() {
        hand.addCard(cardKing);
        hand.addCard(card5);
        assertEquals(15, hand.getScore());
    }

    /**
     * Tests getScore adjusting Aces: Ace, Ace, 9, 2 should equal 13.
     */
    public void testGetScoreWithAcesAdjusted() {
        hand.addCard(cardAce);
        hand.addCard(cardAce2);
        hand.addCard(card9);
        hand.addCard(card2);
        assertEquals(13, hand.getScore());
    }

    /**
     * Tests getScore on an empty hand.
     */
    public void testGetScoreEmpty() {
        assertEquals(0, hand.getScore());
    }

    /**
     * Tests getScore with a single Ace counting as 11.
     */
    public void testGetScoreSingleAce() {
        hand.addCard(cardAce);
        assertEquals(11, hand.getScore());
    }

    /**
     * Tests getScore with three Aces.
     */
    public void testGetScoreThreeAces() {
        hand.addCard(cardAce);
        hand.addCard(cardAce2);
        hand.addCard(new Card("Hearts", "A"));
        assertEquals(13, hand.getScore());
    }

    /**
     * Tests getScore on a bust hand without Aces.
     */
    public void testGetScoreBust() {
        hand.addCard(cardKing);
        hand.addCard(new Card("Clubs", "K"));
        hand.addCard(card5);
        assertEquals(25, hand.getScore());
    }

    /**
     * Tests getCards returns the underlying LinkedChain.
     */
    public void testGetCards() {
        assertNotNull(hand.getCards());
        assertEquals(0, hand.getCards().size());
        hand.addCard(card5);
        assertEquals(1, hand.getCards().size());
    }

    /**
     * Tests size method.
     */
    public void testSize() {
        assertEquals(0, hand.size());
        hand.addCard(card5);
        assertEquals(1, hand.size());
        hand.addCard(cardKing);
        assertEquals(2, hand.size());
    }

    /**
     * Tests isBust method.
     */
    public void testIsBust() {
        assertFalse(hand.isBust());
        hand.addCard(cardKing);
        hand.addCard(card5);
        assertFalse(hand.isBust());

        hand.addCard(new Card("Diamonds", "8"));
        assertTrue(hand.isBust());
    }

    /**
     * Tests isBlackjack method.
     */
    public void testIsBlackjack() {
        assertFalse(hand.isBlackjack());

        hand.addCard(cardKing);
        hand.addCard(card5);
        assertFalse(hand.isBlackjack());

        hand.clear();
        hand.addCard(cardKing);
        hand.addCard(cardAce);
        assertTrue(hand.isBlackjack());

        // Three cards totaling 21 is not a natural blackjack
        hand.clear();
        hand.addCard(new Card("Hearts", "7"));
        hand.addCard(new Card("Diamonds", "7"));
        hand.addCard(new Card("Clubs", "7"));
        assertEquals(21, hand.getScore());
        assertFalse(hand.isBlackjack());
    }

    /**
     * Tests clear method on non-empty and empty hands.
     */
    public void testClear() {
        hand.addCard(card5);
        hand.addCard(cardKing);
        assertEquals(2, hand.size());

        hand.clear();
        assertEquals(0, hand.size());
        assertEquals(0, hand.getScore());

        // Clear again on empty hand should not crash
        hand.clear();
        assertEquals(0, hand.size());
    }

    /**
     * Tests toString method.
     */
    public void testToString() {
        assertEquals("[]", hand.toString());
        hand.addCard(card5);
        assertEquals("[5 of Hearts]", hand.toString());
    }
}
