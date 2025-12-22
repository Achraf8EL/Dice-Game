package org.dicegame.model;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class RandomizerTest {

    @Test
    public void testGetValueReturnsBetween1And6() {
        Randomizer randomizer = new Randomizer();
        for (int i = 0; i < 100; i++) {
            int value = randomizer.getValue();
            assertTrue(value >= 1 && value <= 6, "Value should be between 1 and 6, got: " + value);
        }
    }

    @Test
    public void testGetValueIsRandom() {
        Randomizer randomizer = new Randomizer();
        int first = randomizer.getValue();
        int second = randomizer.getValue();
        // Not a perfect test, but checks if it's not always the same
        assertNotEquals(0, first - second); // Assuming randomness, but could fail rarely
    }
}