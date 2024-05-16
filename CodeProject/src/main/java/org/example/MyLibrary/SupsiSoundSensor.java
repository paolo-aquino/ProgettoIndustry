package org.example.MyLibrary;

import org.iot.raspberry.grovepi.GrovePi;
import org.iot.raspberry.grovepi.sensors.analog.GroveSoundSensor;
import org.iot.raspberry.grovepi.sensors.synch.SensorMonitor;

import java.io.IOException;

/**
 * A custom sound sensor implementation that extends GroveSoundSensor and implements SupsiMonitor interface.
 * This sensor monitors sound intensity asynchronously.
 */
public class SupsiSoundSensor extends GroveSoundSensor implements SupsiMonitor<Double> {
    final private SensorMonitor<Double> soundMonitor;

    /**
     * Constructs a SupsiSoundSensor object with the specified GrovePi instance, pin number, and read interval.
     * @param grovePi The GrovePi instance to use for communication.
     * @param pin The pin number to which the sensor is connected.
     * @param readInterval The interval (in milliseconds) at which the sensor reads sound intensity.
     * @throws IOException If an I/O error occurs during sensor initialization.
     */
    public SupsiSoundSensor(GrovePi grovePi, int pin, long readInterval) throws IOException {
        super(grovePi, pin);
        soundMonitor = new SensorMonitor<>(this, readInterval);
        soundMonitor.start();
    }

    /**
     * Constructs a SupsiSoundSensor object with the specified GrovePi instance and pin number.
     * Uses a default read interval of 100 milliseconds.
     * @param grovePi The GrovePi instance to use for communication.
     * @param pin The pin number to which the sensor is connected.
     * @throws IOException If an I/O error occurs during sensor initialization.
     */
    public SupsiSoundSensor(GrovePi grovePi, int pin) throws IOException {
        this(grovePi, pin, 100);
    }

    /**
     * Retrieves the SensorMonitor associated with this sound sensor.
     * @return The SensorMonitor instance monitoring this sound sensor.
     */
    @Override
    public SensorMonitor<Double> getSensorMonitor() {
        return soundMonitor;
    }

    /**
     * Returns a string representation of this sound sensor.
     * Delegates to the toString() method of the underlying SensorMonitor.
     * @return A string representation of this sound sensor.
     */
    @Override
    public String toString(){
        return soundMonitor.toString();
    }
}
