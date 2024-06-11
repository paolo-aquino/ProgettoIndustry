package org.example.MyLibrary;

import org.iot.raspberry.grovepi.pi4j.lcd.GroveRgbLcdPi4J;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Class representing an RGB LCD with additional functionalities.
 * Extends GroveRgbLcdPi4J for Raspberry Pi.
 *
 * @author Paolo Aquino
 */
public final class SupsiRgbLcd extends GroveRgbLcdPi4J {
    private static final int CLEAR_DISPLAY = 0x01;
    private static final int FIRST_ROW = 0x80;
    private static final int SECOND_ROW = (0x80 | 0x40);
    private static final int SCROLL_RIGHT = 0x1E;
    private static final int SCROLL_LEFT = 0x18;
    private static final int CGRAM_ADDR = 0x40;

    private int row;
    private int column;
    private Color tmpColor;

    public SupsiRgbLcd() throws Exception {
        super();
        tmpColor = null;
        row = 0;
        column = 0;
    }

    /**
     * Sets the LED rgb color
     *
     * @param color Selects the color of the display
     * @throws IOException
     */
    public void setRGB(final Color color) throws IOException {
        if(color == null || (tmpColor != null && tmpColor.equals(color)))
            return;

        tmpColor = color;

        setRGB(color.getR(), color.getG(), color.getB());
    }

    /**
     * Clears the LCD.
     *
     * @throws IOException
     */
    public void clrLcd() throws IOException {
        writeCommand(CLEAR_DISPLAY);
    }

    /**
     * Writes a command to the LCD.
     *
     * @param command The command to be sent to the LCD.
     * @throws IOException
     */
    private void writeCommand(final int command) throws IOException {
        this.execTEXT((io) -> {
            io.write(128, command);
            io.sleep(50L);
        });
    }

    /**
     * Sets the cursor to a specific position on the LCD.
     *
     * @param row Selects the line of the display. Range: 0 - 1 Rows.
     * @param position Starts from the position of the line. Range: 0 - 16 Columns.
     * @throws IllegalArgumentException if row or position are outside the specified ranges.
     * @throws IOException
     */
    public void setCursorToPosition(final int row, final int position) throws IllegalArgumentException, IOException {
        if(row < 0 || row > 1)
            throw new IllegalArgumentException("Row value cannot be outside the range 0 - 1");

        if(position < 0 || position > 16)
            throw new IllegalArgumentException("Column value cannot be outside the range 0 - 16");

        writeCommand((row == 0? FIRST_ROW : SECOND_ROW) | position);
    }

    /**
     * Sets the text of the LCD using a formatted string.
     *
     * @param format The format string.
     * @param args Arguments referenced by the format specifiers in the format string.
     * @throws IOException
     */
    public void setTextf(@NotNull final String format, final Object ... args) throws IOException {
        final String text = String.format(format,args);
        setText(text);
    }

    /**
     * Sets the text of the LCD to a specific position using a formatted string.
     *
     * @param row The row of the display.
     * @param position The position within the row.
     * @param format The format string.
     * @param args Arguments referenced by the format specifiers in the format string.
     * @throws IOException
     */
    public void setPosTextf(final int row, final int position, @NotNull final String format, final Object ... args) throws IOException {
        setCursorToPosition(row,position);
        setTextf(format, args);
    }

    /**
     * Adds text to the LCD using a formatted string.
     *
     * @param format The format string.
     * @param args Arguments referenced by the format specifiers in the format string.
     * @throws IOException
     */
    public void addTextf(@NotNull final String format, final Object ... args) throws IOException {
        final String text = String.format(format,args);

        this.execTEXT((io) -> {
            char[] var4 = text.toCharArray();

            for (char c : var4) {
                if (c == '\n' || column == 16) {
                    column = 0;
                    ++row;

                    if (row == 2)
                        break;

                    io.write(128, SECOND_ROW);

                    if (c == '\n')
                        continue;
                }

                ++column;
                io.write(64, c);
            }

            io.sleep(100L);
        });
    }
}