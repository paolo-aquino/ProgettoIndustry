package org.example.MainProject;

import org.example.MyLibrary.SupsiLed;
import org.example.MyLibrary.SupsiUltrasonicRanger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class representing the Conveyor of the Factory.
 * <p>
 * This class handles the operations and states of a conveyor belt in a factory,
 * including speed calculation, cookies counting, and LED light signaling based on speed.
 *
 * @author Paolo Aquino
 * @author Zeno Darani
 * @author Matteo Cazzani
 */
public final class Conveyor {

    /**
     * Enum representing the speed of the Conveyor.
     * <p>
     * This enum defines the two possible speeds of the conveyor: FAST and SLOW,
     * each associated with a specific string name used for database tagging.
     *
     * @author Paolo Aquino
     * @author Zeno Darani
     * @author Matteo Cazzani
     */
    private enum Speed {
        FAST("high_speed"),
        SLOW("low_speed");

        // The name of the tag to be sent to the database
        private final String name;

        Speed(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    // Default distance threshold for speed calculation
    private static final int DEFAULT_DISTANCE = 100;

    // Default distance threshold for detecting cookie crossings
    private static final int COOKIE_DEFAULT_DISTANCE = 4;

    // Fields related to speed measurement
    private final SupsiUltrasonicRanger speedRanger;
    private boolean firstRotation;
    private double rpm;
    private Speed speed;
    private boolean speedSignal;
    private long startTime;
    private long initIntervalTime;
    private boolean isInLoop;
    private final List<Double> rpmList;

    // Fields related to LED signaling
    private final SupsiLed redLight;
    private final SupsiLed blueLight;

    // Fields related to cookie counting
    private final SupsiUltrasonicRanger counterRanger;
    private int cookiesCounter;
    private boolean isReading;


    public Conveyor(final SupsiLed redLight, final SupsiLed blueLight, final SupsiUltrasonicRanger speedRanger, SupsiUltrasonicRanger counterRanger) {
        this.speedRanger = speedRanger;
        firstRotation = true;
        rpm = 0;
        speed = Speed.SLOW;
        speedSignal = false;
        startTime = 0;
        initIntervalTime = 0;
        isInLoop = true;
        rpmList = new ArrayList<>();

        this.redLight = redLight;
        this.blueLight = blueLight;

        this.counterRanger = counterRanger;
        cookiesCounter = 0;
        isReading = false;
    }

    /**
     * Calculates the speed (in RPM) of the conveyor belt based on readings from the ultrasonic ranger.
     */
    public void speedCalculator() {
        if(speedRanger.isValid()) {
            final double value = speedRanger.getValue();

            if(firstRotation) {
                initIntervalTime = System.currentTimeMillis();
                firstRotation = false;
            }

            if(value <= DEFAULT_DISTANCE) {
                if(isInLoop) {
                    long endTime = System.currentTimeMillis();
                    if (startTime != 0){
                        double calcRpm = 2 * 3.14 / ((endTime - startTime)/1000.0) * 9.549;
                        rpmList.add(calcRpm);
                    }

                    startTime = System.currentTimeMillis();
                    isInLoop = false;
                }
            } else {
                if(!isInLoop) {
                    isInLoop = true;
                }
            }

            long updateTime = System.currentTimeMillis() - initIntervalTime;
            if(updateTime >= 5_000) {
                if(!rpmList.isEmpty()) {
                    rpm = Collections.max(rpmList);
                    speed = rpm > 30 ? Speed.FAST : Speed.SLOW;
                    speedSignal = true;
                    firstRotation = true;
                    rpmList.clear();
                }
            }
        }
    }

    /**
     * Detects whether a cookie is crossing the path of the counter ranger.
     *
     * @return true if a cookie is detected crossing, false otherwise
     */
    public boolean isCookieCrossing() {
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

    /**
     * Checks if the speed signal is ready to be read.
     *
     * @return true if the speed signal is ready, false otherwise
     */
    public boolean isSignalReady() {
        if(speedSignal) {
            speedSignal = false;
            return true;
        }
        return false;
    }

    /**
     * Blinks the LED lights based on the current speed of the conveyor.
     *
     * @throws IOException if an I/O error occurs
     */
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

    /**
     * Gets the current RPM (revolutions per minute) of the conveyor.
     *
     * @return the current RPM
     */
    public double getRpm() {
        return rpm;
    }

    /**
     * Gets the current speed of the conveyor as a string.
     *
     * @return the current speed
     */
    public String getSpeed() {
        return speed.toString();
    }

    /**
     * Gets the total count of cookies that have crossed the counter ranger.
     *
     * @return the total cookie count
     */
    public int getCookiesCounter() {
        return cookiesCounter;
    }
}