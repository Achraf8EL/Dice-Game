package org.dicegame.persistence;

import org.dicegame.model.Saisie;
import org.dicegame.model.ScoreEleve;

import javax.sql.DataSource;

public class PersistentScoreEleve extends ScoreEleve {
    private final SaisieDao saisieDao;
    private final JoueurDao joueurDao;

    public PersistentScoreEleve(DataSource ds) {
        this.saisieDao = new SaisieDao(ds);
        this.joueurDao = new JoueurDao(ds);
        // charger les saisies existantes depuis la BDD
        try {
            java.util.List<org.dicegame.model.Saisie> existing = this.saisieDao.listAll();
            if (existing != null) {
                for (org.dicegame.model.Saisie s : existing) {
                    super.add(s);
                }
            }
        } catch (Exception ex) {
            // ne pas empêcher l'application de démarrer si la lecture échoue
            ex.printStackTrace();
        }
    }

    /**
     * Reload all saisies from the database into memory (clears existing).
     */
    public void reload() {
        try {
            super.clear();
            java.util.List<org.dicegame.model.Saisie> existing = this.saisieDao.listAll();
            if (existing != null) {
                for (org.dicegame.model.Saisie s : existing) {
                    super.add(s);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void add(Saisie s) {
        if (s == null) return;
        // tenter de persister (nomAffiche est le nom complet)
        String nomAffiche = s.getNomSaisie();
        Integer score = s.getScoreSaisie();
        Long joueurId = null;
        // si nomAffiche contient un espace, on essaie de déduire nom/prénom
        if (nomAffiche != null) {
            String[] parts = nomAffiche.trim().split(" ", 2);
            if (parts.length == 2) {
                try {
                    joueurId = joueurDao.insertIfNotExists(parts[0], parts[1]);
                } catch (Exception ex) {
                    // ignore and proceed with null joueurId
                    joueurId = null;
                }
            }
        }
        try {
            saisieDao.insert(nomAffiche, score, joueurId);
        } catch (Exception ex) {
            // log to console and continue to keep UI responsive
            ex.printStackTrace();
        }
        super.add(s);
    }
}

