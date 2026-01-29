package com.kartersanamo.enums;

import java.util.List;
import java.util.Random;

public enum Turn {
    PLAYER, COMPUTER;

    private static final List<Turn> VALUES = List.of(values());
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    public static Turn randomTurn() {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }

    public static Turn nextTurn(Turn turn) {
        return VALUES.get((turn.ordinal() + 1) % SIZE);
    }
}
