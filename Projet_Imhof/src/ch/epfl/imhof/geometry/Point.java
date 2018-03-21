package ch.epfl.imhof.geometry;

import java.util.function.Function;
/**
 * a cartesian point
 * 
 * @author Mortiniera Thevie (234914)
 * @author Leimgruber Isaac (236908)
 */
public final class Point {
    private final double x, y;

    /**
     * @param x
     *            the x coordinate
     * @param y
     *            the y coordinate Builder initialize (x,y) values
     */
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }
    /**
     * 
     * @param p1A first point in spaceA
     * @param p1B first point in spaceB
     * @param p2A second point in spaceA
     * @param p2B second point in spaceB
     * @throws IllegalArgument exception at least one of the coordinate of two points are the same
     * @return  a function mapping spaceA to spaceB (which axes must be parallel, and space belong to R²) 
     */
    public static Function<Point, Point> alignedCoordinateChange(Point p1A,
            Point p1B, Point p2A, Point p2B) {
        if (p1A.x == p2A.x || p1A.y == p2A.y || p1B.x == p2B.x
                || p1B.y == p2B.y)
            throw new IllegalArgumentException("invalid points");
        double devX = p1A.x - p2A.x;
        double devY = p1A.y - p2A.y;
        // caculate the dilatation coeffs for x and y
        double xDilatCoeff = (p1B.x - p2B.x) / devX;
        double yDilatCoeff = (p1B.y - p2B.y) / devY;
        // calculate the height and width difference for x and y
        double xDifference = p1B.x - p1A.x * xDilatCoeff;
        double yDifference = p1B.y - p1A.y * yDilatCoeff;
        
        return p -> new Point(p.x * xDilatCoeff + xDifference, p.y
                * yDilatCoeff + yDifference);
    }

    /**
     * @return x value
     */
    public double x() {
        return x;
    }

    /**
     * @return y value
     */
    public double y() {
        return y;
    }
}
