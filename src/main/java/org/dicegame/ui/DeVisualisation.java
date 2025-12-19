package org.dicegame.ui;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class DeVisualisation extends StackPane implements PropertyChangeListener {
    private final Label label = new Label("1");

    public DeVisualisation() {
        getStyleClass().add("die");
        label.getStyleClass().add("die-label");
        getChildren().add(label);
        setMinSize(110, 110);
        setPrefSize(110, 110);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("valeurActuel".equals(evt.getPropertyName())) {
            Object v = evt.getNewValue();
            Platform.runLater(() -> label.setText(String.valueOf(v)));
        }
    }
}
