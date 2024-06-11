package org.example.MyLibrary;

import org.iot.raspberry.grovepi.GrovePi;
import org.iot.raspberry.grovepi.sensors.digital.GroveUltrasonicRanger;
import org.iot.raspberry.grovepi.sensors.synch.SensorMonitor;

/**
 * A custom ultrasonic ranger sensor implementation that extends GroveUltrasonicRanger and implements SupsiMonitor interface.
 * This sensor monitors distance asynchronously.
 */
public final class SupsiUltrasonicRanger extends GroveUltrasonicRanger implements SupsiMonitor<Double> {
    private final SensorMonitor<Double> rangerMonitor;

    /**
     * Constructs a SupsiUltrasonicRanger object with the specified GrovePi instance, pin number, and read interval.
     * @param grovePi The GrovePi instance to use for communication.
     * @param pin The pin number to which the sensor is connected.
     * @param readInterval The interval (in milliseconds) at which the sensor reads distance.
     */
    public SupsiUltrasonicRanger(final GrovePi grovePi, final int pin, final long readInterval) {
        super(grovePi, pin);
        rangerMonitor = new SensorMonitor<>(this, readInterval);
        rangerMonitor.start();
    }

    /**
     * Constructs a SupsiUltrasonicRanger object with the specified GrovePi instance and pin number.
     * Uses a default read interval of 100 milliseconds.
     * @param grovePi The GrovePi instance to use for communication.
     * @param pin The pin number to which the sensor is connected.
     */
    public SupsiUltrasonicRanger(final GrovePi grovePi, final int pin) {
        this(grovePi, pin, 10);
    }

    /**
     * Retrieves the SensorMonitor associated with this ultrasonic ranger sensor.
     * @return The SensorMonitor instance monitoring this ultrasonic ranger sensor.
     */
    @Override
    public SensorMonitor<Double> getSensorMonitor() {
        return rangerMonitor;
    }

    /**
     * Returns a string representation of this ultrasonic ranger sensor.
     * Delegates to the toString() method of the underlying SensorMonitor.
     * @return A string representation of this ultrasonic ranger sensor.
     */
    @Override
    public String toString() {
        return rangerMonitor.toString();
    }

}
