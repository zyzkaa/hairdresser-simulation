package com.example.projekt;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;

public class MainMenu extends VBox {
    public MainMenu(Settings settings, Stage primaryStage){
        this.setAlignment(Pos.CENTER);
        this.getStylesheets().add(getClass().getResource("/css/mainStyle.css").toExternalForm());

        Label title = new Label("hairdresser");
        title.setId("title");
        Label author = new Label("zyzkaa");
        author.setId("author");

        TextButton startButton = new TextButton("start simulation");
        startButton.setOnAction(actionEvent -> {
            FXMLLoader simulationLoader = new FXMLLoader();
            simulationLoader.setLocation(getClass().getResource("simulation.fxml"));
            simulationLoader.setControllerFactory(param -> {
                if (param == SimulationController.class) {
                    return new SimulationController(settings, primaryStage);
                } else {
                    try {
                        return param.getDeclaredConstructor().newInstance();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            try {
                AnchorPane simPane = simulationLoader.<AnchorPane>load();
                primaryStage.setScene(new Scene(simPane));
            } catch (IOException e) {
            }
        });

        this.setMargin(startButton, new Insets(10, 0, 30, 0));

        TextButton settingsButton = new TextButton("settings");
        settingsButton.setOnAction(actionEvent -> {
            Scene settingsScene = null;
            try {
                settingsScene = new Scene(new SettingsScene(settings, primaryStage), 1200, 700);
            } catch (FileNotFoundException e) {

            }
            primaryStage.setScene(settingsScene);
        });

        this.getChildren().addAll(title, author, startButton, settingsButton);
    }
}
