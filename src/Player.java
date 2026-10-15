/**
 * Represents the user in Blackjack. Holds a bankroll, a current bet,
 * and a hand.
 *
 * @author Sami Saleh
 * @version October 2026
 */
public class Player {
    private double bankroll;
    private double currentBet;
    private Hand hand;

    /**
     * Creates a player with a starting bankroll.
     *
     * @param bankroll the starting money
     * @throws IllegalArgumentException if bankroll is not positive
     */
    public Player(double bankroll) {
        if (bankroll <= 0) {
            throw new IllegalArgumentException("Bankroll must be positive");
        }
        this.bankroll = bankroll;
        currentBet = 0;
        hand = new Hand();
    }

    /**
     * Places a bet and takes it out of the bankroll.
     *
     * @param amount the amount to bet
     * @throws IllegalArgumentException if amount is not positive or
     *         is more than the bankroll
     */
    public void placeBet(double amount) {
        if (amount <= 0 || amount > bankroll) {
            throw new IllegalArgumentException(
                "Invalid bet. Enter a positive number within your bankroll.");
        }
        bankroll -= amount;
        currentBet = amount;
    }

    /**
     * Checks if the player can double down (two cards and enough money).
     *
     * @return true if double down is allowed
     */
    public boolean canDoubleDown() {
        return hand.size() == 2 && currentBet > 0 && bankroll >= currentBet;
    }

    /**
     * Doubles the current bet.
     *
     * @throws IllegalStateException if double down is not allowed
     */
    public void doubleDown() {
        if (!canDoubleDown()) {
            throw new IllegalStateException("Cannot double down now");
        }
        bankroll -= currentBet;
        currentBet *= 2;
    }

    /**
     * Adds a payout to the bankroll.
     *
     * @param payout the amount to add
     * @throws IllegalArgumentException if payout is negative
     */
    public void receivePayout(double payout) {
        if (payout < 0) {
            throw new IllegalArgumentException("Payout cannot be negative");
        }
        bankroll += payout;
    }

    /**
     * Adds one card to the player's hand.
     *
     * @param card the card to add
     * @throws IllegalArgumentException if card is null
     */
    public void addCard(Card card) {
        hand.addCard(card);
    }

    /**
     * @return the player's hand score
     */
    public int getScore() {
        return hand.getScore();
    }

    /**
     * @return true if the player's hand is over 21
     */
    public boolean isBust() {
        return hand.isBust();
    }

    /**
     * @return true if the player has no money left
     */
    public boolean isBroke() {
        return bankroll <= 0;
    }

    /**
     * @return the bankroll
     */
    public double getBankroll() {
        return bankroll;
    }

    /**
     * @return the current bet
     */
    public double getCurrentBet() {
        return currentBet;
    }

    /**
     * @return the player's hand
     */
    public Hand getHand() {
        return hand;
    }

    /**
     * Clears the hand and bet for a new round.
     */
    public void reset() {
        hand.clear();
        currentBet = 0;
    }

    /**
     * @return the player as text
     */
    @Override
    public String toString() {
        return "Player Hand: " + hand.toString() + " Bankroll: $" + bankroll;
    }
}
