# Dice-Game
Projet Dice game M2 2025

## Configuration de la base de données

Le projet utilise PostgreSQL. Les paramètres peuvent être passés via des variables d'environnement ou des propriétés système.

- `DB_URL` (par défaut `jdbc:postgresql://localhost:5432/dicegame`)
- `DB_USER` (par défaut `postgres`)
- `DB_PASSWORD` (par défaut vide)
- `DB_SCHEMA` (par défaut `dicegame`)
- `ENABLE_FLYWAY` (true|false) : si `true`, Flyway exécutera les migrations au démarrage

Exemple pour lancer l'application avec Flyway activé :

```bash
export DB_URL=jdbc:postgresql://localhost:5432/dicegame
export DB_USER=postgres
export DB_PASSWORD=postgres
export ENABLE_FLYWAY=true
mvn -DskipTests package
```

Les scripts de migration se trouvent dans `src/main/resources/db/migration`.
