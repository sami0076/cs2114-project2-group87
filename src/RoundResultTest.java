import student.TestCase;

/**
 * Tests for the RoundResult class and its storage in LinkedChain.
 *
 * @author Pratham Singh
 * @version October 2026
 */
public class RoundResultTest extends TestCase {
    private RoundResult win;
    private RoundResult loss;
    private RoundResult tie;
    private RoundResult blackjack;

    /**
     * Sets up round results for testing.
     */
    public void setUp() {
        win = new RoundResult("Win", 25.0, 50.0);
        loss = new RoundResult("Loss", 25.0, 0.0);
        tie = new RoundResult("Tie", 25.0, 25.0);
        blackjack = new RoundResult("Blackjack", 100.0, 250.0);
    }

    /**
     * Tests valid constructor and getter values.
     */
    public void testConstructorValid() {
        assertEquals("Win", win.getOutcome());
        assertEquals(25.0, win.getBetAmount(), 0.001);
        assertEquals(50.0, win.getPayout(), 0.001);
    }

    /**
     * Tests constructor rejects invalid or null outcomes.
     */
    public void testConstructorBadOutcome() {
        Exception thrown = null;
        try {
            new RoundResult(null, 10.0, 20.0);
        } catch (Exception e) {
            thrown = e;
        }
        assertTrue(thrown instanceof IllegalArgumentException);

        thrown = null;
        try {
            new RoundResult("Draw", 10.0, 20.0);
        } catch (Exception e) {
            thrown = e;
        }
        assertTrue(thrown instanceof IllegalArgumentException);
    }

    /**
     * Tests constructor rejects zero or negative bet amounts.
     */
    public void testConstructorBadBet() {
        Exception thrown = null;
        try {
            new RoundResult("Win", 0.0, 0.0);
        } catch (Exception e) {
            thrown = e;
        }
        assertTrue(thrown instanceof IllegalArgumentException);

        thrown = null;
        try {
            new RoundResult("Win", -20.0, 0.0);
        } catch (Exception e) {
            thrown = e;
        }
        assertTrue(thrown instanceof IllegalArgumentException);
    }

    /**
     * Tests constructor rejects negative payout amounts.
     */
    public void testConstructorBadPayout() {
        Exception thrown = null;
        try {
            new RoundResult("Loss", 10.0, -5.0);
        } catch (Exception e) {
            thrown = e;
        }
        assertTrue(thrown instanceof IllegalArgumentException);
    }

    /**
     * Tests getOutcome.
     */
    public void testGetOutcome() {
        assertEquals("Win", win.getOutcome());
        assertEquals("Loss", loss.getOutcome());
        assertEquals("Tie", tie.getOutcome());
        assertEquals("Blackjack", blackjack.getOutcome());
    }

    /**
     * Tests getBetAmount.
     */
    public void testGetBetAmount() {
        assertEquals(25.0, win.getBetAmount(), 0.001);
        assertEquals(100.0, blackjack.getBetAmount(), 0.001);
    }

    /**
     * Tests getPayout.
     */
    public void testGetPayout() {
        assertEquals(50.0, win.getPayout(), 0.001);
        assertEquals(0.0, loss.getPayout(), 0.001);
    }

    /**
     * Tests getNetProfit for win, loss, and tie.
     */
    public void testGetNetProfit() {
        assertEquals(25.0, win.getNetProfit(), 0.001);
        assertEquals(-25.0, loss.getNetProfit(), 0.001);
        assertEquals(0.0, tie.getNetProfit(), 0.001);
        assertEquals(150.0, blackjack.getNetProfit(), 0.001);
    }

    /**
     * Tests equals with matching objects.
     */
    public void testEquals() {
        assertTrue(win.equals(win));
        assertTrue(win.equals(new RoundResult("Win", 25.0, 50.0)));
    }

    /**
     * Tests equals with non-matching or null objects.
     */
    public void testEqualsBadInput() {
        assertFalse(win.equals(null));
        assertFalse(win.equals("Win"));
        assertFalse(win.equals(new RoundResult("Loss", 25.0, 50.0)));
        assertFalse(win.equals(new RoundResult("Win", 30.0, 50.0)));
        assertFalse(win.equals(new RoundResult("Win", 25.0, 60.0)));
    }

    /**
     * Tests toString.
     */
    public void testToString() {
        assertEquals("Win (Bet: $25.0, Payout: $50.0)", win.toString());
        assertEquals("Loss (Bet: $25.0, Payout: $0.0)", loss.toString());
    }

    /**
     * Tests reusing generic LinkedChain with RoundResult elements.
     */
    public void testLinkedChainReuse() {
        LinkedChain<RoundResult> history = new LinkedChain<RoundResult>();
        assertTrue(history.isEmpty());
        assertEquals(0, history.size());

        assertNull(history.remove(win));

        history.add(win);
        history.add(loss);
        assertEquals(2, history.size());
        assertTrue(history.contains(win));
        assertTrue(history.contains(loss));
        assertFalse(history.contains(tie));

        assertNull(history.remove(tie));
        assertEquals(2, history.size());

        RoundResult removed = history.remove(win);
        assertEquals(win, removed);
        assertEquals(1, history.size());
        assertFalse(history.contains(win));
    }
}
