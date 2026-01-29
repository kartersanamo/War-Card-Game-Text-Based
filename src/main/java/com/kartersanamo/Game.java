package com.kartersanamo;

import com.kartersanamo.enums.CardValue;
import com.kartersanamo.enums.GameState;
import com.kartersanamo.enums.PlayerType;
import com.kartersanamo.enums.Turn;

import java.util.ArrayList;
import java.util.List;

import static com.kartersanamo.Main.scanner;

public class Game {
    private List<Player> players;
    private Deck deck;
    private static GameState gameState;
    private Turn turn;
    private List<PlayedCard> middle;
    private List<PlayedCard> faceDown;
    private int round;
    private int warCount;
    private boolean simulate = false;
    private Player winner;
    private int cardsPlayed = 0;

    public Game() {
        players = List.of(
                new Player(PlayerType.HUMAN),
                new Player(PlayerType.COMPUTER)
        );
        deck = new Deck();
        deck.populateDeck();
        deck.dealCards(players.get(0), players.get(1));
        turn = Turn.randomTurn();
        middle = new ArrayList<PlayedCard>();
        faceDown = new ArrayList<PlayedCard>();
        round = 1;
        warCount = 0;
    }

    public void startGame(boolean simulate) throws InterruptedException {
        this.simulate = simulate;
        gameState = GameState.IN_PROGRESS;
        while (gameState == GameState.IN_PROGRESS) {
            clearScreen();
            sendGame();
            nextTurn();

            // Check for game over
            if (players.get(0).getHandSize() == 0) {
                endGame(players.get(1));
            } else if (players.get(1).getHandSize() == 0) {
                endGame(players.getFirst());
            }
        }
    }

    public void endGame(Player winner) {
        this.winner = winner;
        String playerIcon = turn == Turn.COMPUTER ? "🤖" : "👤";
        System.out.println("\n" + playerIcon + " " + winner.getPlayerType() + " wins the game!");
        gameState = GameState.FINISHED;
        System.out.println("#\uFE0F⃣ Rounds: " + round);
        System.out.println("\uD83C\uDCCF Cards Played: " + cardsPlayed);

        System.exit(0);
    }

    public void nextTurn() throws InterruptedException {
        turn = Turn.nextTurn(turn);
        processInput();

        if (bothPlayersHavePlayedFaceUp()) {
            clearScreen();
            sendGame();
            System.out.println("⚔️  Comparing Cards...\n");
            if (!simulate) { Thread.sleep(1500); }
            comparePlayedCards();
            if (!simulate) { Thread.sleep(2000); }
        }
    }

    private boolean bothPlayersHavePlayedFaceUp() {
        int needed = warCount + 1;

        int p1Count = 0;
        int p2Count = 0;

        for (PlayedCard pc : middle) {
            if (pc.player == players.get(0)) p1Count++;
            else if (pc.player == players.get(1)) p2Count++;
        }

        return p1Count >= needed && p2Count >= needed;
    }

    private void comparePlayedCards() throws InterruptedException {
        // Get the last two cards played (the face-up cards)
        PlayedCard player1Card = null;
        PlayedCard player2Card = null;

        for (int i = middle.size() - 1; i >= 0; i--) {
            PlayedCard pc = middle.get(i);
            if (pc.player == players.get(0) && player1Card == null) {
                player1Card = pc;
            } else if (pc.player == players.get(1) && player2Card == null) {
                player2Card = pc;
            }

            if (player1Card != null && player2Card != null) {
                break;
            }
        }

        int value1 = CardValue.enumToValue(player1Card.card.value);
        int value2 = CardValue.enumToValue(player2Card.card.value);

        if (value1 == value2) {
            // WAR!
            warCount++;
            System.out.println("💥 WAR! Both players played " + value1 + "!");
            System.out.println("   Each player places 3 cards face down...\n");
            if (!simulate) { Thread.sleep(2000); }

            // Check if both players have enough cards for war
            if (players.get(0).getHandSize() < 4 || players.get(1).getHandSize() < 4) {
                // Not enough cards for a war-player with more cards wins
                Player winner = players.get(0).getHandSize() > players.get(1).getHandSize()
                        ? players.get(0) : players.get(1);
                System.out.println("⚠️  Not enough cards for WAR!");
                System.out.println("🎉 " + winner.getPlayerType() + " WINS by default!");

                endGame(winner);
            }

            // Continue the war - place 3 face down cards
            for (Player player : players) {
                for (int i = 0; i < 3; i++) {
                    if (player.getHandSize() > 0) {
                        Card card = player.getHand().getCards().getFirst();
                        player.placeCard(this, player, card, true);
                    }
                }
            }

            clearScreen();
            sendGame();
            System.out.println("🎴 3 cards placed face down by each player");
            System.out.println("   Now playing the deciding card...\n");
            if (!simulate) { Thread.sleep(2000); }

            // Don't clear middle or increment round - continue to next turn for face-up card
            return;
        }

        // Normal win (or war resolution)
        Player winner = value1 > value2 ? players.get(0) : players.get(1);

        if (warCount > 0) {
            System.out.println("🎉 " + winner.getPlayerType() + " WINS THE WAR!");
            System.out.println("   +" + (middle.size() + faceDown.size()) + " cards to " + winner.getPlayerType() + "\n");
        } else {
            System.out.println("🎉 " + winner.getPlayerType() + " WINS THIS ROUND!");
            System.out.println("   +" + (middle.size() + faceDown.size()) + " cards to " + winner.getPlayerType() + "\n");
        }

        for (PlayedCard played : middle) {
            winner.addCard(played.card);
        }

        middle.clear();
        faceDown.clear();
        warCount = 0;
        round++;
    }

    private Player getCurrentPlayer() {
        return turn == Turn.PLAYER ? players.get(0) : players.get(1);
    }

    private void processInput() throws InterruptedException {
        Player currentPlayer = getCurrentPlayer();

        if (turn == Turn.COMPUTER) {
            System.out.println("🤖 Computer's turn...");
            if (!simulate) { Thread.sleep(1500); }
        } else {
            System.out.println("👤 Your turn!");
            System.out.print("   [P] Play card  [Q] Quit\n   → ");

            String input = "p";
            if (!simulate) { input = scanner.nextLine().toLowerCase(); }

            if (input.equals("q")) {
                System.out.println("\n👋 Thanks for playing!");
                System.exit(0);
            } else if (!input.equals("p")) {
                return;
            }
        }

        if (currentPlayer.getHandSize() == 0) {
            System.out.println("⚠️  " + currentPlayer.getPlayerType() + " has no cards left!");
            return;
        }

        Card cardToPlay = currentPlayer.getHand().getCards().getFirst();
        String playerIcon = turn == Turn.COMPUTER ? "🤖" : "👤";
        System.out.println("\n" + playerIcon + " " + currentPlayer.getPlayerType() + " plays: " + formatCard(cardToPlay));
        if (!simulate) { Thread.sleep(800); }
        currentPlayer.placeCard(this, currentPlayer, cardToPlay, false);
    }

    private void clearScreen() {
        // Print multiple newlines for a "clear" effect
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private String formatCard(Card card) {
        String suite = getSuitSymbol(card.suite.toString());
        return "[ " + card.value + " " + suite + " ]";
    }

    private String getSuitSymbol(String suit) {
        return switch (suit) {
            case "HEARTS" -> "♥️";
            case "DIAMONDS" -> "♦️";
            case "CLUBS" -> "♣️";
            case "SPADES" -> "♠️";
            default -> suit;
        };
    }

    private String getLeaderIcon(Player p1, Player p2) {
        if (p1.getHandSize() > p2.getHandSize()) {
            return "👑 ";
        } else if (p1.getHandSize() == p2.getHandSize()) {
            return "⚖️  ";
        }
        return "   ";
    }

    public void sendGame() {
        Player player1 = players.getFirst();
        Player player2 = players.getLast();

        // Header
        String warIndicator = warCount > 0 ? " - WAR x" + warCount : "";
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║       WAR - Round " + String.format("%-3d", round) + warIndicator + String.format("%" + (13 - warIndicator.length()) + "s", "") + "║");
        System.out.println("╚══════════════════════════════════════════╝");
        System.out.println();

        // Player 1 Display
        String p1Icon = getLeaderIcon(player1, player2);
        String p1Cards = String.format("%-2d", player1.getHandSize());
        System.out.println("┌────────────────────────────────────────┐");
        System.out.println("│ " + p1Icon + String.format("%-35s", player1.getPlayerType()) + " │");
        System.out.println("│ 🃏  " + p1Cards + " cards remaining                │");
        System.out.println("└────────────────────────────────────────┘");
        System.out.println();

        // Middle Display
        String battleTitle = warCount > 0 ? "  💥 WAR! 💥  " : "    BATTLE     ";
        System.out.println("          ╔═══════════════╗");
        System.out.println("          ║" + battleTitle + "║");
        System.out.println("          ╚═══════════════╝");
        System.out.println();

        if (middle.isEmpty()) {
            System.out.println("              [ Empty ]");
        } else {
            for (PlayedCard playedCard : middle) {
                String playerIcon = playedCard.player.getPlayerType() == PlayerType.COMPUTER ? "🤖" : "👤";
                System.out.println("          " + playerIcon + formatCard(playedCard.card));
            }
            System.out.println();
            for (PlayedCard playedCard : faceDown) {
                String playerIcon = playedCard.player.getPlayerType() == PlayerType.COMPUTER ? "🤖" : "👤";
                System.out.println("          " + playerIcon + " [ 🂠 FACE DOWN ]");
            }

            if (!faceDown.isEmpty()) {
                System.out.println("\n          (" + faceDown.size() + " cards face down)");
            }
        }
        System.out.println();

        // Player 2 Display
        String p2Icon = getLeaderIcon(player2, player1);
        String p2Cards = String.format("%-2d", player2.getHandSize());
        System.out.println("┌────────────────────────────────────────┐");
        System.out.println("│ " + p2Icon + String.format("%-35s", player2.getPlayerType()) + " │");
        System.out.println("│ 🃏  " + p2Cards + " cards remaining                │");
        System.out.println("└────────────────────────────────────────┘");
        System.out.println();
        System.out.println("══════════════════════════════════════════");
        System.out.println();
    }

    // GETTERS
    public GameState getGameState() {
        return gameState;
    }

    public Turn getTurn() {
        return turn;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Deck getDeck() {
        return deck;
    }

    public void placeCard(Card card, Player player, boolean war) {
        if (war) {
            faceDown.add(new PlayedCard(card, player));
        } else {
            middle.add(new PlayedCard(card, player));
        }
        cardsPlayed++;
    }
}