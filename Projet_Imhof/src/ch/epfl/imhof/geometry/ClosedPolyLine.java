package ch.epfl.imhof.geometry;

import java.util.List;

/**
 * A ClosedPolyLine
 * 
 * @author Mortiniera Thevie (234914)
 * @author Leimgruber Isaac (236908)
 * 
 */
public final class ClosedPolyLine extends PolyLine {
    /**
     * 
     * @param points
     *            A point list 
     *            Builds a closedPolyLine
     */
    public ClosedPolyLine(List<Point> points) {
        super(points);
    }

    /**
     * @return true because the object is a closed polyLine
     */
    @Override
    public boolean isClosed() {
        return true;
    }

    /**
     * 
     * @return the polyLine's area
     */
    public double area() {
        int size = points().size();
        double sum = 0d;
        for (int i = 0; i < size; i++) {
            sum += points().get(Math.floorMod(i, size)).x()
                    * (points().get(Math.floorMod(i + 1, size)).y() - points()
                            .get(Math.floorMod(i - 1, size)).y());
        }
        return 0.5 * Math.abs(sum);
    }

    /**
     * 
     * @param p
     *            a point
     * @return true if the point p is contained in the current Closed PolyLine
     */
    public boolean containsPoint(Point p) {
        int indice = 0;
        int size = points().size();
        for (int i = 0; i < size; i++) {
            Point p1 = points().get(i);
            Point p2 = points().get(Math.floorMod(i + 1, size));
            if (p1.y() <= p.y()) {
                if ((p2.y() > p.y()) && atLeft(p, p1, p2)) {
                    indice++;
                }
            } else {
                if ((p2.y() <= p.y()) && atLeft(p, p2, p1)) {
                    indice--;
                }
            }
        }
        return indice != 0;
    }

    /**
     * 
     * @param p
     *            the point to be compared
     * @param p1
     *            a second point
     * @param p2
     *            a third point
     * @return true if p is at left from the line built by p1 and p2
     */
    private boolean atLeft(Point p, Point p1, Point p2) {
        return (p1.x() - p.x()) * (p2.y() - p.y()) - (p2.x() - p.x())
                * (p1.y() - p.y()) > 0;
    }
}
