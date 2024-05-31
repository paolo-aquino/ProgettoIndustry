package org.example.MyLibrary;

import org.iot.raspberry.grovepi.sensors.synch.SensorMonitor;

/**
 * An interface for monitoring sensor values asynchronously.
 * @param <R> The type of sensor value
 */
public interface SupsiMonitor<R> {
    /**
     * Retrieves the underlying SensorMonitor instance.
     * @return The SensorMonitor instance.
     */
    SensorMonitor<R> getSensorMonitor();

    /**
     * Checks if the current value is valid.
     * @return True if the value is valid, false otherwise.
     */
    default boolean isValid() {
        return getSensorMonitor().isValid();
    }

    /**
     * Retrieves the current sensor value.
     * @return The current sensor value.
     */
    default R getValue() {
        return getSensorMonitor().getValue();
    }

    /**
     * Checks if the current value is valid and retrieves it
     * @return The current sensor value or null
     */
    default R getValidValue() {
        if (isValid())
            return getValue();
        return null;
    }

    /**
     * Checks if the monitor is running.
     * @return True if the monitor is running, false otherwise.
     */
    default boolean isRunning() {
        return getSensorMonitor().isRunning();
    }

    /**
     * Retrieves the error occurred during monitoring.
     * @return The error occurred during monitoring, or null if no error occurred.
     */
    default Exception getError() {
        return getSensorMonitor().getError();
    }

    /**
     * Stops the sensor monitoring.
     */
    default void stop(){
        getSensorMonitor().stop();
    }

    /**
     * Checks if monitoring should stop on error.
     * @return True if monitoring should stop on error, false otherwise.
     */
    default boolean isStopOnError() {
        return getSensorMonitor().isStopOnError();
    }

    /**
     * Sets whether monitoring should stop on error.
     * @param stopOnError True if monitoring should stop on error, false otherwise.
     */
    default void setStopOnError(boolean stopOnError) {
        getSensorMonitor().setStopOnError(stopOnError);
    }
}
