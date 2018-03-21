package ch.epfl.imhof.painting;

import java.util.Arrays;

/**
 * Represents a pack of line's decorations
 * 
 * @author Mortiniera Thevie (234914)
 * @author Leimgruber Isaac (236908)
 */

public final class LineStyle {

    /**
     * Represents a line cap
     * 
     * @author Mortiniera Thevie (234914)
     * @author Leimgruber Isaac (236908)
     */
    public enum LineCap {
        Butt, Round, Square
    }

    /**
     * Represents a line join
     * 
     * @author Mortiniera Thevie (234914)
     * @author Leimgruber Isaac (236908)
     */
    public enum LineJoin {
        Bevel, Miter, Round
    }

    private final Color colorLine;
    private final float[] dashingPattern;
    private final LineCap lineCap;
    private final LineJoin lineJoin;
    private final double width;

    /**
     * 
     * @param width
     *            : the line's width
     * @param colorLine
     *            : the line's color
     */
    public LineStyle(double width, Color colorLine) {
        this(width, colorLine, LineCap.Butt, LineJoin.Miter);
    }

    /**
     * 
     * @param width
     *            : the line's width
     * @param colorLine
     *            : the line's color
     * @param lineCap
     *            : the line's cap
     * @param lineJoin
     *            : the line's join
     * @param dashingPattern
     *            : the line's dashing pattern
     */
    public LineStyle(double width, Color colorLine, LineCap lineCap,
            LineJoin lineJoin, float... dashingPattern) {
        if (width < 0 || negOrnull(dashingPattern)) {
            throw new IllegalArgumentException(
                    "width < 0 or one of the elements of the sequence is <= 0");
        }
        this.width = width;
        this.colorLine = colorLine;
        this.dashingPattern = dashingPattern;
        this.lineCap = lineCap;
        this.lineJoin = lineJoin;

    }

    /**
     * 
     * @return the line's color
     */
    public Color colorLine() {
        return colorLine;
    }

    /**
     * 
     * @return a copy of the line's dashing pattern
     */
    public float[] dashingPattern() {
        return Arrays.copyOf(dashingPattern, dashingPattern.length);
    }

    /**
     * 
     * @return the line's cap
     */
    public LineCap lineCap() {
        return lineCap;
    }

    /**
     * 
     * @return the line's join
     */
    public LineJoin lineJoin() {
        return lineJoin;
    }

    /**
     * 
     * @return the line's width
     */
    public double width() {
        return width;
    }

    /**
     * 
     * @param newColorline
     *            : a new line's color
     * @return a new LineStyle with the new line's color
     */
    public LineStyle withColorLine(Color newColorline) {
        return new LineStyle(width, newColorline);
    }

    /**
     * 
     * @param newCap
     *            : the cap to change
     * @return a new LineStyle with the new cap
     */

    public LineStyle withLineCap(LineCap newCap) {
        return new LineStyle(width, colorLine, newCap, lineJoin, dashingPattern);
    }

    /**
     * 
     * @param newJoin
     *            : the join to change
     * @return a new LineStyle with the new join
     */
    public LineStyle withLineJoin(LineJoin newJoin) {
        return new LineStyle(width, colorLine, lineCap, newJoin, dashingPattern);
    }

    /**
     * 
     * @param newWidth
     *            : a new width
     * @return a new LineStyle with the new width
     */
    public LineStyle withWidth(float newWidth) {
        return new LineStyle(newWidth, colorLine);
    }

    private boolean negOrnull(float[] dashingPattern) {
        if (dashingPattern.length >= 1) {
            for (int i = 0; i < dashingPattern.length; i++) {
                if ((dashingPattern[i] <= 0f)) {
                    return true;
                }
            }
        }
        return false;
    }
}
