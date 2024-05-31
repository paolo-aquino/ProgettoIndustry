package org.example.MainProject;

import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import org.iot.raspberry.grovepi.sensors.listener.GroveButtonListener;

import java.time.Instant;

public class Oven {
    private final GroveButtonListener groveButtonListener;
    private boolean isButtonWorking;
    private int buttonCounter;
    private long clickTime;

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
                    CookiesFactorySimulator.WRITE_API.writePoint(CookiesFactorySimulator.BUCKET, CookiesFactorySimulator.ORG, button);
                }

            }
        };

        isButtonWorking = true;
        buttonCounter = 0;
        clickTime = 0;
    }

    public GroveButtonListener getGroveButtonListener() {
        return groveButtonListener;
    }

    public boolean isButtonWorking() {
        return isButtonWorking;
    }

    public void setButtonWorking(boolean buttonWorking) {
        isButtonWorking = buttonWorking;
    }

    public int getButtonCounter() {
        return buttonCounter;
    }

    public void setButtonCounter(int buttonCounter) {
        this.buttonCounter = buttonCounter;
    }

    public long getClickTime() {
        return clickTime;
    }

    public void setClickTime(long clickTime) {
        this.clickTime = clickTime;
    }
}