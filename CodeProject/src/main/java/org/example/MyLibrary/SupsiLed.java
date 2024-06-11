package org.example.MyLibrary;

import org.iot.raspberry.grovepi.GrovePi;
import org.iot.raspberry.grovepi.sensors.digital.GroveLed;

import java.io.IOException;

/**
 * Class representing an LED with additional functionality.
 * <p>
 * This class extends the GroveLed class to include methods for turning the LED on, off, and blinking.
 *
 * @author Paolo Aquino
 * @author Zeno Darani
 * @author Matteo Cazzani
 */
public final class SupsiLed extends GroveLed {

    // Indicates if the LED is currently on
    private boolean isOn;

    // Tracks the start time for blink operations
    private long startTime;

    /**
     * Constructor for the SupsiLed class.
     *
     * @param grovePi the GrovePi instance
     * @param pin the pin to which the LED is connected
     * @throws IOException if an I/O error occurs
     */
    public SupsiLed(final GrovePi grovePi, final int pin) throws IOException {
        super(grovePi, pin);
        isOn = false;
        startTime = System.currentTimeMillis();
    }

    /**
     * Turns the LED on by setting its brightness to the maximum value.
     *
     * @throws IOException if an I/O error occurs
     */
    public void on() throws IOException {
        set(255);
        isOn = true;
    }


    /**
     * Turns the LED off by setting its brightness to zero.
     *
     * @throws IOException if an I/O error occurs
     */
    public void off() throws IOException {
        if(isOn) {
            set(0);
            isOn = false;
        }
    }

    /**
     * Blinks the LED with a predefined interval.
     *
     * The LED toggles its state (on/off) every 1.5 seconds.
     *
     * @throws IOException if an I/O error occurs
     */
    public void blink() throws IOException {
        long time = System.currentTimeMillis() - startTime;

        if(time >= 1_500) {
            if(isOn) {
                off();
            } else {
                on();
            }

            // Reset the start time for the next blink interval
            startTime = System.currentTimeMillis();
        }
    }
}