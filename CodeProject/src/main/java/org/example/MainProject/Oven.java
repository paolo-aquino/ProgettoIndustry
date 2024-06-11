package org.example.MainProject;

import org.iot.raspberry.grovepi.sensors.listener.GroveButtonListener;

public final class Oven {

    private final GroveButtonListener groveButtonListener;
    private boolean isButtonWorking;
    private long pressTime;
    private boolean isButtonPressed;

    public Oven() {
        isButtonWorking = false;
        isButtonPressed = false;

        groveButtonListener = new GroveButtonListener() {
            @Override
            public void onRelease() {
                long time = System.currentTimeMillis() - pressTime;
                isButtonWorking = time < 1_000;
            }

            @Override
            public void onPress() {
                pressTime = System.currentTimeMillis();
            }

            @Override
            public void onClick() {
                isButtonPressed = true;
            }
        };
    }

    public GroveButtonListener getGroveButtonListener() {
        return groveButtonListener;
    }

    public boolean isButtonWorking() {
        return isButtonWorking;
    }

    public boolean isButtonPressed() {
        if(isButtonPressed) {
            isButtonPressed = false;
            return true;
        }

        return false;
    }
}