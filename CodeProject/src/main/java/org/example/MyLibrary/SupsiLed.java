package org.example.MyLibrary;

import org.iot.raspberry.grovepi.GrovePi;
import org.iot.raspberry.grovepi.sensors.digital.GroveLed;

import java.io.IOException;

public class SupsiLed extends GroveLed {
    private final static TypePort PORT = TypePort.DIGITAL;

    private final int sensorID;

    private boolean isOn;
    private long startTime;

    public SupsiLed(GrovePi grovePi, int pin, long readInterval) throws IOException {
        super(grovePi, pin);
        sensorID = pin;
        isOn = false;
        startTime = System.currentTimeMillis();
    }

    public SupsiLed(GrovePi grovePi, int pin) throws IOException {
        this(grovePi, pin, 500);
    }

    public void on() throws IOException {
        set(255);
        isOn = true;
    }

    public void off() throws IOException {
        if(isOn) {
            set(0);
            isOn = false;
        }
    }

    public void blink() throws IOException {
        long time = System.currentTimeMillis() - startTime;

        if(time >= 1000) {
            if(isOn) {
                off();
            } else {
                on();
            }
            startTime = System.currentTimeMillis();
        }
    }
}