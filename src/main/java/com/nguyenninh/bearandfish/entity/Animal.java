package com.nguyenninh.bearandfish.entity;

import com.nguyenninh.bearandfish.HelloApplication;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ConcurrentModificationException;

public class Animal implements Comparable {
    protected String name;
    protected boolean isMale;
    private final double power;
    private ImageView image;

    public Animal(String name, boolean isMale, double power) {
        this.name = name;
        this.isMale = isMale;
        this.power = power;
        image = new ImageView();
        image.setImage(new Image(HelloApplication.class.getResource("/image/null.png").toExternalForm()));
        loadImage();
    }

    public String getName() {
        return name;
    }

    public boolean isMale() {
        return isMale;
    }

    public double getPower() {
        return power;
    }

    public ImageView getImage() {
        return image;
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof Animal animal)
            return Double.compare(this.power, animal.power);
        else
            throw new ConcurrentModificationException();
    }

    public void loadImage() {
        if (this instanceof Fish)
            image.setImage(new Image(HelloApplication.class.getResource("/image/fish.jpg").toExternalForm()));
        else if (this instanceof Bear)
            image.setImage(new Image(HelloApplication.class.getResource("/image/bear.jpg").toExternalForm()));
    }
}
