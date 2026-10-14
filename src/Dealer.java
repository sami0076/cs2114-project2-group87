/**
 * Represents the computer dealer in Blackjack.
 *
 * @author Pratham Singh
 * @version October 2026
 */
public class Dealer {
    private Hand hand;

    /**
     * Creates a dealer with a new hand.
     */
    public Dealer() {
        hand = new Hand();
    }

    /**
     * Creates a dealer with a specified hand.
     *
     * @param hand the hand to use
     * @throws IllegalArgumentException if hand is null
     */
    public Dealer(Hand hand) {
        if (hand == null) {
            throw new IllegalArgumentException("Hand cannot be null");
        }
        this.hand = hand;
    }

    /**
     * Adds one card to the dealer's hand.
     *
     * @param card the card to add
     * @throws IllegalArgumentException if card is null
     */
    public void addCard(Card card) {
        if (card == null) {
            throw new IllegalArgumentException("Card cannot be null");
        }
        hand.addCard(card);
    }

    /**
     * Checks if the dealer must hit according to fixed rules.
     * The dealer hits on 16 or below and stands on 17 or above.
     *
     * @return true if dealer should hit, false if dealer stands
     */
    public boolean shouldHit() {
        return hand.getScore() < 17;
    }

    /**
     * Gets the dealer's current score.
     *
     * @return the hand score
     */
    public int getScore() {
        return hand.getScore();
    }

    /**
     * Checks if the dealer has busted.
     *
     * @return true if score is greater than 21
     */
    public boolean isBust() {
        return hand.isBust();
    }

    /**
     * Gets the dealer's hand.
     *
     * @return the hand
     */
    public Hand getHand() {
        return hand;
    }

    /**
     * Clears the dealer's hand for a new round.
     */
    public void reset() {
        hand.clear();
    }

    /**
     * Returns a string representation of the dealer's hand.
     *
     * @return the dealer hand as text
     */
    @Override
    public String toString() {
        return "Dealer Hand: " + hand.toString();
    }
}
