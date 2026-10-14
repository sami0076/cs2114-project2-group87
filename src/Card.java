/**
 * A playing card with a suit, rank, value, and alternate value
 *
 * @author Sami Saleh
 * @version October 2026
 */
public class Card
{
    private String suit;
    private String rank;
    private int value;
    private int altValue;

    /**
     * Creates a card.
     *
     * @param suit Hearts, Diamonds, Clubs, or Spades
     * @param rank A, 2-10, J, Q, or K
     * @throws IllegalArgumentException if suit or rank is not valid
     */
    public Card(String suit, String rank)
    {
        if (!isValidSuit(suit) || !isValidRank(rank))
        {
            throw new IllegalArgumentException("Invalid card");
        }
        this.suit = suit;
        this.rank = rank;
        if (rank.equals("A"))
        {
            value = 11;
            altValue = 1;
        }
        else if (rank.equals("J") || rank.equals("Q") || rank.equals("K"))
        {
            value = 10;
            altValue = 10;
        }
        else
        {
            value = Integer.parseInt(rank);
            altValue = value;
        }
    }

    /**
     * @return the suit
     */
    public String getSuit()
    {
        return suit;
    }

    /**
     * @return the rank
     */
    public String getRank()
    {
        return rank;
    }

    /**
     * @return the value (11 for an Ace)
     */
    public int getValue()
    {
        return value;
    }

    /**
     * @return the alternate value (1 for an Ace)
     */
    public int getAltValue()
    {
        return altValue;
    }

    /**
     * @return true if this card is an Ace
     */
    public boolean isAce()
    {
        return rank.equals("A");
    }

    /**
     * Two cards are equal if they have the same suit and rank.
     *
     * @param obj the object to compare
     * @return true if equal
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass())
        {
            return false;
        }
        Card other = (Card)obj;
        return suit.equals(other.suit) && rank.equals(other.rank);
    }

    /**
     * @return the card as text, like "K of Diamonds"
     */
    @Override
    public String toString()
    {
        return rank + " of " + suit;
    }

    private boolean isValidSuit(String s)
    {
        return "Hearts".equals(s) || "Diamonds".equals(s) || "Clubs".equals(s) || "Spades".equals(s);
    }

    private boolean isValidRank(String r)
    {
        String[] cardRanks = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
        for (String valid : cardRanks)
        {
            if (valid.equals(r))
            {
                return true;
            }
        }
        return false;
    }
}
