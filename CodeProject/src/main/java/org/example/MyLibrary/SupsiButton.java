package org.example.MyLibrary;

import org.iot.raspberry.grovepi.GrovePi;
import org.iot.raspberry.grovepi.sensors.digital.GroveButton;
import org.iot.raspberry.grovepi.sensors.listener.GroveButtonListener;
import org.iot.raspberry.grovepi.sensors.synch.SensorMonitor;

import java.io.IOException;

public class SupsiButton extends GroveButton implements SupsiMonitor<Boolean> {
    public final static long RESPONSE_TIME = 100;
    private final SensorMonitor<Boolean> buttonMonitor;


    public SupsiButton(GrovePi grovePi, int pin, long readInterval, GroveButtonListener groveButtonListener) throws IOException {
        super(grovePi, pin);

        buttonMonitor = new SensorMonitor<>(this, readInterval);

        setButtonListener(groveButtonListener);

        buttonMonitor.start();
    }

    public SupsiButton(GrovePi grovePi, int pin, GroveButtonListener groveButtonListener) throws IOException{
        this(grovePi, pin, RESPONSE_TIME, groveButtonListener);
    }

    @Override
    public SensorMonitor<Boolean> getSensorMonitor() {
        return buttonMonitor;
    }
}