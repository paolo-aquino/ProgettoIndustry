package org.example.MainProject;

import org.example.MyLibrary.SupsiLed;
import org.example.MyLibrary.SupsiUltrasonicRanger;

import java.io.IOException;


public class Conveyor {

    public enum Speed {
        FAST("high_speed"),
        SLOW("low_speed");

        private final String name;

        Speed(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private static final int DEFAULT_DISTANCE = 10;
    private static final double RADIUS = 0.04;
    private static final double CIRCUMFERENCE = 2 * Math.PI * RADIUS;
    private static final double DEFAULT_SPEED = 0.01;
    private boolean firstRotation;
    private boolean readingValue;
    private long startTime;
    private long rpmTimeStart;
    private int rotations;
    private int rpm;
    private Speed speed;

    private final SupsiLed redLight;
    private final SupsiLed blueLight;

    private final SupsiUltrasonicRanger speedRanger;
    private boolean speedSignal;

    private final SupsiUltrasonicRanger counterRanger;

    public Conveyor(final SupsiLed redLight, final SupsiLed blueLight, final SupsiUltrasonicRanger speedRanger, SupsiUltrasonicRanger counterRanger) {
        this.redLight = redLight;
        this.blueLight = blueLight;
        this.speedRanger = speedRanger;
        this.counterRanger = counterRanger;
        speedSignal = false;

        firstRotation = true;
        readingValue = false;
        startTime = 0;
        rpmTimeStart = 0;
        rotations = 0;
        rpm = 0;
        speed = Speed.SLOW;
    }

    public void speedCalculator() {
        if(firstRotation) {
            if (speedRanger.isValid() && speedRanger.getValue() <= DEFAULT_DISTANCE) {
                readingValue = true;
            } else if (readingValue) {
                if(speedRanger.isValid() && speedRanger.getValue() > DEFAULT_DISTANCE) {
                    startTime = System.currentTimeMillis();
                    firstRotation = false;
                    readingValue = false;

                    rpmTimeStart = startTime;
                }
            }
        } else {
            if(speedRanger.isValid() && speedRanger.getValue() <= DEFAULT_DISTANCE) {
                readingValue = true;
            } else if (readingValue) {
                if(speedRanger.isValid() && speedRanger.getValue() > DEFAULT_DISTANCE) {
                    long endTime = System.currentTimeMillis();
                    long diff = endTime - startTime;

                    double time = diff /1000.0;

                    speed = (CIRCUMFERENCE/time * RADIUS) > DEFAULT_SPEED ? Speed.FAST : Speed.SLOW;

                    startTime = System.currentTimeMillis();
                    readingValue = false;

                    rotations++;
                    if(endTime - rpmTimeStart >= 30_000){
                        rpm = rotations * 2;
                        rotations = 0;
                        rpmTimeStart = System.currentTimeMillis();
                        speedSignal = true;
                    }
                }
            }
        }
    }

    public boolean isSignalReady() {
        if(speedSignal) {
            speedSignal = false;
            return true;
        }
        return false;
    }

    public void ledBlink() throws IOException {
        switch (speed) {
            case SLOW:
                blueLight.off();
                redLight.blink();
                break;
            case FAST:
                redLight.off();
                blueLight.blink();
                break;
        }
    }

    public int getRpm() {
        return rpm;
    }

    public Speed getSpeed() {
        return speed;
    }

}