# cs2114-project2-group87

Blackjack

## Team

- Sami Saleh
- Pratham Singh
- Angelina Gust
- Tien Vu

Run GameController.main to play. Tests are the *Test.java files in src.

## Reflection

What changed from our spec:

- Player: the spec only listed fields. We added placeBet, canDoubleDown, doubleDown, receivePayout, and reset. Player throws IllegalArgumentException on a bad bet as a backup check.
- GameController: we added chooseDecks, dealInitialCards, dealerPlay, finishRound, playRound, play, and getSummary to split up the game loop.
- Deck got a discard method so cards from finished hands go to the discard pile.
- RoundResult uses a String for the outcome instead of an Outcome enum, and we added "Blackjack" as an outcome.
- Card got isAce(). Hand got size, isBust, isBlackjack, clear, and getCards.

What changed in LinkedChain:

- Added removeFirst() so Deck can deal off the top in O(1). It just calls remove().
- Changed remove(T) to return the removed item (or null) instead of a boolean, to match our spec.
- Everything else is unchanged. Hand uses toArray to score, GameController uses toArray for the summary and get(0) to show the dealer's card.

Our unknowns:

- Ace recalculation: figured out. Hand.getScore counts every Ace as 11, then takes off 10 per Ace while the total is over 21.
- Power-ups interrupting the game loop: still open, not sure if we will do this.

What doesn't work yet:

- Splitting and insurance are not started.

## Next Steps

To finish the MVP:

1. Hide the dealer's second card until the player's turn is over.
2. Play through a lot of rounds by hand to look for bugs.

Stretch goals:

- Doing: splitting pairs, insurance bets.
- Dropping/Not sure: Actual GUI, power-ups.

New tests needed:

- Splitting: split a pair, try to split a non-pair, try to split without enough money.
- Insurance: take insurance when the dealer shows an Ace, try it when they don't, try it without enough money, check the 2 to 1 payout.

Who does what:


| Task             | Owner |
| ---------------- | ----- |
| Deck tests       | Tien  |
| Hide dealer card |Pratham|
| Splitting        | Angela|
| Insurance        | Sami  |
| New tests        | Sami  |




## Attribution

| File                    | Who                     | How                                                          |
| ----------------------- | ----------------------- | ------------------------------------------------------------ |
| LinkedChain.java        | Sami Saleh              | Given, modified by Sami: added removeFirst and changed remove(T) |
| LinkedChainTest.java    | Sami Saleh              | wrote                                                        |
| Card.java               | Sami Saleh              | wrote                                                        |
| CardTest.java           | Sami Saleh              | wrote                                                        |
| Player.java             | Pratham Singh           | wrote                                                        |
| PlayerTest.java         | Pratham Singh           | wrote                                                        |
| GameController.java     | Pratham Singh           | wrote, GenAI-assisted                                        |
| GameControllerTest.java | Pratham Singh           | wrote, GenAI-assisted                                        |
| Hand.java               | Angelina Gust           | wrote, GenAI-assisted                                        |
| HandTest.java           | Angelina Gust           | wrote, GenAI-assisted                                        |
| Dealer.java             | Angelina Gust           | wrote, GenAI-assisted                                        |
| DealerTest.java         | Angelina Gust           | wrote, GenAI-assisted                                        |
| RoundResult.java        | Tien Vu                 | wrote, GenAI-assisted                                        |
| RoundResultTest.java    | Tien Vu                 | wrote, GenAI-assisted                                        |
| Deck.java               | Tien Vu                 | wrote                                                        |
| DeckTest.java           | Tien Vu                 | wrote                                                        |

GenAI: Sami used Claude to help plan out writing classes and their tests, making sure to verify with spec before planning. We reviewed the code and ran the tests ourselves. Pratham used gemini to verify the code based on the spec, to create java docs, and planning out writing the test cases.
