package org.dicegame.model;

public class Saisie {
    private String nomSaisie;
    private int scoreSaisie;

    public Saisie (String nomJoueur, int score) {
        this.nomSaisie = nomJoueur;
        this.scoreSaisie = score;

    }

    public String getNomSaisie() {
        return nomSaisie;
    }
    public int getScoreSaisie() {
        return scoreSaisie;
    }

    public void setNomSaisie(String nomSaisie) {
        this.nomSaisie = nomSaisie;
    }
    public void setScoreSaisie(int scoreSaisie) {
        this.scoreSaisie = scoreSaisie;
    }
}
