package com.example.projekt;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class SettingsScene extends VBox {
    private int n = 4;
    private Settings settings;
    private NumberTextField[] fieldArr = new NumberTextField[n];
    private NumberTextField[] pFieldArr;
    private boolean properValues = true;
    private boolean properL = true;
    private boolean properP = true;
    public SettingsScene(Settings settings, Stage primaryStage) throws FileNotFoundException {
        this.setId("root");
        this.getStylesheets().add(getClass().getResource("/css/settingStyle.css").toExternalForm());
        this.settings = settings;


        ImageButton backButton = new ImageButton("left-round.png", 50, 50);
        backButton.setId("back-button");
        backButton.setCancelButton(true);
        backButton.setOnAction(actionEvent -> {
            if(!properValues || !properL || !properP){
                return;
            }
            Scene mainScene = null;
            mainScene = new Scene(new MainMenu(settings, primaryStage), 1200, 700);
            primaryStage.setScene(mainScene);
            ArrayList<Integer> newArray = new ArrayList<Integer>();
            for (int i = 0; i < settings.getN(); i++) {
                newArray.add(pFieldArr[i].getValue());
                settings.setPArray(newArray);
            }
        });

        StackPane topLeft = new StackPane(backButton);
        topLeft.setId("top-left");

        StackPane topRight = new StackPane(new Label("ustawienia"));
        topRight.setId("top-right");

        StackPane top = new StackPane(topRight, topLeft);
        top.setId("top");
        top.setFocusTraversable(true);
        top.requestFocus();

        VBox namesVBox = new VBox();
        String[] names = {"M", "L", "P", "N"};
        Label[] labelArr = new Label[n];
        for (int i = 0; i <n ; i++) {
            labelArr[i] = new Label();
            labelArr[i].setText(names[i]);
            VBox.setMargin(labelArr[i], new Insets(20));
            namesVBox.getChildren().add(labelArr[i]);
        }

        VBox fieldsVBox = new VBox();
        int[] initialValues = settings.exportAsArray();
        for (int i = 0; i <n ; i++) {
            fieldArr[i] = new NumberTextField();
            fieldArr[i].setNewValue(initialValues[i]);
            VBox.setMargin(fieldArr[i], new Insets(20));
            fieldsVBox.getChildren().add(fieldArr[i]);
        }

        HBox middleHBox = new HBox(namesVBox, fieldsVBox);
        middleHBox.setId("middleHBox");


        VBox helpVBox = new VBox();
        helpVBox.setId("helpVBox");
        String[] helpText = {"waiting room capacity",
                "seats",
                "total workers amount",
                "services amount"};
        Label[] helpArray = new Label[n];
        for (int i = 0; i <n ; i++) {
            helpArray[i] = new Label();
            helpArray[i].setText(helpText[i]);
            VBox.setMargin(helpArray[i], new Insets(20, 20, 20, 20));
            helpVBox.getChildren().add(helpArray[i]);
        }
        StackPane helpPane = new StackPane(helpVBox);
        helpPane.setVisible(false);
        helpPane.setManaged(false);

        StackPane middlePane = new StackPane(helpPane, middleHBox);

        HBox pBox = new HBox();
        pBox.setId("pBox");

        addPBoxes(pBox);

        fieldArr[0].textProperty().addListener((observable, oldValue, newValue) -> {
            settings.setM(fieldArr[0].getValue());

            if(fieldArr[0].getValue() == 0){
                fieldArr[0].setNewValue(1);
                return;
            }

            if(fieldArr[0].getValue() > 10){
                fieldArr[0].setNewValue(10);
            }

        });

        fieldArr[1].textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.matches("")){
                return;
            }
            int newPValue = fieldArr[1].getValue();

            if(newPValue == 0){
                fieldArr[1].setNewValue(1);
                return;
            }

            if(newPValue > 10){
                fieldArr[1].setNewValue(10);
                return;
            }

            if(properL && (fieldArr[1].getValue() >= settings.getP())){
                fieldArr[1].changeToRed();
                properL = false;
            }

            if(!properL && (fieldArr[1].getValue() < settings.getP())){
                fieldArr[1].changeToPink();
                properL = true;
            }

            settings.setL(fieldArr[1].getValue());
        });

        fieldArr[2].textProperty().addListener((observable, oldValue, newValue) ->{
            if(newValue.matches("")){
                return;
            }
            int newPValue = fieldArr[2].getValue();

            if(newPValue == 0){
                fieldArr[2].setNewValue(1);
                return;
            }

            if(newPValue > 15){
                fieldArr[2].setNewValue(15);
                return;
            }

            if(properP && (newPValue < settings.getN() || newPValue <= settings.getL())){
                fieldArr[2].changeToRed();
                properP = false;
            }

            if(!properP && (newPValue >= settings.getN() || newPValue > settings.getL())){
                fieldArr[2].changeToPink();
                properP = true;
            }

//            if(properP && (fieldArr[2].getValue() <= settings.getL())){
//                fieldArr[2].changeToRed();
//                properP = false;
//            }
//
//            if(!properP && (fieldArr[2].getValue() > settings.getL())){
//                fieldArr[2].changeToPink();
//                properP = true;
//            }

            settings.setP(newPValue);
            settings.reassignValues();
            addPBoxes(pBox);
        });

        fieldArr[3].textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.matches("")){
                return;
            }
            int newFieldsAmount = fieldArr[3].getValue();

            if(newFieldsAmount == 0){
                fieldArr[3].setText("1");
            }

            if(newFieldsAmount > 6){
                fieldArr[3].setText("6");
            }

            if (properP && (newFieldsAmount > settings.getP())) {
                fieldArr[3].changeToRed();
                properP = false;
            }

            if (!properP && (newFieldsAmount <= settings.getP())) {
                fieldArr[3].changeToPink();
                properP = true;
            }

            settings.setN(fieldArr[3].getValue());
            settings.changeArraySize();
            addPBoxes(pBox);
        });

        HBox bottom = new HBox();
        bottom.setId("bottom");
        ImageButton helpButton = new ImageButton("question.png", 60, 60);
        ImageButton resetButton = new ImageButton("reload.png", 60, 60);
        HBox.setMargin(helpButton, new Insets(0, 10, 0, 0));
        HBox.setMargin(resetButton, new Insets(0, 0, 0, 10));
        bottom.getChildren().addAll(helpButton, resetButton);

        helpButton.setOnAction(actionEvent -> {
            boolean isVisible = helpPane.isVisible();
            helpPane.setVisible(!isVisible);
            helpPane.setManaged(!isVisible);
        });

        resetButton.setOnAction(actionEvent -> {
            try {
                settings.readDataFromFile();
            } catch (IOException e) {

            } finally {
                for (int i = 0; i < n ; i++) {
                    fieldArr[i].setNewValue(initialValues[i]);
                }
                addPBoxes(pBox);
            }
        });

        this.getChildren().addAll(top, middlePane, pBox, bottom);
    }

    private void addPBoxes(HBox hBox){
        hBox.getChildren().clear();
        ArrayList<Integer> pArray = settings.getPArray();
        pFieldArr = new NumberTextField[settings.getN()];
        for (int i = 0; i < settings.getN(); i++) {
            pFieldArr[i] = new NumberTextField();
            pFieldArr[i].setNewValue(pArray.get(i));
            VBox.setMargin(pFieldArr[i], new Insets(10, 0, 0, 0));
            VBox newVBox = new VBox(new Label("P"+String.valueOf(i+1)), pFieldArr[i]);
            HBox.setMargin(newVBox, new Insets(0, 10, 0, 10));
            newVBox.setAlignment(Pos.CENTER);
            hBox.getChildren().add(newVBox);

            pFieldArr[i].textProperty().addListener((observable, oldValue, newValue) -> {
                if(newValue.matches("")){
                    return;
                }

                int sum = 0;
                for (int j = 0; j < settings.getN(); j++) {
                    sum += pFieldArr[j].getValue();
                }

                if(sum != settings.getP()){
                    if(properValues) {
                        for (int j = 0; j < settings.getN(); j++) {
                            pFieldArr[j].changeToRed();
                        }
                        properValues = false;
                    }
                    return;
                }

                if(!properValues){
                    for (int j = 0; j < settings.getN(); j++) {
                        pFieldArr[j].changeToPink();
                    }
                    properValues = true;
                }
            });
        }
    }
}
