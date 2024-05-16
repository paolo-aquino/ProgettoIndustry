package org.example.MyLibrary;

import org.iot.raspberry.grovepi.GrovePi;
import org.iot.raspberry.grovepi.sensors.analog.GroveRotarySensor;
import org.iot.raspberry.grovepi.sensors.data.GroveRotaryValue;
import org.iot.raspberry.grovepi.sensors.synch.SensorMonitor;

import java.io.IOException;

/**
 * A custom rotary sensor implementation that extends GroveRotarySensor and implements SupsiMonitor interface.
 * This sensor monitors rotary values asynchronously.
 */
public class SupsiRotarySensor extends GroveRotarySensor implements SupsiMonitor<GroveRotaryValue> {
    private final SensorMonitor<GroveRotaryValue> rotarySensorMonitor;

    /**
     * Constructs a SupsiRotarySensor object with the specified GrovePi instance, pin number, and read interval.
     * @param grovePi The GrovePi instance to use for communication.
     * @param pin The pin number to which the sensor is connected.
     * @param readInterval The interval (in milliseconds) at which the sensor reads rotary values.
     * @throws IOException If an I/O error occurs during sensor initialization.
     */
    public SupsiRotarySensor(GrovePi grovePi, int pin, long readInterval) throws IOException {
        super(grovePi, pin);
        rotarySensorMonitor = new SensorMonitor<>(this, readInterval);
        rotarySensorMonitor.start();
    }

    /**
     * Constructs a SupsiRotarySensor object with the specified GrovePi instance and pin number.
     * Uses a default read interval of 100 milliseconds.
     * @param grovePi The GrovePi instance to use for communication.
     * @param pin The pin number to which the sensor is connected.
     * @throws IOException If an I/O error occurs during sensor initialization.
     */
    public SupsiRotarySensor(GrovePi grovePi, int pin) throws IOException {
        this(grovePi, pin, 100);
    }

    /**
     * Retrieves the SensorMonitor associated with this rotary sensor.
     * @return The SensorMonitor instance monitoring this rotary sensor.
     */
    @Override
    public SensorMonitor<GroveRotaryValue> getSensorMonitor() {
        return rotarySensorMonitor;
    }

    /**
     * Retrieves the sensor value (rotation angle) in degrees.
     * @return The sensor value in degrees.
     */
    public double getSensorValue() {
        return getValue().getSensorValue();
    }

    /**
     * Retrieves the voltage value of the sensor.
     * @return The voltage value.
     */
    public double getVoltage() {
        return getValue().getVoltage();
    }

    /**
     * Retrieves the rotation angle of the sensor in degrees.
     * @return The rotation angle in degrees.
     */
    public double getDegrees() {
        return getValue().getDegrees();
    }

    /**
     * Returns a string representation of this rotary sensor.
     * Delegates to the toString() method of the underlying SensorMonitor.
     * @return A string representation of this rotary sensor.
     */
    @Override
    public String toString() {
        return rotarySensorMonitor.toString();
    }
}
