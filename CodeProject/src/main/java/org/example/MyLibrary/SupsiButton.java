package org.example.MyLibrary;

import org.iot.raspberry.grovepi.GrovePi;
import org.iot.raspberry.grovepi.sensors.digital.GroveButton;
import org.iot.raspberry.grovepi.sensors.listener.GroveButtonListener;
import org.iot.raspberry.grovepi.sensors.synch.SensorMonitor;

import java.io.IOException;

/**
 * Class representing a button with monitoring capabilities.
 * <p>
 * This class extends the GroveButton class to include sensor monitoring functionality.
 *
 * @author Paolo Aquino
 * @author Zeno Darani
 * @author Matteo Cazzani
 */
public final class SupsiButton extends GroveButton implements SupsiMonitor<Boolean> {

    // Default response time for the button monitor
    private final static long RESPONSE_TIME = 100;

    // Sensor monitor for the button
    private final SensorMonitor<Boolean> buttonMonitor;

    /**
     * Constructor for the SupsiButton class with a specified read interval.
     *
     * @param grovePi the GrovePi instance
     * @param pin the pin to which the button is connected
     * @param readInterval the interval at which the button state is read
     * @throws IOException if an I/O error occurs
     */
    public SupsiButton(final GrovePi grovePi, final int pin, final long readInterval) throws IOException {
        super(grovePi, pin);

        // Initialize the button monitor with the specified read interval
        buttonMonitor = new SensorMonitor<>(this, readInterval);

        // Start the button monitor
        buttonMonitor.start();
    }

    /**
     * Constructor for the SupsiButton class with the default response time.
     *
     * @param grovePi the GrovePi instance
     * @param pin the pin to which the button is connected
     * @throws IOException if an I/O error occurs
     */
    public SupsiButton(final GrovePi grovePi, final int pin) throws IOException {
        this(grovePi, pin, RESPONSE_TIME);
    }

    /**
     * Sets the button listener to handle button events.
     *
     * @param groveButtonListener the listener for button events
     */
    @Override
    public void setButtonListener(final GroveButtonListener groveButtonListener) {
        super.setButtonListener(groveButtonListener);
    }


    @Override
    public SensorMonitor<Boolean> getSensorMonitor() {
        return buttonMonitor;
    }
}