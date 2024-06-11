package org.example.MainProject;

import org.example.MyLibrary.SupsiButton;
import org.example.MyLibrary.SupsiLed;
import org.example.MyLibrary.SupsiRgbLcd;
import org.example.MyLibrary.SupsiUltrasonicRanger;

import java.io.IOException;

/**
 * Class representing the Cookies Factory.
 * <p>
 * This class integrates the various components of the factory, including the display, conveyor,
 * and oven, and provides methods to control and monitor these components.
 *
 * @author Paolo Aquino
 * @author Zeno Darani
 * @author Matteo Cazzani
 */
public final class CookiesFactory {

    // Display instance for showing messages and stats
    private final Display display;

    // Conveyor instance for handling the conveyor belt operations
    private final Conveyor conveyor;

    // Oven instance for handling the oven operations
    private final Oven oven;

    public CookiesFactory(final SupsiRgbLcd lcd, final SupsiLed redLight, final SupsiLed blueLight, final SupsiUltrasonicRanger speedRanger, final SupsiUltrasonicRanger counterRanger, final SupsiButton button) throws IOException {
        display = new Display(lcd);
        conveyor = new Conveyor(redLight, blueLight, speedRanger, counterRanger);
        oven = new Oven(button);
    }

    /**
     * Calculates the speed of the conveyor belt.
     *
     */
    public void speedCalculator() {
        conveyor.speedCalculator();
    }

    /**
     * Checks if the speed signal is ready.
     *
     * @return true if the speed signal is ready, false otherwise
     */
    public boolean isSpeedSignalReady() {
        return conveyor.isSignalReady();
    }

    /**
     * Gets the current RPM (revolutions per minute) of the conveyor.
     *
     * @return the current RPM
     */
    public double getConveyorRPM() {
        return conveyor.getRpm();
    }

    /**
     * Gets the current speed of the conveyor as a string.
     *
     * @return the current speed
     */
    public String getConveyorSpeed() {
        return conveyor.getSpeed();
    }

    /**
     * Blinks the LED lights based on the current speed of the conveyor.
     *
     * @throws IOException if an I/O error occurs
     */
    public void ledBlink() throws IOException {
        conveyor.ledBlink();
    }


    /**
     * Checks if a cookie is crossing the path of the counter ranger.
     *
     * @return true if a cookie is detected crossing, false otherwise
     */
    public boolean isCookieCrossing() {
        return conveyor.isCookieCrossing();
    }

    /**
     * Checks if the oven is currently working.
     *
     * @return true if the oven is working, false otherwise
     */
    public boolean isOvenWorking() {
        return oven.isButtonWorking();
    }

    /**
     * Checks if the oven signal (button press) is ready.
     *
     * @return true if the oven signal is ready, false otherwise
     */
    public boolean isOvenSignalReady() {
        return oven.isButtonPressed();
    }

    /**
     * Displays the current statistics on the display.
     *
     * @throws IOException if an I/O error occurs
     */
    public void showStats() throws IOException {
        display.showStats(oven.isButtonWorking(), conveyor.getRpm(), conveyor.getCookiesCounter());
    }

    /**
     * Displays a custom message on the display.
     *
     * @param text the message text
     * @param args the arguments to format the message
     * @throws IOException if an I/O error occurs
     */
    public void showMessage(final String text, final Object... args) throws IOException {
        display.showMessage(text, args);
    }
}