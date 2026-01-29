package com.kartersanamo;

import com.kartersanamo.enums.CardSuite;
import com.kartersanamo.enums.CardValue;

public class Card {
    public CardSuite suite;
    public CardValue value;

    public Card(CardSuite suite, CardValue value) {
        this.suite = suite;
        this.value = value;
    }

    public String toString() {
        return value.toString() + " of " + suite.toString();
    }
}
