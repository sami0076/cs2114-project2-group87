import student.TestCase;

/**
 * Tests for the Dealer class.
 *
 * @author Pratham Singh
 * @version October 2026
 */
public class DealerTest extends TestCase {
    private Dealer dealer;
    private Card card10;
    private Card card6;
    private Card card7;
    private Card cardAce;

    /**
     * Sets up dealer and cards for testing.
     */
    public void setUp() {
        dealer = new Dealer();
        card10 = new Card("Hearts", "10");
        card6 = new Card("Spades", "6");
        card7 = new Card("Diamonds", "7");
        cardAce = new Card("Clubs", "A");
    }

    /**
     * Tests the default constructor creates a non-null hand.
     */
    public void testDefaultConstructor() {
        assertNotNull(dealer.getHand());
        assertEquals(0, dealer.getScore());
    }

    /**
     * Tests the parameterized constructor with an existing hand.
     */
    public void testParameterizedConstructor() {
        Hand customHand = new Hand();
        customHand.addCard(card10);
        Dealer customDealer = new Dealer(customHand);
        assertEquals(customHand, customDealer.getHand());
        assertEquals(10, customDealer.getScore());
    }

    /**
     * Tests constructor rejects null hand.
     */
    public void testConstructorBadInput() {
        Exception thrown = null;
        try {
            new Dealer(null);
        } catch (Exception e) {
            thrown = e;
        }
        assertTrue(thrown instanceof IllegalArgumentException);
    }

    /**
     * Tests addCard increases hand size and updates score.
     */
    public void testAddCard() {
        dealer.addCard(card10);
        assertEquals(1, dealer.getHand().size());
        assertEquals(10, dealer.getScore());
    }

    /**
     * Tests addCard rejects null card.
     */
    public void testAddCardBadInput() {
        Exception thrown = null;
        try {
            dealer.addCard(null);
        } catch (Exception e) {
            thrown = e;
        }
        assertTrue(thrown instanceof IllegalArgumentException);
    }

    /**
     * Tests shouldHit returns true when score is below 17.
     */
    public void testShouldHitBelow17() {
        // Empty hand score is 0 < 17
        assertTrue(dealer.shouldHit());

        dealer.addCard(card10);
        dealer.addCard(card6);
        assertEquals(16, dealer.getScore());
        assertTrue(dealer.shouldHit());
    }

    /**
     * Tests shouldHit returns false when score is 17 or above.
     */
    public void testShouldHit17OrAbove() {
        dealer.addCard(card10);
        dealer.addCard(card7);
        assertEquals(17, dealer.getScore());
        // Dealer stands on 17
        assertFalse(dealer.shouldHit());

        // Dealer stands on 18
        dealer.reset();
        dealer.addCard(card10);
        dealer.addCard(new Card("Spades", "8"));
        assertEquals(18, dealer.getScore());
        assertFalse(dealer.shouldHit());

        // Dealer stands on soft 17 (Ace + 6)
        dealer.reset();
        dealer.addCard(cardAce);
        dealer.addCard(card6);
        assertEquals(17, dealer.getScore());
        assertFalse(dealer.shouldHit());

        // Dealer stops if busted
        dealer.reset();
        dealer.addCard(card10);
        dealer.addCard(new Card("Clubs", "K"));
        dealer.addCard(new Card("Diamonds", "5"));
        assertEquals(25, dealer.getScore());
        assertFalse(dealer.shouldHit());
    }

    /**
     * Tests getScore on empty and populated hands.
     */
    public void testGetScore() {
        assertEquals(0, dealer.getScore());
        dealer.addCard(cardAce);
        dealer.addCard(card7);
        assertEquals(18, dealer.getScore());
    }

    /**
     * Tests isBust.
     */
    public void testIsBust() {
        assertFalse(dealer.isBust());
        dealer.addCard(card10);
        dealer.addCard(card7);
        assertFalse(dealer.isBust());

        dealer.addCard(new Card("Clubs", "5"));
        assertEquals(22, dealer.getScore());
        assertTrue(dealer.isBust());
    }

    /**
     * Tests getHand.
     */
    public void testGetHand() {
        Hand h = dealer.getHand();
        assertNotNull(h);
        assertEquals(0, h.size());
    }

    /**
     * Tests reset clears the dealer's hand.
     */
    public void testReset() {
        dealer.addCard(card10);
        dealer.addCard(card6);
        assertEquals(2, dealer.getHand().size());

        dealer.reset();
        assertEquals(0, dealer.getHand().size());
        assertEquals(0, dealer.getScore());

        // Reset on empty hand does not crash
        dealer.reset();
        assertEquals(0, dealer.getHand().size());
    }

    /**
     * Tests toString.
     */
    public void testToString() {
        assertEquals("Dealer Hand: []", dealer.toString());
        dealer.addCard(card10);
        assertEquals("Dealer Hand: [10 of Hearts]", dealer.toString());
    }
}
