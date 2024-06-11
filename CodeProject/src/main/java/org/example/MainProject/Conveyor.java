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

    private static final int DEFAULT_DISTANCE = 100;
    private static final int COOKIE_DEFAULT_DISTANCE = 4;

    private boolean firstRotation;
    private double rpm;
    private Speed speed;

    private final SupsiUltrasonicRanger speedRanger;
    private boolean speedSignal;

    private long startTime = 0;
    private long endTime = 0;
    private long initIntervalTime = 0;
    private boolean isInLoop;
    private List<Double> rpmList = new ArrayList<>();

    private final SupsiLed redLight;
    private final SupsiLed blueLight;

    private final SupsiUltrasonicRanger counterRanger;
    private int cookiesCounter;
    private boolean isReading;


    public Conveyor(final SupsiLed redLight, final SupsiLed blueLight, final SupsiUltrasonicRanger speedRanger, SupsiUltrasonicRanger counterRanger) {
        this.redLight = redLight;
        this.blueLight = blueLight;
        this.speedRanger = speedRanger;
        this.counterRanger = counterRanger;
        speedSignal = false;

        firstRotation = true;
        startTime = 0;
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

            if (System.currentTimeMillis() - initIntervalTime >= 5_000) {
                if (!rpmList.isEmpty()) {
                    rpm = Collections.max(rpmList);
                    speed = rpm > 30 ? Speed.FAST : Speed.SLOW;
                    speedSignal = true;
                    firstRotation = true;
                    rpmList.clear();
                }
            }

        }
    }

    public boolean isCookieCrossing() {
        if(counterRanger.isValid())
            System.out.println("BISCOTTIIIIII:: " + counterRanger.getValue());


        if(counterRanger.isValid() && counterRanger.getValue() <= COOKIE_DEFAULT_DISTANCE) {
            isReading = true;
        } else if (isReading) {
            if(counterRanger.isValid() && counterRanger.getValue() > COOKIE_DEFAULT_DISTANCE) {
                cookiesCounter++;
                isReading = false;
                return true;
            }
        }

        return false;
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

    public int getCookiesCounter() {
        return cookiesCounter;
    }

}