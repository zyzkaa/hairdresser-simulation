package com.example.projekt;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class NumberTextField extends TextField {
    private int value;

    public NumberTextField(){
        this.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                setText(oldValue);
                return;
            }

            if(newValue.matches("")){
                return;
            }

            value = Integer.valueOf(newValue);
        });
    }

    public void setNewValue(int newValue){
        value = newValue;
        this.setText(String.valueOf(newValue));
    }

    public int getValue(){
        return value;
    }

    public void changeToRed(){
        ObjectProperty<Color> colorProperty = new SimpleObjectProperty<>(Color.web("#E1AFD1"));
        colorProperty.addListener((obs, oldColor, newColor) -> {
            int red = (int) (newColor.getRed() * 255);
            int green = (int) (newColor.getGreen() * 255);
            int blue = (int) (newColor.getBlue() * 255);
            this.setStyle(String.format("-fx-background-color: rgba(%d, %d, %d, 1);", red, green, blue));
        });

        Timeline fadeIn = new Timeline(
                new KeyFrame(Duration.seconds(0.2), new KeyValue(colorProperty, Color.LIGHTCORAL , Interpolator.LINEAR))
        );

        fadeIn.play();
    }

    public void changeToPink(){
        ObjectProperty<Color> colorProperty = new SimpleObjectProperty<>(Color.LIGHTCORAL);
        colorProperty.addListener((obs, oldColor, newColor) -> {
            int red = (int) (newColor.getRed() * 255);
            int green = (int) (newColor.getGreen() * 255);
            int blue = (int) (newColor.getBlue() * 255);
            this.setStyle(String.format("-fx-background-color: rgba(%d, %d, %d, 1);", red, green, blue));
        });

        Timeline fadeIn = new Timeline(
                new KeyFrame(Duration.seconds(0.2), new KeyValue(colorProperty, Color.web("#E1AFD1"), Interpolator.LINEAR))
        );
        fadeIn.play();
    }
}
