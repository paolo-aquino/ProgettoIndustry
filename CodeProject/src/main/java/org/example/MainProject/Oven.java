package org.example.MainProject;

import org.example.MyLibrary.SupsiButton;
import org.iot.raspberry.grovepi.sensors.listener.GroveButtonListener;

/**
 * Class representing the Oven.
 * <p>
 * This class handles the oven operations including monitoring the button status
 * and tracking button press events.
 *
 * @author Paolo Aquino
 * @author Zeno Darani
 * @author Matteo Cazzani
 */
public final class Oven {

    // Indicates if the button is working based on press duration
    private boolean isButtonWorking;

    // Indicates if the button has been pressed
    private boolean isButtonPressed;

    // Tracks the time when the button was pressed
    private long pressTime;

    public Oven(final SupsiButton button) {
        isButtonWorking = false;
        isButtonPressed = false;
        pressTime = 0;

        final GroveButtonListener groveButtonListener = new GroveButtonListener() {
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

        button.setButtonListener(groveButtonListener);
    }

    /**
     * Checks if the button is working.
     *
     * @return true if the button is working, false otherwise
     */
    public boolean isButtonWorking() {
        return isButtonWorking;
    }

    /**
     * Checks if the button has been pressed and resets the press status.
     *
     * @return true if the button has been pressed, false otherwise
     */
    public boolean isButtonPressed() {
        if(isButtonPressed) {
            isButtonPressed = false;
            return true;
        }

        return false;
    }
}