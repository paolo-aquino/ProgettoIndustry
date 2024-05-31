package org.example.MyLibrary;

import org.iot.raspberry.grovepi.sensors.synch.SensorMonitor;
import org.iot.raspberry.grovepi.sensors.synch.SensorValueSupplier;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LedMonitor extends SensorMonitor<Void> implements SupsiMonitor<Void>{
    private boolean running;
    private final long readInterval;
    private Exception error;
    private boolean stopOnError = true;
    private Thread thread;

    private final SupsiLed led;

    public LedMonitor(SensorValueSupplier<Void> sensor, long readInterval) {
        super(sensor, readInterval);
        this.readInterval = readInterval;
        this.led = (SupsiLed) sensor;
    }

    @Override
    public synchronized boolean start() {
        if (!running) {
            running= true;
            thread = new Thread(this::run);
            thread.start();
        }

        return running;
    }

    private void run() {
        while(running) {
            try {
                led.setBlink();

                if(led.getBlink())
                    led.On();
                else
                    led.Off();

                TimeUnit.MILLISECONDS.sleep(readInterval);
            } catch (InterruptedException | IOException e) {
                Logger.getLogger(SensorMonitor.class.getName()).log(Level.SEVERE, null, e);
                error = e;
                running = stopOnError;
            }
        }
    }

    public void stop() {
        thread.interrupt();
        this.running = false;
    }

    @Override
    public SensorMonitor<Void> getSensorMonitor() {
        return null;
    }

    @Override
    public Void getValidValue() {
        return SupsiMonitor.super.getValidValue();
    }

    @Override
    public Exception getError() {
        return error;
    }

    @Override
    public boolean isStopOnError() {
        return stopOnError;
    }

    @Override
    public void setStopOnError(boolean stopOnError) {
        this.stopOnError = stopOnError;
    }
}