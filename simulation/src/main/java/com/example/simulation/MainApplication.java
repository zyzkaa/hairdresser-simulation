package com.example.projekt;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    Settings settings = new Settings();
    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("Zakład fryzjerski");
        primaryStage.setResizable(false);

        Scene mainScene = new Scene(new MainMenu(settings, primaryStage), 1200, 700);

        primaryStage.setScene(mainScene);
        primaryStage.show();

        primaryStage.setOnCloseRequest((event) -> {
            System.out.println("Closing Stage");
        });
    }

    public static void main(String[] args) {
        Application.launch();
    }
}