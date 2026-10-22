import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class BlackjackGUI extends JFrame {
    private Deck deck;
    private Player player;
    private Dealer dealer;
    private LinkedChain<RoundResult> history;
    private boolean dealerHoleCardHidden;
    private boolean inRound;

    private JLabel bankrollLabel;
    private JLabel currentBetLabel;
    private JLabel statusLabel;
    private JLabel dealerScoreLabel;
    private JLabel playerScoreLabel;
    private JLabel splitScoreLabel;

    private JPanel dealerCardsPanel;
    private JPanel playerCardsPanel;
    private JPanel splitCardsPanel;

    private JTextField betField;
    private JButton dealButton;
    private JButton hitButton;
    private JButton standButton;
    private JButton doubleButton;
    private JButton splitButton;
    private JButton newGameButton;

    public BlackjackGUI() {
        deck = new Deck();
        deck.build(4);
        player = new Player(100);
        dealer = new Dealer();
        history = new LinkedChain<RoundResult>();
        dealerHoleCardHidden = true;
        inRound = false;

        setTitle("Blackjack");
        setSize(850, 680);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        Color tableGreen = new Color(34, 120, 60);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 10));
        topPanel.setBackground(new Color(20, 60, 30));

        bankrollLabel = new JLabel("Bankroll: $" + player.getBankroll());
        bankrollLabel.setForeground(Color.WHITE);
        bankrollLabel.setFont(new Font("SansSerif", Font.BOLD, 16));

        currentBetLabel = new JLabel("Current Bet: $0.00");
        currentBetLabel.setForeground(Color.WHITE);
        currentBetLabel.setFont(new Font("SansSerif", Font.BOLD, 16));

        newGameButton = new JButton("New Game");
        newGameButton.addActionListener(e -> resetGame());

        topPanel.add(bankrollLabel);
        topPanel.add(currentBetLabel);
        topPanel.add(newGameButton);
        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(tableGreen);

        JPanel dealerSection = new JPanel();
        dealerSection.setLayout(new BoxLayout(dealerSection, BoxLayout.Y_AXIS));
        dealerSection.setOpaque(false);

        JLabel dealerTitle = new JLabel("Dealer's Hand");
        dealerTitle.setForeground(Color.WHITE);
        dealerTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        dealerTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        dealerScoreLabel = new JLabel("Score: 0");
        dealerScoreLabel.setForeground(Color.WHITE);
        dealerScoreLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        dealerScoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        dealerCardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        dealerCardsPanel.setOpaque(false);

        dealerSection.add(Box.createVerticalStrut(10));
        dealerSection.add(dealerTitle);
        dealerSection.add(dealerScoreLabel);
        dealerSection.add(dealerCardsPanel);

        JPanel messageSection = new JPanel(new FlowLayout(FlowLayout.CENTER));
        messageSection.setOpaque(false);
        statusLabel = new JLabel("Welcome to Blackjack! Enter a bet and click Deal.");
        statusLabel.setForeground(Color.YELLOW);
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        messageSection.add(statusLabel);

        JPanel playerSection = new JPanel();
        playerSection.setLayout(new BoxLayout(playerSection, BoxLayout.Y_AXIS));
        playerSection.setOpaque(false);

        JLabel playerTitle = new JLabel("Player's Hand");
        playerTitle.setForeground(Color.WHITE);
        playerTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        playerTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        playerScoreLabel = new JLabel("Score: 0");
        playerScoreLabel.setForeground(Color.WHITE);
        playerScoreLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        playerScoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        playerCardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        playerCardsPanel.setOpaque(false);

        splitScoreLabel = new JLabel("Hand 2 Score: 0");
        splitScoreLabel.setForeground(Color.WHITE);
        splitScoreLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        splitScoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        splitScoreLabel.setVisible(false);

        splitCardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        splitCardsPanel.setOpaque(false);
        splitCardsPanel.setVisible(false);

        playerSection.add(playerTitle);
        playerSection.add(playerScoreLabel);
        playerSection.add(playerCardsPanel);
        playerSection.add(splitScoreLabel);
        playerSection.add(splitCardsPanel);

        centerPanel.add(dealerSection);
        centerPanel.add(messageSection);
        centerPanel.add(playerSection);
        add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(2, 1));
        bottomPanel.setBackground(new Color(20, 60, 30));

        JPanel betRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 8));
        betRow.setOpaque(false);

        JLabel betPrompt = new JLabel("Bet Amount: $");
        betPrompt.setForeground(Color.WHITE);
        betPrompt.setFont(new Font("SansSerif", Font.BOLD, 14));

        betField = new JTextField("10", 6);
        dealButton = new JButton("Deal");

        betRow.add(betPrompt);
        betRow.add(betField);
        betRow.add(dealButton);

        JPanel actionRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 8));
        actionRow.setOpaque(false);

        hitButton = new JButton("Hit");
        standButton = new JButton("Stand");
        doubleButton = new JButton("Double Down");
        splitButton = new JButton("Split");

        hitButton.setEnabled(false);
        standButton.setEnabled(false);
        doubleButton.setEnabled(false);
        splitButton.setEnabled(false);

        actionRow.add(hitButton);
        actionRow.add(standButton);
        actionRow.add(doubleButton);
        actionRow.add(splitButton);

        bottomPanel.add(betRow);
        bottomPanel.add(actionRow);
        add(bottomPanel, BorderLayout.SOUTH);

        dealButton.addActionListener(e -> startRound());
        hitButton.addActionListener(e -> hit());
        standButton.addActionListener(e -> stand());
        doubleButton.addActionListener(e -> doubleDown());
        splitButton.addActionListener(e -> split());

        updateUI();
    }

    private void resetGame() {
        deck = new Deck();
        deck.build(4);
        player = new Player(100);
        dealer = new Dealer();
        history = new LinkedChain<RoundResult>();
        inRound = false;
        dealerHoleCardHidden = true;

        dealButton.setEnabled(true);
        betField.setEnabled(true);
        hitButton.setEnabled(false);
        standButton.setEnabled(false);
        doubleButton.setEnabled(false);
        splitButton.setEnabled(false);

        splitCardsPanel.setVisible(false);
        splitScoreLabel.setVisible(false);

        statusLabel.setText("New game started. Place your bet and click Deal.");
        updateUI();
    }

    private void startRound() {
        double bet;
        try {
            bet = Double.parseDouble(betField.getText().trim());
        }
        catch (NumberFormatException ex) {
            statusLabel.setText("Please enter a valid numeric bet.");
            return;
        }

        if (bet <= 0 || bet > player.getBankroll()) {
            statusLabel.setText("Invalid bet. Must be between $1 and $" + player.getBankroll());
            return;
        }

        if (player.getHand(0).size() > 0 || dealer.getHand().size() > 0) {
            for (int i = 0; i < player.getHandCount(); i++) {
                discardHand(player.getHand(i));
            }
            discardHand(dealer.getHand());
            player.reset();
            dealer.reset();
        }

        try {
            player.placeBet(bet);
        }
        catch (IllegalArgumentException ex) {
            statusLabel.setText(ex.getMessage());
            return;
        }

        splitCardsPanel.setVisible(false);
        splitScoreLabel.setVisible(false);

        dealer.addCard(deck.dealCard());
        dealer.addCard(deck.dealCard());
        player.addCard(deck.dealCard());
        player.addCard(deck.dealCard());

        inRound = true;
        dealerHoleCardHidden = true;

        dealButton.setEnabled(false);
        betField.setEnabled(false);

        if (dealer.getHand().getCards().get(0).isAce() && player.getBankroll() >= bet / 2) {
            updateUI();
            int choice = JOptionPane.showConfirmDialog(
                this,
                "Dealer shows an Ace. Place insurance bet for $" + (bet / 2) + "?",
                "Insurance",
                JOptionPane.YES_NO_OPTION
            );
            if (choice == JOptionPane.YES_OPTION) {
                try {
                    player.placeInsurance();
                    statusLabel.setText("Insurance placed.");
                }
                catch (IllegalStateException ex) {
                    statusLabel.setText(ex.getMessage());
                }
            }
        }

        if (player.getHand().isBlackjack()) {
            dealerHoleCardHidden = false;
            updateUI();
            endRound();
            return;
        }

        hitButton.setEnabled(true);
        standButton.setEnabled(true);
        doubleButton.setEnabled(player.canDoubleDown());
        splitButton.setEnabled(player.canSplit());
        statusLabel.setText("Your turn: Hit, Stand, or Double Down.");
        updateUI();
    }

    private void hit() {
        if (!inRound) {
            return;
        }
        player.addCard(deck.dealCard());
        doubleButton.setEnabled(false);
        splitButton.setEnabled(false);

        if (player.isBust()) {
            if (player.getHandCount() > 1 && player.getHand() == player.getHand(0)) {
                player.setActiveHand(1);
                statusLabel.setText("First hand busted! Now playing Hand 2.");
                doubleButton.setEnabled(player.canDoubleDown());
                updateUI();
                return;
            }
            dealerHoleCardHidden = false;
            updateUI();
            endRound();
            return;
        }

        if (player.getScore() == 21) {
            stand();
            return;
        }

        updateUI();
    }

    private void stand() {
        if (!inRound) {
            return;
        }
        if (player.getHandCount() > 1 && player.getHand() == player.getHand(0)) {
            player.setActiveHand(1);
            statusLabel.setText("Now playing Hand 2.");
            doubleButton.setEnabled(player.canDoubleDown());
            updateUI();
            return;
        }

        hitButton.setEnabled(false);
        standButton.setEnabled(false);
        doubleButton.setEnabled(false);
        splitButton.setEnabled(false);

        dealerHoleCardHidden = false;
        if (!player.allBust()) {
            while (dealer.shouldHit()) {
                dealer.addCard(deck.dealCard());
            }
        }
        updateUI();
        endRound();
    }

    private void doubleDown() {
        if (!inRound || !player.canDoubleDown()) {
            return;
        }
        player.doubleDown();
        player.addCard(deck.dealCard());
        doubleButton.setEnabled(false);
        splitButton.setEnabled(false);

        if (player.getHandCount() > 1 && player.getHand() == player.getHand(0)) {
            player.setActiveHand(1);
            statusLabel.setText("Hand 1 doubled. Now playing Hand 2.");
            updateUI();
            return;
        }

        hitButton.setEnabled(false);
        standButton.setEnabled(false);
        dealerHoleCardHidden = false;

        if (!player.allBust()) {
            while (dealer.shouldHit()) {
                dealer.addCard(deck.dealCard());
            }
        }
        updateUI();
        endRound();
    }

    private void split() {
        if (!inRound || !player.canSplit()) {
            return;
        }
        player.split();
        player.getHand(0).addCard(deck.dealCard());
        player.getHand(1).addCard(deck.dealCard());

        splitCardsPanel.setVisible(true);
        splitScoreLabel.setVisible(true);

        splitButton.setEnabled(false);
        doubleButton.setEnabled(player.canDoubleDown());
        statusLabel.setText("Split! Playing Hand 1.");
        updateUI();
    }

    private void endRound() {
        inRound = false;
        dealerHoleCardHidden = false;

        hitButton.setEnabled(false);
        standButton.setEnabled(false);
        doubleButton.setEnabled(false);
        splitButton.setEnabled(false);

        if (player.getInsuranceBet() > 0 && dealer.getHand().isBlackjack()) {
            player.receivePayout(player.getInsuranceBet() * 3);
        }

        boolean allowBj = player.getHandCount() == 1;
        StringBuilder resultMessage = new StringBuilder();

        for (int i = 0; i < player.getHandCount(); i++) {
            Hand hand = player.getHand(i);
            double bet = player.getBet(i);
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

            player.receivePayout(payout);
            history.add(new RoundResult(outcome, bet, payout));

            if (player.getHandCount() > 1) {
                resultMessage.append("Hand ").append(i + 1).append(": ");
            }
            resultMessage.append(outcome);
            if (payout > 0) {
                resultMessage.append(" (+$").append(payout).append(")");
            }
            resultMessage.append(". ");
        }

        statusLabel.setText(resultMessage.toString().trim());

        if (player.isBroke()) {
            dealButton.setEnabled(false);
            betField.setEnabled(false);
            statusLabel.setText(statusLabel.getText() + " You are broke! Game over.");
        }
        else {
            dealButton.setEnabled(true);
            betField.setEnabled(true);
        }

        updateUI();
    }

    private void updateUI() {
        bankrollLabel.setText("Bankroll: $" + player.getBankroll());
        if (inRound) {
            currentBetLabel.setText("Current Bet: $" + player.getCurrentBet());
        }
        else {
            currentBetLabel.setText("Current Bet: $0.00");
        }

        dealerCardsPanel.removeAll();
        LinkedChain<Card> dCards = dealer.getHand().getCards();
        Card[] dealerArray = dCards.toArray(new Card[dCards.size()]);
        for (int i = dealerArray.length - 1; i >= 0; i--) {
            boolean hideThisCard = dealerHoleCardHidden
                && dCards.size() >= 2
                && dealerArray[i].equals(dCards.get(1));
            dealerCardsPanel.add(createCardPanel(dealerArray[i], hideThisCard));
        }

        if (dealer.getHand().size() == 0) {
            dealerScoreLabel.setText("Score: 0");
        }
        else if (dealerHoleCardHidden && dCards.size() >= 1) {
            dealerScoreLabel.setText("Showing: " + dCards.get(0).getValue());
        }
        else {
            dealerScoreLabel.setText("Score: " + dealer.getScore()
                + (dealer.isBust() ? " (Bust!)" : ""));
        }

        playerCardsPanel.removeAll();
        LinkedChain<Card> pCards = player.getHand(0).getCards();
        Card[] playerArray = pCards.toArray(new Card[pCards.size()]);
        for (int i = playerArray.length - 1; i >= 0; i--) {
            playerCardsPanel.add(createCardPanel(playerArray[i], false));
        }

        if (player.getHandCount() > 1) {
            playerScoreLabel.setText("Hand 1 Score: " + player.getHand(0).getScore()
                + (player.getHand() == player.getHand(0) && inRound ? " (Active)" : "")
                + (player.getHand(0).isBust() ? " (Bust!)" : ""));

            splitCardsPanel.removeAll();
            LinkedChain<Card> sCards = player.getHand(1).getCards();
            Card[] splitArray = sCards.toArray(new Card[sCards.size()]);
            for (int i = splitArray.length - 1; i >= 0; i--) {
                splitCardsPanel.add(createCardPanel(splitArray[i], false));
            }
            splitScoreLabel.setText("Hand 2 Score: " + player.getHand(1).getScore()
                + (player.getHand() == player.getHand(1) && inRound ? " (Active)" : "")
                + (player.getHand(1).isBust() ? " (Bust!)" : ""));
            splitCardsPanel.setVisible(true);
            splitScoreLabel.setVisible(true);
        }
        else {
            playerScoreLabel.setText("Score: " + player.getScore()
                + (player.isBust() ? " (Bust!)" : ""));
            splitCardsPanel.setVisible(false);
            splitScoreLabel.setVisible(false);
        }

        dealerCardsPanel.revalidate();
        dealerCardsPanel.repaint();
        playerCardsPanel.revalidate();
        playerCardsPanel.repaint();
        splitCardsPanel.revalidate();
        splitCardsPanel.repaint();
    }

    private JPanel createCardPanel(Card card, boolean hidden) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(80, 115));

        if (hidden) {
            panel.setBackground(new Color(40, 70, 140));
            panel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
            JLabel label = new JLabel("?", SwingConstants.CENTER);
            label.setFont(new Font("SansSerif", Font.BOLD, 28));
            label.setForeground(Color.WHITE);
            panel.add(label, BorderLayout.CENTER);
            return panel;
        }

        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        String suit = card.getSuit();
        String rank = card.getRank();
        String symbol = "";
        Color color = Color.BLACK;

        if ("Hearts".equals(suit)) {
            symbol = "\u2665";
            color = Color.RED;
        }
        else if ("Diamonds".equals(suit)) {
            symbol = "\u2666";
            color = Color.RED;
        }
        else if ("Clubs".equals(suit)) {
            symbol = "\u2663";
            color = Color.BLACK;
        }
        else if ("Spades".equals(suit)) {
            symbol = "\u2660";
            color = Color.BLACK;
        }

        JLabel top = new JLabel(" " + rank);
        top.setFont(new Font("SansSerif", Font.BOLD, 14));
        top.setForeground(color);

        JLabel center = new JLabel(symbol, SwingConstants.CENTER);
        center.setFont(new Font("SansSerif", Font.PLAIN, 32));
        center.setForeground(color);

        JLabel bottom = new JLabel(rank + " ", SwingConstants.RIGHT);
        bottom.setFont(new Font("SansSerif", Font.BOLD, 14));
        bottom.setForeground(color);

        panel.add(top, BorderLayout.NORTH);
        panel.add(center, BorderLayout.CENTER);
        panel.add(bottom, BorderLayout.SOUTH);

        return panel;
    }

    private void discardHand(Hand hand) {
        LinkedChain<Card> cards = hand.getCards();
        while (!cards.isEmpty()) {
            deck.discard(cards.removeFirst());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BlackjackGUI gui = new BlackjackGUI();
            gui.setVisible(true);
        });
    }
}
