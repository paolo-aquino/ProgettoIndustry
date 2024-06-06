package org.example.MyLibrary;

import org.iot.raspberry.grovepi.GrovePi;
import org.iot.raspberry.grovepi.sensors.digital.GroveLed;
import org.iot.raspberry.grovepi.sensors.synch.SensorMonitor;
import org.iot.raspberry.grovepi.sensors.synch.SensorValueSupplier;

import java.io.IOException;
import java.util.Objects;

public class SupsiLed extends GroveLed {
    private final static TypePort PORT = TypePort.DIGITAL;

    private final int sensorID;

    private boolean isOn;
    private long startTime;

    public SupsiLed(GrovePi grovePi, int pin, long readInterval) throws IOException {
        super(grovePi, pin);
        sensorID = pin;
        isOn = false;
    }

    public SupsiLed(GrovePi grovePi, int pin) throws IOException {
        this(grovePi, pin, 500);
    }

    public void on() throws IOException {
        set(255);
    }

    public void off() throws IOException {
        set(0);
    }

    public void blink() throws IOException {
        long time = System.currentTimeMillis() - startTime;

        if(time > 1000 && isOn) {
            off();
            isOn = false;
        } else if (time > 1000) {
            on();
            isOn = true;
        }

        startTime = System.currentTimeMillis();
    }

    public boolean getOn() {
        return isOn;
    }

}