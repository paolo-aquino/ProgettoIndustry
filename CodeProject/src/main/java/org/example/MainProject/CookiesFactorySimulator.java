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
import java.util.logging.Level;
import java.util.logging.Logger;

public class CookiesFactorySimulator {
    private static boolean acquisitionON = true;

    public static final String BUCKET = "grafana_test";
    public static final String ORG = "SUPSI";
    private static final String TOKEN = "xBkf3KGBhgbujwgKbkSof-l1JStrM17j2-fxq1wETteFec0OmAm66sd1g4q-7BmEK8PUphHc6E9ciAGDqwMB1g==";
    private static final InfluxDBClient CLIENT = InfluxDBClientFactory.create("http://169.254.193.89:8086", TOKEN.toCharArray());
    public static final WriteApiBlocking WRITE_API = CLIENT.getWriteApiBlocking();


    public static void main(String[] args) throws Exception {
        Logger.getLogger("GrovePi").setLevel(Level.OFF);
        Logger.getLogger("RaspberryPi").setLevel(Level.OFF);

        // Raspberry - Gp1
        GrovePi grovePi = new GrovePi4J();

        // LCD display - I2C port
        SupsiRgbLcd lcd = new SupsiRgbLcd();

        // Leds - Red and Blue - D2 and D3 ports
        SupsiLed redLight = new SupsiLed(grovePi, 2);
        SupsiLed blueLight = new SupsiLed(grovePi, 3);

        // Ultrasonic ranger - D4 port
        SupsiUltrasonicRanger speedRanger = new SupsiUltrasonicRanger(grovePi, 4);

        // Ultrasonic ranger - D5 port
        SupsiUltrasonicRanger counterRanger = new SupsiUltrasonicRanger(grovePi, 5);

        // Button - D6 port
        SupsiButton button = new SupsiButton(grovePi, 6);

        // Factory Object
        CookiesFactory factory = new CookiesFactory(lcd, redLight, blueLight, speedRanger, counterRanger, button);

        factory.showMessage("Welcome To our Cookies Factory!");

        while(true) {
            // Calculates the speed
            factory.speedCalculator();
            if(factory.isSpeedSignalReady()) {
                Point speedPoint = Point.measurement("conveyor_speed").addField("speed", factory.getConveyorRPM())
                        .addTag("speed_cat", factory.getConveyorSpeed()).time(Instant.now(), WritePrecision.MS);
                WRITE_API.writePoint(BUCKET, ORG, speedPoint);
            }

            // BlinkLED
            factory.ledBlink();

            if(factory.isOvenSignalReady()) {
                Point oven = Point.measurement("oven_door").addField("is_okay", factory.isOvenWorking()).time(Instant.now(), WritePrecision.MS);
                WRITE_API.writePoint(BUCKET, ORG, oven);
            }

            factory.showStats();

        }

    }
}