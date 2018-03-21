package ch.epfl.imhof.geometry;

import java.util.List;

/**
 * An open PolyLine
 * 
 * @author Mortiniera Thevie (234914)
 * @author Leimgruber Isaac (236908)
 */
public final class OpenPolyLine extends PolyLine {
    /**
     * 
     * @param points
     *            a point list 
     *            Builds an OpenPolyLine
     */
    public OpenPolyLine(List<Point> points) {
        super(points);
    }

    /**
     * return false because of this objects status which is always open
     */
    @Override
    public boolean isClosed() {
        return false;
    }
}
