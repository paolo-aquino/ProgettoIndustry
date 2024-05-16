package org.example.MainProject;

import org.example.MyLibrary.*;
import org.iot.raspberry.grovepi.GrovePi;
import org.iot.raspberry.grovepi.pi4j.GrovePi4J;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CookiesFactory {
    private static boolean acquisitionON = true;
    final private static List<SupsiMonitor<?>> sensors = new ArrayList<>();

    private static void addSensorMonitored(SupsiMonitor<?> sensorMonitor){
        sensors.add(sensorMonitor);
    }

    /**
     * All sensors in list will stop monitoring
     */
    private static void stopAll(){
        for (SupsiMonitor<?> sensor : sensors)
            sensor.stop();
    }

    public static void main(String[] args) throws Exception {
        Logger.getLogger("GrovePi").setLevel(Level.WARNING);
        Logger.getLogger("RaspberryPi").setLevel(Level.WARNING);

        GrovePi grovePi = new GrovePi4J(); // --> raspberry;
        SupsiRgbLcd lcd = new SupsiRgbLcd();

        while(true){
            //TODO aggiungi codice
        }

    }
}