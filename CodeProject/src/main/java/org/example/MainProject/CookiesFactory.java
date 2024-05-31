package org.example.MainProject;

import org.example.MyLibrary.SupsiLed;
import org.example.MyLibrary.SupsiRgbLcd;
import org.example.MyLibrary.SupsiUltrasonicRanger;
import org.iot.raspberry.grovepi.sensors.listener.GroveButtonListener;

import java.io.IOException;

public class CookiesFactory {

    private final Oven oven;
    private final Conveyor conveyor;
    private final Display display;

    public CookiesFactory(final SupsiLed redligth, final SupsiLed blueLight) {
        oven = new Oven();
        conveyor = new Conveyor(redligth, blueLight);
        display = new Display();
    }

    public void speedCalculator(final SupsiUltrasonicRanger ranger) throws IOException {
        conveyor.speedCalculator(ranger);
    }

    public void ledToggle() throws IOException {
        conveyor.ledToggle();
    }

    public void showStats(final SupsiRgbLcd lcd) throws IOException {
        display.showStats(lcd, oven.isButtonWorking(), conveyor.getRpm());
    }

    public GroveButtonListener getGroveButtonListener() {
        return oven.getGroveButtonListener();
    }
}