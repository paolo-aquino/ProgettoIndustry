package org.example.SensorsTest;

import org.iot.raspberry.grovepi.GrovePi;
import org.iot.raspberry.grovepi.pi4j.GrovePi4J;
import org.iot.raspberry.grovepi.sensors.digital.GroveButton;
import org.iot.raspberry.grovepi.sensors.listener.GroveButtonListener;
import org.iot.raspberry.grovepi.sensors.synch.SensorMonitor;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ButtonTest {

    public static int BUTTON_PIN = 2;

    public static void main(String[] args) throws Exception{
        Logger.getLogger("GrovePi").setLevel(Level.SEVERE);
        Logger.getLogger("RaspberryPi").setLevel(Level.SEVERE);



        GrovePi grovePi = new GrovePi4J();
        GroveButton button = new GroveButton(grovePi, BUTTON_PIN);
        SensorMonitor<Boolean> buttonMonitor = new SensorMonitor(button, 100);
        GroveButtonListener buttonListener = new GroveButtonListener() {
            int clicks = 0;

            @Override
            public void onRelease() {

            }

            @Override
            public void onPress() {

            }

            @Override
            public void onClick() {
                clicks += 1;
                System.out.println("Button Clicked " + clicks + " times.");
            }
        };
        button.setButtonListener(buttonListener);
        buttonMonitor.start();
    }
}
