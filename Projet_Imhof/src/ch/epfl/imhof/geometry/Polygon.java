package ch.epfl.imhof.geometry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * a polygon
 * 
 * @author Mortiniera Thevie (234914)
 * @author Leimgruber Isaac (236908)
 */
public final class Polygon {
    private final ClosedPolyLine shell;
    private final List<ClosedPolyLine> holes;

    /**
     * Builds a polygon with holes
     * 
     * @param shell
     *            a closed polyline which is the polygon'shell
     * @param holes
     *            a closed polyline's list
     */
    public Polygon(ClosedPolyLine shell, List<ClosedPolyLine> holes) {
        this.shell = shell;
        this.holes = Collections.unmodifiableList(new ArrayList<>(holes));
    }

    /**
     * Builds a holeless polygon
     * 
     * @param shell
     *            a closed polyline which is the polygon'shell
     */
    public Polygon(ClosedPolyLine shell) {
        this(shell, Collections.emptyList());
    }

    /**
     * @return a closed polyline
     */
    public ClosedPolyLine shell() {
        return shell;
    }

    /**
     * 
     * @return a list of closed polyline which represent the holes
     */
    public List<ClosedPolyLine> holes() {
        return holes;
    }
}
