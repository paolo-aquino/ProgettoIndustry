package org.example.MyLibrary;

import org.iot.raspberry.grovepi.GrovePi;
import org.iot.raspberry.grovepi.sensors.digital.GroveButton;
import org.iot.raspberry.grovepi.sensors.listener.GroveButtonListener;
import org.iot.raspberry.grovepi.sensors.synch.SensorMonitor;

import java.io.IOException;
import java.util.Objects;

public class SupsiButton extends GroveButton implements SupsiMonitor<Boolean> {
    public final static long RESPONSE_TIME = 100;
    private final static TypePort PORT = TypePort.DIGITAL;

    private final int sensorID;
    private final SensorMonitor<Boolean> buttonMonitor;

    public SupsiButton(GrovePi grovePi, int pin, long readInterval) throws IOException {
        super(grovePi, pin);

        sensorID = pin;
        buttonMonitor = new SensorMonitor<>(this, readInterval);

        buttonMonitor.start();
    }

    public SupsiButton(GrovePi grovePi, int pin) throws IOException{
        this(grovePi, pin, RESPONSE_TIME);
    }

    @Override
    public void setButtonListener(GroveButtonListener groveButtonListener) {
        super.setButtonListener(groveButtonListener);
    }

    @Override
    public SensorMonitor<Boolean> getSensorMonitor() {
        return buttonMonitor;
    }

    @Override
    public int getSensorID() {
        return sensorID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        SupsiButton that = (SupsiButton) o;

        return sensorID == that.sensorID && Objects.equals(buttonMonitor, that.buttonMonitor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sensorID, buttonMonitor, PORT);
    }
}