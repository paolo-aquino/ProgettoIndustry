package org.example.MainProject;

import org.example.MyLibrary.Color;
import org.example.MyLibrary.SupsiRgbLcd;

import java.io.IOException;
import java.util.Arrays;

/**
 * Class representing the Display.
 * <p>
 * This class handles the display operations for showing messages and statistics on an RGB LCD.
 *
 * @author Paolo Aquino
 * @author Zeno Darani
 * @author Matteo Cazzani
 */
public final class Display {

    // The RGB LCD instance
    private final SupsiRgbLcd lcd;

    // Array to track which messages have been shown
    private final boolean[] isShown;

    // The start time for the LCD to control the timing of displayed messages
    private long lcdStartTime;

    public Display(final SupsiRgbLcd lcd) throws IOException {
        this.lcd = lcd;
        isShown = new boolean[3];
        lcdStartTime = System.currentTimeMillis();
        lcd.setRGB(Color.ON);
    }

    /**
     * Shows statistics on the display in a cyclic manner.
     * <p>
     * The display shows different messages based on the elapsed time since the last reset:
     * <p>
     * - The button working status for the first 5 seconds
     * <p>
     * - The RPM for the next 5 seconds
     * <p>
     * - The cookie count for the following 5 seconds
     * <p>
     * After 15 seconds, the display cycle resets.
     *
     * @param isButtonWorking the status of whether the button is working
     * @param rpm the current RPM of the conveyor
     * @param cookies the total cookie count
     * @throws IOException if an I/O error occurs
     */
    public void showStats(final boolean isButtonWorking, final double rpm, final int cookies) throws IOException {
        final long time = System.currentTimeMillis() - lcdStartTime;

        if(time < 5_000 && !isShown[0]) {
            lcd.setTextf("The button is    working: %b", isButtonWorking);
            isShown[0] = true;
        } else if(time > 5_000 && time < 10_000 && !isShown[1]) {
            lcd.setTextf("RPM: %.2f", rpm);
            isShown[1] = true;
        } else if(time > 10_000 && time < 15_000 && !isShown[2]) {
            lcd.setTextf("Cookies: %d", cookies);
            isShown[2] = true;
        } else if(time > 15_000) {
            lcdStartTime = System.currentTimeMillis();
            Arrays.fill(isShown, false);
        }

    }

    /**
     * Displays a custom message on the RGB LCD.
     *
     * @param text the message text
     * @param args the arguments to format the message
     * @throws IOException if an I/O error occurs
     */
    public void showMessage(final String text, final Object... args) throws IOException {
        lcd.setTextf(text, args);
    }
}