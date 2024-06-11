package org.example.MainProject;

import org.example.MyLibrary.Color;
import org.example.MyLibrary.SupsiRgbLcd;

import java.io.IOException;
import java.util.Arrays;

public final class Display {
    private long lcdStartTime;
    private final SupsiRgbLcd lcd;
    private final boolean[] isShown;

    public Display(final SupsiRgbLcd lcd) throws IOException {
        lcdStartTime = System.currentTimeMillis();
        this.lcd = lcd;
        isShown = new boolean[3];
        lcd.setRGB(Color.ON);
    }

    public void showStats(final boolean isButtonWorking, final double rpm, final int cookies) throws IOException {
        long time = System.currentTimeMillis() - lcdStartTime;

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

    public void showMessage(final String text, final Object... args) throws IOException {
        lcd.setTextf(text, args);
    }
}