package org.example.MainProject;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApiBlocking;
import org.example.MyLibrary.*;
import org.iot.raspberry.grovepi.GrovePi;
import org.iot.raspberry.grovepi.pi4j.GrovePi4J;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CookiesFactorySimulator {
    private static boolean acquisitionON = true;

    public static final String BUCKET = "Grafana";
    public static final String ORG = "Supsi";
    private static final String TOKEN = "b21xsGBINmFAwno-66qgvc7EgfSTQbMRoEvjRCW-Z39XNa4mTJLwjeaGNJ1tj5snuPgkhwGkAMgSIDxHjkSb2Q==";
    private static final InfluxDBClient CLIENT = InfluxDBClientFactory.create("http://169.254.201.100:8086", TOKEN.toCharArray());
    public static final WriteApiBlocking WRITE_API = CLIENT.getWriteApiBlocking();

    private static final List<SupsiMonitor<?>> sensors = new ArrayList<>();

    private static void addSensorMonitored(SupsiMonitor<?> sensorMonitor) {
        sensors.add(sensorMonitor);
    }

    /**
     * All sensors in list will stop monitoring
     */
    private static void stopAll() {
        for (SupsiMonitor<?> sensor : sensors)
            sensor.stop();
    }

    public static void main(String[] args) throws Exception {
        Logger.getLogger("GrovePi").setLevel(Level.WARNING);
        Logger.getLogger("RaspberryPi").setLevel(Level.WARNING);

        // Raspberry - Gp1
        GrovePi grovePi = new GrovePi4J();

        // LCD display - I2C port
        @SuppressWarnings("resource")
        SupsiRgbLcd lcd = new SupsiRgbLcd();

        // Leds - Red and Blue - D1 and D2 ports
        SupsiLed redLight = new SupsiLed(grovePi, 2);
        SupsiLed blueLight = new SupsiLed(grovePi, 3);

        // Factory Object
        CookiesFactory factory = new CookiesFactory(redLight, blueLight);

        // Ultrasonic ranger - D7 port
        SupsiUltrasonicRanger speedRanger = new SupsiUltrasonicRanger(grovePi, 4);

        // Button - D3 port - Seems like its never used, but trust me it is used!
        SupsiButton button = new SupsiButton(grovePi, 5, factory.getGroveButtonListener());

        while(true){

            // Button Listener calls the method onClick() in Oven class when pressed.

            // Toggle LED
            factory.ledToggle(); //TODO aggiustare il firstAccess

            // Calculate speed and send the Point to influxdb
            factory.speedCalculator(speedRanger);

        }

    }
}