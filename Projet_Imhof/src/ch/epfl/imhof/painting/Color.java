package ch.epfl.imhof.painting;

/**
 * Represents a Color using RGB values between 0 and 1
 * 
 * @author Mortiniera Thevie (234914)
 * @author Leimgruber Isaac (236908)
 */

public final class Color {
    public final static Color RED = rgb(1.0, 0.0, 0.0), GREEN = rgb(0.0, 1.0,
            0.0), BLUE = rgb(0.0, 0.0, 1.0), WHITE = rgb(1.0, 1.0, 1.0),
            BLACK = rgb(0.0, 0.0, 0.0);
    private final double red, green, blue;

    private Color(double red, double green, double blue) {
        if (!(belongTo(red) && belongTo(green) && belongTo(blue))) {
            throw new IllegalArgumentException(
                    " out of range 0.0 to 1.0 inclusive");
        }
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    /**
     * 
     * @param p
     *            the shade of gray between 0 and 1
     * @return a new shaded gray color
     */
    public static Color gray(double p) {
        return new Color(p, p, p);
    }

    /**
     * 
     * @param red
     *            : the value of red
     * @param green
     *            : the value of green
     * @param blue
     *            : the value of blue
     * @return a new Color with the corresponding proportion of red, green and
     *         blue
     */
    public static Color rgb(double red, double green, double blue) {
        return new Color(red, green, blue);
    }

    /**
     * 
     * @param packedColor
     *            : the packed values of red, green and blue
     * @return a new Color with the corresponding proportion of red, green and
     *         blue
     */
    public static Color rgb(int packedColor) {
        int red = ((packedColor & 0xFF0000) >> 16);
        int green = ((packedColor & 0x00FF00) >> 8);
        int blue = packedColor & 0x0000FF;
        return new Color(red / 255d, green / 255d, blue / 255d);
    }

    /**
     * 
     * @return the blue value
     */
    public double blue() {
        return blue;
    }

    /**
     * 
     * @return a new Java color
     */
    public java.awt.Color convert() {
        return new java.awt.Color((float) red, (float) green, (float) blue);
    }

    /**
     * 
     * @return the green value
     */
    public double green() {
        return green;
    }

    /**
     * 
     * @param that
     *            : the color to be mixed with
     * @return a new Color which is a mix with the current and the one in
     *         parameter
     */
    public Color multiply(Color that) {
        return new Color(red * that.red(), green * that.green(), blue
                * that.blue());
    }

    /**
     * 
     * @return the red value
     */
    public double red() {
        return red;
    }

    private boolean belongTo(double a) {
        return a >= 0.0 && a <= 1.0;
    }
}
