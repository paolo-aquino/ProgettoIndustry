package org.example.SensorsTest;

import org.iot.raspberry.grovepi.GrovePi;
import org.iot.raspberry.grovepi.pi4j.GrovePi4J;
import org.iot.raspberry.grovepi.sensors.digital.GroveUltrasonicRanger;
import org.iot.raspberry.grovepi.sensors.synch.SensorMonitor;

import java.util.logging.Level;
import java.util.logging.Logger;

public class RangerCounterTest {

    public static int RANGER_PIN = 3;
    public static int ACTIVATION_RANGE = 2;


    public static void main(String[] args) throws Exception {
        Logger.getLogger("GrovePi").setLevel(Level.SEVERE);
        Logger.getLogger("RaspberryPi").setLevel(Level.SEVERE);

        GrovePi grovepi = new GrovePi4J();

        GroveUltrasonicRanger ranger = new GroveUltrasonicRanger(grovepi, RANGER_PIN);
        SensorMonitor<Double> rangerMonitor = new SensorMonitor<>(ranger, 10);

        rangerMonitor.start();
        while(true){
            if (rangerMonitor.isValid()){
                double value = rangerMonitor.getValue();
                System.out.println(value);

                if (value <= ACTIVATION_RANGE){
                    System.out.println("ACTIVATED\nACTIVATED\nACTIVATED");
                }
            }
        }
    }
}
