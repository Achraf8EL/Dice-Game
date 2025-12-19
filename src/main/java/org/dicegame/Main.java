package org.dicegame;

import javafx.application.Application;
import javafx.stage.Stage;
import org.dicegame.ui.JavaFXIHM;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        new JavaFXIHM(stage).show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
