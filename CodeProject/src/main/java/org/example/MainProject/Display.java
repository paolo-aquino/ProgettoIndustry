package org.example.MainProject;

import org.example.MyLibrary.Color;
import org.example.MyLibrary.SupsiRgbLcd;

import java.io.IOException;

public class Display {
    private long lcdStartTime;

    public void showStats(final SupsiRgbLcd lcd, final boolean isButtonWorking, final int rpm) throws IOException {
        if(isButtonWorking) {
            lcd.setRGB(Color.GREEN);
            lcd.setTextf("Button is: %s", isButtonWorking ? "Working" : "NOT WORKING!");
        } else {
            lcd.setRGB(Color.RED);

        }

        if(rpm != 0)
            lcd.setTextf("RPM: %d", rpm);
    }
}