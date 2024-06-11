package org.example.MainProject;

import org.example.MyLibrary.Color;
import org.example.MyLibrary.SupsiRgbLcd;

import java.io.IOException;

public class Display {
    private long lcdStartTime;
    private final SupsiRgbLcd lcd;

    public Display(final SupsiRgbLcd lcd) throws IOException {
        lcdStartTime = System.currentTimeMillis();
        this.lcd = lcd;
        lcd.setRGB(Color.ON);
    }

    public void showStats(final boolean isButtonWorking, final int rpm) throws IOException {
        long time = System.currentTimeMillis() - lcdStartTime;

        if(time < 5_000) {
            lcd.setTextf("The button is working: %b", isButtonWorking);
        } else if (time < 10_000) {
            lcd.setTextf("RPM: %d", rpm);
            lcdStartTime = System.currentTimeMillis();
        }

    }

    public void showMessage(String text, Object... args) throws IOException {
        lcd.setTextf(text, args);
    }
}