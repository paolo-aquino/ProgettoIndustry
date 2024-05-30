package org.example.MainProject;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import org.example.MyLibrary.*;
import org.iot.raspberry.grovepi.GrovePi;
import org.iot.raspberry.grovepi.pi4j.GrovePi4J;

import com.influxdb.client.write.Point;
import org.iot.raspberry.grovepi.sensors.listener.GroveButtonListener;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

enum Speed {
    FAST("high_speed"),
    SLOW("low_speed");

    private final String name;

    Speed(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}

class CookiesFactory {

    //  WriteApi
    private final WriteApiBlocking writeApi;

    // Oven fields
    private final GroveButtonListener groveButtonListener;
    private boolean isButtonWorking;
    private int buttonCounter;
    private long clickTime;

    // Conveyor fields
    private static final int DEFAULT_DISTANCE = 10;
    private static final double RADIUS = 0.04;
    private static final double CIRCUMFERENCE = 2 * Math.PI * RADIUS;
    private static final double DEFAULT_SPEED = 0.5;
    private boolean firstRotation;
    private boolean readingValue;
    private long startTime;
    private long rpmTimeStart;
    private int rotations;
    private int rpm;
    private Speed speed;

    // LCD fields
    private long lcdStartTime;

    public CookiesFactory(final WriteApiBlocking writeApi) {
        this.writeApi = writeApi;

        isButtonWorking = true;
        buttonCounter = 0;
        clickTime = 0;

        groveButtonListener = new GroveButtonListener() {
            @Override
            public void onRelease() {

            }

            @Override
            public void onPress() {

            }

            @Override
            public void onClick() {
                buttonCounter++;

                clickTime = clickTime == 0 ? System.currentTimeMillis() : System.currentTimeMillis() - clickTime;

                if(clickTime < 10_000) {
                    isButtonWorking = true;
                    buttonCounter = 0;
                    clickTime = 0;
                } else if (buttonCounter % 2 != 0 && clickTime < 25_000) {
                    isButtonWorking = false;
                    clickTime = 0;
                } else if (clickTime < 25_000) {
                    clickTime = System.currentTimeMillis();

                    Point button = Point.measurement("oven_door").addField("is_okay", isButtonWorking).time(Instant.now(), WritePrecision.MS);
                    writeApi.writePoint(CookiesFactorySimulator.BUCKET, CookiesFactorySimulator.ORG, button);
                }

            }
        };

        firstRotation = true;
        readingValue = false;
        startTime = 0;
        rpmTimeStart = 0;
        rotations = 0;
        rpm = 0;
        speed = Speed.SLOW;
    }

    public void speedCalculator(final SupsiUltrasonicRanger ranger) {
        if(firstRotation) {
            if (ranger.isValid() && ranger.getValue() <= DEFAULT_DISTANCE) {
                readingValue = true;
            } else if (readingValue) {
                if(ranger.isValid() && ranger.getValue() > DEFAULT_DISTANCE) {
                    startTime = System.currentTimeMillis();
                    firstRotation = false;
                    readingValue = false;

                    rpmTimeStart = startTime;
                }
            }
        } else {
            if(ranger.isValid() && ranger.getValue() <= DEFAULT_DISTANCE) {
                readingValue = true;
            } else if (readingValue) {
                if(ranger.isValid() && ranger.getValue() > DEFAULT_DISTANCE) {
                    long endTime = System.currentTimeMillis();
                    long diff = endTime - startTime;

                    double time = diff /1000.0;
                    speed = (CIRCUMFERENCE/time) > DEFAULT_SPEED ? Speed.SLOW : Speed.FAST;

                    startTime = System.currentTimeMillis();
                    readingValue = false;

                    rotations++;
                    if(endTime - rpmTimeStart >= 60_000){
                        Point speedPoint = Point.measurement("conveyor_speed").addField("speed", rotations)
                                        .addTag("speed_cat", speed.toString()).time(Instant.now(), WritePrecision.MS);
                        writeApi.writePoint(CookiesFactorySimulator.BUCKET, CookiesFactorySimulator.ORG, speedPoint);

                        rpm = rotations;
                        rotations = 0;
                        rpmTimeStart = System.currentTimeMillis();
                    }
                }
            }
        }
    }

    public void showStats(final SupsiRgbLcd lcd) throws IOException {
        if(isButtonWorking) {
            lcd.setRGB(Color.GREEN);
            lcd.setTextf("Button is: %s", isButtonWorking ? "Working" : "NOT WORKING!");
        } else {
            lcd.setRGB(Color.RED);

        }

        if(rpm != 0)
            lcd.setTextf("RPM: %d", rpm);
    }

    public GroveButtonListener getGroveButtonListener() {
        return groveButtonListener;
    }
}


public class CookiesFactorySimulator {
    private static boolean acquisitionON = true;

    public static String BUCKET = "Grafana";
    public static String ORG = "Supsi";

    final private static List<SupsiMonitor<?>> sensors = new ArrayList<>();

    private static void addSensorMonitored(SupsiMonitor<?> sensorMonitor){
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

        String token = "b21xsGBINmFAwno-66qgvc7EgfSTQbMRoEvjRCW-Z39XNa4mTJLwjeaGNJ1tj5snuPgkhwGkAMgSIDxHjkSb2Q==";
        InfluxDBClient client = InfluxDBClientFactory.create("http://169.254.201.100:8086", token.toCharArray());
        WriteApiBlocking writeApi = client.getWriteApiBlocking();

        // Factory Object
        CookiesFactory factory = new CookiesFactory(writeApi);

        // Raspberry - Gp1
        GrovePi grovePi = new GrovePi4J();

        // LCD display - I2C port
        @SuppressWarnings("resource")
        SupsiRgbLcd lcd = new SupsiRgbLcd();

        // Ultrasonicranger - D7 port
        SupsiUltrasonicRanger ranger = new SupsiUltrasonicRanger(grovePi, 7);

        // Button - D2 port
        SupsiButton button = new SupsiButton(grovePi, 2, factory.getGroveButtonListener());

        while(true){
            factory.speedCalculator(ranger);
        }

    }
}