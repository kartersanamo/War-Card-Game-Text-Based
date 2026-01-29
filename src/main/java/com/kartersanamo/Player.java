package com.kartersanamo;

import com.kartersanamo.enums.PlayerType;

public class Player {
    private PlayerType playerType;
    private Deck hand;

    public Player(PlayerType playerType) {
        this.playerType = playerType;
        this.hand = new Deck();
    }

    public Deck getHand() {
        return hand;
    }

    public int getHandSize() {
        int count = 0;
        for (Card card: hand.getCards()) {
            if (card != null) {
                count++;
            }
        }
        return count;
    }

    public void addCard(Card card) {
        hand.getCards().add(card);
    }

    public PlayerType getPlayerType() {
        return playerType;
    }

    public void placeCard(Game game, Player currentPlayer, Card card, boolean war) {
        hand.removeCard(card);
        game.placeCard(card, currentPlayer, war);
    }
}
