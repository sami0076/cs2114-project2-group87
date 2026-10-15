import java.util.Scanner;
import student.TestCase;

/**
 * Tests for the GameController class.
 *
 * @author Sami Saleh
 * @version October 2026
 */
public class GameControllerTest extends TestCase {
    private static final String BAD_BET =
        "Invalid bet. Enter a positive number within your bankroll.";
    private static final String BAD_COMMAND =
        "Command not recognized. Please type 'Hit' or 'Stand'.";

    private GameController make(String input) {
        return new GameController(new Scanner(input));
    }

    private GameController withBet(String bet) {
        GameController game = make("1 " + bet);
        game.chooseDecks();
        game.placeBet();
        return game;
    }

    /**
     * Tests the constructor sets up a new game.
     */
    public void testConstructor() {
        GameController game = make("");
        assertEquals(100, game.getPlayer().getBankroll(), 0.01);
        assertTrue(game.getHistory().isEmpty());
        assertEquals(0, game.getDealer().getScore());
    }

    /**
     * Tests the constructor rejects a null scanner.
     */
    public void testConstructorBadInput() {
        Exception thrown = null;
        try {
            new GameController(null);
        }
        catch (Exception e) {
            thrown = e;
        }
        assertTrue(thrown instanceof IllegalArgumentException);
    }

    /**
     * Tests choosing 2 decks gives exactly 104 cards.
     */
    public void testChooseDecks() {
        GameController game = make("2");
        game.chooseDecks();
        for (int i = 0; i < 52; i++) {
            game.processTurn("hit");
            game.processTurn("hit");
            game.getPlayer().reset();
        }
        Exception thrown = null;
        try {
            game.processTurn("hit");
        }
        catch (Exception e) {
            thrown = e;
        }
        assertTrue(thrown instanceof IllegalStateException);
    }

    /**
     * Tests text, 0, and 7 decks are rejected and the user is re-prompted.
     */
    public void testChooseDecksBadInput() {
        GameController game = make("abc 0 7 1");
        game.chooseDecks();
        String out = systemOut().getHistory();
        assertEquals(3, out.split("Invalid number of decks").length - 1);
        game.processTurn("hit");
        assertEquals(1, game.getPlayer().getHand().size());
    }

    /**
     * Tests a valid $10 bet with a $100 bankroll.
     */
    public void testPlaceBet() {
        GameController game = make("10");
        game.placeBet();
        assertEquals(10, game.getPlayer().getCurrentBet(), 0.01);
        assertEquals(90, game.getPlayer().getBankroll(), 0.01);
    }

    /**
     * Tests text, negative, zero, and over-bankroll bets are rejected.
     */
    public void testPlaceBetBadInput() {
        GameController game = make("abc -5 0 500 $10 10");
        game.placeBet();
        String out = systemOut().getHistory();
        assertEquals(5, out.split(BAD_BET).length - 1);
        assertEquals(10, game.getPlayer().getCurrentBet(), 0.01);
        assertEquals(90, game.getPlayer().getBankroll(), 0.01);
    }

    /**
     * Tests placeBet with no input left does not change state.
     */
    public void testPlaceBetNoInput() {
        GameController game = make("");
        game.placeBet();
        assertEquals(0, game.getPlayer().getCurrentBet(), 0.01);
        assertEquals(100, game.getPlayer().getBankroll(), 0.01);
    }

    /**
     * Tests dealInitialCards gives two cards each.
     */
    public void testDealInitialCards() {
        GameController game = withBet("10");
        game.dealInitialCards();
        assertEquals(2, game.getPlayer().getHand().size());
        assertEquals(2, game.getDealer().getHand().size());
    }

    /**
     * Tests dealInitialCards with no deck built throws.
     */
    public void testDealInitialCardsNoDeck() {
        GameController game = make("");
        Exception thrown = null;
        try {
            game.dealInitialCards();
        }
        catch (Exception e) {
            thrown = e;
        }
        assertTrue(thrown instanceof IllegalStateException);
    }

    /**
     * Tests Hit adds one card and Stand ends the turn.
     */
    public void testProcessTurn() {
        GameController game = withBet("10");
        assertTrue(game.processTurn("Hit"));
        assertEquals(1, game.getPlayer().getHand().size());
        assertFalse(game.processTurn("stand"));
        assertEquals(1, game.getPlayer().getHand().size());
    }

    /**
     * Tests an unknown command shows an error and changes nothing.
     */
    public void testProcessTurnBadInput() {
        GameController game = withBet("10");
        assertTrue(game.processTurn("Jump"));
        assertTrue(game.processTurn(null));
        assertEquals(0, game.getPlayer().getHand().size());
        assertEquals(90, game.getPlayer().getBankroll(), 0.01);
        assertTrue(systemOut().getHistory().contains(BAD_COMMAND));
    }

    /**
     * Tests Hit that busts ends the turn.
     */
    public void testProcessTurnBust() {
        GameController game = withBet("10");
        game.getPlayer().addCard(new Card("Hearts", "K"));
        game.getPlayer().addCard(new Card("Spades", "Q"));
        game.getPlayer().addCard(new Card("Clubs", "2"));
        assertFalse(game.processTurn("hit"));
        assertTrue(game.getPlayer().isBust());
    }

    /**
     * Tests Double on the first two cards doubles the bet and ends the turn.
     */
    public void testProcessTurnDouble() {
        GameController game = withBet("10");
        game.getPlayer().addCard(new Card("Hearts", "5"));
        game.getPlayer().addCard(new Card("Spades", "6"));
        assertFalse(game.processTurn("double"));
        assertEquals(20, game.getPlayer().getCurrentBet(), 0.01);
        assertEquals(3, game.getPlayer().getHand().size());
    }

    /**
     * Tests Double on the third card is rejected.
     */
    public void testProcessTurnDoubleThirdCard() {
        GameController game = withBet("10");
        game.getPlayer().addCard(new Card("Hearts", "2"));
        game.getPlayer().addCard(new Card("Spades", "3"));
        game.getPlayer().addCard(new Card("Clubs", "4"));
        assertTrue(game.processTurn("double"));
        assertEquals(10, game.getPlayer().getCurrentBet(), 0.01);
        assertEquals(3, game.getPlayer().getHand().size());
        assertTrue(systemOut().getHistory().contains(
            "You can only double down on your first two cards."));
    }

    /**
     * Tests the dealer draws until 17 or more.
     */
    public void testDealerPlay() {
        GameController game = withBet("10");
        game.getDealer().addCard(new Card("Hearts", "2"));
        game.dealerPlay();
        assertTrue(game.getDealer().getScore() >= 17);
    }

    /**
     * Tests the dealer takes no card when already at 17.
     */
    public void testDealerPlayStandsOn17() {
        GameController game = withBet("10");
        game.getDealer().addCard(new Card("Hearts", "K"));
        game.getDealer().addCard(new Card("Spades", "7"));
        game.dealerPlay();
        assertEquals(2, game.getDealer().getHand().size());
    }

    /**
     * Tests a win pays 2x and is recorded in history.
     */
    public void testFinishRoundWin() {
        GameController game = withBet("10");
        game.getPlayer().addCard(new Card("Hearts", "K"));
        game.getPlayer().addCard(new Card("Spades", "9"));
        game.getDealer().addCard(new Card("Clubs", "K"));
        game.getDealer().addCard(new Card("Clubs", "8"));
        RoundResult result = game.finishRound();
        assertEquals("Win", result.getOutcome());
        assertEquals(110, game.getPlayer().getBankroll(), 0.01);
        assertEquals(1, game.getHistory().size());
        assertEquals(0, game.getPlayer().getHand().size());
        assertEquals(0, game.getDealer().getHand().size());
    }

    /**
     * Tests loss, bust, tie, blackjack, and dealer bust outcomes.
     */
    public void testFinishRoundOutcomes() {
        GameController game = withBet("10");
        game.getPlayer().addCard(new Card("Hearts", "K"));
        game.getPlayer().addCard(new Card("Spades", "7"));
        game.getDealer().addCard(new Card("Clubs", "K"));
        game.getDealer().addCard(new Card("Clubs", "9"));
        assertEquals("Loss", game.finishRound().getOutcome());
        assertEquals(90, game.getPlayer().getBankroll(), 0.01);

        game.getPlayer().placeBet(10);
        game.getPlayer().addCard(new Card("Hearts", "K"));
        game.getPlayer().addCard(new Card("Spades", "Q"));
        game.getPlayer().addCard(new Card("Spades", "5"));
        game.getDealer().addCard(new Card("Clubs", "K"));
        game.getDealer().addCard(new Card("Clubs", "Q"));
        game.getDealer().addCard(new Card("Clubs", "5"));
        assertEquals("Loss", game.finishRound().getOutcome());

        game.getPlayer().placeBet(10);
        game.getPlayer().addCard(new Card("Hearts", "K"));
        game.getPlayer().addCard(new Card("Spades", "8"));
        game.getDealer().addCard(new Card("Clubs", "Q"));
        game.getDealer().addCard(new Card("Clubs", "8"));
        assertEquals("Tie", game.finishRound().getOutcome());
        assertEquals(80, game.getPlayer().getBankroll(), 0.01);

        game.getPlayer().placeBet(10);
        game.getPlayer().addCard(new Card("Hearts", "A"));
        game.getPlayer().addCard(new Card("Spades", "K"));
        game.getDealer().addCard(new Card("Clubs", "Q"));
        game.getDealer().addCard(new Card("Clubs", "9"));
        assertEquals("Blackjack", game.finishRound().getOutcome());
        assertEquals(95, game.getPlayer().getBankroll(), 0.01);

        game.getPlayer().placeBet(10);
        game.getPlayer().addCard(new Card("Hearts", "2"));
        game.getPlayer().addCard(new Card("Spades", "3"));
        game.getDealer().addCard(new Card("Clubs", "Q"));
        game.getDealer().addCard(new Card("Clubs", "6"));
        game.getDealer().addCard(new Card("Diamonds", "K"));
        assertEquals("Win", game.finishRound().getOutcome());
        assertEquals(105, game.getPlayer().getBankroll(), 0.01);
        assertEquals(5, game.getHistory().size());
    }

    /**
     * Tests finishRound with no bet throws and records nothing.
     */
    public void testFinishRoundNoBet() {
        GameController game = make("");
        Exception thrown = null;
        try {
            game.finishRound();
        }
        catch (Exception e) {
            thrown = e;
        }
        assertTrue(thrown instanceof IllegalStateException);
        assertTrue(game.getHistory().isEmpty());
    }

    /**
     * Tests one full round is recorded.
     */
    public void testPlayRound() {
        GameController game = make("1 10 stand");
        game.chooseDecks();
        game.playRound();
        assertEquals(1, game.getHistory().size());
    }

    /**
     * Tests a round with bad bets and commands still finishes.
     */
    public void testPlayRoundBadInput() {
        GameController game = make("1 abc 500 10 jump stand");
        game.chooseDecks();
        game.playRound();
        assertEquals(1, game.getHistory().size());
        assertTrue(systemOut().getHistory().contains(BAD_BET));
    }

    /**
     * Tests the whole game runs and prints a summary.
     */
    public void testPlay() {
        GameController game = make("1 play 10 stand quit");
        game.play();
        assertEquals(1, game.getHistory().size());
        assertTrue(systemOut().getHistory().contains("Rounds played: 1"));
    }

    /**
     * Tests the game survives a bad menu command.
     */
    public void testPlayBadInput() {
        GameController game = make("9 1 maybe quit");
        game.play();
        assertTrue(systemOut().getHistory().contains(
            "Command not recognized. Please type 'Play' or 'Quit'."));
        assertTrue(game.getHistory().isEmpty());
    }

    /**
     * Tests the summary after rounds are played.
     */
    public void testGetSummary() {
        GameController game = withBet("10");
        game.getPlayer().addCard(new Card("Hearts", "K"));
        game.getPlayer().addCard(new Card("Spades", "9"));
        game.getDealer().addCard(new Card("Clubs", "K"));
        game.getDealer().addCard(new Card("Clubs", "8"));
        game.finishRound();
        assertEquals("Rounds played: 1, Wins: 1, Losses: 0, Ties: 0, "
            + "Final money: $110.0", game.getSummary());
    }

    /**
     * Tests the summary with no rounds played.
     */
    public void testGetSummaryEmpty() {
        assertEquals("Rounds played: 0, Wins: 0, Losses: 0, Ties: 0, "
            + "Final money: $100.0", make("").getSummary());
    }
}
