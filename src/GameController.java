import java.util.Scanner;

/**
 * Runs the Blackjack game loop in the console.
 *
 * @author Sami Saleh
 * @version October 2026
 */
public class GameController {
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
     * Handles one player command: Hit, Stand, or Double.
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
     * Decides the outcome, pays the player, records the result, and
     * discards the cards.
     *
     * @return the result of the round
     * @throws IllegalStateException if no bet was placed
     */
    public RoundResult finishRound() {
        double bet = player.getCurrentBet();
        if (bet <= 0) {
            throw new IllegalStateException("No bet was placed");
        }
        int playerScore = player.getScore();
        int dealerScore = dealer.getScore();
        boolean playerBj = player.getHand().isBlackjack();
        boolean dealerBj = dealer.getHand().isBlackjack();

        String outcome;
        double payout;
        if (player.isBust()) {
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

        player.receivePayout(payout);
        RoundResult result = new RoundResult(outcome, bet, payout);
        history.add(result);
        discardHand(player.getHand());
        discardHand(dealer.getHand());
        player.reset();
        dealer.reset();
        return result;
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
        System.out.println("Your hand: " + player.getHand()
            + " (" + player.getScore() + ")");

        boolean turn = !player.getHand().isBlackjack();
        while (turn) {
            System.out.println("Hit, Stand, or Double?");
            if (!in.hasNext()) {
                break;
            }
            turn = processTurn(in.next());
            System.out.println("Your hand: " + player.getHand()
                + " (" + player.getScore() + ")");
        }
        if (!player.isBust()) {
            dealerPlay();
        }
        System.out.println("Dealer hand: " + dealer.getHand()
            + " (" + dealer.getScore() + ")");
        System.out.println("Result: " + finishRound());
    }

    /**
     * Runs the game until the user quits or runs out of money.
     */
    public void play() {
        chooseDecks();
        while (!player.isBroke()) {
            System.out.println("Type 'Play' or 'Quit':");
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
