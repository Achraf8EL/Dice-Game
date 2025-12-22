package org.dicegame.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class De {
    private int valeurActuel;
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    private final Randomizer randomizer;

    private long idDe;
    private Long partieId;
    private Integer tour;
    private int de1;
    private int de2;
    private int somme;
    private int pointsGagnes;
    private java.time.OffsetDateTime createdAt;

    public De(Randomizer randomizer) {
        this.randomizer = randomizer;
        this.valeurActuel = 1;
    }

    public De() {
        this(new Randomizer());
    }
    public int getValeurActuel() {
        return valeurActuel;
    }

    public void setValeurActuel(int valeurActuel) {
        int old = this.valeurActuel;
        this.valeurActuel = valeurActuel;
        support.firePropertyChange("valeurActuel", old, this.valeurActuel);

    }
    public void Lancer() {
        int v = randomizer.getValue();
        setValeurActuel(v);
    }
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }
    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }

    public long getIdDe() { return idDe; }
    public void setIdDe(long idDe) { this.idDe = idDe; }

    public Long getPartieId() { return partieId; }
    public void setPartieId(Long partieId) { this.partieId = partieId; }

    public Integer getTour() { return tour; }
    public void setTour(Integer tour) { this.tour = tour; }

    public int getDe1() { return de1; }
    public void setDe1(int de1) { this.de1 = de1; }

    public int getDe2() { return de2; }
    public void setDe2(int de2) { this.de2 = de2; }

    public int getSomme() { return somme; }
    public void setSomme(int somme) { this.somme = somme; }

    public int getPointsGagnes() { return pointsGagnes; }
    public void setPointsGagnes(int pointsGagnes) { this.pointsGagnes = pointsGagnes; }

    public java.time.OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(java.time.OffsetDateTime createdAt) { this.createdAt = createdAt; }
}
