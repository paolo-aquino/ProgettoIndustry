package org.example.MainProject;

import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import org.iot.raspberry.grovepi.sensors.listener.GroveButtonListener;

import java.sql.SQLOutput;
import java.time.Instant;

public class Oven {
    private final GroveButtonListener groveButtonListener;
    private boolean isButtonWorking;
    private int buttonCounter;
    private long clickTime;
    private int clicks;

    public Oven() {
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

                clickTime = System.currentTimeMillis();

                /*
                if(clickTime < 5_000) {
                    if (buttonCounter > 2) {
                        Point button = Point.measurement("oven_door").addField("is_okay", false).time(Instant.now(), WritePrecision.MS);
                        CookiesFactorySimulator.WRITE_API.writePoint(CookiesFactorySimulator.BUCKET, CookiesFactorySimulator.ORG, button);
                        buttonCounter = 0;
                    }
                }




                if(clickTime < 10_000) {
                    isButtonWorking = true;
                    buttonCounter = 0;
                    clickTime = 0;
                } else {
                    clickTime = System.currentTimeMillis();

                    Point button = Point.measurement("oven_door").addField("is_okay", isButtonWorking).time(Instant.now(), WritePrecision.MS);
                    CookiesFactorySimulator.WRITE_API.writePoint(CookiesFactorySimulator.BUCKET, CookiesFactorySimulator.ORG, button);

                    System.out.println();
                    System.out.println("TEOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
                    System.out.println();
                }
                 */

            }
        };

        isButtonWorking = true;
        buttonCounter = 0;
        clickTime = 0;
    }

    public GroveButtonListener getGroveButtonListener() {
        return groveButtonListener;
    }

    public boolean isWorking() {
        long time = System.currentTimeMillis() - clickTime;

        System.out.println("TIMEEEEEEEEEEEEEEE: " + time);
        System.out.println("BUTTONCOUNTERRRR" + buttonCounter);

        if(time < 15_000) {
            if (time > 3000) {
                if (buttonCounter == 1) {
                    Point button = Point.measurement("oven_door").addField("is_okay", false).time(Instant.now(), WritePrecision.MS);
                    CookiesFactorySimulator.WRITE_API.writePoint(CookiesFactorySimulator.BUCKET, CookiesFactorySimulator.ORG, button);
                    buttonCounter = 0;
                }
            }

            if (buttonCounter == 2) {
                Point button = Point.measurement("oven_door").addField("is_okay", true).time(Instant.now(), WritePrecision.MS);
                CookiesFactorySimulator.WRITE_API.writePoint(CookiesFactorySimulator.BUCKET, CookiesFactorySimulator.ORG, button);
                buttonCounter = 0;
            }

        }

        return true;
    }

    public boolean isButtonWorking() {
        return isButtonWorking;
    }

}