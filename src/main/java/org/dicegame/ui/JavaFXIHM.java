package org.dicegame.ui;

import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.dicegame.controller.Partie;
import org.dicegame.model.*;
import javafx.application.Platform;
import javafx.stage.Modality;

import org.dicegame.persistence.ScoreEleveFactory;


public class JavaFXIHM implements IHM {
    private boolean finDePartie = false;


    private Button playBtn;
    private boolean tourEnCours = false;


    private final Stage stage;

    private final ScoreEleve scoreEleve = ScoreEleveFactory.create();
    private final Randomizer randomizer = new Randomizer();

    private int nbTours = 10;
    private int pointsSi7 = 10;

    private Label statusLabel;
    private DeVisualisation dieView1;
    private DeVisualisation dieView2;

    private Partie partieEnCours;
    private Joueur joueurEnCours;

    private int tourCourant = 1;
    private int scoreCourant = 0;

    private enum Phase { DE1, DE2, CALCUL }
    private Phase phase = Phase.DE1;

    public JavaFXIHM(Stage stage) {
        this.stage = stage;
    }

    public void show() {
        stage.setTitle("Dice Game");

        Label title = new Label("Dice Game");
        title.getStyleClass().add("title");
        playBtn = new Button("Jouer");

        Button scoresBtn = new Button("Tableau des scores");
        Button rulesBtn = new Button("Règles");
        Button paramsBtn = new Button("Paramètres");

        playBtn.setOnAction(e -> jouer());
        scoresBtn.setOnAction(e -> afficherTableauScores());
        rulesBtn.setOnAction(e -> afficherRegles());
        paramsBtn.setOnAction(e -> openConfigDialog());

        HBox top = new HBox(10, title, new Region(), playBtn, scoresBtn, rulesBtn, paramsBtn);
        HBox.setHgrow(top.getChildren().get(1), Priority.ALWAYS);
        top.setAlignment(Pos.CENTER_LEFT);
        top.setPadding(new Insets(14));
        top.getStyleClass().add("topbar");

        // Dés + titres
        dieView1 = new DeVisualisation();
        dieView2 = new DeVisualisation();

        Label l1 = new Label("Dé 1");
        l1.getStyleClass().add("die-title");
        VBox box1 = new VBox(8, l1, dieView1);
        box1.setAlignment(Pos.CENTER);

        Label l2 = new Label("Dé 2");
        l2.getStyleClass().add("die-title");
        VBox box2 = new VBox(8, l2, dieView2);
        box2.setAlignment(Pos.CENTER);

        HBox diceBox = new HBox(30, box1, box2);
        diceBox.setAlignment(Pos.CENTER);
        diceBox.setPadding(new Insets(30));

        statusLabel = new Label("Prêt. Configure nbTours=" + nbTours + ", pointsSi7=" + pointsSi7);
        statusLabel.getStyleClass().add("status");

        BorderPane root = new BorderPane();
        root.setTop(top);
        root.setCenter(diceBox);
        root.setBottom(statusLabel);
        BorderPane.setMargin(statusLabel, new Insets(10, 14, 14, 14));

        Scene scene = new Scene(root, 900, 520);
        scene.getStylesheets().add(getClass().getResource("/ui/style.css").toExternalForm());

        stage.setScene(scene);
        stage.show();
    }

    private void setActiveDie(int which) {
        dieView1.getStyleClass().remove("active");
        dieView2.getStyleClass().remove("active");

        if (which == 1) dieView1.getStyleClass().add("active");
        if (which == 2) dieView2.getStyleClass().add("active");
    }

    @Override
    public void jouer() {

        // 1) Nouvelle partie
        if (partieEnCours == null) {

            TextInputDialog prenomDlg = new TextInputDialog();
            prenomDlg.setTitle("Jouer");
            prenomDlg.setHeaderText("Entrez votre prénom");
            String prenom = prenomDlg.showAndWait().orElse("").trim();
            if (prenom.isEmpty()) return;

            TextInputDialog nomDlg = new TextInputDialog();
            nomDlg.setTitle("Jouer");
            nomDlg.setHeaderText("Entrez votre nom");
            String nom = nomDlg.showAndWait().orElse("").trim();
            if (nom.isEmpty()) return;

            joueurEnCours = new Joueur(nom, prenom);
            partieEnCours = new Partie(joueurEnCours, scoreEleve, randomizer);
            partieEnCours.setNbTours(nbTours);
            partieEnCours.setPointsSi7(pointsSi7);

            partieEnCours.getDe1().addPropertyChangeListener(dieView1);
            partieEnCours.getDe2().addPropertyChangeListener(dieView2);

            tourCourant = 1;
            scoreCourant = 0;
            tourEnCours = false;
            finDePartie = false;

            playBtn.setText("Lancer tour");
            playBtn.setDisable(false);
            statusLabel.setText("Partie prête. Tour 1/" + nbTours + " (clique sur 'Lancer tour')");
            return;
        }

        // 2) Partie finie => on ignore les clics (on attend que le reset soit fait)
        if (finDePartie) return;

        // 3) Un clic = un tour
        lancerUnTour();
    }

    private void lancerUnTour() {


        if (tourEnCours) return; // évite double clic pendant animation
        tourEnCours = true;
        playBtn.setDisable(true);

        // Dé 1
        setActiveDie(1);
        statusLabel.setText("Tour " + tourCourant + "/" + nbTours + " - Dé 1");
        partieEnCours.getDe1().Lancer();

        PauseTransition p1 = new PauseTransition(Duration.millis(250));
        p1.setOnFinished(e1 -> {
            // Dé 2
            setActiveDie(2);
            statusLabel.setText("Tour " + tourCourant + "/" + nbTours + " - Dé 2");
            partieEnCours.getDe2().Lancer();

            PauseTransition p2 = new PauseTransition(Duration.millis(250));
            p2.setOnFinished(e2 -> {
                setActiveDie(0);

                int v1 = partieEnCours.getDe1().getValeurActuel();
                int v2 = partieEnCours.getDe2().getValeurActuel();
                int somme = v1 + v2;

                if (somme == 7) scoreCourant += pointsSi7;

                statusLabel.setText("Tour " + tourCourant + "/" + nbTours +
                        " - " + v1 + " + " + v2 + " = " + somme + " | Score=" + scoreCourant);

                // avancer tour
                tourCourant++;

                if (tourCourant > nbTours) {
                    tourEnCours = false;
                    // on laisse le bouton désactivé, la fin va faire le reset
                    finirPartieEtPreparerNouveauJoueur();
                    return;
                }


                playBtn.setDisable(false);
                tourEnCours = false;

            });
            p2.play();
        });
        p1.play();
    }




    private void finirPartieEtPreparerNouveauJoueur() {
        if (finDePartie) return;
        finDePartie = true;

        playBtn.setDisable(true); // blocage pendant la fin

        String nomAffiche = "";
        try {
            // sauvegarde score
            joueurEnCours.Majscore(scoreCourant);
            nomAffiche = joueurEnCours.getNomJoueur() + " " + joueurEnCours.getPrenomJoueur();
            scoreEleve.add(new Saisie(nomAffiche, scoreCourant));

            statusLabel.setText("Terminé. Score de " + nomAffiche + " = " + scoreCourant);

            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.initOwner(stage);
            a.initModality(Modality.WINDOW_MODAL);
            a.setTitle("Partie finie");
            a.setHeaderText("Partie terminée");
            a.setContentText("Score final : " + scoreCourant + "\n(ajouté au tableau des scores)");
            a.showAndWait();


        } catch (Exception ex) {
            // si une erreur empêche le reset, on la voit au moins en console
            ex.printStackTrace();
            statusLabel.setText("Erreur pendant la fin de partie (voir console).");
        } finally {
            // RESET garanti même s'il y a une exception
            partieEnCours = null;
            joueurEnCours = null;

            tourCourant = 1;
            scoreCourant = 0;
            tourEnCours = false;
            finDePartie = false;

            setActiveDie(0);

            playBtn.setText("Jouer");
            playBtn.setDisable(false);

            statusLabel.setText("Prêt. Cliquez sur 'Jouer' pour un nouveau joueur.");
        }
    }




    @Override
    public void afficherTableauScores() {
        Stage dialog = new Stage();
        dialog.initOwner(stage);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Tableau des scores");
        TableView<Saisie> table = new TableView<>();
        TableColumn<Saisie, String> colNom = new TableColumn<>("Nom");
        colNom.setCellValueFactory(new PropertyValueFactory<>("nomSaisie"));

        TableColumn<Saisie, Integer> colScore = new TableColumn<>("Score");
        colScore.setCellValueFactory(new PropertyValueFactory<>("scoreSaisie"));

        table.getColumns().addAll(colNom, colScore);
        table.setItems(FXCollections.observableArrayList(scoreEleve.getSaisies()));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        Button refreshBtn = new Button("Rafraîchir");
        refreshBtn.setOnAction(ev -> {
            // If underlying ScoreEleve is persistent, ask it to reload
            if (scoreEleve instanceof org.dicegame.persistence.PersistentScoreEleve) {
                ((org.dicegame.persistence.PersistentScoreEleve) scoreEleve).reload();
            }
            table.setItems(FXCollections.observableArrayList(scoreEleve.getSaisies()));
        });

        HBox controls = new HBox(8, refreshBtn);
        controls.setPadding(new Insets(8));
        controls.setAlignment(Pos.CENTER_RIGHT);

        BorderPane root = new BorderPane(table);
        root.setTop(controls);
        root.setPadding(new Insets(12));

        Scene scene = new Scene(root, 520, 380);
        scene.getStylesheets().add(getClass().getResource("/ui/style.css").toExternalForm());

        dialog.setScene(scene);
        dialog.showAndWait();
    }

    @Override
    public void afficherRegles() {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Règles du jeu");
        a.setHeaderText("Cahier des charges");
        a.setContentText(
                "- Jeu de dés\n" +
                        "- Le joueur lance " + nbTours + " x 2 dés\n" +
                        "- Si la somme fait 7 : +" + pointsSi7 + " points\n" +
                        "- Fin de partie : score enregistré"
        );
        a.showAndWait();
    }

    @Override
    public void configurerParametres(int nbTours, int pointsSi7) {
        this.nbTours = nbTours;
        this.pointsSi7 = pointsSi7;
        if (statusLabel != null) {
            statusLabel.setText("Paramètres mis à jour : nbTours=" + nbTours + ", pointsSi7=" + pointsSi7);
        }
    }

    private void openConfigDialog() {
        Dialog<ButtonType> d = new Dialog<>();
        d.setTitle("Configurer les paramètres");

        TextField toursField = new TextField(String.valueOf(nbTours));
        TextField pointsField = new TextField(String.valueOf(pointsSi7));

        GridPane gp = new GridPane();
        gp.setHgap(10);
        gp.setVgap(10);
        gp.setPadding(new Insets(12));
        gp.addRow(0, new Label("Nombre de tours :"), toursField);
        gp.addRow(1, new Label("Points si somme = 7 :"), pointsField);

        d.getDialogPane().setContent(gp);
        d.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        d.showAndWait().ifPresent(bt -> {
            if (bt == ButtonType.OK) {
                try {
                    int t = Integer.parseInt(toursField.getText().trim());
                    int p = Integer.parseInt(pointsField.getText().trim());
                    if (t <= 0 || p < 0) throw new NumberFormatException();
                    configurerParametres(t, p);
                } catch (NumberFormatException ex) {
                    new Alert(Alert.AlertType.ERROR, "Valeurs invalides. Exemple: tours=10, points=10").showAndWait();
                }
            }
        });
    }
}
