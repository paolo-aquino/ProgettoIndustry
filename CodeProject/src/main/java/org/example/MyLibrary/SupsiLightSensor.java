package org.example.MyLibrary;

import org.iot.raspberry.grovepi.GrovePi;
import org.iot.raspberry.grovepi.sensors.analog.GroveLightSensor;
import org.iot.raspberry.grovepi.sensors.synch.SensorMonitor;

import java.io.IOException;

/**
 * A custom light sensor implementation that extends GroveLightSensor and implements SupsiMonitor interface.
 * This sensor monitors light intensity asynchronously.
 */
public class SupsiLightSensor extends GroveLightSensor implements SupsiMonitor<Double> {
    final private SensorMonitor<Double> lightMonitor;

    /**
     * Constructs a SupsiLightSensor object with the specified GrovePi instance, pin number, and read interval.
     * @param grovePi The GrovePi instance to use for communication.
     * @param pin The pin number to which the sensor is connected.
     * @param readInterval The interval (in milliseconds) at which the sensor reads light intensity.
     * @throws IOException If an I/O error occurs during sensor initialization.
     */
    public SupsiLightSensor(GrovePi grovePi, int pin, long readInterval) throws IOException {
        super(grovePi, pin);
        lightMonitor = new SensorMonitor<>(this,readInterval);
        lightMonitor.start();
    }

    /**
     * Constructs a SupsiLightSensor object with the specified GrovePi instance and pin number.
     * Uses a default read interval of 100 milliseconds.
     * @param grovePi The GrovePi instance to use for communication.
     * @param pin The pin number to which the sensor is connected.
     * @throws IOException If an I/O error occurs during sensor initialization.
     */
    public SupsiLightSensor(GrovePi grovePi, int pin) throws IOException {
        this(grovePi, pin, 100);
    }

    /**
     * Retrieves the SensorMonitor associated with this light sensor.
     * @return The SensorMonitor instance monitoring this light sensor.
     */
    @Override
    public SensorMonitor<Double> getSensorMonitor() {
        return lightMonitor;
    }

    /**
     * Returns a string representation of this light sensor.
     * Delegates to the toString() method of the underlying SensorMonitor.
     * @return A string representation of this light sensor.
     */
    @Override
    public String toString(){
        return lightMonitor.toString();
    }
}
