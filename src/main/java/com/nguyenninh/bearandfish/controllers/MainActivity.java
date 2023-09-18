package com.nguyenninh.bearandfish.controllers;

import com.nguyenninh.bearandfish.entity.*;
import javafx.animation.AnimationTimer;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainActivity implements Initializable, Runnable {
    public GridPane gridPane;
    public VBox vBoxSetting;
    public TextField numWidth;
    public TextField numHeight;
    public TextField speedText;
    public Slider speedSlider;
    public TextField priceBearText;
    public Slider priceBearSlider;
    public TextField priceFishText;
    public Slider priceFishSlider;
    public Button btnSubmit;

    //Thread
    private Thread threadMain;

    //FPS
    private double FPS;

    //FOREST
    private Forest forest;
    private int width;
    private int height;

    //OPP
    private AnimationTimer timer;
    private List<Animal> dataNodes = new ArrayList<>();

    private double cellWidth;
    private double cellHeight;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //DEFAULT SETTING
        numWidth.setText("10");
        numHeight.setText("10");
        speedText.setText("1");
        priceBearText.setText("1");
        priceFishText.setText("1");

        numWidth.setOnAction(e -> numWidth.setText((Integer.parseInt(numWidth.getText()) >= 1) ? numWidth.getText() : "10"));
        numHeight.setOnAction(e -> numHeight.setText((Integer.parseInt(numHeight.getText()) >= 1) ? numHeight.getText() : "10"));

        speedText.setOnAction(e -> changeValue(speedText, speedSlider));
        speedSlider.valueProperty().addListener((observable, oldValue, newValue)
                -> speedText.setText(String.format("%.0f", newValue.doubleValue())));

        priceBearText.setOnAction(e -> changeValue(priceBearText, priceBearSlider));
        priceBearSlider.valueProperty().addListener((observable, oldValue, newValue)
                -> priceBearText.setText(String.format("%.0f", newValue.doubleValue())));

        priceFishText.setOnAction(e -> changeValue(priceFishText, priceFishSlider));
        priceFishSlider.valueProperty().addListener((observable, oldValue, newValue)
                -> priceFishText.setText(String.format("%.0f", newValue.doubleValue())));

        btnSubmit.setOnAction(e -> {
            vBoxSetting.setDisable(true);

            FPS = Double.parseDouble(speedText.getText());
            width = Integer.parseInt(numWidth.getText());
            height = Integer.parseInt(numHeight.getText());
            forest = Forest.getInstance(width, height);
            forest.add(EAnimal.BEAR, Integer.parseInt(priceBearText.getText()));
            forest.add(EAnimal.FISH, Integer.parseInt(priceFishText.getText()));

            for (int i = 0; i < width - 1; i++) {
                ColumnConstraints column = new ColumnConstraints();
                column.setHgrow(Priority.SOMETIMES);
                column.setMinWidth(10.0);
                column.setPrefWidth(100.0);
                gridPane.getColumnConstraints().add(column);
            }

            for (int i = 0; i < height - 1; i++) {
                RowConstraints row = new RowConstraints();
                row.setMinHeight(10.0);
                row.setPrefHeight(30.0);
                row.setVgrow(Priority.SOMETIMES);
                gridPane.getRowConstraints().add(row);
            }

            cellWidth = gridPane.getColumnConstraints().get(1).getPrefWidth();
            cellHeight = gridPane.getRowConstraints().get(1).getPrefHeight();
            startThreadMain();
        });
    }

    public void startThreadMain() {
        threadMain = new Thread(this);
        threadMain.start();
    }

    @Override
    public void run() {
        timer = new AnimationTimer() {
            private long lastUpdate = 0;
            private int i = 1;

            @Override
            public void handle(long now) {
                long delta = now - lastUpdate;

                if (delta >= 1_000_000_000 / FPS) {
                    draw();
                    System.out.println("Draw " + i++);

                    lastUpdate = now;
                }
            }
        };
        timer.start();
    }

    private void draw() {
        gridPane.getChildren().removeAll(removeGP());
        dataNodes.clear();
        forest.go();

        if (forest != null) {
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    if (forest.getData()[i][j] != null) {
                        ImageView imageView = forest.getData()[i][j].getImage();
//                        imageView.setPreserveRatio(true);
//                        imageView.setFitWidth(cellWidth);
//                        imageView.setFitHeight(cellHeight);

                        if (!gridPane.getChildren().contains(imageView)) {
                            gridPane.add(imageView, i, j);
                            dataNodes.add(forest.getData()[i][j]);
                        }
                    }
                }
            }
        }

        if (!fishExist()) {
            System.out.println("Kết thúc chương trình");
            timer.stop();
//            gridPane.getChildren().removeAll(removeGP());
//            vBoxSetting.setDisable(false);
            return;
        }
    }

    private List<ImageView> removeGP(){
        List<ImageView> img = new ArrayList<>();
        for (Animal animal: dataNodes) {
            if (animal != null) img.add(animal.getImage());
        }
        return img;
    }

    private boolean fishExist(){
        for (Animal animal: dataNodes) {
            if (animal instanceof Fish)
                return true;
        }
        return false;
    }

    public void changeValue(TextField text, Slider slider) {
        try {
            Double value = Double.parseDouble(text.getText());
            if (value < 1) {
                value = 1.0;
                text.setText("1");
            }
            slider.setValue(value);
        } catch (NumberFormatException ex) {
            text.setText("1");
            slider.setValue(1);
        }
    }
}
