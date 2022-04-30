package com.company.utils;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;

import static com.company.utils.ColorUtilities.HSLtoRGB;

public class Blockies extends Canvas{

    private int[] randSeed;
    private final String seed;
    private int[] spotcolor, color, bgcolor;
    private GraphicsContext gc;

    public Blockies() {
        super();
        this.seed = ((Double)Math.floor((Math.random()*Math.pow(10,16)))).toString();
        randSeed = new int[4];
        bgcolor = new int[3];
        color = new int[3];
        spotcolor = new int[3];
        this.setWidth(120);
        this.setHeight(120);
        gc = this.getGraphicsContext2D();
        renderIcon(8, 15);
    }

    public Blockies(String seed) {
        super();
        this.seed = seed.toLowerCase();
        randSeed = new int[4];
        bgcolor = new int[3];
        color = new int[3];
        spotcolor = new int[3];
        this.setWidth(120);
        this.setHeight(120);
        gc = this.getGraphicsContext2D();
        renderIcon(8, 15);
    }

    public Blockies(String seed, @NotNull int size, @NotNull int scale) {
        super();
        this.seed = seed.toLowerCase();
        randSeed = new int[4];
        bgcolor = new int[3];
        color = new int[3];
        spotcolor = new int[3];
        this.setWidth(size * scale);
        this.setHeight(size * scale);
        gc = this.getGraphicsContext2D();
        renderIcon(size, scale);

    }

    private Canvas renderIcon(int size , int scale) {

        seedRand(seed);

        generateColors();

        ArrayList<Double> imageData = createImageData(size);

        int width = (int) Math.sqrt(imageData.size());

        gc.setFill(Color.rgb(bgcolor[0] , bgcolor[1] , bgcolor[2]));

        gc.fillRect(0 , 0 , this.getWidth() , this.getHeight());

        gc.setFill(Color.rgb(color[0] , color[1] , color[2]));


        for (int i = 0; i < imageData.size(); i++) {
            // if data is 0, leave the background
            if (imageData.get(i) != 0) {
                double row = Math.floor(i / width);
                int col = i % width;
                // if data is 2, choose spot color, if 1 choose foreground
                if (imageData.get(i) == 1) {
                    gc.setFill(Color.rgb(color[0] , color[1] , color[2]));
                }
                if (imageData.get(i) == 2) {
                    gc.setFill(Color.rgb(spotcolor[0] , spotcolor[1] , spotcolor[2]));
                }
                gc.fillRect(col * scale , row * scale , scale , scale);
            }
        }

        return this;
    }

    //Copy and paste from the seedrand() method of Blockies.js
    private void seedRand(String seed) {
        for (var i = 0; i < randSeed.length; i++) {
            randSeed[i] = 0;
        }
        for (var i = 0; i < seed.length(); i++) {
            randSeed[i % 4] = ((randSeed[i % 4] << 5) - randSeed[i % 4]) + seed.charAt(i);
        }
    }

    //Copy and paste from the rand() method of Blockies.js
    private double rand() {
        // based on Java's String.hashCode(), expanded to 4 32bit values
        var t = randSeed[0] ^ (randSeed[0] << 11);
        randSeed[0] = randSeed[1];
        randSeed[1] = randSeed[2];
        randSeed[2] = randSeed[3];
        randSeed[3] = (randSeed[3] ^ (randSeed[3] >> 19) ^ t ^ (t >> 8));
        double t1 = randSeed[3] >>> 0;
        double t2 = ((1 << 31) >>> 0);
        return Math.abs(t1 / t2);
    }

    /**
     * Generates the three colors needed for the avatar: background, foreground, and spot;
     */
    private void generateColors(){
        double[] temp = createColor();
        color = HSLtoRGB((float) temp[0] , (float) temp[1] , (float) temp[2] , 1);
        temp = createColor();
        bgcolor = HSLtoRGB((float) temp[0] , (float) temp[1] , (float) temp[2] , 1);
        temp = createColor();
        spotcolor = HSLtoRGB((float) temp[0] , (float) temp[1] , (float) temp[2] , 1);
    }

    /**
     * Generates an HSL (Hue-Saturation-Lightness) color using the rand() function
     * @return A double-array containing values from 0 to 1 in order HSL.
     */
    private double[] createColor() {
        //saturation is the whole color spectrum
        double h = Math.floor(rand() * 360);
        //saturation goes from 40 to 100, it avoids greyish colors
        double s = (rand() * 60) + 40;
        //lightness can be anything from 0 to 100, but probabilities are a bell curve around 50%
        double l = (rand() + rand() + rand() + rand()) * 25;
        return new double[]{ h , s , l };
    }

    /**
     * Creates an array of integer which represent each pixel in the image in order to specify which of the 3 possible
     * color has to be used in that specific pixel.
     * @param size
     * @return An {@code integer}-array containing only values 0, 1 or 2 which, in the final image, represent:
     * <ul><li> 0: background-color
     * <li> 1: foreground-color
     * <li> 2: spot-color</ul>
     * The distribution is done using the rand() function, where the 0 and 1 have both a 43% probability, while the spot the remaining 13%.
     */
    ArrayList<Double> createImageData(int size) {
        int width = size, height = size;
        double dataWidth = Math.ceil(width / 2);
        double mirrorWidth = width - dataWidth;
        ArrayList<Double> data = new ArrayList<Double>();
        for (var y = 0; y < height; y++) {
            ArrayList<Double> row = new ArrayList<Double>();
            for (var x = 0; x < dataWidth; x++) {
                // this makes foreground and background color to have a 43% (1/2.3) probability
                // spot color has 13% chance
                double temp = Math.floor(rand() * 2.3);
                row.add(x , temp);
            }
            ArrayList<Double> r = new ArrayList<>();
            r.addAll(row.subList(0 , (int) mirrorWidth));
            Collections.reverse(r);
            row.addAll(r);
            for (var i = 0; i < row.size(); i++) {
                data.add(row.get(i));
            }
        }
        return data;
    }



}
