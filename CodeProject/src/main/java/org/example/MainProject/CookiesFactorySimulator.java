package org.example.MainProject;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import org.example.MyLibrary.*;
import org.iot.raspberry.grovepi.GrovePi;
import org.iot.raspberry.grovepi.pi4j.GrovePi4J;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CookiesFactorySimulator {
    private static boolean acquisitionON = true;

    public static final String BUCKET = "grafana_test";
    public static final String ORG = "SUPSI";
    private static final String TOKEN = "xBkf3KGBhgbujwgKbkSof-l1JStrM17j2-fxq1wETteFec0OmAm66sd1g4q-7BmEK8PUphHc6E9ciAGDqwMB1g==";
    private static final InfluxDBClient CLIENT = InfluxDBClientFactory.create("http://169.254.193.89:8086", TOKEN.toCharArray());
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
        Logger.getLogger("GrovePi").setLevel(Level.OFF);
        Logger.getLogger("RaspberryPi").setLevel(Level.OFF);

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
            factory.ledToggle();

            // Calculate speed and send the Point to influxdb
            factory.speedCalculator(speedRanger);

            factory.isOvenWorking();

        }

    }
}