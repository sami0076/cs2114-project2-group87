import java.util.Scanner;

/**
 * Runs the Blackjack game loop in the console.
 *
 * @author Sami Saleh
 * @version October 2026
 */
public class GameController {
    private static final double PEEK_COST = 25;
    private static final double DOUBLE_COST = 40;

    private Deck deck;
    private Player player;
    private Dealer dealer;
    private LinkedChain<RoundResult> history;
    private Scanner in;

    /**
     * Creates a game that reads input from the given scanner.
     *
     * @param in the input source
     * @throws IllegalArgumentException if in is null
     */
    public GameController(Scanner in) {
        if (in == null) {
            throw new IllegalArgumentException("Scanner cannot be null");
        }
        this.in = in;
        deck = new Deck();
        player = new Player(100);
        dealer = new Dealer();
        history = new LinkedChain<RoundResult>();
    }

    /**
     * Asks for the number of decks (1-6) until a valid number is entered.
     */
    public void chooseDecks() {
        System.out.println("How many decks (1-6)?");
        while (in.hasNext()) {
            String input = in.next();
            try {
                deck.build(Integer.parseInt(input));
                return;
            }
            catch (IllegalArgumentException e) {
                System.out.println("Invalid number of decks. Enter 1 to 6.");
            }
        }
    }

    /**
     * Asks for a bet until a valid amount is entered.
     */
    public void placeBet() {
        System.out.println("Bankroll: $" + player.getBankroll()
            + ". Enter your bet:");
        while (in.hasNext()) {
            String input = in.next();
            try {
                player.placeBet(Double.parseDouble(input));
                return;
            }
            catch (IllegalArgumentException e) {
                System.out.println(
                    "Invalid bet. Enter a positive number within your bankroll.");
            }
        }
    }

    /**
     * Deals two cards each to the player and dealer.
     */
    public void dealInitialCards() {
        for (int i = 0; i < 2; i++) {
            player.addCard(deck.dealCard());
            dealer.addCard(deck.dealCard());
        }
    }

    /**
     * Offers insurance when the dealer shows an Ace.
     */
    public void offerInsurance() {
        if (!dealer.getHand().getCards().get(0).isAce()) {
            return;
        }
        System.out.println("Dealer shows an Ace. Insurance? (Yes/No)");
        while (in.hasNext()) {
            String input = in.next();
            if ("no".equalsIgnoreCase(input)) {
                return;
            }
            if ("yes".equalsIgnoreCase(input)) {
                try {
                    player.placeInsurance();
                }
                catch (IllegalStateException e) {
                    System.out.println(e.getMessage());
                }
                return;
            }
            System.out.println("Please type 'Yes' or 'No'.");
        }
    }

    /**
     * Handles one player command: Hit, Stand, Double, or Split.
     *
     * @param command the command typed by the user
     * @return true if the player's turn continues, false if it is over
     */
    public boolean processTurn(String command) {
        if ("hit".equalsIgnoreCase(command)) {
            player.addCard(deck.dealCard());
            return !player.isBust();
        }
        if ("stand".equalsIgnoreCase(command)) {
            return false;
        }
        if ("double".equalsIgnoreCase(command)) {
            if (!player.canDoubleDown()) {
                System.out.println(
                    "You can only double down on your first two cards.");
                return true;
            }
            player.doubleDown();
            player.addCard(deck.dealCard());
            return false;
        }
        if ("split".equalsIgnoreCase(command)) {
            if (!player.canSplit()) {
                System.out.println("Cannot split. You need a matching pair "
                    + "on your first two cards and enough money to match "
                    + "your bet.");
                return true;
            }
            player.split();
            player.getHand(0).addCard(deck.dealCard());
            player.getHand(1).addCard(deck.dealCard());
            return true;
        }
        if ("peek".equalsIgnoreCase(command)) {
            if (!player.usePowerUp("Peek")) {
                System.out.println("You do not have that power-up.");
                return true;
            }
            System.out.println("Hole card: "
                + dealer.getHand().getCards().get(1));
            return true;
        }
        if ("freedouble".equalsIgnoreCase(command)) {
            if (player.getHand().size() != 2) {
                System.out.println(
                    "You can only double down on your first two cards.");
                return true;
            }
            if (!player.usePowerUp("Free Double")) {
                System.out.println("You do not have that power-up.");
                return true;
            }
            player.freeDouble();
            player.addCard(deck.dealCard());
            return false;
        }
        System.out.println(
            "Command not recognized. Please type 'Hit' or 'Stand'.");
        return true;
    }

    /**
     * Dealer takes cards until the score is 17 or more.
     */
    public void dealerPlay() {
        while (dealer.shouldHit()) {
            dealer.addCard(deck.dealCard());
        }
    }

    /**
     * Decides the outcome of every hand in play, pays the player,
     * records the results, and discards the cards.
     *
     * @return the result of the first hand
     * @throws IllegalStateException if no bet was placed
     */
    public RoundResult finishRound() {
        if (player.getBet(0) <= 0) {
            throw new IllegalStateException("No bet was placed");
        }
        if (player.getInsuranceBet() > 0 && dealer.getHand().isBlackjack()) {
            player.receivePayout(player.getInsuranceBet() * 3);
            System.out.println("Insurance pays $"
                + player.getInsuranceBet() * 2);
        }
        boolean allowBj = player.getHandCount() == 1;
        RoundResult first = null;
        for (int i = 0; i < player.getHandCount(); i++) {
            RoundResult result =
                settle(player.getHand(i), player.getBet(i), allowBj);
            history.add(result);
            System.out.println("Result: " + result);
            if (first == null) {
                first = result;
            }
            discardHand(player.getHand(i));
        }
        discardHand(dealer.getHand());
        player.reset();
        dealer.reset();
        return first;
    }

    /**
     * Scores one hand against the dealer and pays the player.
     *
     * @param hand the hand to settle
     * @param bet the bet on that hand
     * @param allowBj true if a two-card 21 pays 3 to 2, which is false
     *        for split hands
     * @return the result for that hand
     */
    private RoundResult settle(Hand hand, double bet, boolean allowBj) {
        int playerScore = hand.getScore();
        int dealerScore = dealer.getScore();
        boolean playerBj = allowBj && hand.isBlackjack();
        boolean dealerBj = dealer.getHand().isBlackjack();

        String outcome;
        double payout;
        if (hand.isBust()) {
            outcome = "Loss";
            payout = 0;
        }
        else if (playerBj && dealerBj) {
            outcome = "Tie";
            payout = bet;
        }
        else if (playerBj) {
            outcome = "Blackjack";
            payout = bet * 2.5;
        }
        else if (dealerBj || (!dealer.isBust() && dealerScore > playerScore)) {
            outcome = "Loss";
            payout = 0;
        }
        else if (dealer.isBust() || playerScore > dealerScore) {
            outcome = "Win";
            payout = bet * 2;
        }
        else {
            outcome = "Tie";
            payout = bet;
        }

        if (outcome.equals("Blackjack")) {
            player.addPowerUp("Peek");
        }
        player.receivePayout(payout);
        return new RoundResult(outcome, bet, payout);
    }

    /**
     * Plays one full round: bet, deal, player turn, dealer turn, result.
     */
    public void playRound() {
        placeBet();
        if (player.getCurrentBet() <= 0) {
            return;
        }
        dealInitialCards();
        System.out.println("Dealer shows: "
            + dealer.getHand().getCards().get(0));
        offerInsurance();
        playHand();
        if (player.getHandCount() > 1) {
            player.setActiveHand(1);
            System.out.println("Now playing your second hand.");
            playHand();
        }
        if (!player.allBust()) {
            dealerPlay();
        }
        System.out.println("Dealer hand: " + dealer.getHand()
            + " (" + dealer.getScore() + ")");
        finishRound();
    }

    /**
     * Buys one power-up for the player.
     *
     * @param name the power-up name
     * @param cost the price
     */
    private void buy(String name, double cost) {
        if (player.buyPowerUp(name, cost)) {
            System.out.println("Bought " + name + ".");
        }
        else {
            System.out.println("Not enough money.");
        }
    }

    /**
     * Plays the hand the player is currently on until the turn ends.
     */
    private void playHand() {
        System.out.println("Your hand: " + player.getHand()
            + " (" + player.getScore() + ")");
        boolean turn = !player.getHand().isBlackjack();
        while (turn) {
            System.out.println(
                "Hit, Stand, Double, Split, Peek, or FreeDouble?");
            if (!in.hasNext()) {
                return;
            }
            turn = processTurn(in.next());
            System.out.println("Your hand: " + player.getHand()
                + " (" + player.getScore() + ")");
        }
    }

    /**
     * Runs the game until the user quits or runs out of money.
     */
    public void play() {
        chooseDecks();
        while (!player.isBroke()) {
            System.out.println("Type 'Play', 'Quit', 'BuyPeek' ($"
                + PEEK_COST + "), or 'BuyDouble' ($" + DOUBLE_COST + "):");
            if (!in.hasNext()) {
                break;
            }
            String input = in.next();
            if ("quit".equalsIgnoreCase(input)) {
                break;
            }
            else if ("play".equalsIgnoreCase(input)) {
                playRound();
            }
            else if ("buypeek".equalsIgnoreCase(input)) {
                buy("Peek", PEEK_COST);
            }
            else if ("buydouble".equalsIgnoreCase(input)) {
                buy("Free Double", DOUBLE_COST);
            }
            else {
                System.out.println(
                    "Command not recognized. Please type 'Play' or 'Quit'.");
            }
        }
        System.out.println(getSummary());
    }

    /**
     * @return rounds played, wins, losses, ties, and final money
     */
    public String getSummary() {
        RoundResult[] results =
            history.toArray(new RoundResult[history.size()]);
        int wins = 0;
        int losses = 0;
        int ties = 0;
        for (RoundResult r : results) {
            if (r.getOutcome().equals("Loss")) {
                losses++;
            }
            else if (r.getOutcome().equals("Tie")) {
                ties++;
            }
            else {
                wins++;
            }
        }
        return "Rounds played: " + history.size() + ", Wins: " + wins
            + ", Losses: " + losses + ", Ties: " + ties
            + ", Final money: $" + player.getBankroll();
    }

    /**
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * @return the dealer
     */
    public Dealer getDealer() {
        return dealer;
    }

    /**
     * @return the round history
     */
    public LinkedChain<RoundResult> getHistory() {
        return history;
    }

    private void discardHand(Hand hand) {
        LinkedChain<Card> cards = hand.getCards();
        while (!cards.isEmpty()) {
            deck.discard(cards.removeFirst());
        }
    }

    /**
     * Starts the game.
     *
     * @param args not used
     */
    public static void main(String[] args) {
        new GameController(new Scanner(System.in)).play();
    }
}
