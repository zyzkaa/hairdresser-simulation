package com.example.projekt;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class ImageButton extends CustomButton{
    public ImageButton (String file, double height, double width){
        this.setImage(file, height, width);
    }

    public void setImage(String file, double height, double width) {
        String path = "/imgs/" + file;
        Image image = new Image(getClass().getResourceAsStream(path));
        ImageView imageView = new ImageView(image);

        this.setPrefHeight(height);
        this.setPrefWidth(width);
        this.setMaxHeight(height);
        this.setMaxWidth(width);

        imageView.setFitHeight(height);
        imageView.setFitWidth(width);
        imageView.setPreserveRatio(true);
        this.setGraphic(imageView);
    }

    public ImageButton(){
        this.setFocusTraversable(false);
    }
}
