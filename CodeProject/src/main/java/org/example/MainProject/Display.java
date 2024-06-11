package org.example.MainProject;

import org.example.MyLibrary.Color;
import org.example.MyLibrary.SupsiRgbLcd;

import java.io.IOException;

public class Display {
    private long lcdStartTime;
    private final SupsiRgbLcd lcd;

    private boolean isOnWelcomeMessage;

    public Display(final SupsiRgbLcd lcd) {
        lcdStartTime = System.currentTimeMillis();
        isOnWelcomeMessage = true;
        this.lcd = lcd;
    }

    public void showStats(final boolean isButtonWorking, final int rpm) throws IOException {
        long time = System.currentTimeMillis() - lcdStartTime;

//        if(time < 30_000 && isOnWelcomeMessage) {
//            lcd.setText("Welcome to our Cookies Factory!");
//            isOnWelcomeMessage = false;
//        } else if (!isOnWelcomeMessage) {
//            if(time )
//        }

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