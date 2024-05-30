package org.example.SensorsTest;

import org.example.MyLibrary.SupsiUltrasonicRanger;
import org.iot.raspberry.grovepi.GrovePi;
import org.iot.raspberry.grovepi.pi4j.GrovePi4J;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UltrasonicRangerTest {

    //Speed windmill
    private static final int DEFAULT_DISTANCE = 5;
    private static final double RADIUS = 0.04;
    private static final double CIRCUMFERENCE = 2 * Math.PI * RADIUS;

    public static void main(String[] args) throws Exception {
        Logger.getLogger("GrovePi").setLevel(Level.OFF);
        Logger.getLogger("RaspberryPi").setLevel(Level.OFF);

        GrovePi grovepi = new GrovePi4J();

        //SupsiRgbLcd lcd = new SupsiRgbLcd();
        SupsiUltrasonicRanger ranger = new SupsiUltrasonicRanger(grovepi, 3);
        //SupsiUltrasonicRanger ranger2 = new SupsiUltrasonicRanger(grovepi, 2);

        boolean firstRotation = true;
        boolean readingValue = false;
        long startTime = 0;
        while(true) {

            if(ranger.isValid()) {
                double ra = ranger.getValue();
                System.out.println(ra > 2 ? "Distance: " + ra : "VALOREEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE ");
            }

            if(firstRotation) {
                if (ranger.isValid() && ranger.getValue() <= DEFAULT_DISTANCE) {
                    readingValue = true;
                } else if (readingValue) {
                    if(ranger.isValid() && ranger.getValue() > DEFAULT_DISTANCE) {
                        startTime = System.currentTimeMillis();
                        System.out.println("Start: " + startTime);
                        firstRotation = false;
                        readingValue = false;
                    }
                }
            } else {
                if(ranger.isValid() && ranger.getValue() <= DEFAULT_DISTANCE) {
                    readingValue = true;
                } else if (readingValue) {
                    if(ranger.isValid() && ranger.getValue() > DEFAULT_DISTANCE) {
                        long endTime = System.currentTimeMillis();
                        System.out.println("End: " + endTime);
                        long diff = endTime - startTime;
                        System.out.println("Difference : " + diff);

                        double time = (double) diff /1000;
                        System.out.println("Velocita tangenziale: " + CIRCUMFERENCE/time);

                        startTime = System.currentTimeMillis();
                        System.out.println("New Start: " + startTime);
                    }
                }
            }
        }
    }
}