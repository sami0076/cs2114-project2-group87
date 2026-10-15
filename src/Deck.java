import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A deck of cards that has the ability to shuffle, deal, and discard the cards
 * within the deck.
 * 
 * @author Angelina Gust
 * @version October 2026
 */
public class Deck
{
    private LinkedChain<Card> drawPile;
    private LinkedChain<Card> discardPile;

    /**
     * Creates a new, empty deck with draw and discard piles.
     */
    public Deck()
    {
        this.drawPile = new LinkedChain<>();
        this.discardPile = new LinkedChain<>();
    }


    /**
     * Builds the requested number of standard decks, along with shuffling the
     * cards and storing them in the draw pile.
     * 
     * @param numDecks
     *            the number of standard decks to build
     * @throws IllegalArgumentException
     *             if numDecks is less than 1 or greater than 6
     */
    public void build(int numDecks)
    {
        if (numDecks < 1 || numDecks > 6)
        {
            throw new IllegalArgumentException(
                "Number of decks must be between 1 and 6.");
        }

        this.drawPile = new LinkedChain<>();
        this.discardPile = new LinkedChain<>();

        String[] suits = { "Hearts", "Diamonds", "Clubs", "Spades" };
        String[] ranks = { "A", "2", "3", "4", "5", "6", "7", "8", "9", "10",
            "J", "Q", "K" };

        List<Card> tempCards = new ArrayList<>();

        for (int i = 0; i < numDecks; i++)
        {
            for (String suit : suits)
            {
                for (String rank : ranks)
                {
                    tempCards.add(new Card(suit, rank));
                }
            }
        }

        Collections.shuffle(tempCards);

        for (Card card : tempCards)
        {
            drawPile.add(card);
        }
    }


    /**
     * Removes and returns the top card from the draw pile. If the draw pile is
     * empty, it causes the cards to be shuffled and put back in the draw pile.
     * 
     * @return the Card dealt from the top of the draw pile
     * @throws IllegalStateException
     *             if both the draw and the discard pile are empty
     */
    public Card dealCard()
    {
        if (drawPile.isEmpty())
        {
            if (discardPile.isEmpty())
            {
                throw new IllegalStateException(
                    "Both draw pile and discard pile are empty. "
                        + "Cannot deal card.");
            }
            shuffle();
        }
        return drawPile.removeFirst();
    }


    /**
     * Recycles all cards from the discard pile, and rearranges their order
     * before putting them back into the draw pile.
     */
    private void shuffle()
    {
        List<Card> tempCards = new ArrayList<>();
        while (!discardPile.isEmpty())
        {
            tempCards.add(discardPile.removeFirst());
        }

        Collections.shuffle(tempCards);

        for (Card card : tempCards)
        {
            drawPile.add(card);
        }
    }


    /**
     * Adds a played card to the discard pile.
     * 
     * @param card
     *            the Card to be discarded
     */
    public void discard(Card card)
    {
        discardPile.add(card);
    }

}
