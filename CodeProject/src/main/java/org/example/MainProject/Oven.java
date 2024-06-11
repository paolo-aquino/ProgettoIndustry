package org.example.MainProject;

import org.iot.raspberry.grovepi.sensors.listener.GroveButtonListener;

public class Oven {
    private final static long LONG_PERIOD = 20_000;
    private final static long SHORT_PERIOD = 3_000;

    private final GroveButtonListener groveButtonListener;
    private boolean isButtonWorking;
    private int buttonCounter;
    private long clickTime;
    private boolean firstClick = true;
    private boolean buttonSignal = false;

    private boolean isButtonPressed;

    public Oven() {
        isButtonWorking = true;
        buttonCounter = 0;
        isButtonPressed = false;

        clickTime = System.currentTimeMillis();
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
                isButtonPressed = true;

                if(firstClick) {
                    clickTime = System.currentTimeMillis();
                } else {
                    if(System.currentTimeMillis() - clickTime <= SHORT_PERIOD && !buttonSignal) {
                        clickTime = System.currentTimeMillis();
                        buttonSignal = true;
                        isButtonWorking = true;
                    }

                    if(System.currentTimeMillis() - clickTime > LONG_PERIOD && buttonSignal) {
                        clickTime = System.currentTimeMillis();
                        buttonSignal = false;
                    }

                    isButtonWorking = false;
                }

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