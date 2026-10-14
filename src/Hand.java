/**
 * Represents a hand of cards held by a participant in Blackjack.
 *
 * @author Pratham Singh
 * @version October 2026
 */
public class Hand {
    private LinkedChain<Card> cards;
    private int total;
    private int softAces;

    /**
     * Creates an empty hand.
     */
    public Hand() {
        cards = new LinkedChain<Card>();
        total = 0;
        softAces = 0;
    }

    /**
     * Adds one card to the hand.
     *
     * @param card the card to add
     * @throws IllegalArgumentException if card is null
     */
    public void addCard(Card card) {
        if (card == null) {
            throw new IllegalArgumentException("Card cannot be null");
        }
        cards.add(card);
    }

    /**
     * Calculates the hand total and adjusts Ace values as needed.
     *
     * @return the calculated score
     */
    public int getScore() {
        total = 0;
        softAces = 0;
        Card[] cardArray = cards.toArray(new Card[cards.size()]);
        for (Card card : cardArray) {
            if (card.isAce()) {
                total += 11;
                softAces++;
            } else {
                total += card.getValue();
            }
        }
        while (total > 21 && softAces > 0) {
            total -= 10;
            softAces--;
        }
        return total;
    }

    /**
     * Gets the collection of cards in this hand.
     *
     * @return the cards in the hand
     */
    public LinkedChain<Card> getCards() {
        return cards;
    }

    /**
     * Gets the number of cards in this hand.
     *
     * @return the number of cards
     */
    public int size() {
        return cards.size();
    }

    /**
     * Checks if the hand has busted.
     *
     * @return true if score is greater than 21
     */
    public boolean isBust() {
        return getScore() > 21;
    }

    /**
     * Checks if the hand is a blackjack (two cards totaling 21).
     *
     * @return true if blackjack, false otherwise
     */
    public boolean isBlackjack() {
        return cards.size() == 2 && getScore() == 21;
    }

    /**
     * Clears all cards from the hand.
     */
    public void clear() {
        cards.clear();
        total = 0;
        softAces = 0;
    }

    /**
     * Returns a string representation of the hand.
     *
     * @return the hand as text
     */
    @Override
    public String toString() {
        return cards.toString();
    }
}
