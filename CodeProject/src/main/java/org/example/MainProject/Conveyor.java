package org.example.MainProject;

import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import org.example.MyLibrary.SupsiLed;
import org.example.MyLibrary.SupsiUltrasonicRanger;

import java.io.IOException;
import java.time.Instant;

public class Conveyor {

    private enum Speed {
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

    private static final int DEFAULT_DISTANCE = 10;
    private static final double RADIUS = 0.04;
    private static final double CIRCUMFERENCE = 2 * Math.PI * RADIUS;
    private static final double DEFAULT_SPEED = 0.01;
    private boolean firstRotation;
    private boolean readingValue;
    private long startTime;
    private long rpmTimeStart;
    private int rotations;
    private int rpm;
    private Speed speed;

    private final SupsiLed redLight;
    private final SupsiLed blueLight;

    public Conveyor(final SupsiLed redLight, final SupsiLed blueLight) {
        firstRotation = true;
        readingValue = false;
        startTime = 0;
        rpmTimeStart = 0;
        rotations = 0;
        rpm = 0;
        speed = Speed.SLOW;

        this.redLight = redLight;
        this.blueLight = blueLight;
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

                    speed = (CIRCUMFERENCE/time * RADIUS) > DEFAULT_SPEED ? Speed.FAST : Speed.SLOW;

                    startTime = System.currentTimeMillis();
                    readingValue = false;

                    rotations++;
                    if(endTime - rpmTimeStart >= 30_000){
                        rotations *= 2;
                        Point speedPoint = Point.measurement("conveyor_speed").addField("speed", rotations)
                                .addTag("speed_cat", speed.toString()).time(Instant.now(), WritePrecision.MS);
                        CookiesFactorySimulator.WRITE_API.writePoint(CookiesFactorySimulator.BUCKET, CookiesFactorySimulator.ORG, speedPoint);

                        rpm = rotations;
                        rotations = 0;
                        rpmTimeStart = System.currentTimeMillis();
                    }
                }
            }
        }
    }

    public void ledToggle() throws IOException {
        switch (speed) {
            case SLOW:
                blueLight.off();
                redLight.blink();
                break;
            case FAST:
                redLight.off();
                blueLight.blink();
                break;
        }
    }

    public boolean isFirstRotation() {
        return firstRotation;
    }

    public void setFirstRotation(boolean firstRotation) {
        this.firstRotation = firstRotation;
    }

    public boolean isReadingValue() {
        return readingValue;
    }

    public void setReadingValue(boolean readingValue) {
        this.readingValue = readingValue;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getRpmTimeStart() {
        return rpmTimeStart;
    }

    public void setRpmTimeStart(long rpmTimeStart) {
        this.rpmTimeStart = rpmTimeStart;
    }

    public int getRotations() {
        return rotations;
    }

    public void setRotations(int rotations) {
        this.rotations = rotations;
    }

    public int getRpm() {
        return rpm;
    }

    public void setRpm(int rpm) {
        this.rpm = rpm;
    }

}