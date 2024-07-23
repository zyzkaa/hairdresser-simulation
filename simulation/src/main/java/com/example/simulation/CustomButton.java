package com.example.projekt;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class CustomButton extends Button {
    public CustomButton(){
        ObjectProperty<Color> colorProperty = new SimpleObjectProperty<>(Color.web("#E1AFD1"));
        colorProperty.addListener((obs, oldColor, newColor) -> {
            int red = (int) (newColor.getRed() * 255);
            int green = (int) (newColor.getGreen() * 255);
            int blue = (int) (newColor.getBlue() * 255);
            this.setStyle(String.format("-fx-background-color: rgba(%d, %d, %d, 1);", red, green, blue));
        });

        Timeline fadeIn = new Timeline(
                new KeyFrame(Duration.seconds(0.2), new KeyValue(colorProperty, Color.WHITESMOKE, Interpolator.LINEAR))
        );

        Timeline fadeOut = new Timeline(
                new KeyFrame(Duration.seconds(0.2), new KeyValue(colorProperty, Color.web("#E1AFD1"), Interpolator.LINEAR))
        );

        this.setOnMouseEntered(e -> fadeIn.playFromStart());
        this.setOnMouseExited(e -> fadeOut.playFromStart());
    }
}
