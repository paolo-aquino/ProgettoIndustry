package org.example.MyLibrary;

import org.iot.raspberry.grovepi.GrovePi;
import org.iot.raspberry.grovepi.sensors.data.GroveTemperatureAndHumidityValue;
import org.iot.raspberry.grovepi.sensors.digital.GroveTemperatureAndHumiditySensor;
import org.iot.raspberry.grovepi.sensors.synch.SensorMonitor;

/**
 * A custom temperature and humidity sensor implementation that extends GroveTemperatureAndHumiditySensor and implements SupsiMonitor interface.
 * This sensor monitors temperature and humidity asynchronously.
 */
public class SupsiTemperatureAndHumiditySensor extends GroveTemperatureAndHumiditySensor implements SupsiMonitor<GroveTemperatureAndHumidityValue> {
    private final SensorMonitor<GroveTemperatureAndHumidityValue> gthMonitor;

    /**
     * Constructs a SupsiTemperatureAndHumiditySensor object with the specified GrovePi instance, pin number, DHT type, and read interval.
     * @param grovePi The GrovePi instance to use for communication.
     * @param pin The pin number to which the sensor is connected.
     * @param dhtType The type of DHT sensor being used (DHT11 or DHT22).
     * @param readInterval The interval (in milliseconds) at which the sensor reads temperature and humidity.
     */
    public SupsiTemperatureAndHumiditySensor(GrovePi grovePi, int pin, Type dhtType, long readInterval) {
        super(grovePi, pin, dhtType);
        gthMonitor = new SensorMonitor<>(this, readInterval);
        gthMonitor.start();
    }

    /**
     * Constructs a SupsiTemperatureAndHumiditySensor object with the specified GrovePi instance, pin number, and DHT type.
     * Uses a default read interval of 1000 milliseconds.
     * @param grovePi The GrovePi instance to use for communication.
     * @param pin The pin number to which the sensor is connected.
     * @param dhtType The type of DHT sensor being used (DHT11 or DHT22).
     */
    public SupsiTemperatureAndHumiditySensor(GrovePi grovePi, int pin, Type dhtType) {
        this(grovePi, pin, dhtType, 1000);
    }

    /**
     * Retrieves the SensorMonitor associated with this temperature and humidity sensor.
     * @return The SensorMonitor instance monitoring this temperature and humidity sensor.
     */
    @Override
    public SensorMonitor<GroveTemperatureAndHumidityValue> getSensorMonitor() {
        return gthMonitor;
    }

    /**
     * Retrieves the current temperature reading from the sensor.
     * @return The current temperature value.
     */
    public double getTemperature() {
        return getValue().getTemperature();
    }

    /**
     * Retrieves the current humidity reading from the sensor.
     * Note: This method is functional for humidity values between 20% and 90%.
     * @return The current humidity value.
     */
    public double getHumidity() {
        return getValue().getHumidity();
    }

    /**
     * Returns a string representation of this temperature and humidity sensor.
     * Delegates to the toString() method of the underlying SensorMonitor.
     * @return A string representation of this temperature and humidity sensor.
     */
    @Override
    public String toString() {
        return gthMonitor.toString();
    }
}
