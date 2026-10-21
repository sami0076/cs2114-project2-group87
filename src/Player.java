/**
 * Represents the user in Blackjack. Holds a bankroll and up to two
 * hands, each with its own bet. A split creates the second hand, which
 * is played after the first one.
 *
 * @author Sami Saleh
 * @version October 2026
 */
public class Player {
    private double bankroll;
    private Hand[] hands;
    private double[] bets;
    private int handCount;
    private int active;
    private double insuranceBet;

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
        hands = new Hand[] {new Hand(), new Hand()};
        bets = new double[2];
        handCount = 1;
        active = 0;
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
        bets[0] = amount;
    }

    /**
     * Checks if the player can double down (two cards and enough money).
     *
     * @return true if double down is allowed
     */
    public boolean canDoubleDown() {
        return hands[active].size() == 2 && bets[active] > 0
            && bankroll >= bets[active];
    }

    /**
     * Doubles the bet on the hand being played.
     *
     * @throws IllegalStateException if double down is not allowed
     */
    public void doubleDown() {
        if (!canDoubleDown()) {
            throw new IllegalStateException("Cannot double down now");
        }
        bankroll -= bets[active];
        bets[active] *= 2;
    }

    /**
     * Checks if the player can split. The first hand must be the only
     * hand, hold two cards of the same value, and the bankroll must
     * cover a second bet of the same size.
     *
     * @return true if splitting is allowed
     */
    public boolean canSplit() {
        return handCount == 1 && hands[0].size() == 2 && bets[0] > 0
            && bankroll >= bets[0]
            && hands[0].getCards().get(0).getValue()
                == hands[0].getCards().get(1).getValue();
    }

    /**
     * Splits the first hand into two hands, moving one card to the new
     * hand and matching the original bet. The caller deals one more
     * card to each hand.
     *
     * @throws IllegalStateException if splitting is not allowed
     */
    public void split() {
        if (!canSplit()) {
            throw new IllegalStateException("Cannot split now");
        }
        bankroll -= bets[0];
        bets[1] = bets[0];
        handCount = 2;
        hands[1].addCard(hands[0].getCards().removeFirst());
    }

    /**
     * Takes a side bet of half the original bet against a dealer
     * blackjack.
     *
     * @throws IllegalStateException if the bankroll cannot cover it
     */
    public void placeInsurance() {
        double half = bets[0] / 2;
        if (half > bankroll) {
            throw new IllegalStateException("Not enough money for insurance.");
        }
        bankroll -= half;
        insuranceBet = half;
    }

    /**
     * @return the insurance side bet
     */
    public double getInsuranceBet() {
        return insuranceBet;
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
     * Adds one card to the hand being played.
     *
     * @param card the card to add
     * @throws IllegalArgumentException if card is null
     */
    public void addCard(Card card) {
        hands[active].addCard(card);
    }

    /**
     * @return the score of the hand being played
     */
    public int getScore() {
        return hands[active].getScore();
    }

    /**
     * @return true if the hand being played is over 21
     */
    public boolean isBust() {
        return hands[active].isBust();
    }

    /**
     * @return true if every hand in play is over 21
     */
    public boolean allBust() {
        for (int i = 0; i < handCount; i++) {
            if (!hands[i].isBust()) {
                return false;
            }
        }
        return true;
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
     * @return the bet on the hand being played
     */
    public double getCurrentBet() {
        return bets[active];
    }

    /**
     * @return the hand being played
     */
    public Hand getHand() {
        return hands[active];
    }

    /**
     * @param index the hand number, 0 or 1
     * @return that hand
     */
    public Hand getHand(int index) {
        return hands[index];
    }

    /**
     * @param index the hand number, 0 or 1
     * @return the bet on that hand
     */
    public double getBet(int index) {
        return bets[index];
    }

    /**
     * @return how many hands are in play, 1 or 2 after a split
     */
    public int getHandCount() {
        return handCount;
    }

    /**
     * Switches which hand is being played.
     *
     * @param index the hand number, from 0 to getHandCount() - 1
     * @throws IllegalArgumentException if index is out of range
     */
    public void setActiveHand(int index) {
        if (index < 0 || index >= handCount) {
            throw new IllegalArgumentException("No such hand");
        }
        active = index;
    }

    /**
     * Clears both hands and both bets for a new round.
     */
    public void reset() {
        hands[0].clear();
        hands[1].clear();
        bets[0] = 0;
        bets[1] = 0;
        handCount = 1;
        active = 0;
        insuranceBet = 0;
    }

    /**
     * @return the player as text
     */
    @Override
    public String toString() {
        return "Player Hand: " + hands[active].toString()
            + " Bankroll: $" + bankroll;
    }
}
