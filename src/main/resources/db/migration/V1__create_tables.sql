-- Script complet de création de la base de données pour Dice Game
-- Schéma: dicegame

CREATE SCHEMA IF NOT EXISTS dicegame;

-- Table joueur
CREATE TABLE IF NOT EXISTS dicegame.joueur (
    "idJoueur" SERIAL PRIMARY KEY,
    "nomJoueur" VARCHAR(100) NOT NULL,
    "prenomJoueur" VARCHAR(100) NOT NULL,
    "scoreJoueur" INTEGER DEFAULT 0
);

-- Table partie
CREATE TABLE IF NOT EXISTS dicegame.partie (
    "idPartie" SERIAL PRIMARY KEY,
    "nbTours" INTEGER NOT NULL,
    "pointsSi7" INTEGER NOT NULL,
    "joueur_id" INTEGER REFERENCES dicegame.joueur("idJoueur") ON DELETE CASCADE,
    "scoreFinal" INTEGER DEFAULT 0
);

-- Table de (pour les lancers de dés)
CREATE TABLE IF NOT EXISTS dicegame.de (
    "idDe" SERIAL PRIMARY KEY,
    "partie_id" INTEGER REFERENCES dicegame.partie("idPartie") ON DELETE SET NULL,
    "tour" INTEGER,
    "de1" INTEGER NOT NULL,
    "de2" INTEGER NOT NULL,
    "somme" INTEGER NOT NULL,
    "pointsGagnes" INTEGER NOT NULL,
    "created_at" TIMESTAMP WITH TIME ZONE DEFAULT now()
);

-- Table saisie (pour les saisies utilisateur)
CREATE TABLE IF NOT EXISTS dicegame.saisie (
    "idSaisie" SERIAL PRIMARY KEY,
    "joueur_id" INTEGER REFERENCES dicegame.joueur("idJoueur") ON DELETE CASCADE,
    "partie_id" INTEGER REFERENCES dicegame.partie("idPartie") ON DELETE CASCADE,
    "tour" INTEGER NOT NULL,
    "valeur" INTEGER NOT NULL
);

-- Index pour optimiser les requêtes
CREATE INDEX IF NOT EXISTS idx_joueur_nom_prenom ON dicegame.joueur ("nomJoueur", "prenomJoueur");
CREATE INDEX IF NOT EXISTS idx_partie_joueur_id ON dicegame.partie ("joueur_id");
CREATE INDEX IF NOT EXISTS idx_de_partie_id ON dicegame.de ("partie_id");
CREATE INDEX IF NOT EXISTS idx_de_created_at ON dicegame.de ("created_at" DESC);
CREATE INDEX IF NOT EXISTS idx_saisie_joueur_partie ON dicegame.saisie ("joueur_id", "partie_id");

