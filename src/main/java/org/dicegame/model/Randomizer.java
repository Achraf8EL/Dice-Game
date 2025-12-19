package org.dicegame.model;

import java.util.concurrent.ThreadLocalRandom;

public class Randomizer {

    public int getValue() {
        return ThreadLocalRandom.current().nextInt(1,7);
    }
}
