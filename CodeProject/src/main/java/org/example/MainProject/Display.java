package org.example.MainProject;

import org.example.MyLibrary.Color;
import org.example.MyLibrary.SupsiRgbLcd;

import java.io.IOException;

public class Display {
    private long lcdStartTime;
    private final SupsiRgbLcd lcd;
    private boolean isShown;

    public Display(final SupsiRgbLcd lcd) throws IOException {
        lcdStartTime = System.currentTimeMillis();
        this.lcd = lcd;
        isShown = false;
        lcd.setRGB(Color.ON);
    }

    public void showStats(final boolean isButtonWorking, final double rpm) throws IOException {
        long time = System.currentTimeMillis() - lcdStartTime;

        if(time < 5_000 && !isShown) {
            lcd.setTextf("The button is    working: %b", isButtonWorking);
            isShown = true;
        } else if (time > 5_000 && isShown) {
            lcd.setTextf("RPM: %.2f", rpm);
            isShown = false;
        } else if (time > 10_000) {
            lcdStartTime = System.currentTimeMillis();
        }

    }

    public void showMessage(String text, Object... args) throws IOException {
        lcd.setTextf(text, args);
    }
}