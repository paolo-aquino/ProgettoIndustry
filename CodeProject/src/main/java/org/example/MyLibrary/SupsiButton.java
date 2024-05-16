package org.example.MyLibrary;

import org.iot.raspberry.grovepi.GrovePi;
import org.iot.raspberry.grovepi.sensors.digital.GroveButton;
import org.iot.raspberry.grovepi.sensors.listener.GroveButtonListener;
import org.iot.raspberry.grovepi.sensors.synch.SensorMonitor;

import java.io.IOException;

public class SupsiButton extends GroveButton implements SupsiMonitor<Boolean> {
    public final static long RESPONSE_TIME = 100;
    private final SensorMonitor<Boolean> buttonMonitor;


    public SupsiButton(GrovePi grovePi, int pin, long readInterval) throws IOException {
        super(grovePi, pin);

        buttonMonitor = new SensorMonitor<>(this, readInterval);

        setButtonListener(new GroveButtonListener() {
            @Override
            public void onRelease() {

            }

            @Override
            public void onPress() {

            }

            @Override
            public void onClick() {

            }
        });

        buttonMonitor.start();

    }

    public SupsiButton(GrovePi grovePi, int pin) throws IOException{
        this(grovePi, pin, RESPONSE_TIME);
    }

    @Override
    public SensorMonitor<Boolean> getSensorMonitor() {
        return buttonMonitor;
    }
}