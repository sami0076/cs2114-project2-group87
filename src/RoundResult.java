/**
 * Represents the outcome of a single round of Blackjack.
 *
 * @author Pratham Singh
 * @version October 2026
 */
public class RoundResult {
    private String outcome;
    private double betAmount;
    private double payout;

    /**
     * Creates a round result.
     *
     * @param outcome   the outcome of the round (Win, Loss, Tie, or Blackjack)
     * @param betAmount the amount bet on the round
     * @param payout    the payout returned to the player
     * @throws IllegalArgumentException if outcome is invalid or amounts are invalid
     */
    public RoundResult(String outcome, double betAmount, double payout) {
        if (!isValidOutcome(outcome)) {
            throw new IllegalArgumentException("Invalid outcome");
        }
        if (betAmount <= 0) {
            throw new IllegalArgumentException("Bet amount must be positive");
        }
        if (payout < 0) {
            throw new IllegalArgumentException("Payout cannot be negative");
        }
        this.outcome = outcome;
        this.betAmount = betAmount;
        this.payout = payout;
    }

    /**
     * @return the outcome of the round
     */
    public String getOutcome() {
        return outcome;
    }

    /**
     * @return the bet amount
     */
    public double getBetAmount() {
        return betAmount;
    }

    /**
     * @return the payout amount
     */
    public double getPayout() {
        return payout;
    }

    /**
     * @return the net profit (payout minus bet amount)
     */
    public double getNetProfit() {
        return payout - betAmount;
    }

    /**
     * Two RoundResult objects are equal if their outcome, bet amount,
     * and payout are the same.
     *
     * @param obj the object to compare
     * @return true if equal
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        RoundResult other = (RoundResult) obj;
        return outcome.equals(other.outcome)
                && Double.compare(betAmount, other.betAmount) == 0
                && Double.compare(payout, other.payout) == 0;
    }

    /**
     * @return the round result as text
     */
    @Override
    public String toString() {
        return outcome + " (Bet: $" + betAmount + ", Payout: $" + payout + ")";
    }

    private boolean isValidOutcome(String o) {
        return "Win".equals(o) || "Loss".equals(o) || "Tie".equals(o) || "Blackjack".equals(o);
    }
}
