package org.example.MyLibrary;

import org.iot.raspberry.grovepi.GrovePi;
import org.iot.raspberry.grovepi.sensors.digital.GroveLed;

import java.io.IOException;

public final class SupsiLed extends GroveLed {
    private final static long RESPONSE_TIME = 500;

    private boolean isOn;
    private long startTime;

    public SupsiLed(final GrovePi grovePi, final int pin, final long readInterval) throws IOException {
        super(grovePi, pin);
        isOn = false;
        startTime = System.currentTimeMillis();
    }

    public SupsiLed(final GrovePi grovePi, final int pin) throws IOException {
        this(grovePi, pin, RESPONSE_TIME);
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

        if(time >= 1_500) {
            if(isOn) {
                off();
            } else {
                on();
            }
            startTime = System.currentTimeMillis();
        }
    }
}