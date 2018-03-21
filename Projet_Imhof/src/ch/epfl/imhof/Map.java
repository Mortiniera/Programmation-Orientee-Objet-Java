package ch.epfl.imhof;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;

/**
 * A map out of PolyLines and Polygons
 * @author Mortiniera Thevie (234914)
 * @author Leimgruber Isaac (236908)
 */
public final class Map {
    private final List<Attributed<PolyLine>> polyLines;
    private final List<Attributed<Polygon>> polygons;
/**
 * 
 * @param polyLines
 *          a PolyLines list
 * @param polygons
 *          a Polygons list
 */
    public Map(List<Attributed<PolyLine>> polyLines, List<Attributed<Polygon>> polygons) {
        this.polyLines = Collections.unmodifiableList(new ArrayList<>(polyLines));
        this.polygons = Collections.unmodifiableList(new ArrayList<>(polygons));
    }

    /**
     * 
     * @return
     *          the PolyLines list
     */
    public List<Attributed<PolyLine>> polyLines() {
        return polyLines;
    }
/**
 * 
 * @return
 *          the Polygons list
 */
    public List<Attributed<Polygon>> polygons() {
        return polygons;
    }
    /**
     * a to be built map
     * @author Mortiniera Thevie (234914)
     * @author Leimgruber Isaac (236908)
     */
    public static class Builder {
        private List<Attributed<PolyLine>> polyLines = new ArrayList<>();
        private List<Attributed<Polygon>> polygons = new ArrayList<>();

        /**
         * 
         * @param newPolyLine
         *          a PolyLine
         *          adds the PolyLine to the map's PolyLines list
         */
        public void addPolyLine(Attributed<PolyLine> newPolyLine) {
            polyLines.add(newPolyLine);
        }
/**
 * 
 * @param newPolygon
 *          a Polygon
 *          adds the Polygon to the map Polygons list
 */
        public void addPolygon(Attributed<Polygon> newPolygon) {
            polygons.add(newPolygon);
        }
/**
 * 
 * @return
 *          a new built Map
 */
        public Map build() {
            return new Map(polyLines, polygons);
        }    
    }
}
