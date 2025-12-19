package org.dicegame.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Random;

public class De {
    private int valeurActuel;
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    private final Randomizer randomizer;

    public De(Randomizer randomizer) {
        this.randomizer = randomizer;
        this.valeurActuel = 1;
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
}
