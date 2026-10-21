import student.TestCase;

/**
 * Tests for the Player class.
 *
 * @author Sami Saleh
 * @version October 2026
 */
public class PlayerTest extends TestCase {
    private Player player;
    private Card king;
    private Card five;
    private Card ace;

    /**
     * Sets up a player with $100 and some cards.
     */
    public void setUp() {
        player = new Player(100);
        king = new Card("Hearts", "K");
        five = new Card("Spades", "5");
        ace = new Card("Clubs", "A");
    }

    /**
     * Tests the constructor with a valid bankroll.
     */
    public void testConstructor() {
        assertEquals(100, player.getBankroll(), 0.01);
        assertEquals(0, player.getCurrentBet(), 0.01);
        assertEquals(0, player.getHand().size());
    }

    /**
     * Tests the constructor rejects zero and negative bankrolls.
     */
    public void testConstructorBadInput() {
        Exception thrown = null;
        try {
            new Player(0);
        }
        catch (Exception e) {
            thrown = e;
        }
        assertTrue(thrown instanceof IllegalArgumentException);

        thrown = null;
        try {
            new Player(-50);
        }
        catch (Exception e) {
            thrown = e;
        }
        assertTrue(thrown instanceof IllegalArgumentException);
    }

    /**
     * Tests a valid $10 bet with a $100 bankroll.
     */
    public void testPlaceBet() {
        player.placeBet(10);
        assertEquals(10, player.getCurrentBet(), 0.01);
        assertEquals(90, player.getBankroll(), 0.01);
        player.reset();
        player.placeBet(90);
        assertEquals(0, player.getBankroll(), 0.01);
    }

    /**
     * Tests zero, negative, and over-bankroll bets leave state unchanged.
     */
    public void testPlaceBetBadInput() {
        double[] badBets = {0, -5, 101};
        for (double bet : badBets) {
            Exception thrown = null;
            try {
                player.placeBet(bet);
            }
            catch (Exception e) {
                thrown = e;
            }
            assertTrue(thrown instanceof IllegalArgumentException);
            assertEquals(100, player.getBankroll(), 0.01);
            assertEquals(0, player.getCurrentBet(), 0.01);
        }
    }

    /**
     * Tests canDoubleDown on the first two cards.
     */
    public void testCanDoubleDown() {
        player.placeBet(10);
        player.addCard(king);
        player.addCard(five);
        assertTrue(player.canDoubleDown());
    }

    /**
     * Tests canDoubleDown is false with no bet, three cards, or low money.
     */
    public void testCanDoubleDownFalse() {
        player.addCard(king);
        player.addCard(five);
        assertFalse(player.canDoubleDown());

        player.placeBet(60);
        assertFalse(player.canDoubleDown());

        player.reset();
        player.placeBet(10);
        player.addCard(king);
        player.addCard(five);
        player.addCard(ace);
        assertFalse(player.canDoubleDown());
    }

    /**
     * Tests doubleDown doubles the bet and takes it from the bankroll.
     */
    public void testDoubleDown() {
        player.placeBet(10);
        player.addCard(king);
        player.addCard(five);
        player.doubleDown();
        assertEquals(20, player.getCurrentBet(), 0.01);
        assertEquals(80, player.getBankroll(), 0.01);
    }

    /**
     * Tests doubleDown on the third card is rejected.
     */
    public void testDoubleDownThirdCard() {
        player.placeBet(10);
        player.addCard(king);
        player.addCard(five);
        player.addCard(ace);
        Exception thrown = null;
        try {
            player.doubleDown();
        }
        catch (Exception e) {
            thrown = e;
        }
        assertTrue(thrown instanceof IllegalStateException);
        assertEquals(10, player.getCurrentBet(), 0.01);
        assertEquals(90, player.getBankroll(), 0.01);
    }

    /**
     * Tests receivePayout adds money.
     */
    public void testReceivePayout() {
        player.placeBet(10);
        player.receivePayout(20);
        assertEquals(110, player.getBankroll(), 0.01);
        player.receivePayout(0);
        assertEquals(110, player.getBankroll(), 0.01);
    }

    /**
     * Tests receivePayout rejects a negative payout.
     */
    public void testReceivePayoutBadInput() {
        Exception thrown = null;
        try {
            player.receivePayout(-10);
        }
        catch (Exception e) {
            thrown = e;
        }
        assertTrue(thrown instanceof IllegalArgumentException);
        assertEquals(100, player.getBankroll(), 0.01);
    }

    /**
     * Tests addCard and getScore.
     */
    public void testAddCardAndScore() {
        player.addCard(king);
        player.addCard(five);
        assertEquals(2, player.getHand().size());
        assertEquals(15, player.getScore());
    }

    /**
     * Tests addCard rejects null.
     */
    public void testAddCardNull() {
        Exception thrown = null;
        try {
            player.addCard(null);
        }
        catch (Exception e) {
            thrown = e;
        }
        assertTrue(thrown instanceof IllegalArgumentException);
        assertEquals(0, player.getHand().size());
    }

    /**
     * Tests getScore on an empty hand.
     */
    public void testGetScoreEmpty() {
        assertEquals(0, player.getScore());
    }

    /**
     * Tests isBust over and under 21.
     */
    public void testIsBust() {
        player.addCard(king);
        player.addCard(five);
        assertFalse(player.isBust());
        player.addCard(new Card("Diamonds", "K"));
        assertTrue(player.isBust());
    }

    /**
     * Tests isBroke with money and with none left.
     */
    public void testIsBroke() {
        assertFalse(player.isBroke());
        player.placeBet(100);
        assertTrue(player.isBroke());
    }

    /**
     * Tests reset clears the hand and bet but keeps the bankroll.
     */
    public void testReset() {
        player.placeBet(10);
        player.addCard(king);
        player.reset();
        assertEquals(0, player.getHand().size());
        assertEquals(0, player.getCurrentBet(), 0.01);
        assertEquals(90, player.getBankroll(), 0.01);
        player.reset();
        assertEquals(0, player.getHand().size());
    }

    /**
     * Tests toString.
     */
    public void testToString() {
        player.addCard(king);
        assertEquals("Player Hand: [K of Hearts] Bankroll: $100.0",
            player.toString());
        player.reset();
        assertEquals("Player Hand: [] Bankroll: $100.0", player.toString());
    }

    /**
     * Tests splitting a pair makes a second hand with a matching bet.
     */
    public void testSplit() {
        player.placeBet(10);
        player.addCard(new Card("Hearts", "8"));
        player.addCard(new Card("Spades", "8"));
        assertTrue(player.canSplit());
        player.split();
        assertEquals(2, player.getHandCount());
        assertEquals(1, player.getHand(0).size());
        assertEquals(1, player.getHand(1).size());
        assertEquals(10, player.getBet(1), 0.01);
        assertEquals(80, player.getBankroll(), 0.01);
        assertFalse(player.canSplit());
    }

    /**
     * Tests two 10-value cards of different ranks can be split but two
     * cards of different values cannot.
     */
    public void testCanSplitByValue() {
        player.placeBet(10);
        player.addCard(king);
        player.addCard(new Card("Spades", "10"));
        assertTrue(player.canSplit());
        player.reset();
        player.placeBet(10);
        player.addCard(king);
        player.addCard(five);
        assertFalse(player.canSplit());
    }

    /**
     * Tests splitting is rejected with no money to match the bet.
     */
    public void testSplitNoMoney() {
        player.placeBet(100);
        player.addCard(new Card("Hearts", "8"));
        player.addCard(new Card("Spades", "8"));
        assertFalse(player.canSplit());
        Exception thrown = null;
        try {
            player.split();
        }
        catch (Exception e) {
            thrown = e;
        }
        assertTrue(thrown instanceof IllegalStateException);
        assertEquals(1, player.getHandCount());
    }

    /**
     * Tests canSplit is false with no bet placed and with one card.
     */
    public void testCanSplitNotTwoCards() {
        player.addCard(new Card("Hearts", "8"));
        player.addCard(new Card("Spades", "8"));
        assertFalse(player.canSplit());
        player.reset();
        player.placeBet(10);
        player.addCard(new Card("Hearts", "8"));
        assertFalse(player.canSplit());
    }

    /**
     * Tests doubling down on the second hand doubles only that bet.
     */
    public void testDoubleDownOnSplitHand() {
        player.placeBet(10);
        player.addCard(new Card("Hearts", "8"));
        player.addCard(new Card("Spades", "8"));
        player.split();
        player.getHand(1).addCard(five);
        player.setActiveHand(1);
        assertTrue(player.canDoubleDown());
        player.doubleDown();
        assertEquals(20, player.getCurrentBet(), 0.01);
        assertEquals(10, player.getBet(0), 0.01);
        assertEquals(70, player.getBankroll(), 0.01);
    }

    /**
     * Tests allBust is true only when every hand is over 21.
     */
    public void testAllBust() {
        player.placeBet(10);
        player.addCard(new Card("Hearts", "8"));
        player.addCard(new Card("Spades", "8"));
        player.split();
        player.getHand(0).addCard(king);
        player.getHand(0).addCard(new Card("Diamonds", "K"));
        assertFalse(player.allBust());
        player.getHand(1).addCard(new Card("Clubs", "Q"));
        player.getHand(1).addCard(new Card("Diamonds", "Q"));
        assertTrue(player.allBust());
    }

    /**
     * Tests switching between the two hands after a split.
     */
    public void testSetActiveHand() {
        player.placeBet(10);
        player.addCard(new Card("Hearts", "8"));
        player.addCard(new Card("Spades", "8"));
        player.split();
        player.setActiveHand(1);
        player.addCard(five);
        assertEquals(2, player.getHand(1).size());
        assertEquals(13, player.getScore());
        player.setActiveHand(0);
        assertEquals(1, player.getHand().size());
    }

    /**
     * Tests setActiveHand rejects a hand that is not in play.
     */
    public void testSetActiveHandBadInput() {
        Exception thrown = null;
        try {
            player.setActiveHand(1);
        }
        catch (Exception e) {
            thrown = e;
        }
        assertTrue(thrown instanceof IllegalArgumentException);
        thrown = null;
        try {
            player.setActiveHand(-1);
        }
        catch (Exception e) {
            thrown = e;
        }
        assertTrue(thrown instanceof IllegalArgumentException);
    }

    /**
     * Tests reset clears the second hand and its bet.
     */
    public void testResetAfterSplit() {
        player.placeBet(10);
        player.addCard(new Card("Hearts", "8"));
        player.addCard(new Card("Spades", "8"));
        player.split();
        player.reset();
        assertEquals(1, player.getHandCount());
        assertEquals(0, player.getHand(0).size());
        assertEquals(0, player.getHand(1).size());
        assertEquals(0, player.getBet(1), 0.01);
        assertEquals(80, player.getBankroll(), 0.01);
    }
}
