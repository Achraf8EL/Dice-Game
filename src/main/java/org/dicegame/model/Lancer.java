package org.dicegame.model;

public class Lancer {
    private final int tour;
    private final int de1;
    private final int de2;
    private final int somme;
    private final int pointsGagnes;

    public Lancer(int tour, int de1, int de2, int pointsGagnes) {
        this.tour = tour;
        this.de1 = de1;
        this.de2 = de2;
        this.somme = de1 + de2;
        this.pointsGagnes = pointsGagnes;
    }

    public int getTour() { return tour; }
    public int getDe1() { return de1; }
    public int getDe2() { return de2; }
    public int getSomme() { return somme; }
    public int getPointsGagnes() { return pointsGagnes; }
}
