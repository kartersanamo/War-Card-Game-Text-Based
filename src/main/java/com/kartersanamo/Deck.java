package com.kartersanamo;

import com.kartersanamo.enums.CardSuite;
import com.kartersanamo.enums.CardValue;

import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    private ArrayList<Card> cards;

    public Deck() {
        cards = new ArrayList<>();
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public void populateDeck() {
        for (CardSuite suite: CardSuite.values()) {
            for (CardValue value: CardValue.values()) {
                cards.add(new Card(suite, value));
            }
        }
    }

    public void dealCards(Player player1, Player player2) {
        shuffle();
        Player player = player1;
        for (Card card: cards) {
            player = (player == player1) ? player2 : player1;
            player.addCard(card);
        }
    }

    public void removeCard(Card card) {
        cards.remove(card);
    }
}
