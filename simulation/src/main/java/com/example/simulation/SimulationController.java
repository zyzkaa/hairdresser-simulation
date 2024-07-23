package com.example.projekt;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;


import java.net.URL;
import java.util.*;
import java.util.concurrent.Semaphore;

public class SimulationController implements Initializable{
    @FXML
    private HBox seatHBox;
    @FXML
    private HBox workersHBox;
    @FXML
    private StackPane activeWorkersPane;
    @FXML
    private StackPane usedSeatHBox;
    @FXML
    private VBox queueVBox;
    @FXML
    private VBox afterQueueVBox;
    @FXML
    private HBox buttonHBox;
    private Settings settings;
    private Rectangle seats[];
    private Stage primaryStage;
    private ImageButton backButton;
    private Random random = new Random();
    Queue queue;
    private Thread queueThread;
    private ArrayList<Circle> circlesQueue = new ArrayList();
    private Semaphore queueSem;
    private Circle circleOnSeat[];
    private String colors[] = {"db7474", "9e5f82", "a5f0ea", "e8b184", "f0f0a5",  "d7a5f0"};
    private Polygon workersArr[][];

    public SimulationController(Settings settings, Stage primaryStage){
        this.settings = settings;
        this.primaryStage = primaryStage;
        queue = new Queue(this, settings);
        queueSem = new Semaphore(settings.getM());
        circleOnSeat = new Circle[settings.getL()];
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            addBackButton();
        } catch (Exception e) {
            e.printStackTrace();
        }

        addSeats();
        queueVBox.setAlignment(Pos.TOP_CENTER);
        queueVBox.setScaleY(-1);
        afterQueueVBox.setAlignment(Pos.TOP_CENTER);
        afterQueueVBox.setScaleY(-1);
        startQueueThread();
        addWorkers();
    }

    private void addWorkers() {
        workersHBox.setAlignment(Pos.CENTER_RIGHT);
        workersArr = new Polygon[settings.getN()][];
        ArrayList pValues = settings.getPArray();
        for (int i = 0; i < settings.getN(); i++) {
            workersArr[i] = new Polygon[(int)pValues.get(i)];
        }
        Platform.runLater(() -> {
            for (int i = 0; i < settings.getN(); i++) {
                for (int j = 0; j < (int)pValues.get(i); j++) {
                    Polygon triangle = new Polygon();
                    double sideLength = 50.0;
                    double height = (Math.sqrt(3) / 2) * sideLength;
                    triangle.getPoints().addAll(new Double[]{
                            0.0, -height / 2,
                            -sideLength / 2, height / 2,
                            sideLength / 2, height / 2
                    });
                    triangle.setFill(Color.web(colors[i]));
                    triangle.setStroke(Color.web("505050"));
                    triangle.setStrokeWidth(2);
                    workersArr[i][j] = triangle;
                    workersHBox.getChildren().add(workersArr[i][j]);
                }
            }
        });
    }


    private void addBackButton(){
        backButton = new ImageButton("left-round.png", 50, 50);
        buttonHBox.getChildren().add(backButton);
        backButton.toFront();
        backButton.setOnAction(actionEvent -> {
            queueThread.interrupt();
            primaryStage.setScene(new Scene(new MainMenu(settings, primaryStage), 1200, 700));
        });
    }

    private void startQueueThread() {
        queueThread = new Thread(() -> {
            while (true) {
                try {
                    Client newClient = new Client(queue, queueSem, settings.getN());
                    newClient.start();
                    Thread.sleep(getRandomDelay());
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        queueThread.start();
    }

    private long getRandomDelay() {
        return 1000 + random.nextInt(5000);
    }

    public void addCircle(int color) {
        Circle circle = new Circle(20);
        circle.setFill(Color.web(colors[color]));
        circle.setStrokeWidth(2);
        circle.setStroke(Color.web("505050"));
        VBox.setMargin(circle, new Insets(10));
        circlesQueue.add(circle);
        Platform.runLater(() -> {
            queueVBox.getChildren().add(circle);
        });
        TranslateTransition translateTransition = new TranslateTransition();
        translateTransition.setNode(circle);
        translateTransition.setFromX(-200);
        translateTransition.setToX(0);
        translateTransition.play();
    }

    public void moveCircleToSeat(int seatId, int color, long delay){
        for (int i = 0; i < circlesQueue.size(); i++) {
            if(circlesQueue.get(i).getFill().equals(Color.web(colors[color]))){
                circleOnSeat[seatId] = circlesQueue.remove(i);
                break;
            }
        }

        ScaleTransition t1 = new ScaleTransition(Duration.millis(200), circleOnSeat[seatId]);
        t1.setToX(0);
        t1.setToY(0);
        t1.play();

        ScaleTransition t2 = new ScaleTransition(Duration.millis(200), circleOnSeat[seatId]);
        t2.setToX(1);
        t2.setToY(1);

        TranslateTransition t3 = new TranslateTransition(Duration.millis(500), circleOnSeat[seatId]);
        t3.setByY(300);

        TranslateTransition t4 = new TranslateTransition(Duration.millis(500), circleOnSeat[seatId]);
        t4.setByX(924 - (seatId * 92));

        TranslateTransition t5 = new TranslateTransition(Duration.millis(200), circleOnSeat[seatId]);
        t5.setByY(94);

        SequentialTransition s1 = new SequentialTransition(t2, t3, t4, t5);

        t1.setOnFinished(actionEvent -> {
            Platform.runLater(() -> {
                afterQueueVBox.getChildren().add(circleOnSeat[seatId]);
                s1.play();
            });
        });

        FillTransition f1 = new FillTransition(Duration.millis(delay), circleOnSeat[seatId]);
        f1.setToValue(Color.WHITESMOKE);

        s1.setOnFinished(event -> {
            Platform.runLater(() -> {
                usedSeatHBox.getChildren().add(circleOnSeat[seatId]);
                circleOnSeat[seatId].setTranslateX(-25 - (seatId*92));
                circleOnSeat[seatId].setTranslateY(0);
                f1.play();
            });
        });
    }

    public void exitCircleFromSeat(int seatId){
        TranslateTransition t1 = new TranslateTransition(Duration.millis(1000), circleOnSeat[seatId]);
        t1.setByY(-300);
        t1.play();
        t1.setOnFinished(actionEvent -> {
            Platform.runLater(() -> {
                usedSeatHBox.getChildren().remove(circleOnSeat[seatId]);
            });
        });
    }

    private void addSeats(){
        seats = new Rectangle[settings.getL()];
        try {
            for (int i = 0; i < settings.getL(); i++) {
                seats[i] = new Rectangle(70 ,70);
                seats[i].setStrokeWidth(2);
                seats[i].setStroke(Color.web("505050"));
                HBox.setMargin(seats[i], new Insets(10));
                seats[i].setFill(Color.web("93cca4"));
                seats[i].setArcWidth(15);
                seats[i].setArcHeight(15);
                seatHBox.getChildren().add(seats[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void moveWorker(int color, int workerNo, int seatId){
        double x = workersArr[color][workerNo].getLayoutX();
        double newX = -925 + x + 28;
        double finalX = -19 - (seatId*92);

        TranslateTransition t1 = new TranslateTransition(Duration.millis(700), workersArr[color][workerNo]);
        t1.setByY(-325);

        RotateTransition r1 = new RotateTransition(Duration.millis(300), workersArr[color][workerNo]);
        if(newX == finalX){
            r1.setByAngle(0);
        } else if(newX < finalX) {
            r1.setByAngle(90);
        } else {
            r1.setByAngle(-90);
        }

        r1.setCycleCount(1);
        r1.setAutoReverse(false);

        SequentialTransition s1 = new SequentialTransition(t1, r1);

        s1.play();

        TranslateTransition t2 = new TranslateTransition(Duration.millis(300), workersArr[color][workerNo]);
        t2.setToX(finalX);

        RotateTransition r2 = new RotateTransition(Duration.millis(300), workersArr[color][workerNo]);
        r2.setByAngle(-r1.getByAngle());
        r2.setCycleCount(1);
        r2.setAutoReverse(false);

        SequentialTransition s2 = new SequentialTransition(t2, r2);

        s1.setOnFinished(actionEvent -> {
            Platform.runLater(() -> {
                activeWorkersPane.getChildren().add(workersArr[color][workerNo]);
                workersArr[color][workerNo].setTranslateX(newX);
                workersArr[color][workerNo].setTranslateY(-65);
                s2.play();
            });
        });
    }

    public void exitWorker(int color, int workerNo){
        TranslateTransition t1 = new TranslateTransition(Duration.millis(500), workersArr[color][workerNo]);
        t1.setByY(250);

        RotateTransition r1 = new RotateTransition(Duration.millis(100), workersArr[color][workerNo]);
        r1.setByAngle(180);
        r1.setCycleCount(1);
        r1.setAutoReverse(false);

        RotateTransition r2 = new RotateTransition(Duration.millis(100), workersArr[color][workerNo]);
        r2.setByAngle(-90);
        r2.setCycleCount(1);
        r2.setAutoReverse(false);

        TranslateTransition t2 = new TranslateTransition(Duration.millis(200), workersArr[color][workerNo]);
        t2.setToX(0);

        SequentialTransition s1 = new SequentialTransition(r1, t1, r2, t2);

        s1.play();

        RotateTransition r3 = new RotateTransition(Duration.millis(100), workersArr[color][workerNo]);
        r3.setByAngle(90);
        r3.setCycleCount(1);
        r3.setAutoReverse(false);

        TranslateTransition t3 = new TranslateTransition(Duration.millis(200), workersArr[color][workerNo]);
        t3.setToY(0);

        RotateTransition r4 = new RotateTransition(Duration.millis(100), workersArr[color][workerNo]);
        r4.setByAngle(180);
        r4.setCycleCount(1);
        r4.setAutoReverse(false);

        SequentialTransition s2 = new SequentialTransition(r3, t3, r4);

        s1.setOnFinished(actionEvent -> {
            Platform.runLater(() -> {
                workersHBox.getChildren().add(workersArr[color][workerNo]);
                workersArr[color][workerNo].setTranslateX(0);
                workersArr[color][workerNo].setTranslateY(-75);
                s2.play();
            });
        });
    }
}
