package org.dicegame.ui;

public interface IHM {
    void jouer();
    void afficherTableauScores();
    void afficherRegles();
    void configurerParametres(int nbTours, int pointsSi7);
}
