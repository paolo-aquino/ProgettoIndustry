package org.example.MainProject;

import org.example.MyLibrary.SupsiButton;
import org.example.MyLibrary.SupsiLed;
import org.example.MyLibrary.SupsiRgbLcd;
import org.example.MyLibrary.SupsiUltrasonicRanger;
import org.iot.raspberry.grovepi.sensors.listener.GroveButtonListener;

import java.io.IOException;

public class CookiesFactory {

    private final Display display;
    private final Conveyor conveyor;
    private final Oven oven;

    public CookiesFactory(final SupsiRgbLcd lcd, final SupsiLed redligth, final SupsiLed blueLight, final SupsiUltrasonicRanger speedRanger, final SupsiUltrasonicRanger counterRanger, final SupsiButton button) throws IOException {
        display = new Display(lcd);
        conveyor = new Conveyor(redligth, blueLight, speedRanger, counterRanger);

        oven = new Oven();
        button.setButtonListener(getGroveButtonListener());
    }

    public void speedCalculator() throws IOException {
        conveyor.speedCalculator();
    }

    public boolean isSpeedSignalReady() {
        return conveyor.isSignalReady();
    }

    public double getConveyorRPM() {
        return conveyor.getRpm();
    }

    public String getConveyorSpeed() {
        return conveyor.getSpeed().toString();
    }

    public void ledBlink() throws IOException {
        conveyor.ledBlink();
    }

    public boolean isOvenWorking() {
        return oven.isButtonWorking();
    }

    public boolean isOvenSignalReady() {
        return oven.isButtonPressed();
    }

    public void showStats() throws IOException {
        display.showStats(oven.isButtonWorking(), conveyor.getRpm());
    }

    public void showMessage(String text, Object... args) throws IOException {
        display.showMessage(text, args);
    }

    public GroveButtonListener getGroveButtonListener() {
        return oven.getGroveButtonListener();
    }
}