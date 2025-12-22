package org.dicegame.persistence;

import javax.sql.DataSource;

import org.dicegame.model.Saisie;
import org.dicegame.model.ScoreEleve;

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
                System.out.println("[DB DEBUG] PersistentScoreEleve loaded " + existing.size() + " saisies from DB");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    
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
        String nomAffiche = s.getNomSaisie();
        Integer score = s.getScoreSaisie();
        Long joueurId = null;
        if (nomAffiche != null) {
            String[] parts = nomAffiche.trim().split(" ", 2);
            if (parts.length == 2) {
                try {
                    System.out.println("[DB DEBUG] trying to insert/find joueur: " + parts[0] + " " + parts[1]);
                    joueurId = joueurDao.insertIfNotExists(parts[0], parts[1]);
                    System.out.println("[DB DEBUG] joueurId returned: " + joueurId);
                } catch (Exception ex) {
                    joueurId = null;
                }
            }
        }
        try {
            System.out.println("[DB DEBUG] inserting saisie nomAffiche='" + nomAffiche + "' score=" + score + " joueurId=" + joueurId);
            saisieDao.insert(nomAffiche, score, joueurId);
            System.out.println("[DB DEBUG] insert succeeded");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        super.add(s);
    }
}

