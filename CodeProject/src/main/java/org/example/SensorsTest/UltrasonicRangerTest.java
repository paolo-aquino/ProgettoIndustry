package org.example.SensorsTest;

import org.example.MyLibrary.SupsiUltrasonicRanger;
import org.iot.raspberry.grovepi.GrovePi;
import org.iot.raspberry.grovepi.pi4j.GrovePi4J;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UltrasonicRangerTest {

    //Speed windmill
    private static final int DEFAULT_DISTANCE = 10;
    private static final double RADIUS = 0.04;
    private static final double CIRCUMFERENCE = 2 * Math.PI * RADIUS;

    public static void main(String[] args) throws Exception {
        Logger.getLogger("GrovePi").setLevel(Level.OFF);

        Logger.getLogger("RaspberryPi").setLevel(Level.OFF);

        GrovePi grovepi = new GrovePi4J();

        SupsiUltrasonicRanger ranger = new SupsiUltrasonicRanger(grovepi, 7);

        boolean firstRotation = true;
        boolean readingValue = false;
        long startTime = 0;

        long rpmTimeStart = 0;
        int rotations = 0;

        while(true) {
            if(firstRotation) {
                if (ranger.isValid() && ranger.getValue() <= DEFAULT_DISTANCE) {
                    readingValue = true;
                } else if (readingValue) {
                    if(ranger.isValid() && ranger.getValue() > DEFAULT_DISTANCE) {
                        startTime = System.currentTimeMillis();
                        System.out.println("Start: " + startTime);
                        firstRotation = false;
                        readingValue = false;

                        rpmTimeStart = startTime;
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


                        double time = diff /1000.0;
                        System.out.println("Velocita tangenziale: " + CIRCUMFERENCE/time);

                        startTime = System.currentTimeMillis();
                        System.out.println("New Start: " + startTime);
                        readingValue = false;


                        rotations++;
                        if(endTime - rpmTimeStart >= 60_000){
                            System.out.println();
                            System.out.println();
                            System.out.println("RPM: " + rotations);

                            rotations = 0;
                            rpmTimeStart = System.currentTimeMillis();
                        }
                    }
                }
            }
        }
    }
}