package org.dicegame.controller;

import org.dicegame.model.De;
import org.dicegame.model.*;

public class Partie {
private int nbTours = 10;
private int pointsSi7 = 10;

private final De de1;
private final De de2;

private final Joueur joueur;
private final ScoreEleve scoreEleve;

public Partie(Joueur joueur, ScoreEleve scoreEleve,Randomizer randomizer) {
    this.joueur = joueur;
    this.scoreEleve = scoreEleve;
    this.de1 = new De(randomizer);
    this.de2 = new De(randomizer);
}

public int lancerDe1(){
    de1.Lancer();
    return de1.getValeurActuel();
}

    public int lancerDe2(){
        de2.Lancer();
        return de2.getValeurActuel();
    }

    public De getDe1() {
        return de1;
    }
    public De getDe2() {
    return de2;
    }

    public void setNbTours(int nbTours) {
        this.nbTours = nbTours;
    }
    public void setPointsSi7(int pointsSi7) {
    this.pointsSi7 = pointsSi7;
    }

    public int demarer() {
        int score = 0;

        for (int i = 0; i < nbTours; i++) {
            de1.Lancer();
            de2.Lancer();

            int somme = de1.getValeurActuel() + de2.getValeurActuel();
            if (somme == 7) {
                score += pointsSi7;
            }
        }

        joueur.Majscore(score);

        String nomAffiche = joueur.getNomJoueur() + " " + joueur.getPrenomJoueur();
        scoreEleve.add(new Saisie(nomAffiche, score));

        return score;
    }

    public Joueur getJoueur() {
    return joueur;
    }
    public ScoreEleve getScoreEleve() {
    return scoreEleve;
    }


}
