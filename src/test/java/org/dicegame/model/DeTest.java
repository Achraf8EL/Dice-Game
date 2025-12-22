package org.dicegame.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DeTest {

    @Test
    public void testConstructorWithRandomizer() {
        Randomizer randomizer = new Randomizer();
        De de = new De(randomizer);
        assertEquals(1, de.getValeurActuel());
    }

    @Test
    public void testDefaultConstructor() {
        De de = new De();
        assertEquals(1, de.getValeurActuel());
    }

    @Test
    public void testSetValeurActuel() {
        De de = new De();
        de.setValeurActuel(5);
        assertEquals(5, de.getValeurActuel());
    }

    @Test
    public void testLancerChangesValue() {
        De de = new De();
        int initial = de.getValeurActuel();
        de.Lancer();
        int after = de.getValeurActuel();
        assertTrue(after >= 1 && after <= 6);
        // May be same as initial, but that's ok
    }
}