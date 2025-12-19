package org.dicegame.model;

public class Joueur {
    private String nomJoueur;
    private String prenomJoueur;
    private int scoreJoueur;

    public Joueur(String nomJoueur, String prenomJoueur) {
        this.nomJoueur = nomJoueur;
        this.prenomJoueur = prenomJoueur;
        this.scoreJoueur = 0;
    }
    public String getNomJoueur() {
        return nomJoueur;
    }
    public String getPrenomJoueur() {
        return prenomJoueur;
    }
    public int getScoreJoueur() {
        return scoreJoueur;
    }
    public void Majscore (int newScore) {
        this.scoreJoueur = newScore;
    }
}
