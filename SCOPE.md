Project Name : Blackjack
What are you building?
A console blackjack game where a user plays against a computer dealer. You start with a certain amount of money, place a bet, and get two cards. You then choose to take more cards (referred to as “hit”)  or stop (referred to as “stand”), trying to get closer to 21 than the dealer without going over. The dealer has fixed rules so it never makes choices. After each round, your money updates and the result is recorded. The game ends when you quit or run out of money, then it shows a summary of how you do. 

What is your MVP?
Choose how many decks to play with (1-6). All the cards are recombined into one draw pile and shuffled. When the pile runs low, the used cards are shuffled back in.
User placing a bet each round. Money updates every round
Options for User:
Hit: Taking another card
Stand: not taking anymore cards
Double down: User doubles bet and takes exactly one more card, then stands. Only allowed on the first two cards
The computer dealer keeps taking cards until the hand is 17 or more, then stops.
Correct scoring (face cards count as 10, number cards count as their number, and ace counts as 1 or 11, whichever is better for the hand without going over 21)
Payout method: An ace with a card with value 10 on the first two cards is considered a “blackjack” and pays 3 to 2, so a $100 bet wins $150. If the user and dealer tie, the user gets their money only back.
Round history showing win/loss/tie for each hand, plus a summary when the user stops playing (rounds played, wins/losses/ties, final money).
Input validation so bad input shows an error message and asks the user again.

Stretch Goals:
Splitting Pairs: If the user is dealt two cards of the same value (e.g., two 8s), allow them to double their initial bet and split the cards into two distinct hands played sequentially.
Insurance Bets: If the dealer's visible card is an Ace, allow the user to place a side bet (half their original bet) that pays 2 to 1 if the dealer's hidden card is a 10-value card.
Visual Interface: Transition the console output to a simple GUI that displays card images and interactive buttons for Hit, Stand, and Double Down.
a new "Power-Up" inventory system, allowing players to earn, purchase, and deploy special abilities (e.g., "Peek," "Free Double Down") that dynamically interrupt and alter the standard flow of the game

What will your LinkedChain hold?
Card
Used for the draw pile, the user’s hand, and the dealer’s hand.
Duplicates: With 6 decks, the draw pile has six identical versions of each card, so the collection has to allow repeats.
Unordered: A hand’s total is the same no matter what order the cards arrived in. The draw pile is shuffled by picking a random card, so no stored order is needed
RoundResult
Contains Outcome, bet amount, payout. Used to store the history of games played.
Duplicates: Identical results are very common, like multiple losses or multiple blackjacks.
Unordered: The history is only counted and totaled, never looked up by position.

What bad input must it survive?
Invalid Bets: Entering text, symbols, negative numbers, zero, or amounts greater than the user's current bankroll.
Entering unrecognized commands when prompted to Hit or Stand, or attempting to Double Down on the third card (which is against the rules).
The background logic must survive the draw pile running out of cards mid-hand by seamlessly shuffling the used cards back in before the next card is dealt.
What don’t you know how to do yet?
design the core game loop so that a power-up can safely interrupt the strict rules
We need an algorithm that automatically recalculates an Ace's value from 11 down to 1 if a subsequent "Hit" causes the player's total to exceed 21.

