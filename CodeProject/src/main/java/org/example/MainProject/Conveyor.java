package org.example.MainProject;

import org.example.MyLibrary.SupsiLed;
import org.example.MyLibrary.SupsiUltrasonicRanger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


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
    //private long startTime;
    private long rpmTimeStart;
    private int rotations;
    private double rpm;
    private Speed speed;

    private final SupsiLed redLight;
    private final SupsiLed blueLight;

    private final SupsiUltrasonicRanger speedRanger;
    private boolean speedSignal;

    private final SupsiUltrasonicRanger counterRanger;

    long startTime = 0;
    long endTime = 0;
    long initIntervalTime = 0;
    boolean isInLoop;
    List<Double> rpmList = new ArrayList<Double>();

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

        isInLoop = true;
    }

    public void speedCalculator() {
        if (speedRanger.isValid()){
            double value = speedRanger.getValue();

            if (firstRotation){
                initIntervalTime = System.currentTimeMillis();
                firstRotation = false;
            }

            if (value <= DEFAULT_DISTANCE){

                if (isInLoop){
                    endTime = System.currentTimeMillis();
                    if (startTime != 0){
                        double calcRpm = 2 * 3.14 / ((endTime - startTime)/1000.0) * 9.549;
                        rpmList.add(calcRpm);

                    }
                    startTime = System.currentTimeMillis();
                    isInLoop = false;
                }

            }else{
                if (!isInLoop){
                    isInLoop = true;
                }
            }

            if (System.currentTimeMillis() - initIntervalTime >= 5_000){
                if (rpmList.size() != 0) {
                    rpm = Collections.max(rpmList);
                    speedSignal = true;
                    firstRotation = true;
                    rpmList.clear();
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

    public double getRpm() {
        return rpm;
    }

    public Speed getSpeed() {
        return speed;
    }

}