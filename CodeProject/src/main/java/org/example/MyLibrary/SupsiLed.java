package org.example.MyLibrary;

import org.iot.raspberry.grovepi.GrovePi;
import org.iot.raspberry.grovepi.sensors.digital.GroveLed;
import org.iot.raspberry.grovepi.sensors.synch.SensorMonitor;
import org.iot.raspberry.grovepi.sensors.synch.SensorValueSupplier;

import java.io.IOException;

public class SupsiLed extends GroveLed implements SensorValueSupplier<Void>, SupsiMonitor<Void>{
    private final LedMonitor ledMonitor;
    private boolean blink;
    private boolean firstAccess;

    public SupsiLed(GrovePi grovePi, int pin, long readInterval) throws IOException {
        super(grovePi, pin);
        ledMonitor = new LedMonitor(this, readInterval);
        blink = false;
        firstAccess = true;
    }

    public SupsiLed(GrovePi grovePi, int pin) throws IOException {
        this(grovePi, pin, 500);
    }

    public void On() throws IOException {
        set(255);
    }

    public void Off() throws IOException {
        set(0);
    }

    public void startToggle() throws IOException {
        ledMonitor.start();
    }

    public void stopToggle() throws IOException {
        if (firstAccess) {
            firstAccess = false;
            return;
        }

        ledMonitor.stop();
    }

    public boolean getBlink() {
        return blink;
    }

    public void setBlink() {
        blink = !blink;
    }

    @Override
    public SensorMonitor getSensorMonitor() {
        return null;
    }

    @Override
    public Void get() throws Exception {
        return null;
    }
}