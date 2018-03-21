package ch.epfl.imhof.geometry;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

/**
 * a polyline
 * 
 * @author Mortiniera Thevie (234914)
 * @author Leimgruber Isaac (236908)
 */
public abstract class PolyLine {
    private List<Point> points;

    /**
     * Builds a polyline
     * 
     * @param points
     *            a point list
     * @throw an IllegalArgumentException if the list is empty
     */
    public PolyLine(List<Point> points) {
        if (points.isEmpty())
            throw new IllegalArgumentException("point's list is empty");

        this.points = Collections.unmodifiableList(new ArrayList<>(points));
    }

    public abstract boolean isClosed();

    /**
     * 
     * @return the polyline's point's list
     */
    public List<Point> points() {
        return points;
    }

    /**
     * 
     * @return the first polyline's point
     */
    public Point firstPoint() {
        return points.get(0);
    }

    public final static class Builder {
        private List<Point> points = new ArrayList<Point>();

        /**
         * 
         * @param p
         *            a point to be added 
         *            adds p to the point list
         */
        public void addPoint(Point p) {
            points.add(p);
        }

        /**
         * 
         * @return Builds an OpenPolyLine with the current point list
         */
        public OpenPolyLine buildOpen() {
            return new OpenPolyLine(points);
        }

        /**
         * 
         * @return Builds a ClosedPolyLine with the current point list
         */
        public ClosedPolyLine buildClosed() {
            return new ClosedPolyLine(points);
        }
    }
}
