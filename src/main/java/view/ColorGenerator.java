package view;

import javafx.scene.paint.Color;

import java.util.Random;

public class ColorGenerator {

    public static Color getRandomColor() {
        return Color.color(Math.random(), Math.random(), Math.random());
    }

}
