# Dice Game - Jeu de Dés

Salut ! Bienvenue dans notre petit projet de jeu de dés, réalisé en Java avec une interface graphique en JavaFX. C'est un jeu simple et amusant où on lance des dés pour gagner des points. On va te guider à travers tout ce qu'il faut savoir : comment ça marche, comment l'installer, et même quelques exemples de données de la base pour que tu voies comment ça fonctionne.

## Qu'est-ce que c'est ?

Le Dice Game est une application Java qui simule un jeu de dés. Le joueur lance deux dés pendant 10 tours (par défaut), et s'il obtient un 7, il gagne des points (10 points par défaut). À la fin, on calcule le score total et on le sauvegarde. Tout est stocké dans une base de données PostgreSQL pour garder un historique des parties et des joueurs.

L'interface est faite avec JavaFX, c'est moderne et facile à utiliser. On peut jouer, voir les scores, consulter les règles, changer les paramètres, et même regarder l'historique des parties.

## Comment ça marche ?

### Le principe du jeu
- On crée un joueur avec son nom et prénom.
- On démarre une partie : 10 tours de lancers.
- À chaque tour :
  - On lance deux dés (chacun de 1 à 6).
  - Si la somme fait 7, on gagne des points (10 par défaut).
  - Sinon, rien.
- À la fin des 10 tours, le score total est calculé et sauvegardé.
- On peut voir les scores de tous les joueurs dans un tableau.

### Architecture de l'app
L'application est structurée en plusieurs packages :
- **model** : Les classes de base comme `De` (le dé), `Joueur`, , etc.
- **controller** : La logique du jeu avec `Partie`.
- **persistence** : Accès à la base de données avec des DAOs (Data Access Objects) pour `Joueur`, `Partie`, `De`, etc.
- **ui** : L'interface utilisateur avec JavaFX.
- **tools** : Outils comme `DbInspect` pour inspecter la DB.

On utilise Flyway pour les migrations de base de données, ce qui permet de garder le schéma à jour automatiquement.

### Technologies utilisées
- Java 11+ (ou plus récent)
- JavaFX pour l'UI
- PostgreSQL pour la base de données
- Maven pour la gestion des dépendances
- Docker pour la base de données (via docker-compose)
- Flyway pour les migrations DB

## Installation et lancement

### Prérequis
- Java 11 ou plus (on recommande Java 17 ou 21)
- Maven
- Docker (pour la base de données)

### Étapes
1. **Cloner le repo** :
   ```bash
   git clone https://github.com/Achraf8EL/Dice-Game.git
   cd dice-game
   ```

2. **Démarrer la base de données** :
   On utilise Docker Compose pour PostgreSQL.
   ```bash
   docker-compose up -d
   ```
   Ça lance PostgreSQL sur le port 5433 avec la DB `dicegame`.

3. **Configurer les variables d'environnement** (optionnel, sinon valeurs par défaut) :
   ```bash
   export DB_URL=jdbc:postgresql://localhost:5433/dicegame
   export DB_USER=postgres
   export DB_PASSWORD=postgres
   export ENABLE_FLYWAY=true
   ```

4. **Compiler et lancer l'app** :
   ```bash
   mvn clean compile
   mvn exec:java -Dexec.mainClass="org.dicegame.Main"
   ```
   Ou simplement :
   ```bash
   mvn -DskipTests package
   java -jar target/dice-game-1.0.jar
   ```

L'app se lance avec une fenêtre JavaFX. Clique sur "Jouer" pour commencer !

### Tests
On a des tests unitaires avec JUnit. Pour les lancer :
```bash
mvn test
```

## Configuration de la base de données

Comme dit plus haut, on utilise PostgreSQL. Les paramètres peuvent être passés via des variables d'environnement ou des propriétés système.

- `DB_URL` (par défaut `jdbc:postgresql://localhost:5433/dicegame`)
- `DB_USER` (par défaut `postgres`)
- `DB_PASSWORD` (par défaut vide)
- `DB_SCHEMA` (par défaut `dicegame`)
- `ENABLE_FLYWAY` (true|false) : si `true`, Flyway exécute les migrations au démarrage

Les scripts de migration sont dans `src/main/resources/db/migration`.

Exemple pour lancer avec Flyway activé :
```bash
export DB_URL=jdbc:postgresql://localhost:5433/dicegame
export DB_USER=postgres
export DB_PASSWORD=postgres
export ENABLE_FLYWAY=true
mvn -DskipTests package
```

## Schéma de la base de données

Voici le schéma des tables (grâce à Flyway) :

- **joueur** : idJoueur, nomJoueur, prenomJoueur, scoreJoueur
- **partie** : idPartie, nbTours, pointsSi7, joueur_id, scoreFinal
- **de** : idDe, partie_id, tour, de1, de2, somme, pointsGagnes, created_at
- **saisie** : idSaisie, joueur_id, partie_id, tour, valeur

On a des index pour optimiser les requêtes.

## Exemples de données

Pour te donner une idée, on a exécuté quelques requêtes SQL sur la base de données. Voici des exemples de données :

### Joueurs
```sql
SELECT * FROM dicegame.joueur ORDER BY idJoueur DESC LIMIT 5;
```
Résultat :
```
 idjoueur | nomjoueur | prenomjoueur | scorejoueur 
----------+-----------+--------------+-------------
       32 | Durand    | Pierre       |         120
       31 | Martin    | Marie        |         200
       30 | Dupont    | Jean         |         150
       29 | ooo       | ooooooo      |          20
       28 | aaaaa     | aerrrrrr     |          10
```

### Parties
```sql
SELECT idpartie, nbtours, pointssi7, joueur_id, scorefinal FROM dicegame.partie ORDER BY idpartie DESC LIMIT 5;
```
Résultat :
```
 idpartie | nbtours | pointssi7 | joueur_id | scorefinal 
----------+---------+-----------+-----------+------------
       24 |      10 |        10 |         3 |         30
       23 |      10 |        10 |         2 |         80
       22 |      10 |        10 |         1 |         50
       21 |      10 |        10 |        29 |         20
       20 |      10 |        10 |        28 |         10
```

### Lancers de dés
```sql
SELECT idde, partie_id, tour, de1, de2, somme, pointsgagnes FROM dicegame.de ORDER BY created_at DESC LIMIT 5;
```
Résultat :
```
 idde | partie_id | tour | de1 | de2 | somme | pointsgagnes 
------+-----------+------+-----+-----+-------+--------------
   25 |         3 |    1 |   3 |   4 |     7 |           10
   24 |         2 |    1 |   1 |   2 |     3 |            0
   23 |         1 |    1 |   3 |   4 |     7 |           10
   22 |         3 |    2 |   5 |   2 |     7 |           10
   21 |         2 |    2 |   6 |   3 |     9 |            0
```

Ces données montrent comment les scores sont calculés : par exemple, dans la partie 1, au tour 1, dés 3+4=7, donc 10 points gagnés.

## Fonctionnalités de l'interface

- **Jouer** : Lance une nouvelle partie.
- **Tableau des scores** : Voit les scores de tous les joueurs.
- **Règles** : Rappel des règles du jeu.
- **Paramètres** : Change le nombre de tours ou les points pour un 7.
- **Parties** : Historique des parties jouées.

## Contribution

Si tu veux contribuer, n'hésite pas ! Fork le repo, fais tes changements, et ouvre une PR. On apprécie les tests et la documentation.

## Licence

Ce projet est sous licence MIT. Amuse-toi bien !


## Licence

Achraf EL Messaoudi @Achraf8EL && Abdelkader benyacoub @abdelkaderben01 

MASTER MIAGE SID 2025