# War — Text-Based Card Game

A console implementation of the classic **War** card game in Java. Play against the computer or watch two computer players simulate a full game.

## Requirements

- **Java 25** (see `pom.xml`)
- **Maven** (for building and running)

## How to Run

From the project root:

```bash
mvn compile exec:java -Dexec.mainClass="com.kartersanamo.Main"
```

Or run the `Main` class from your IDE (IntelliJ, Eclipse, etc.).

## Game Modes

On startup you can choose:

| Option | Description |
|--------|-------------|
| **p** | Play Against Computer — You vs the computer |
| **s** | Simulate — Two computer players play automatically |
| **q** | Quit |

## How to Play

- Each player is dealt half of a shuffled 52-card deck.
- On your turn, press **P** to play your top card (or **Q** to quit).
- Higher card wins the round and takes all cards played (yours, opponent’s, and any face-down “war” cards).
- **War**: If both players play the same rank, it’s War:
  - Each player places 3 cards face down, then plays one card face up.
  - The higher of those face-up cards wins all cards in the middle (including the face-down ones).
  - If a player doesn’t have enough cards for War (4+ cards), the other player wins the game.
- The game ends when one player has no cards left. The other player wins.

## Project Structure

```
src/main/java/com/kartersanamo/
├── Main.java          # Entry point, menu (Play / Simulate / Quit)
├── Game.java          # Game loop, turns, War logic, console UI
├── Player.java        # Player state and hand
├── Deck.java          # Deck creation, shuffle, deal
├── Card.java          # Single card (suit + value)
├── PlayedCard.java    # Card + player (for middle pile)
└── enums/
    ├── CardSuite.java
    ├── CardValue.java
    ├── GameState.java
    ├── PlayerType.java  # HUMAN / COMPUTER
    └── Turn.java        # PLAYER / COMPUTER
```

## License

Use and modify as you like for learning or personal projects.
