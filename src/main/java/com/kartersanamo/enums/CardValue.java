package com.kartersanamo.enums;

import java.util.HashMap;

public enum CardValue {
    ACE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING;

    public static int enumToValue(CardValue value) {
        HashMap<CardValue, Integer> map = new HashMap<>();
        map.put(ACE, 1);
        map.put(JACK, 10);
        map.put(QUEEN, 10);
        map.put(KING, 10);
        map.put(TEN, 10);
        map.put(NINE, 9);
        map.put(EIGHT, 8);
        map.put(SEVEN, 7);
        map.put(SIX, 6);
        map.put(FIVE, 5);
        map.put(FOUR, 4);
        map.put(THREE, 3);
        map.put(TWO, 2);
        return map.get(value);
    }
}
