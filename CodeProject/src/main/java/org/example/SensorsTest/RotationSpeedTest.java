package org.example.SensorsTest;

import org.iot.raspberry.grovepi.GrovePi;
import org.iot.raspberry.grovepi.pi4j.GrovePi4J;
import org.iot.raspberry.grovepi.sensors.digital.GroveUltrasonicRanger;
import org.iot.raspberry.grovepi.sensors.synch.SensorMonitor;

import java.util.logging.Level;
import java.util.logging.Logger;

public class RotationSpeedTest {

    public static int RANGER_PIN = 6;
    public static int ACTIVATION_RANGE = 5;


    public static void main(String[] args) throws Exception{
        Logger.getLogger("GrovePi").setLevel(Level.OFF);
        Logger.getLogger("RaspberryPi").setLevel(Level.OFF);

        GrovePi grovepi = new GrovePi4J();

        GroveUltrasonicRanger ranger = new GroveUltrasonicRanger(grovepi, RANGER_PIN);
        SensorMonitor<Double> rangerMonitor = new SensorMonitor<>(ranger, 100);

        double speed = 0; // velocità in rad/s
        long startTime = 0;
        long endTime = 0;
        boolean isInLoop = true;

        rangerMonitor.start();
        while(true){

            if (rangerMonitor.isValid()){
                double value = rangerMonitor.getValue();

                if (value <= ACTIVATION_RANGE){

                    if (isInLoop){
                        System.out.println("Calculating");
                        endTime = System.currentTimeMillis();
                        if (startTime != 0){
                            speed = 2 * 3.14 / ((endTime - startTime)/1000.0);
                        }
                        startTime = System.currentTimeMillis();
                        isInLoop = false;
                    }

                }else{
                    if (!isInLoop){
                        isInLoop = true;
                    }
                }

                System.out.println("RPM: " + (speed * 9.549));
            }



        }

    }
}
