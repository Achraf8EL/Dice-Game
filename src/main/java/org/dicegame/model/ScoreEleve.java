package org.dicegame.model;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScoreEleve {
    private final List<Saisie> saisies = new ArrayList<>();

    public void add(Saisie s) {
        if ( s != null) saisies.add(s);
    }
    public List<Saisie> getSaisies() {
        return Collections.unmodifiableList(saisies);
    }
}
