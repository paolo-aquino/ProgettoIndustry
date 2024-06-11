package org.example.MyLibrary;

import org.iot.raspberry.grovepi.GrovePi;
import org.iot.raspberry.grovepi.sensors.digital.GroveButton;
import org.iot.raspberry.grovepi.sensors.listener.GroveButtonListener;
import org.iot.raspberry.grovepi.sensors.synch.SensorMonitor;

import java.io.IOException;

public final class SupsiButton extends GroveButton implements SupsiMonitor<Boolean> {
    private final static long RESPONSE_TIME = 100;

    private final SensorMonitor<Boolean> buttonMonitor;

    public SupsiButton(final GrovePi grovePi, final int pin, final long readInterval) throws IOException {
        super(grovePi, pin);

        buttonMonitor = new SensorMonitor<>(this, readInterval);
        buttonMonitor.start();
    }

    public SupsiButton(final GrovePi grovePi, final int pin) throws IOException {
        this(grovePi, pin, RESPONSE_TIME);
    }

    @Override
    public void setButtonListener(final GroveButtonListener groveButtonListener) {
        super.setButtonListener(groveButtonListener);
    }

    @Override
    public SensorMonitor<Boolean> getSensorMonitor() {
        return buttonMonitor;
    }
}