CREATE SCHEMA IF NOT EXISTS dicegame;

CREATE TABLE IF NOT EXISTS dicegame.joueur (
  id SERIAL PRIMARY KEY,
  nom VARCHAR(100) NOT NULL,
  prenom VARCHAR(100) NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);

CREATE TABLE IF NOT EXISTS dicegame.saisie (
  id SERIAL PRIMARY KEY,
  joueur_id INTEGER REFERENCES dicegame.joueur(id) ON DELETE SET NULL,
  nom_affiche VARCHAR(200) NOT NULL,
  score INTEGER NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);

CREATE INDEX IF NOT EXISTS idx_saisie_score_desc ON dicegame.saisie (score DESC);

